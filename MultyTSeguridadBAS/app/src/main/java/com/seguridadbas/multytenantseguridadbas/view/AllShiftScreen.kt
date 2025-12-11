@file:OptIn(ExperimentalMaterial3Api::class)
package com.seguridadbas.multytenantseguridadbas.view

import android.hardware.Sensor
import android.hardware.SensorPrivacyManager
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.window.Dialog
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.shiftscontroller.ShiftsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.shifts.ShortShiftData
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllShiftScreen(
    modifier: Modifier = Modifier,
    navigateBackToMore: () -> Unit,
    onShiftClicked: (shiftId: String) -> Unit = {},
    shiftsController: ShiftsController
){

    var allShifts by remember { mutableStateOf( emptyList<ShortShiftData>() ) }

    var tenantId by remember { mutableStateOf("") }
    var bearerToken by remember { mutableStateOf("") }


    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    var showDateDialog by remember { mutableStateOf(false) }
    var selectedStartDate by remember { mutableStateOf<String?>(null) }
    var selectedEndDate by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            bearerToken = storedData.token.replace("\"","").trim()
            tenantId = dataStoreController.getTenantId().first().replace("\"","").trim()

            loadShifts(
                null,   bearerToken,    tenantId,     shiftsController,
                onSuccess = { shifts -> allShifts = shifts  },
                onError = { error ->  Log.e("shifts screen", error) }
            )
        }
    }

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(

        modifier= modifier.fillMaxSize().nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("Turnos generados", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(  onClick = { navigateBackToMore() }  ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "navigate back to more"
                            )
                        }
                    },
                    scrollBehavior = scrollBehaviour,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BasYellow, titleContentColor = Color.Black
                    ),
                    actions = {
                        IconButton(  onClick = { showDateDialog = true }  ) {
                            Icon(
                                imageVector = Icons.Default.DateRange, contentDescription = "fechas"
                            )
                        }
                    }
                )
            }
        }

    ){ paddingVals ->


        if(showDateDialog){
            DateRangeDialog(
                onDismiss = {
                    selectedStartDate = null
                    selectedEndDate = null

                    loadShifts(
                        null,   bearerToken,    tenantId,     shiftsController,
                        onSuccess = { shifts -> allShifts = shifts  },
                        onError = { error ->  Log.e("shifts screen", error) }
                    )

                    showDateDialog = false
                },
                onConfirm = { startDate, endDate ->
                    selectedStartDate = startDate
                    selectedEndDate  = endDate

                    val startDateStr = startDate.toString()
                    val endDateStr = endDate.toString()

                    loadShifts(
                        listOf(startDateStr, endDateStr),   bearerToken,    tenantId,     shiftsController,
                        onSuccess = { shifts -> allShifts = shifts  },
                        onError = { error ->  Log.e("shifts screen", error) }
                    )
                    showDateDialog = false
                }
            )
        }

        if(allShifts.isNotEmpty()){
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(BasBackground)
                    .padding(paddingVals)
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items( allShifts ){ item ->
                    ShiftItem(
                        item, Modifier,
                        onShiftClicked = onShiftClicked
                    )

                }
            }
        }else{
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = if(selectedStartDate != null || selectedEndDate != null){
                        "No se encontraron turnos con los filtros aplicados"
                    }else{
                        "No hay turnos generados"
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black, textDecoration = TextDecoration.Underline,
                    fontFamily = FontFamily.Monospace
                )

                if(selectedStartDate != null || selectedEndDate != null){
                    Spacer(Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            selectedStartDate = null
                            selectedEndDate = null

                            loadShifts(
                                null,   bearerToken,    tenantId,     shiftsController,
                                onSuccess = { shifts -> allShifts = shifts  },
                                onError = { error ->  Log.e("shifts screen", error) }
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


@Composable
private fun DateRangeDialog(
    onDismiss: () -> Unit,
    onConfirm: (String?,  String?) -> Unit,
){
    var startDate by remember { mutableStateOf<String?>(null) }
    var endDate by remember { mutableStateOf<String?>(null) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp)).padding(10.dp)
        ){
            Text(
                text = "Seleccionar rango de fechas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedButton(
                onClick = { showStartDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    startDate.toString() ?: "Fecha inicial", Modifier.padding(8.dp)
                )
            }

            Spacer( modifier = Modifier.height(8.dp) )

            OutlinedButton(
                onClick = { showEndDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    endDate.toString() ?: "Fecha final", Modifier.padding(8.dp)
                )
            }

            Spacer( modifier = Modifier.height(12.dp) )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                OutlinedButton( onClick = onDismiss ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { onConfirm(startDate, endDate) },
                    enabled = startDate!=null && endDate!=null
                ) {
                    Text("Aplicar")
                }
            }
        }
    }

    val format  = SimpleDateFormat("yyyy-MM-dd")

    if(showStartDatePicker){
        DatePickerDialogo(
            onDismissRequest = { showStartDatePicker = false },
            onDateSelected = {  date ->
                startDate = format.format( Date(date ?: 0L) ) + "T00:00:00.000Z"
                showStartDatePicker  =false
            }
        )
    }

    if( showEndDatePicker ){
        DatePickerDialogo(
            onDismissRequest = { showEndDatePicker = false },
            onDateSelected = {  date ->
                endDate = format.format( Date(date ?: 0L) ) + "T23:59:00.000Z"
                showEndDatePicker  =false
            }
        )
    }
}


@Composable
private fun DatePickerDialogo(
    onDismissRequest: () -> Unit,
    onDateSelected: (Long?) -> Unit,
){
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        onDismissRequest()
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton( onClick = onDismissRequest ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@Composable
private fun ShiftItem(
    shift: ShortShiftData,
    modifier: Modifier,
    onShiftClicked: (shiftId: String) -> Unit = {}
){

    Box(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(24))
            .border(
                2.dp,
                BasGray,
                shape = RoundedCornerShape(24, 24, 24, 24)
            )
            .shadow(2.dp)
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White)
            .clickable {
                onShiftClicked(shift.id)
            }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.Start
        ){

            Text(
                text = shift.guardName,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(top = 6.dp))

            Text(
                text = shift.stationName,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.padding(top = 6.dp))

            Text(
                text = shift.stationSchedule,
                maxLines = 3,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(top = 6.dp))

            Text(
                text = shift.startDate,
                maxLines = 3,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

        }
    }

}

private fun loadShifts(
    startTimeRange: List<String>? = null,
    bearerToken: String, tenantId: String, shiftsController: ShiftsController,
    onSuccess: (List<ShortShiftData>) -> Unit,
    onError: (String) -> Unit
){
    CoroutineScope(Dispatchers.IO).launch {
        if( !bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty() ){
            val result = shiftsController.getAllShifts("Bearer $bearerToken", tenantId, startTimeRange)

            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Success -> {  onSuccess( result.data?.toList() ?: emptyList() )  }
                    is Resource.Error -> {    onError(result.message.toString())      }
                    else -> {   onError(result.message.toString())    }
                }

            }
        }else{
            withContext(Dispatchers.Main){
                onError("token o tenantId inválidos")
            }

        }
    }
}