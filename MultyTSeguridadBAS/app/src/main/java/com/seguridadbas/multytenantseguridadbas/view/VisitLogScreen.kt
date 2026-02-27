package com.seguridadbas.multytenantseguridadbas.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.icu.util.TimeZone
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.chargemap.compose.numberpicker.NumberPicker
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.repository.FileRepository
import com.seguridadbas.multytenantseguridadbas.controllers.visitlogscontroller.VisitLogController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.visitorlogs.DataVisitLog
import com.seguridadbas.multytenantseguridadbas.model.visitorlogs.IdPhoto
import com.seguridadbas.multytenantseguridadbas.model.visitorlogs.VisitorLogRequestBody
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisitLogScreen(
    modifier: Modifier = Modifier,
    visitLogController: VisitLogController,
    navigateBackToMore: () -> Unit = {}
) {

    var token by remember { mutableStateOf("") }
    var tenantId by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)
    val scope = rememberCoroutineScope()
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var visitDate by remember { mutableStateOf("") }
    var exitTime by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var numPeople by remember { mutableStateOf(1) }

    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPhotoFile by remember { mutableStateOf<File?>(null) }
    var isLoadingPhoto by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val fileRepository = remember { FileRepository(context = context) }

    // Variable para rastrear qué acción ejecutar después de otorgar permisos
    var pendingAction by remember { mutableStateOf<String?>(null) }

    // Launcher para la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        Log.d("VisitLogScreen", "Camera result: $success")
        if (success) {
            Log.d("VisitLogScreen", "Photo taken successfully. File: ${selectedPhotoFile?.absolutePath}")
            if(selectedPhotoFile != null){
                selectedPhotoUri = Uri.fromFile(selectedPhotoFile!!)
            }
            isLoadingPhoto = false
        } else {
            Log.e("VisitLogScreen", "Camera was cancelled or failed")
            selectedPhotoUri = null
            selectedPhotoUri = null
            isLoadingPhoto = false
        }
    }

    // Launcher para la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        Log.d("VisitLogScreen", "Gallery result: $uri")
        if (uri != null) {
            isLoadingPhoto = true
            selectedPhotoUri = uri
            selectedPhotoFile = fileRepository.uriToFile(uri)
            Log.d("VisitLogScreen", "Photo file from gallery: ${selectedPhotoFile?.absolutePath}")
            isLoadingPhoto = false
        }
    }

    // Launcher para permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Log.d("VisitLogScreen", "Permissions result: $permissions")

        val cameraGranted = permissions[Manifest.permission.CAMERA] == true
        val storageGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
                || permissions[Manifest.permission.READ_MEDIA_IMAGES] == true

        when {
            cameraGranted && pendingAction == "CAMERA" -> {
                Log.d("VisitLogScreen", "Camera permission granted, launching camera")
                launchCamera(fileRepository, { uri, file ->
                    selectedPhotoUri = uri
                    selectedPhotoFile = file
                    isLoadingPhoto = true
                }, cameraLauncher)
                pendingAction = null
            }
            storageGranted && pendingAction == "GALLERY" -> {
                Log.d("VisitLogScreen", "Storage permission granted, launching gallery")
                galleryLauncher.launch("image/*")
                pendingAction = null
            }
            else -> {
                Log.e("VisitLogScreen", "Permissions not granted")
                errorMessage = "Se requieren permisos para continuar"
                pendingAction = null
            }
        }
    }

    fun requestPermissionsAndTakePicture(action: String) {
        Log.d("VisitLogScreen", "Requesting permissions for action: $action")
        pendingAction = action

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        permissionLauncher.launch(permissions)
    }

    fun sendVisitorLog() {
        if (firstName.isBlank() || lastName.isBlank() || idNumber.isBlank()) {
            errorMessage = "Por favor completa todos los campos requeridos"
            return
        }

        if (selectedPhotoFile == null) {
            errorMessage = "Por favor selecciona una foto de la cédula"
            return
        }

        isLoading = true
        errorMessage = ""

        scope.launch {
            try {
                // Crear el objeto IdPhoto con metadatos del archivo
                val idPhotoObj = IdPhoto(
                    name = selectedPhotoFile!!.name,
                    type = "image/jpeg",
                    sizeInBytes = fileRepository.getFileSizeInBytes(selectedPhotoFile!!).toInt(),
                    storageKey = fileRepository.getStorageKey(selectedPhotoFile!!.name, tenantId),
                    downloadUrl = "",
                    id = "",
                    privateUrl = "",
                    publicUrl = ""
                )

                // Crear los datos de la bitácora
                val visitorLogData = DataVisitLog(
                    visitDate = visitDate.ifBlank { getCurrentDateTimeISO() },
                    firstName = firstName,
                    lastName = lastName,
                    idNumber = idNumber,
                    reason = reason,
                    numPeople = numPeople,
                    exitTime = exitTime.ifBlank { null },
                    idPhoto = listOf(idPhotoObj)
                )

                val requestBody = VisitorLogRequestBody(data = visitorLogData)

                // Enviar al servidor
                when (val result = visitLogController.sendVisitLog("Bearer $token", tenantId, requestBody)) {
                    is Resource.Success -> {
                        successMessage = "¡Bitácora registrada exitosamente!"
                        Log.d("VisitLogScreen", "Visitor log sent successfully")
                        // Limpiar formulario después de 2 segundos
                        scope.launch {
                            kotlinx.coroutines.delay(2000)
                            firstName = ""
                            lastName = ""
                            idNumber = ""
                            reason = ""
                            visitDate = ""
                            exitTime = ""
                            numPeople = 1
                            selectedPhotoUri = null
                            selectedPhotoFile = null
                            successMessage = ""
                        }
                    }
                    is Resource.Error -> {
                        errorMessage = result.message ?: "Error al registrar la bitácora"
                        Log.e("VisitLogScreen", "Error sending visitor log: ${result.message}")
                    }
                    else -> {
                        // Loading
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                Log.e("VisitLogScreen", "Exception while sending visitor log", e)
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val storedData = dataStoreController.getDataFromStore().first()
                token = storedData.token
                token = token.replace("\"", "").trim()

                tenantId = dataStoreController.getTenantId().first()
                tenantId = tenantId.replace("\"", "").trim()
                Log.d("VisitLogScreen", "Data loaded. Token: ${token.take(10)}..., TenantId: $tenantId")
            } catch (e: Exception) {
                Log.e("VisitLogScreen", "Error loading data from DataStore", e)
                errorMessage = "Error al cargar datos"
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Crear Bitácoras",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateBackToMore() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "navigate back to more"
                        )
                    }
                },
                scrollBehavior = scrollBehaviour,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BasYellow,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingVals ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVals)
                .background(color = BasBackground)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BasYellow)
                    .padding(top = 50.dp, bottom = 50.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.baslogo),
                    contentDescription = "Logo de bas"
                )
            }

            // Contenido
            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                // Mensajes de error/éxito
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (successMessage.isNotEmpty()) {
                    Text(
                        text = successMessage,
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFC8E6C9), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Campo Nombre
                Text(
                    text = "Ingrese el nombre:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                VisitLogTextField(
                    modifier = Modifier,
                    typedText = firstName,
                    onTextChange = { firstName = it },
                    placeholder = "Nombre:",
                    isSingleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo Apellido
                Text(
                    text = "Ingrese el apellido:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                VisitLogTextField(
                    modifier = Modifier,
                    typedText = lastName,
                    onTextChange = { lastName = it },
                    placeholder = "Apellido:",
                    isSingleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Fila: Cédula + Número de personas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(0.6f)) {
                        Text(
                            text = "Ingrese la cédula:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        VisitLogTextField(
                            modifier = Modifier,
                            typedText = idNumber,
                            onTextChange = { idNumber = it },
                            placeholder = "Cédula:",
                            isSingleLine = true,
                            maxLines = 1
                        )
                    }

                    Column(modifier = Modifier.weight(0.4f)) {
                        Text(
                            text = "Cantidad de personas:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        NumberPicker(
                            modifier = Modifier.padding(end = 15.dp),
                            value = numPeople,
                            range = 1..20,
                            onValueChange = { numPeople = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Razón
                Text(
                    text = "Razón de ingreso:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                VisitLogTextField(
                    modifier = Modifier,
                    typedText = reason,
                    onTextChange = { reason = it },
                    placeholder = "Razón:",
                    isSingleLine = false,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Sección de Foto
                Text(
                    text = "Foto de la cédula:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Mostrar foto si existe
                if (selectedPhotoUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Gray)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedPhotoUri),
                            contentDescription = "Foto de cédula",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Botón para eliminar foto
                        IconButton(
                            onClick = {
                                selectedPhotoUri = null
                                selectedPhotoFile = null
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(
                                    Color.Black.copy(alpha = 0.6f),
                                    RoundedCornerShape(50)
                                )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Eliminar foto",
                                tint = Color.White
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No hay foto seleccionada",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones de cámara y galería
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PhotoButton(
                        text = "📷 Cámara",
                        onClick = { requestPermissionsAndTakePicture("CAMERA") },
                        modifier = Modifier.weight(1f),
                        isLoading = isLoadingPhoto
                    )

                    PhotoButton(
                        text = "🖼️ Galería",
                        onClick = { requestPermissionsAndTakePicture("GALLERY") },
                        modifier = Modifier.weight(1f),
                        isLoading = isLoadingPhoto
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de envío
                Button(
                    onClick = { sendVisitorLog() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Enviar Bitácora",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}


@Composable
private fun VisitLogTextField(
    modifier: Modifier,
    typedText: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    isSingleLine: Boolean,
    maxLines: Int
) {
    TextField(
        value = typedText,
        onValueChange = onTextChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        placeholder = { Text(text = placeholder) },
        singleLine = isSingleLine,
        maxLines = maxLines,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
private fun PhotoButton(
    modifier: Modifier,
    onClick: () -> Unit,
    text: String,
    isLoading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(8),
        colors = ButtonDefaults.buttonColors(
            containerColor = BasYellow,
            contentColor = Color.Black
        ),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = Color.Black,
                strokeWidth = 1.dp
            )
        } else {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

private fun getCurrentDateTimeISO(): String {
    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.US)
    //sdf.timeZone = TimeZone.getTimeZone("UTC") as java.util.TimeZone
    return sdf.format(Date())
}


private fun launchCamera(
    fileRepository: FileRepository,
    onUriCreated: (Uri, File) -> Unit,
    cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>
) {
    try {
        val (uri, file) = fileRepository.createCameraFileUri()
        onUriCreated(uri, file)
        cameraLauncher.launch(uri)
        Log.d("launchCamera", "Camera launched with URI: $uri")
    } catch (e: Exception) {
        Log.e("launchCamera", "Error launching camera", e)
    }
}