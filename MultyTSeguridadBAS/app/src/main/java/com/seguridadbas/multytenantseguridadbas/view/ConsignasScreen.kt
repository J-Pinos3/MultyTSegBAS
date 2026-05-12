package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun ConsignasScreen(
    onBackClick: () -> Unit = {},
    toolbarColor: Color = BasBlue,
    pillButtonColor: Color = Color(0xFF8AAAE5)
) {
    var showDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Listas preparadas para integración futura
    val pendingConsignas = emptyList<String>()
    val completedConsignas = emptyList<String>()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                delay(2000) // Simulación de carga de API
                isRefreshing = false
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo de pantalla
        Image(
            painter = painterResource(id = R.drawable.splashscreenbackground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Toolbar y Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(toolbarColor)
                        .statusBarsPadding()
                        .padding(bottom = 24.dp)
                ) {
                    Column {
                        // Toolbar superior
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Atrás",
                                    tint = Color.Black
                                )
                            }

                            Button(
                                onClick = { showDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = pillButtonColor),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Agregar Tarea",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }
                        }

                    }
                }

                // Contenido de las cards
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Título de la pantalla
                    Text(
                        text = "Consignas",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    )

                    // Card de Consignas Pendientes
                    ConsignaCard(
                        title = "Consignas Pendientes",
                        items = pendingConsignas,
                        emptyMessage = "No hay consignas pendientes. Cuando requiera, agregue una consigna."
                    )

                    // Card de Consignas Completadas
                    ConsignaCard(
                        title = "Consignas Completadas",
                        items = completedConsignas,
                        emptyMessage = "Aún no se ha completado ninguna consigna. Cuando requiera agregue alguna."
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

        // Diálogo para agregar consigna
        if (showDialog) {
            AddConsignaDialog(
                onDismiss = { showDialog = false },
                onConfirm = { task, date ->
                    // Aquí se manejaría la lógica de guardado en el futuro
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddConsignaDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var taskText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cabecera con título y botón de cerrar
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Agregar Consigna",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Black
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Campo: Tarea a realizar
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Tarea a realizar:",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                    OutlinedTextField(
                        value = taskText,
                        onValueChange = { taskText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        placeholder = { Text("Ej: cerrar ventana trasera", fontSize = 14.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Campo: Fecha y hora para realizar tarea
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Fecha y hora para realizar tarea:",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Selector de fecha y hora (UI personalizada similar a la imagen)
                    WheelDateTimePicker()
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de acción: Aceptar
                Button(
                    onClick = { onConfirm(taskText, "Mock Date") },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD100)), // Amarillo
                    shape = RoundedCornerShape(50),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "Aceptar",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WheelDateTimePicker() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        contentAlignment = Alignment.Center
    ) {
        // Indicador de selección central
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
        )
        
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mocks de las columnas del selector
            WheelColumn(listOf("Dom 3 may", "Lun 4 may", "hoy", "Mié 6 may", "Jue 7 may"), 2)
            WheelColumn(listOf("2", "3", "4", "5", "6"), 2)
            WheelColumn(listOf("30", "35", "40", "45", "50"), 2)
            WheelColumn(listOf("a.m.", "p.m."), 1)
        }
    }
}

@Composable
fun WheelColumn(items: List<String>, selectedIndex: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items.forEachIndexed { index, s ->
            val isSelected = index == selectedIndex
            Text(
                text = s,
                fontSize = if (isSelected) 17.sp else 13.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                color = if (isSelected) Color.Black else Color.Gray.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun ConsignaCard(
    title: String,
    items: List<String>,
    emptyMessage: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(min = 250.dp) // Altura mínima para que se vea similar a la imagen
        ) {
            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emptyMessage,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        // Aquí iría el diseño de cada item de consigna en el futuro
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .background(Color.LightGray, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item)
                        }
                    }
                }
            }
        }
    }
}
