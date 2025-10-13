package com.seguridadbas.multytenantseguridadbas.view

import android.icu.text.AlphabeticIndex
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
/*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberUpdatedMarkerState
*/
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray

@Preview(showSystemUi = true)
@Composable
fun BusinessScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = BasBackground)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //CustomGoogleMaps(modifier.weight(0.6f), -0.1938418,-78.4941259 )

        Spacer(modifier.padding(top = 40.dp  ))

        BadgeButtonReports(modifier,
            title = "Reportes de entrada / salida",
            description = "Aquí puede encontrar los reportes de ingreso de los guardias"
        )

        Spacer(modifier.padding(top = 20.dp  ))

        BadgeButtonReports(modifier,
            title = "Reportes de sitio",
            description = "Aquí puede encontrar las vueltas completadas por los guardias"
        )
    }

}

@Composable
fun BadgeButtonReports(modifier: Modifier, title: String, description: String){
    val context = LocalContext.current

    Surface(
        modifier = modifier
            .clickable{
                Toast.makeText(context, title, Toast.LENGTH_SHORT).show( )
            },
        shape = RoundedCornerShape(20),
        border = BorderStroke(1.dp, BasGray),
        shadowElevation = 2.dp,
        color = Color.Transparent
    ){
        Column(
            modifier = modifier.fillMaxWidth().padding(10.dp),
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

/*
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
            state = rememberUpdatedMarkerState(position = marker),
            title = "Ubicación del guardia?"
        )
    }
}
*/

