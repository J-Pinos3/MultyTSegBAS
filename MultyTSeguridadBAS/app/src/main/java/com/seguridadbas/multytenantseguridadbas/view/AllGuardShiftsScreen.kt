package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.guardshiftscontroller.GuardShiftsController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantcontroller.TenantGuardsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.guard_shifts.ShortGuardShift
import com.seguridadbas.multytenantseguridadbas.model.stationreports.GuardShiftByStationData
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
fun AllGuardShiftsScreen(
    modifier: Modifier = Modifier,
    navigateBackToMore: () -> Unit,
    onGuardShiftClicked: (guardShiftId: String) -> Unit = {},
    guardShiftsController: GuardShiftsController
){

    var token by remember { mutableStateOf("") }
    var tenantId by remember { mutableStateOf("") }
    var allGuardShiftsList by remember { mutableStateOf<List<ShortGuardShift>>( emptyList() ) }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)


    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            token = storedData.token
            token = token.replace("\"","").trim()

            tenantId = dataStoreController.getTenantId().first()
            tenantId = tenantId.replace("\"","").trim()


            loadGuardShiftsList(
                token, tenantId, guardShiftsController,
                onSuccess = { list -> allGuardShiftsList = list },
                onError = { error -> Log.e(  "guard shifts screen", error )   },
            )
        }
    }


    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Turnos Realizados", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateBackToMore() }
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "navigate  back to more"
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
    ){paddingVals ->
        if( !allGuardShiftsList.isNullOrEmpty() ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BasBackground)
                    .padding(paddingVals)
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(allGuardShiftsList){ item ->
                    GuardShiftItem(
                        item,
                        modifier,
                        onGuardShiftClicked = onGuardShiftClicked
                    )
                }
            }
        }else{
            EmptyReportsState(Modifier, "No hay reportes sobre turnos", true)
            /*
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "NO hay reportes",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textDecoration = TextDecoration.Underline,
                    fontFamily = FontFamily.Monospace
                )
            }
            */
        }


    }



}



@Composable
private fun GuardShiftItem(
    report: ShortGuardShift,
    modifier: Modifier,
    onGuardShiftClicked: (reportId: String) -> Unit = {}
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
            .height(90.dp)
            .background(Color.White)
            .clickable {
                onGuardShiftClicked(report.id)
            }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.Start
        ){

            Text(
                text = report.guardName,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(top = 6.dp))

            Text(
                text = report.stationName,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

        }
    }
}


private fun loadGuardShiftsList(
    bearerToken: String, tenantId: String,
    guardShiftsController: GuardShiftsController,
    onSuccess: (List<ShortGuardShift>) -> Unit,
    onError: (String) -> Unit
){

    CoroutineScope(Dispatchers.IO).launch {
        if(!bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty()){
            val result = guardShiftsController.getGuardShifts("Bearer $bearerToken", tenantId)

            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Success -> {   onSuccess(result.data!!)      }
                    is Resource.Error -> {    onError(result.message.toString())      }
                    else -> {   onError(result.message.toString())    }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                onError("token o tenantId inválidos")
            }
        }

    }

}
