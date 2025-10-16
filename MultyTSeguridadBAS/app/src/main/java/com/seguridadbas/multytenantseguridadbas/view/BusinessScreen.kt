package com.seguridadbas.multytenantseguridadbas.view

import android.icu.text.AlphabeticIndex
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.seguridadbas.multytenantseguridadbas.R
//import com.google.maps.android.compose.rememberUpdatedMarkerState

import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun BusinessScreen(
    modifier: Modifier = Modifier,
    postSiteName: String = "",
    navigateBackToPostSites: () -> Unit = {}
) {

    // Simulación de datos desde API
    val reportItems = remember {
        listOf(
            ReportItem("Reportes de entrada / salida", "Aquí puede encontrar los reportes de ingreso de los guardias"),
            ReportItem("Reportes de sitio", "Aquí puede encontrar las vueltas completadas por los guardias"),
            ReportItem("Reportes de incidentes", "Registro de situaciones inusuales"),
            ReportItem("Horas extras", "Registro de horas adicionales"),
            ReportItem("Equipamiento", "Control de equipos asignados"),
            ReportItem("Incendio", "Informe incendios"),
            ReportItem("Traspaso", "Informe intruso en edificio"),
        )
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold (
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {   Text(text = postSiteName, fontSize = 18.sp, fontWeight = FontWeight.Bold)   },
                navigationIcon = {
                    IconButton(
                       onClick = {  navigateBackToPostSites()     }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "navigate back to sites"
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
        paddingVals ->

        Column(
          modifier = Modifier
              .fillMaxSize()
              .background(color = BasBackground)
              .padding(paddingVals)
        ) {
            CustomGoogleMaps(
                modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(start = 5.dp, end = 5.dp, bottom = 5.dp),
                -0.1938418,
                -78.4941259
            )

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                items(reportItems) { report ->

                    BadgeButtonReports(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        title = report.title,
                        description = report.description
                    )
                }


                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }




    }




}


@Composable
fun BadgeButtonReports(
    modifier: Modifier,
    title: String,
    description: String,
    onClick: () -> Unit = {}
){
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .clickable{
                Toast.makeText(context, title, Toast.LENGTH_SHORT).show( )
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, BasGray),
        shadowElevation = 4.dp,
        color = Color.White
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalAlignment = Alignment.Start

        ){
            Text(
                text = title,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.padding(top = 10.dp))

            Text(
                text = description,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        }

    }
}


@Composable
fun CustomGoogleMaps(modifier: Modifier, latitude: Double, longitude: Double){

    val marker = LatLng(latitude, longitude)
    val properties by remember { mutableStateOf( MapProperties(mapType = MapType.TERRAIN, isTrafficEnabled = true ) ) }

    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }

    GoogleMap(
        modifier = modifier.padding(10.dp),
        properties = properties,
        uiSettings = uiSettings,
    ){
        Marker(
            state = rememberMarkerState(position = marker),
            title = "Ubicación del guardia?"
        )
    }
}





data class ReportItem(
    val title: String,
    val description: String
)