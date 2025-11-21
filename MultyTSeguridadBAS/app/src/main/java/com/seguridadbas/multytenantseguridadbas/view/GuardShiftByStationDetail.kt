package com.seguridadbas.multytenantseguridadbas.view

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.model.stationreports.GuardShiftByStationData
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun GuardShiftByStationDetail (
    modifier: Modifier = Modifier,
    reportId: String = "",
    navigateBackToGuardShiftsByStation: () -> Unit = {}
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format( Date() )

    var guardShiftData by remember{ mutableStateOf<GuardShiftByStationData?>(null) }
    var tenantId by remember { mutableStateOf("") }
    var bearerToken by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dataStoreCOntroller = DataStoreController(context)

    LaunchedEffect(Unit) {

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
                        onClick ={ navigateBackToGuardShiftsByStation() }
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
                text="Quito Norte",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text="Hora de inicio: 09:12",
                fontSize = 14.sp,
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                //finishtimeinday
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text="Hora de finalizacion: 16:30",
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
                    text="Horario de la estación : Diurno",
                    fontSize = 14.sp,
                )

                Text(
                    text = "N° Guardias: 6"
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
                text="Julio Guardia",
                fontSize = 14.sp
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = "Credenciales: 18272394u4",
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
                text="N° Incidentes ocurridos: 3",
                fontSize = 14.sp
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text="N° Patrullajes realizados: 7",
                fontSize = 14.sp
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(horizontal = 10.dp),
                text="Observaciones: No hay observaciones",
                fontSize = 14.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = modifier.height(8.dp))
            Text(
                //punch in time, inicio del turno-patrullaje
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text="Inicio del turno: 10:12",
                fontSize = 14.sp
            )
            Spacer(modifier = modifier.height(8.dp))

            Text(
                //punch out time, inicio del turno-patrullaje
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text="Fin del turno: 10:12",
                fontSize = 14.sp
            )

        }


    }









}