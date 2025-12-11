package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.guardshiftscontroller.GuardShiftsController
import com.seguridadbas.multytenantseguridadbas.controllers.shiftscontroller.ShiftsController
import com.seguridadbas.multytenantseguridadbas.core.navigation.GuardsShiftDetail
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.shifts.ShiftsData
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
fun ShiftDetailScreen(
    modifier: Modifier = Modifier,
    shiftId: String = "",
    navigateBackToShifts: () -> Unit,
    shiftsController: ShiftsController
) {

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val sdf = SimpleDateFormat("dd/m/yyyy hh:mm:ss")
    val currentDate = sdf.format( Date() )

    var shiftData by remember { mutableStateOf<ShiftsData?>(null) }
    var tenantId by remember { mutableStateOf("") }
    var bearerToken by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dataStoreController = DataStoreController( context )


    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()

            bearerToken = storedData.token
            bearerToken = bearerToken.replace("\"","").trim()

            tenantId = dataStoreController.getTenantId().first()
            tenantId = tenantId.replace("\"","").trim()

            loadShiftDetails(
                bearerToken, tenantId, shiftId, shiftsController,
                onSuccess = { shiftObj -> shiftData = shiftObj  },
                onError = { errorMsg -> Log.e( "shift detail screen", errorMsg ) }
            )
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll( scrollBehaviour.nestedScrollConnection ),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Turno: ${shiftData?.shiftId}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )


                },
                navigationIcon = {
                    IconButton(
                        onClick ={ navigateBackToShifts() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "navigate back to shifts"
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
    ){ paddingVals ->

        Column(
            modifier = Modifier.fillMaxSize()
                .background(BasBackground)
                .padding(paddingVals)
        ){
            //Hora Actual
            Text(
                modifier = modifier.align(Alignment.End).padding(end = 10.dp, top = 5.dp),
                text = currentDate,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = modifier.height(8.dp))

            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp, top = 5.dp),
                text = "Nombre: ${shiftData?.guardFirstName} ${shiftData?.guardLastName}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = "Correo: ${shiftData?.guardEmail}",
                fontSize = 14.sp,
            )

            Spacer(modifier = modifier.height(8.dp))
            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )


            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = "Estación: ${shiftData?.stationName}", fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text =  "Horario de atención\n${shiftData?.stationSchedule}", fontSize = 14.sp,
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = shiftData?.startingTimeInDay ?: "00:00:00", fontSize = 16.sp,
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = shiftData?.finishTimeInDay ?: "00:00:00", fontSize = 16.sp,
            )
            Spacer(modifier = modifier.height(8.dp))

            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )


            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = "Comienzo del turno: ${shiftData?.startTime}", fontSize = 16.sp,
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = "Fin del turno: ${shiftData?.endTime}", fontSize = 16.sp,
            )
            Spacer(modifier = modifier.height(8.dp))
        }


    }





}



private fun loadShiftDetails(
    bearerToken: String, tenantId: String, shiftId: String,
    shiftsController: ShiftsController,
    onSuccess: (ShiftsData) -> Unit,
    onError: (String) -> Unit
){

    CoroutineScope(Dispatchers.IO).launch{
        if(!bearerToken.isNullOrEmpty() || !tenantId.isNullOrEmpty() ){
            val result = shiftsController.getShiftDetail("Bearer $bearerToken", tenantId, shiftId)

            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Success -> { onSuccess(result.data!!) }
                    is Resource.Error -> { onError(result.message.toString()) }
                    else -> {  onError(result.message.toString())  }
                }
            }

        }else{
            withContext(Dispatchers.Main){
                onError("token o tenantId inválidos")
            }
        }
    }

}






