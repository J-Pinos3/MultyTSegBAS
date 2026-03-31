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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.incidentsdetailcontroller.IncidentDetailController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.stationreports.IncidentDetailResponse
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentsByStationDetail(
    modifier: Modifier = Modifier,
    incidentID: String = "",
    navigateBackToIncidents: () -> Unit = {},
    incidentDetailController: IncidentDetailController = hiltViewModel()
){
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())

    var incidentData by remember{ mutableStateOf<IncidentDetailResponse?>(null) }

    val context = LocalContext.current
    val dataStoreController = remember { DataStoreController(context) }

    LaunchedEffect(Unit){
        val storedData = withContext(Dispatchers.IO){
            dataStoreController.getDataFromStore().first()
        }

        var tenantId = withContext(Dispatchers.IO){
            dataStoreController.getTenantId().first()
        }

        tenantId = tenantId.replace("\"","").trim()
        val token = storedData.token.replace("\"","").trim()


        loadIncidentDetail(
            token, tenantId, incidentID, incidentDetailController,
            onSuccess = { data -> incidentData = data },
            onError = {}
        )
    }


    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text="Detalle del incidente $incidentID", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateBackToIncidents() }
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "navigate back to incidents"
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
        print(paddingVals)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BasBackground)
                .padding(paddingVals)
        ){

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
                modifier =modifier.align(Alignment.Start).padding(start = 10.dp),
                text = incidentData?.title ?: "",
                fontSize = 16.sp, fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Row(
               modifier = modifier.fillMaxWidth().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text( text = "Prioridad: " )
                Text( text = incidentData?.priority ?: "" )
            }
            Spacer(modifier = modifier.height(8.dp))

            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text( text = "Cliente: " )
                Text( text = incidentData?.client.let { it?.name + " " + it?.lastName } ?: "" )
            }

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier =modifier.align(Alignment.Start).padding(start = 10.dp),
                text = "Dirección",
                fontSize = 14.sp, fontWeight = FontWeight.Black
            )
            Spacer(modifier = modifier.height(3.dp))

            Text(
                modifier =modifier.align(Alignment.Start).padding(start = 10.dp),
                text = incidentData?.client?.address ?: "",
                fontSize = 16.sp
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier =modifier.align(Alignment.Start).padding(start = 10.dp),
                text = "Guardia: ",
                fontSize = 14.sp, fontWeight = FontWeight.Black
            )
            Spacer(modifier = modifier.height(3.dp))

            Text(
                modifier =modifier.align(Alignment.Start).padding(start = 10.dp),
                text = incidentData?.guardName?.fullName ?: "",
                fontSize = 16.sp
            )

            Spacer(modifier = modifier.height(8.dp))







        }

    }


}


private suspend fun loadIncidentDetail(
    token: String, tenantId: String, incidentId: String,
    incidentDetailController: IncidentDetailController,
    onSuccess: (IncidentDetailResponse) -> Unit,
    onError: () -> Unit
){

    if(!token.isNullOrEmpty() && !tenantId.isNullOrEmpty()){

        val result = withContext(Dispatchers.IO){
            incidentDetailController.getIncidentDetail(token, tenantId, incidentId)
        }

        when(result){
            is Resource.Success -> {  result.data.let { onSuccess(it!!) } }

            is Resource.Error -> {
                Log.e("IncidentDetailSc","No se pudo traer los datos del incidente")
                onError()
            }

            else -> {
                Log.e("IncidentDetailSc","No se pudo traer los datos del incidente")
                onError()
            }
        }


    }else{
        onError()
    }


}