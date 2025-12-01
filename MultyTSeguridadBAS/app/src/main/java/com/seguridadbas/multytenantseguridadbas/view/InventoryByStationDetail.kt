package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller.StationReportsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.stationreports.InventoryByStationData
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
fun InventoryByStationDetail(
    modifier: Modifier = Modifier,
    reportId: String = "",
    navigateBackToInventoryByStat: () -> Unit = {},
    stationsReportsController: StationReportsController
){

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())

    var reportData by remember { mutableStateOf<InventoryByStationData?>(null) }
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
                val result = stationsReportsController.getInventoryByStationDetail("Bearer $bearerToken", tenantId, reportId)

                when(result){
                    is Resource.Success -> {
                        reportData = result.data
                    }

                    is Resource.Error -> {
                        Log.e("Report inventory By station Det","No se pudo traer los datos del reporte: ${result.message}")
                    }

                    else -> {
                        Log.e("Report inventory By station Det","No se pudo traer los datos del reporte")
                    }
                }
            }
        }
    }//launched effect


    Scaffold(
        modifier = modifier.fillMaxSize()
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Detall del reporte $reportId", fontSize = 18.sp, fontWeight = FontWeight.Bold)  },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateBackToInventoryByStat() }
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
        paddingVals ->

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
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = reportData?.belongsToStation.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )

            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row{
                    var checked = false
                    Text(
                        text = "Armadura", fontSize = 14.sp
                    )
                    if(reportData?.armor == true){ checked = true  }else { checked = false }
                    RadioButton(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .size(5.dp)
                            .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                        selected = checked,
                        onClick = {  },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BasYellow,
                            unselectedColor = BasBackground
                        )
                    )
                }

                Text(
                    text = "N°: ${reportData?.armorSerialNumber}", fontSize = 14.sp
                )

            }
            Spacer(modifier = modifier.height(4.dp))
            Text(
                modifier = modifier.padding(start = 10.dp).align(Alignment.Start),
                text = "Tipo: ${reportData?.armorType}", fontSize = 14.sp
            )

            Spacer(modifier = modifier.height(8.dp))

            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )

            //gun

            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row{
                    var checked = false
                    Text(
                        text = "Arma", fontSize = 14.sp
                    )
                    if(reportData?.gun == true){ checked = true  }else { checked = false }
                    RadioButton(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .size(5.dp)
                            .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                        selected = checked,
                        onClick = {  },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BasYellow,
                            unselectedColor = BasBackground
                        )
                    )
                }

                Text(
                    text = "N°: ${reportData?.gunSerialNumber}", fontSize = 14.sp
                )

            }
            Spacer(modifier = modifier.height(4.dp))
            Text(
                modifier = modifier.padding(start = 10.dp).align(Alignment.Start),
                text = "Tipo: ${reportData?.gunType}", fontSize = 14.sp
            )

            Spacer(modifier = modifier.height(8.dp))

            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )

            // radio

            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row{
                    var checked = false
                    Text(
                        text = "Radio", fontSize = 14.sp
                    )
                    if(reportData?.radio == true){ checked = true  }else { checked = false }
                    RadioButton(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .size(5.dp)
                            .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                        selected = checked,
                        onClick = {  },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = BasYellow,
                            unselectedColor = BasBackground
                        )
                    )
                }

                Text(
                    text = "N°: ${reportData?.radioSerialNumber}", fontSize = 14.sp
                )

            }
            Spacer(modifier = modifier.height(4.dp))
            Text(
                modifier = modifier.padding(start = 10.dp).align(Alignment.Start),
                text = "Tipo: ${reportData?.radioType}", fontSize = 14.sp
            )

            Spacer(modifier = modifier.height(8.dp))

            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )


            // tolete
            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                var checked = false
                Text(
                    text = "Tolete", fontSize = 14.sp
                )
                if(reportData?.tolete == true){ checked = true  }else { checked = false }
                RadioButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(5.dp)
                        .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                    selected = checked,
                    onClick = {  },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = BasYellow,
                        unselectedColor = BasBackground
                    )
                )
            }


            //pito
            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                var checked = false
                Text(
                    text = "Pito", fontSize = 14.sp
                )
                if(reportData?.pito == true){ checked = true  }else { checked = false }
                RadioButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(5.dp)
                        .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                    selected = checked,
                    onClick = {  },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = BasYellow,
                        unselectedColor = BasBackground
                    )
                )
            }



            //linterna
            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                var checked = false
                Text(
                    text = "Linterna", fontSize = 14.sp
                )
                if(reportData?.linterna == true){ checked = true  }else { checked = false }
                RadioButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(5.dp)
                        .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                    selected = checked,
                    onClick = {  },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = BasYellow,
                        unselectedColor = BasBackground
                    )
                )
            }


            //Vitacora
            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                var checked = false
                Text(
                    text = "Vitácora", fontSize = 14.sp
                )
                if(reportData?.vitacora == true){ checked = true  }else { checked = false }
                RadioButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(5.dp)
                        .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                    selected = checked,
                    onClick = {  },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = BasYellow,
                        unselectedColor = BasBackground
                    )
                )
            }

            //cinto completo
            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                var checked = false
                Text(
                    text = "Cinto Completo", fontSize = 14.sp
                )
                if(reportData?.cintoCompleto == true){ checked = true  }else { checked = false }
                RadioButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(5.dp)
                        .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                    selected = checked,
                    onClick = {  },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = BasYellow,
                        unselectedColor = BasBackground
                    )
                )
            }



            //poncho de aguas
            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                var checked = false
                Text(
                    text = "Poncho de Aguas", fontSize = 14.sp
                )
                if(reportData?.ponchoDeAguas == true){ checked = true  }else { checked = false }
                RadioButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(5.dp)
                        .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                    selected = checked,
                    onClick = {  },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = BasYellow,
                        unselectedColor = BasBackground
                    )
                )
            }


            //detector de metales
            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                var checked = false
                Text(
                    text = "Detector de Metales", fontSize = 14.sp
                )
                if(reportData?.detectorDeMetales == true){ checked = true  }else { checked = false }
                RadioButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(5.dp)
                        .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                    selected = checked,
                    onClick = {  },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = BasYellow,
                        unselectedColor = BasBackground
                    )
                )
            }


            //caseta
            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                var checked = false
                Text(
                    text = "Caseta", fontSize = 14.sp
                )
                if(reportData?.caseta == true){ checked = true  }else { checked = false }
                RadioButton(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(5.dp)
                        .border(width = 0.5.dp, Color.Black, shape = CircleShape),
                    selected = checked,
                    onClick = {  },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = BasYellow,
                        unselectedColor = BasBackground
                    )
                )
            }


            //transporte
            Text(
                modifier = modifier.padding(start = 10.dp).align(Alignment.Start),
                text = reportData?.transportation.toString(), fontSize = 14.sp
            )

            //Observaciones
            Spacer(modifier = modifier.height(8.dp))

            HorizontalDivider(
                modifier = modifier.padding(vertical = 6.dp),
                thickness = 1.dp, Color(0f,0f,0f,0.14f)
            )
            Text(
                modifier = modifier.padding(start = 10.dp).align(Alignment.Start),
                text = "Observaciones", fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
            Text(
                modifier = modifier.padding(start = 10.dp).align(Alignment.Start),
                text = reportData?.observations.toString(), fontSize = 14.sp,
                maxLines = 2
            )


        }

    }



}