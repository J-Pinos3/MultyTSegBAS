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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantcontroller.TenantGuardsController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.GuardDataResponse
import com.seguridadbas.multytenantseguridadbas.model.SecurityGuardProfile
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
fun SecGuardDetail(
    modifier: Modifier = Modifier,
    guardId: String = "",
    navigateBackToAllGuards: () -> Unit = {},
    tenantGuardsController: TenantGuardsController
) {

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
    val currentDate = sdf.format(Date())

    var guardData by remember { mutableStateOf<SecurityGuardProfile?>(null) }
    var tenantId by remember { mutableStateOf("") }
    var bearerToken by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedDate = dataStoreController.getDataFromStore().first()
            bearerToken = storedDate.token
            bearerToken = bearerToken.replace("\"","").trim()

            tenantId = dataStoreController.getTenantId().first()
            tenantId = tenantId.replace("\"","").trim()

            loadGuardInfo(bearerToken, tenantId, guardId, tenantGuardsController,
                onSuccess = {
                    guardData = it
                },
                onError = {
                    error->Log.e("guard detail screen",error)
                }
            )

        }
    }


    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Detalle del guardia", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateBackToAllGuards() }
                    ){
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "navigate back to all guards"
                        )
                    }
                },
                scrollBehavior = scrollBehaviour,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BasYellow, titleContentColor = Color.Black
                )
            )
        }
    ){ paddingVals ->
        Column(
            modifier = Modifier.fillMaxSize()
                .background(BasBackground).padding(paddingVals)
        ){
            Text(
                modifier  = modifier.align(Alignment.End).padding(end = 10.dp, top = 5.dp),
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
                text = guardData?.fullName.toString().replace("\"","").trim(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            //cédula
            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = guardData?.governmentID.toString().replace("\"","").trim(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = guardData?.gender.toString().replace("\"","").trim(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = guardData?.academicInstruction.toString().replace("\"","").trim(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = guardData?.maritalStatus.toString().replace("\"","").trim(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = guardData?.bloodType.toString().replace("\"","").trim(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Text(
                modifier = modifier.align(Alignment.Start).padding(start = 10.dp),
                text = if(guardData?.isOnDuty == true){
                        "En turno"
                    }else{
                        "Fuera de turno"
                    }
                ,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = modifier.height(8.dp))

            Row(
                modifier = modifier.fillMaxWidth().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = guardData?.birthPlace.toString().replace("\"","").trim(),
                    fontSize = 14.sp,
                )

                Text(
                    text = guardData?.birthDate.toString().replace("\"","").trim(),
                    fontSize = 14.sp,
                )



            }




        }

    }


}


fun loadGuardInfo(
    bearerToken: String, tenantId: String, guardId: String,
    tenantGuardsController: TenantGuardsController,
    onSuccess: (SecurityGuardProfile) -> Unit,
    onError: (String) -> Unit
){

    CoroutineScope(Dispatchers.IO).launch {
        if(!bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty()){
            val result = tenantGuardsController.getSecurityGuardDetails("Bearer $bearerToken",tenantId, guardId)

            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Success -> {
                        onSuccess(result.data!!)
                    }
                    is Resource.Error -> {
                        Log.e("guard detail screen","no se pudo traer la información del guardia ${result.message}")
                        onError(result.message.toString())
                    }

                    else -> {
                        Log.e("guard detail screen","no se pudo traer la información del guardia")
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


