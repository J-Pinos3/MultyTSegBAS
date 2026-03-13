package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.contracts.contract

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NominaScreen(
    modifier: Modifier = Modifier,
    navigateBackToBilling: () -> Unit = {},
    nominaController: Any? = null
){

    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
    val currentDate = sdf.format( Date() )

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

        }
    }

}

@Composable
private fun NominaListItem(
    modifier: Modifier
){
    Box(
        modifier = Modifier
            .padding(14.dp)
            .clip(RoundedCornerShape(20))
            .border(
                1.dp,
                BasGray,
                shape = RoundedCornerShape(24,24,24,24)
            )
            .shadow(1.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable{ }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = "Nombre: Nombre Completo",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Cargo: vendedor",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_pay_money),
                    contentDescription = "Pagar sueldo",
                )
            }
        }

    }
}






