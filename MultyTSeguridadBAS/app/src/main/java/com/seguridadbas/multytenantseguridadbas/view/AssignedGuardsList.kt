package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantguardscontroller.TenantGuardsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.Guard
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBlue
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignedGuardsList(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onGuardClick: (String) -> Unit = {},
    tenantGuardsController: TenantGuardsController = hiltViewModel()
) {
    val context = LocalContext.current
    val dataStoreController = remember { DataStoreController(context) }
    val scope = rememberCoroutineScope()

    var guardsList by remember { mutableStateOf<List<Guard>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    guardsList = listOf(
        Guard("Jhon@mail1.com", "Jhon", "1", "Doe", "123456789", "Masculino", true),
        Guard("Jhon@mail1.com", "Jhon", "1", "Doe", "123456789", "Masculino", true),

    )

    /*
    LaunchedEffect(Unit) {
        scope.launch {
            val storedData = dataStoreController.getDataFromStore().first()
            val token = storedData.token.replace("\"", "").trim()
            val tenantId = dataStoreController.getTenantId().first().replace("\"", "").trim()

            if (token.isNotEmpty() && tenantId.isNotEmpty()) {
                val result = tenantGuardsController.getSecGuards("Bearer $token", tenantId)
                when (result) {
                    is Resource.Success -> {
                        guardsList = result.data ?: emptyList()
                    }
                    is Resource.Error -> {
                        Log.e("AssignedGuardsList", "Error: ${result.message}")
                    }
                    else -> {}
                }
            }
            isLoading = false
        }
    }
    */

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Guardias Asignados",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Fondo
            Image(
                painter = painterResource(id = R.drawable.splashscreenbackground),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "PERSONAL ASIGNADO",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = BasYellow)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(guardsList) { guard ->
                            GuardCardItem(guard = guard, onClick = { onGuardClick(guard.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GuardCardItem(
    guard: Guard,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BasBlue.copy(alpha = 0.8f) // Similar al toolbar pero con algo de transparencia
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${guard.firstName} ${guard.lastName}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = if (guard.gender.isNotEmpty()) guard.gender else "No especificado",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }

            // Estado (Status Badge)
            StatusBadge(isOnDuty = guard.isOnDuty)

            Spacer(modifier = Modifier.width(8.dp))

            // Acción (Flecha)
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun StatusBadge(isOnDuty: Boolean) {
    val statusText = if (isOnDuty) "EN TURNO" else "LIBRE"
    val statusColor = if (isOnDuty) Color.Green else Color.Red

    Surface(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = statusColor,
                shape = RoundedCornerShape(4.dp)
            ),
        color = Color.DarkGray.copy(alpha = 0.5f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(statusColor, CircleShape)
            )
            Text(
                text = statusText,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
