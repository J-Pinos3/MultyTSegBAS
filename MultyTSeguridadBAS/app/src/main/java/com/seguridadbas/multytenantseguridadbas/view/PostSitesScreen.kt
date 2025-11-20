package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.stationscontroller.StationsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.station.ShortStation
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//@Preview(showSystemUi = true)
@Composable
fun PostSitesScreen(
    modifier: Modifier = Modifier,
    onPostSiteClicked: (String) -> Unit = {},
    stationsController: StationsController

){

    var token by remember { mutableStateOf("") }
    var tenantId by remember { mutableStateOf("") }
    var sitesList by remember { mutableStateOf<List<ShortStation>>( emptyList() ) }


    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            token = storedData.token
            token = token.replace("\"","").trim()

            tenantId = dataStoreController.getTenantId().first()

            tenantId = tenantId.replace("\"","").trim()
            Log.i("Sites","Token: $token \n TenantId: $tenantId")

            if( !token.isNullOrEmpty() && !tenantId.isNullOrEmpty() ){
                val result = stationsController.getAllStations("Bearer $token", tenantId)

                when(result){
                    is Resource.Success -> {

                        sitesList = result.data?.toList()!!
                        Log.i("Sites","objetos: ${sitesList[0].stationName} ---")
                    }
                    is Resource.Error -> {
                        sitesList = emptyList()
                        Log.e("Sites","No se pudo traer los sitios ${result.message.toString()}")
                    }
                    else -> {
                        Log.e("Sites","No se pudo traer los sitios")
                    }
                }
            }


        }
    }



    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .background(color = BasBackground)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        items(sitesList){ site ->
            PostSiteItemList(
                site, modifier, onPostSiteClicked
            )
        }
    }

}


@Composable
private fun PostSiteItemList(
    sitesList: ShortStation,
    modifier: Modifier,
    onPostSiteClicked: (String) -> Unit = {}
    ){

    Box(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(24))
            .border(
                2.dp,
                BasGray,
                shape = RoundedCornerShape(0,24,0,24)
            )
            .shadow(2.dp)
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
            .clickable{
                onPostSiteClicked(sitesList.stationId)
            }
    ){
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .padding(10.dp),
           horizontalAlignment = Alignment.Start
       ){
           Text(
               text = sitesList.stationName,
               fontSize = 20.sp,
               textAlign = TextAlign.Start,
               fontWeight = FontWeight.Bold
           )

           Spacer(modifier = Modifier.padding(top = 10.dp))

           Text(
               text = sitesList.stationSchedule,
               fontSize = 16.sp,
               textAlign = TextAlign.Start,
               fontWeight = FontWeight.Bold
           )
       }
    }
}


