package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.model.GuardDataResponse
import com.seguridadbas.multytenantseguridadbas.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AssignedGuardDetailsScreen(
    guard: GuardDataResponse = GuardDataResponse(),
    onBack: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    // Función de refresh simulada
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                // Aquí iría la lógica para recargar los datos del guardia desde el repositorio/ViewModel
                delay(1500) 
                isRefreshing = false
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.splashscreenbackground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Toolbar
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = guard.fullName,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BasBlue
                )
            )

            // Contenedor con Pull to Refresh
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                // Contenido Scrolleable
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header del guardia
                    GuardHeader(guard)

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Card: Información Personal
                    PersonalInformationCard(guard)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card: Documentos
                    DocumentsCard(guard)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // Indicador de Pull to Refresh
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = Color.White,
                    contentColor = BasBlue
                )
            }
        }
    }
}

@Composable
fun GuardHeader(guard: GuardDataResponse) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar / Ícono
        Box(
            modifier = Modifier
                .size(80.dp)
                .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = guard.fullName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = guard.gender,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Badge de estado
            StatusBadgeDetail(isOnDuty = guard.isOnDuty)
        }
    }
}

@Composable
fun StatusBadgeDetail(isOnDuty: Boolean) {
    val borderColor = if (isOnDuty) Color.Green else Color.Red
    val statusText = if (isOnDuty) "EN TURNO" else "FUERA DE TURNO"

    Surface(
        color = BasGrayDark,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(borderColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = statusText,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PersonalInformationCard(guard: GuardDataResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Información personal",
                color = BasBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(R.drawable.ic_bloodtype, "Tipo de Sangre", guard.bloodType)
            InfoRow(R.drawable.ic_calendar, "Fecha de Nacimiento", guard.birthDate)
            InfoRow(R.drawable.ic_place, "Lugar de Nacimiento", guard.birthPlace)
            InfoRow(R.drawable.ic_marital_status, "Estado Civil", guard.maritalStatus)
            InfoRow(R.drawable.ic_school, "Nivel Académico", guard.academicInstruction)
            InfoRow(R.drawable.ic_home, "Dirección", guard.address ?: "No disponible")
            InfoRow(R.drawable.ic_identification_card, "Credencial", guard.guardCredentials)
        }
    }
}

@Composable
fun InfoRow(icon: Int, label: String, value: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = icon), contentDescription = null, tint = BasBlue, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Text(text = value, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp)
    }
}

@Composable
fun DocumentsCard(guard: GuardDataResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Documentos",
                color = BasBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Aquí se listarían los documentos. Como GuardDataResponse tiene listas de Any?, 
            // simularemos algunos documentos o usaremos los campos disponibles si se mapean.
            
            val demoDocuments = listOf(
                DocumentItem("Récord Policial", "PDF"),
                DocumentItem("Imagen de Credencial", "JPG")
            )

            demoDocuments.forEach { doc ->
                DocumentRow(doc)
            }
        }
    }
}

data class DocumentItem(val name: String, val type: String)

@Composable
fun DocumentRow(doc: DocumentItem) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = if (doc.type == "PDF") R.drawable.ic_download_pdf else R.drawable.ic_download_image
            Icon(painter = painterResource(icon), contentDescription = null, tint = BasBlue, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = doc.name, color = Color.Black, fontSize = 14.sp, modifier = Modifier.weight(1f))
            IconButton(onClick = { /* Lógica de descarga */ }) {
                Icon(painter = painterResource(R.drawable.ic_download_file), contentDescription = "Descargar", tint = BasBlue)
            }
        }
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 0.5.dp)
    }
}
