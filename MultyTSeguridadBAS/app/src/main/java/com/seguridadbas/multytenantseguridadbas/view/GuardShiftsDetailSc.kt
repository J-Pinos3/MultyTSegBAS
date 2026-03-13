package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.guardshiftscontroller.GuardShiftsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.guard_shifts.GuardShift
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
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
fun GuardShiftsDetailSc(
    modifier : Modifier = Modifier,
    guardShiftId: String = "",
    navigateBackToAllGuardsShifts: () -> Unit = {},
    guardShiftsController: GuardShiftsController
){

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val sdf = SimpleDateFormat("dd/m/yyyy hh:mm:ss")
    val currentDate = sdf.format( Date() )

    var guardShiftData by remember { mutableStateOf<GuardShift?>(null) }
    var tenantId by remember { mutableStateOf("") }
    var bearerToken by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()

            bearerToken = storedData.token
            bearerToken = bearerToken.replace("\"","").trim()

            tenantId = dataStoreController.getTenantId().first()
            tenantId = tenantId.replace("\"","").trim()

            loadGuardShiftDetail(
                bearerToken, tenantId, guardShiftId,
                guardShiftsController,
                onSuccess = { guardShiftObj -> guardShiftData = guardShiftObj  },
                onError = { errorMsg -> Log.e( "guard shift detail", errorMsg ) }
            )
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
                        text = "Turno: ${guardShiftData?.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )


                },
                navigationIcon = {
                    IconButton(
                        onClick ={ navigateBackToAllGuardsShifts() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "navigate back to business"
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
    ){
            paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BasBackground)
                .padding(paddingValues)
        ){
            //Hora Actual
            Text(
                modifier = modifier.align(Alignment.End).padding(end = 10.dp, top = 5.dp),
                text = currentDate,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = modifier.height(8.dp))

            //Estación
            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )
            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text=guardShiftData?.stationName ?: "----",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text=guardShiftData?.startingTimeInDay?:"00:00:00",//hora inicio
                fontSize = 14.sp,
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                //finishtimeinday
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text=guardShiftData?.finishTimeInDay ?:"00:00:00",//hora finalizacion
                fontSize = 14.sp,
            )

            Spacer(modifier = modifier.height(8.dp))

            Row(
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                //stationschedule
                Text(
                    text=guardShiftData?.shiftSchedule ?:"",
                    fontSize = 14.sp,
                )

                Text(
                    text = "N° Guardias: ${guardShiftData?.numberOfGuardsInStation}"
                )
            }
            Spacer(modifier = modifier.height(8.dp))

            //Turno realizado por Guardia guardShift
            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )
            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text= guardShiftData?.guardName ?:"Guardia",
                fontSize = 14.sp
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = guardShiftData?.guardNameId ?:"Credenciales: 1111111111",
                fontSize = 14.sp
            )

            Spacer(modifier = modifier.height(8.dp))

            //datos del turno Realizado
            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )
            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text="N° Incidentes ocurridos: ${guardShiftData?.numberOfIncidentsDuringShift }",
                fontSize = 14.sp
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text="N° Patrullajes realizados: ${guardShiftData?.numberOfPatrolsDuringShift}",
                fontSize = 14.sp
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(horizontal = 10.dp),
                text="Observaciones: ${guardShiftData?.observations ?: "No hay observaciones"}",
                fontSize = 14.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = modifier.height(8.dp))
            Text(
                //punch in time, inicio del turno-patrullaje
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text="Inicio del turno: ${guardShiftData?.punchInTime}",
                fontSize = 14.sp
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                //punch out time, inicio del turno-patrullaje
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text="Fin del turno: ${guardShiftData?.punchOutTime}",
                fontSize = 14.sp
            )

        }


    }



}


private fun loadGuardShiftDetail(
    bearerToken: String, tenantId: String, guardShiftID: String,
    guardShiftsController: GuardShiftsController,
    onSuccess: (GuardShift) -> Unit,
    onError: (String) -> Unit
){
    //Log.v("data failing", bearerToken + " - - - - - " + tenantId + " + + + + + " + guardShiftID)
    CoroutineScope(Dispatchers.IO).launch {
        if(!bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty() ){
            val result = guardShiftsController.getGuardShiftsDetail("Bearer $bearerToken", tenantId, guardShiftID)
            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Success -> { onSuccess(result.data!!) }
                    is Resource.Error -> { onError(result.message.toString()) }
                    else -> { onError(result.message.toString()) }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                onError("token o tenantId inválidos")
            }
        }
    }

}