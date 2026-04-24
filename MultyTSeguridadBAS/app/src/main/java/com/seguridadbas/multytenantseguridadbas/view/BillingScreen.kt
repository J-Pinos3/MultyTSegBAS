package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.billingaccountcontroller.BillingAccountController
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.invoicescontroller.InvoiceController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.BillingDataResponse
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.UpdatedBillingDataResponse
import com.seguridadbas.multytenantseguridadbas.model.invoices.InvoiceByClientObject
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showSystemUi = true)
@Composable
fun BillingScreen(
    modifier: Modifier = Modifier,
    navigateBackToHome: () -> Unit = {},
    invoiceController: InvoiceController = hiltViewModel()//invoices by customer ID
){

    var allClientInvoices by remember { mutableStateOf( emptyList<InvoiceByClientObject>() ) }

    var tenantId by remember { mutableStateOf("") }
    var bearerToken by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }//SHOULD BE CLIENTID!!!!

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)


    LaunchedEffect(Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            bearerToken = storedData.token.replace("\"","").trim()
            tenantId = dataStoreController.getTenantId().first().replace("\"","").trim()
            userId = storedData.id.replace("\"","").trim()

            loadInvoices(
                bearerToken, tenantId, userId,invoiceController,
                onSuccess = { invoices -> allClientInvoices = invoices },
                onError = { error -> Log.e("billings screen", error) }
            )
        }

    }


    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.fillMaxSize().nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title={ Text(text = "Nómina") },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateBackToHome()  }
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
                ),
                actions = {
                    IconButton(
                        onClick = { /** navegar a servicios */  }
                    ){
                        Icon(
                            painter =painterResource( R.drawable.ic_orders ),
                            contentDescription = "pagos de servicios"
                        )
                    }
                }
            )
        }
    ){paddingVals ->
        paddingVals


        if(allClientInvoices.isNotEmpty()){
            LazyColumn(
                modifier = modifier.fillMaxSize().background(BasBackground)
                    .padding(paddingVals).statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                items(allClientInvoices){ item->
                    BillingStateItem(
                        item,
                        Modifier,
                    )
                }
            }
        }

    }
}



private fun loadInvoices(//                 should be clientID!!!!!
    bearerToken: String, tenantId: String, userId: String,invoiceController: InvoiceController,
    onSuccess: (List<InvoiceByClientObject>) -> Unit,
    onError: (String) -> Unit
){
    CoroutineScope(Dispatchers.IO).launch {
        if( !bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty() ){
            val result = invoiceController.getInvoicesByCustomer("Bearer $bearerToken", tenantId, userId)

            withContext(Dispatchers.Main ){
                when(result){
                    is Resource.Success -> {  onSuccess( result.data!! )  }
                    is Resource.Error -> {    onError(result.message!!)      }
                    else -> {   onError(result.message!!)    }
                }
            }

        }else{
            withContext(Dispatchers.Main){
                onError("token o tenantId inválidos")
            }
        }
    }
}


@Composable
private fun BillingStateItem(
    billingObj: InvoiceByClientObject,
    modifier: Modifier = Modifier,
){
    Box(
        modifier = modifier.padding(10.dp)
            .clip(RoundedCornerShape(24))
            .border(
                2.dp, BasGray,
                shape = RoundedCornerShape(24,24,24,24)
            )
            .shadow(2.dp)
            .fillMaxWidth()
            .background(Color.White)
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.Start
        ){
            // Header de factura
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "FACTURA ${billingObj.invoiceNumber}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Vence: ${billingObj.dueDate?.take(10) ?: "N/A"}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(12.dp))

            // Línea separadora
            Divider(color = BasGray, thickness = 1.dp)

            Spacer(Modifier.height(8.dp))

            // Tabla de items (si hay payments/items)
            if (!billingObj.payments.isNullOrEmpty()) {
                Column {
                    // Encabezados de la tabla
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Descripción", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                        Text("Cant.", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
                        Text("Precio", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.8f))
                        Text("Tax", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.6f))
                        Text("Total", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.8f))
                    }

                    Spacer(Modifier.height(4.dp))

                    // Items de la factura
                    billingObj.payments.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(item.name ?: "", fontSize = 12.sp, modifier = Modifier.weight(2f))
                            Text("${item.quantity ?: 0}", fontSize = 12.sp, modifier = Modifier.weight(0.7f))
                            Text("$${item.rate ?: 0}", fontSize = 12.sp, modifier = Modifier.weight(0.8f))
                            Text("${item.taxRate ?: 0}%", fontSize = 12.sp, modifier = Modifier.weight(0.6f))
                            Text("$${item.lineTotal ?: 0}", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.8f))
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Divider(color = BasGray, thickness = 1.dp)
            }

            Spacer(Modifier.height(8.dp))

            // Totales alineados a la derecha
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text("Subtotal:", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text("$${billingObj.subtotal ?: "0"}", fontSize = 14.sp)
                }

                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text("Total:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("$${billingObj.total ?: "0"}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                }
            }

            Spacer(Modifier.height(8.dp))
            Divider(color = BasGray, thickness = 1.dp)
        }
    }
}