package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller.StationReportsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.stationreports.GuardShiftByStationData
import com.seguridadbas.multytenantseguridadbas.model.stationreports.IncidentsByStationData
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentsByStationScreen(
    modifier: Modifier = Modifier,
    navigateBackToBusiness: () -> Unit = {},
    onIncidentClicked: (incidentId: String) -> Unit = {},
    stationsReportsController: StationReportsController
){

    var allIncidentsByStation by remember { mutableStateOf(emptyList<IncidentsByStationData>()) }
    var filteredIncidents by remember { mutableStateOf(emptyList<IncidentsByStationData>()) }
    var tenantId by remember { mutableStateOf("") }
    var bearerToken by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    var showDateDialog by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }


    // Efecto para carga inicial
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            bearerToken = storedData.token.replace("\"","").trim()
            tenantId = dataStoreController.getTenantId().first().replace("\"","").trim()

            loadIncidents(null, null, bearerToken, tenantId,
                stationsReportsController,
            onSuccess = { incidents -> allIncidentsByStation = incidents},
                onError = {error -> Log.e("incidents by station screen", error)}
            )
        }
    }

    // Efecto para filtrar cuando cambia el texto de búsqueda
    LaunchedEffect(searchText, allIncidentsByStation) {
        if (searchText.isBlank()) {
            filteredIncidents = allIncidentsByStation
        } else {
            filteredIncidents = allIncidentsByStation.filter { incident ->
                incident.title.contains(searchText, ignoreCase = true) ||
                        incident.description.contains(searchText, ignoreCase = true)
            }
        }
    }

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text(text="Incidentes", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(
                            onClick = { navigateBackToBusiness() }
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "navigate back to business"
                            )
                        }
                    },
                    scrollBehavior = scrollBehaviour,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BasYellow, titleContentColor = Color.Black
                    ),
                    actions ={
                        IconButton(
                            onClick = { showDateDialog = true }
                        ){
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "fechas"
                            )
                        }
                    }
                )

                // Search Bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Buscar por título o descripción...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotBlank()) {
                            IconButton(
                                onClick = { searchText = "" }
                            ) {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                                    contentDescription = "Limpiar búsqueda"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ){
            paddingValues ->

        // Dialog para selección de fechas
        if (showDateDialog) {
            DateRangeDialog(
                onDismiss = { showDateDialog = false },
                onConfirm = { startDate, endDate ->
                    selectedStartDate = startDate
                    selectedEndDate = endDate
                    // Convertir fechas a string para la API (ajusta el formato según tu API)
                    val startDateStr = startDate.toString()
                    val endDateStr = endDate.toString()
                    loadIncidents(null, listOf(startDateStr, endDateStr), bearerToken, tenantId,
                        stationsReportsController,
                        onSuccess = { incidents -> allIncidentsByStation = incidents  },
                        onError = {error -> Log.e("incidents by station screen", error)}
                    )
                    showDateDialog = false
                },
                onClearFilters = {
                    selectedStartDate = null
                    selectedEndDate = null
                    searchText = ""
                    loadIncidents(null, null, bearerToken, tenantId,
                        stationsReportsController,
                        onSuccess = { incidents -> allIncidentsByStation = incidents},
                        onError = {error -> Log.e("incidents by station screen", error)}
                    )
                    showDateDialog = false
                }
            )
        }

        if (filteredIncidents.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BasBackground)
                    .padding(paddingValues)
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(filteredIncidents){ item ->
                    IncidentByStationItem(
                        item,
                        modifier,
                        onReportClicked = onIncidentClicked
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = if (searchText.isNotBlank() || selectedStartDate != null || selectedEndDate != null) {
                        "No se encontraron incidentes con los filtros aplicados"
                    } else {
                        "NO hay reportes"
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textDecoration = TextDecoration.Underline,
                    fontFamily = FontFamily.Monospace
                )

                if (searchText.isNotBlank() || selectedStartDate != null || selectedEndDate != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            selectedStartDate = null
                            selectedEndDate = null
                            searchText = ""
                            loadIncidents(null, null, bearerToken, tenantId,
                                stationsReportsController,
                                onSuccess = { incidents -> allIncidentsByStation = incidents},
                                onError = {error -> Log.e("incidents by station screen", error)}
                            )
                        }
                    ) {
                        Text("Limpiar filtros")
                    }
                }
            }
        }
    }
}




// Dialog para selección de rango de fechas
@Composable
fun DateRangeDialog(
    onDismiss: () -> Unit,
    onConfirm: (LocalDate?, LocalDate?) -> Unit,
    onClearFilters: () -> Unit
) {
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Text(
                text = "Seleccionar rango de fechas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Selector de fecha inicial
            OutlinedButton(
                onClick = { showStartDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = startDate?.toString() ?: "Seleccionar fecha inicial",
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Selector de fecha final
            OutlinedButton(
                onClick = { showEndDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = endDate?.toString() ?: "Seleccionar fecha final",
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onClearFilters
                ) {
                    Text("Limpiar")
                }

                Row {
                    OutlinedButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { onConfirm(startDate, endDate) },
                        enabled = startDate != null && endDate != null
                    ) {
                        Text("Aplicar")
                    }
                }
            }
        }
    }

    // Date Pickers
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            onDateSelected = { date ->
                startDate = date
                showStartDatePicker = false
            }
        )
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            onDateSelected = { date ->
                endDate = date
                showEndDatePicker = false
            }
        )
    }
}

// Date Picker Dialog (necesitarás implementar esto o usar uno existente)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    // Implementa tu DatePicker aquí
    // Puedes usar DatePicker de Material3 o una librería externa
    // Ejemplo básico:
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismissRequest
    ) {
        // Aquí iría tu implementación del DatePicker
        Text("Implementa tu DatePicker aquí")
    }
}


@Composable
private fun IncidentByStationItem(
    report: IncidentsByStationData,
    modifier: Modifier = Modifier,
    onReportClicked: (reportId: String) -> Unit = {}
){

    Box(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(24))
            .border(
                2.dp,
                BasGray,
                shape = RoundedCornerShape(0, 24, 0, 24)
            )
            .shadow(2.dp)
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
            .clickable {
                onReportClicked(report.id)
            }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.Start
        ){

            Text(
                text = report.title,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(top = 6.dp))

            Text(
                text = report.date,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.padding(top = 6.dp))

            Text(
                text = report.description,
                maxLines = 3,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


 fun loadIncidents(
    title: String? = null,
    rangeDate: List<String>? = null,
    bearerToken: String,
    tenantId: String,
    stationsReportsController: StationReportsController,
    onSuccess: (List<IncidentsByStationData>) -> Unit,
    onError: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        if (!bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty()) {
            val result = stationsReportsController.getIncidents(
                "Bearer $bearerToken",
                tenantId,
                title,
                rangeDate
            )

            withContext(Dispatchers.Main) {
                when(result) {
                    is Resource.Success -> {
                        val incidents = result.data?.toList() ?: emptyList()
                        onSuccess(incidents)
                    }
                    is Resource.Error -> {
                        val errorMsg = "No se pudieron traer los incidentes ${result.message}"
                        Log.e("incidents by station screen", errorMsg)
                        onError(errorMsg)
                    }
                    else -> {
                        val errorMsg = "No se pudieron traer los incidentes"
                        Log.e("incidents by station screen", errorMsg)
                        onError(errorMsg)
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                onError("Token o tenantId vacíos")
            }
        }
    }
}