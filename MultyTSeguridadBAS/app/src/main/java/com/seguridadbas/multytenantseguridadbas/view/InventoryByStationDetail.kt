package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller.StationReportsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.stationreports.InventoryByStationData
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import com.seguridadbas.multytenantseguridadbas.view.customwidget.BooleanItem
import com.seguridadbas.multytenantseguridadbas.view.customwidget.BooleanItemRow
import com.seguridadbas.multytenantseguridadbas.view.customwidget.InventorySection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryByStationDetail(
    modifier: Modifier = Modifier,
    reportId: String = "",
    navigateBackToInventoryByStat: () -> Unit = {},
    stationsReportsController: StationReportsController
) {
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Estados
    var reportData by remember { mutableStateOf<InventoryByStationData?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Formateador de fecha (se instancia una sola vez)
    val dateFormat = remember { SimpleDateFormat("dd/M/yyyy hh:mm:ss") }
    val currentDate = remember { dateFormat.format(Date()) }

    // Contexto y controladores
    val context = LocalContext.current
    val dataStoreController = remember { DataStoreController(context) }

    // Carga de datos
    LaunchedEffect(reportId) {
        loading = true
        error = null

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val storedData = dataStoreController.getDataFromStore().first()
                val bearerToken = storedData.token.replace("\"", "").trim()
                val tenantId = dataStoreController.getTenantId().first().replace("\"", "").trim()

                if (bearerToken.isNotEmpty() && tenantId.isNotEmpty()) {
                    val result = stationsReportsController.getInventoryByStationDetail(
                        "Bearer $bearerToken",
                        tenantId,
                        reportId
                    )

                    withContext(Dispatchers.Main) {
                        when (result) {
                            is Resource.Success -> {
                                reportData = result.data
                                loading = false
                            }
                            is Resource.Error -> {
                                error = "Error: ${result.message}"
                                loading = false
                                Log.e("InventoryDetail", error!!)
                            }
                            else -> {
                                error = "Error desconocido"
                                loading = false
                            }
                        }
                    }
                } else {
                    error = "Credenciales inválidas"
                    loading = false
                }
            } catch (e: Exception) {
                error = "Error de conexión: ${e.message}"
                loading = false
                Log.e("InventoryDetail", "Exception", e)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            InventoryDetailTopBar(
                reportId = reportId,
                navigateBack = navigateBackToInventoryByStat,
                scrollBehaviour = scrollBehaviour
            )
        }
    ) { paddingValues ->
        InventoryDetailContent(
            modifier = Modifier.padding(paddingValues),
            loading = loading,
            error = error,
            currentDate = currentDate,
            reportData = reportData
        )
    }
}

// TopBar separado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InventoryDetailTopBar(
    reportId: String,
    navigateBack: () -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Detalle del Reporte",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Volver"
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

// Contenido principal
@Composable
private fun InventoryDetailContent(
    modifier: Modifier = Modifier,
    loading: Boolean,
    error: String?,
    currentDate: String,
    reportData: InventoryByStationData?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = BasBackground)
            .padding(horizontal = 16.dp)
    ) {
        when {
            loading -> {
                LoadingState()
            }
            error != null -> {
                ErrorState(error = error)
            }
            reportData == null -> {
                EmptyState()
            }
            else -> {
                // Fecha actual
                Text(
                    text = currentDate,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.End
                )

                // Contenido del reporte
                InventoryReportContent(reportData = reportData)
            }
        }
    }
}

// Estados de UI
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(error: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_dialog_alert),
            contentDescription = "Error",
            tint = Color.Red,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = error,
            color = Color.Red,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No se encontró el reporte",
            color = Color.Gray
        )
    }
}

// Contenido del reporte
@Composable
private fun InventoryReportContent(
    reportData: InventoryByStationData
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        // Estación
        item {
            InventorySection(title = "Estación") {
                Text(
                    text = reportData.belongsToStation ?: "No especificado",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Items con número de serie y tipo
        item {
            InventorySection(title = "Equipamiento Principal") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BooleanItemRow(
                        item = BooleanItem(
                            label = "Armadura",
                            value = reportData.armor,
                            serialNumber = reportData.armorSerialNumber,
                            type = reportData.armorType
                        )
                    )

                    BooleanItemRow(
                        item = BooleanItem(
                            label = "Arma",
                            value = reportData.gun,
                            serialNumber = reportData.gunSerialNumber,
                            type = reportData.gunType
                        )
                    )

                    BooleanItemRow(
                        item = BooleanItem(
                            label = "Radio",
                            value = reportData.radio,
                            serialNumber = reportData.radioSerialNumber,
                            type = reportData.radioType
                        )
                    )
                }
            }
        }

        // Items booleanos simples
        item {
            InventorySection(title = "Equipamiento Adicional") {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(
                        BooleanItem("Tolete", reportData.tolete),
                        BooleanItem("Pito", reportData.pito),
                        BooleanItem("Linterna", reportData.linterna),
                        BooleanItem("Vitácora", reportData.vitacora),
                        BooleanItem("Cinto Completo", reportData.cintoCompleto),
                        BooleanItem("Poncho de Aguas", reportData.ponchoDeAguas),
                        BooleanItem("Detector de Metales", reportData.detectorDeMetales),
                        BooleanItem("Caseta", reportData.caseta)
                    ).forEach { item ->
                        BooleanItemRow(item = item)
                    }
                }
            }
        }

        // Transporte
        item {
            InventorySection(title = "Transporte") {
                Text(
                    text = reportData.transportation ?: "No especificado",
                    fontSize = 14.sp
                )
            }
        }

        // Observaciones
        item {
            InventorySection(title = "Observaciones") {
                Text(
                    text = reportData.observations ?: "Sin observaciones",
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}