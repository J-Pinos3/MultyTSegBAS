package com.seguridadbas.multytenantseguridadbas.view

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller.StationReportsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.stationreports.InventoryByStationData
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import com.seguridadbas.multytenantseguridadbas.view.customwidget.EmptyReportsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryByStationScreen(
    modifier: Modifier,
    navigateBackToBusiness: () -> Unit,
    onInventoryReportClicked: (reportId: String) -> Unit,
    stationsReportsController: StationReportsController
){

    var inventoryByStationList by remember{ mutableStateOf<List<InventoryByStationData>>( emptyList() ) }
    var filteredInventory by remember { mutableStateOf<List<InventoryByStationData>>( emptyList() ) }
    var tenantId by remember { mutableStateOf("") }
    var bearerToken by remember { mutableStateOf("") }
    var siteName by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)





    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            bearerToken = storedData.token
            bearerToken = bearerToken.replace("\"","").trim()

            tenantId = dataStoreController.getTenantId().first()
            tenantId = tenantId.replace("\"","").trim()


            loadInitialInventory(bearerToken, tenantId, stationsReportsController,
                onSuccess = {
                    inventoryByStationList = it
                },
                onError = { error -> Log.e("inventory by station screen",error) }
            )
            /*if( !bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty() ){
                val result = stationsReportsController.getInventoryByStation(
                    "Bearer $bearerToken", tenantId, null  )


                when()
            }
            */

        }
    }

    LaunchedEffect( siteName, inventoryByStationList  ) {
        if(siteName.isBlank()){
            filteredInventory = inventoryByStationList
        }else{
            filteredInventory = inventoryByStationList.filter { inventory ->
                inventory.belongsToStation.contains(siteName, ignoreCase = true)
            }
        }
    }

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            Column{
                CenterAlignedTopAppBar(
                    title={ Text(text = "Inventario", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(
                            onClick = { navigateBackToBusiness()  }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "navigate back to business"
                            )
                        }
                    },
                    scrollBehavior = scrollBehaviour,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BasYellow, titleContentColor = Color.Black
                    )
                )

                OutlinedTextField(
                    value = siteName,
                    onValueChange = {siteName = it},
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Sitio") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    trailingIcon ={
                        if(siteName.isNotBlank()){
                            IconButton(
                                onClick = {siteName = ""}
                            ) {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                                    contentDescription = "Limpiar búsqueda"
                                )
                            }
                        }
                    },
                    singleLine = true, shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ){
        paddingVals ->

        if(filteredInventory.isNotEmpty()){
            LazyColumn(
                modifier = modifier.fillMaxSize().background(color = BasBackground)
                    .padding(paddingVals).statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(filteredInventory){ item ->
                    InventoryByStationItem(
                        item, modifier,
                        onReportClicked = onInventoryReportClicked
                    )
                }
            }
        }else{
            EmptyReportsState(Modifier, "No hay reportes para este sitio", true)
        }
    }
}


@Composable
private fun InventoryByStationItem(
    report: InventoryByStationData,
    modifier: Modifier = Modifier,
    onReportClicked: (reportId: String) -> Unit = {}
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
            .height(100.dp)
            .background(Color.White)
            .clickable {
                onReportClicked(report.id)
            }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.Start
        ){

            Text(
                text = report.belongsToStation,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(top = 6.dp))

            Text(
                text = report.observations,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )
        }
    }

}

fun loadInitialInventory(
    bearerToken: String, tenantId: String,
    stationsReportsController: StationReportsController,
    onSuccess: (List<InventoryByStationData>) -> Unit,
    onError: (String) -> Unit
){
    CoroutineScope(Dispatchers.IO).launch {
        if(!bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty()){
            val result = stationsReportsController.getInventoryByStation(
                "Bearer $bearerToken", tenantId, null
            )

            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Success -> {
                        onSuccess( result.data?.toList()!! )
                    }

                    is Resource.Error -> {
                        Log.e("inventory by station screen","no se pudo traer el inventario ${result.message}")
                        onError(result.message.toString())
                    }

                    else -> {
                        Log.e("inventory by station screen","no se pudo traer el inventario")
                        onError(result.message.toString())
                    }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                onError("token o tenantId inválidos")
            }
        }
    }
}