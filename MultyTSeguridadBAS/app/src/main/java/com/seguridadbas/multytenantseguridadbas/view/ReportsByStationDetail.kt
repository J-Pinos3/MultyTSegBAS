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
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller.StationReportsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.stationreports.ReportsByStationData
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsByStationDetail(
    modifier: Modifier = Modifier,
    reportId: String = "",
    navigateBackToReportsByStation: () -> Unit = {},
    stationReportsController: StationReportsController
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())

    var reportData by remember {  mutableStateOf<ReportsByStationData?>(null) }
    var tenantId by remember { mutableStateOf("") }
    var bearerToken by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            bearerToken = storedData.token
            bearerToken =bearerToken.replace("\"","").trim()

            tenantId = dataStoreController.getTenantId().first()
            tenantId = tenantId.replace("\"","").trim()

            if( !bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty() ){
                val result = stationReportsController.getReportByStationDetail(
                    "Bearer $bearerToken",    tenantId, reportId
                )

                when(result){
                    is Resource.Success -> {
                        reportData = result.data
                    }

                    is Resource.Error-> {
                        Log.e("Report grd-shift detail","No se pudo traer los datos del reporte: ${result.message}")
                    }

                    else ->{
                        Log.e("Report grd-shift detail","No se pudo traer los datos del reporte: ")
                    }
                }

            }
        }
    }


    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Detalle del turno $reportId", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick ={ navigateBackToReportsByStation() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "navigate back to business"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
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
                modifier= modifier.align(Alignment.Start).padding(start = 10.dp),
                text =  reportData?.station?.stationName.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 20.dp),
                text = reportData?.station?.startingTimeInDay ?: "00:00:0",
                fontSize = 14.sp
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 20.dp),
                text = reportData?.station?.finishTimeInDay ?: "00:00:0",
                fontSize = 14.sp
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
                    text=reportData?.station?.stationSchedule ?:"",
                    fontSize = 14.sp,
                )

                Text(
                    text = "N° Guardias: ${reportData?.station?.numberOfGuardsInStation}"
                )
            }
            Spacer(modifier = modifier.height(8.dp))

            //titulo del reporte
            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text=reportData?.generatedDate ?: "----",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text=reportData?.title ?: "----",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text=reportData?.content ?: "----",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }


    }

}

