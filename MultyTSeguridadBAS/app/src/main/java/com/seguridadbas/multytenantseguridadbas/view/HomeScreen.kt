package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.certifservicescontroller.CertificationServicesController
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.invoicescontroller.InvoiceController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantinvitation.TenantInvitationController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.certifications.CertificationDataResponse
import com.seguridadbas.multytenantseguridadbas.model.invoices.AllInvoicesRespData
import com.seguridadbas.multytenantseguridadbas.model.services.ServiceDataResponse
import com.seguridadbas.multytenantseguridadbas.model.tenantinvitation.AcceptTokenResponse
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import com.seguridadbas.multytenantseguridadbas.view.badgeshome.CertificationBadgeItems
import com.seguridadbas.multytenantseguridadbas.view.badgeshome.ServiceBadgeItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//@Preview(showSystemUi = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    certificationServicesController: CertificationServicesController,
    tenantInvitationController: TenantInvitationController,
    invoiceController: InvoiceController,
    tenantId: String,
    onBillingClicked: () -> Unit = {}
){
    var currentTenantId by remember { mutableStateOf(tenantId) }
    //token typed by the user
    var verificationCode by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var acceptTokenResponse by remember { mutableStateOf(AcceptTokenResponse()) }

    var bearerToken by remember { mutableStateOf("") }

    var certificationsList by remember{ mutableStateOf<List<CertificationDataResponse>>( emptyList() ) }
    var servicesList by remember { mutableStateOf<List<ServiceDataResponse>>( emptyList() ) }

    var invoiceAmount by remember { mutableStateOf("$0.00") }
    var dueDateInvoice by remember { mutableStateOf("1999/01/01") }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    LaunchedEffect(currentTenantId) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            userId = dataStoreController.getDataFromStore().first().id
            userId = userId.replace("\"","").trim()


            bearerToken = storedData.token
            bearerToken = bearerToken.replace("\"","").trim()

            Log.i("Home","token: ${bearerToken.substring(0,6)} and tenantId: $currentTenantId")

            if( !bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty() ){
                val certificationsResult = certificationServicesController.getAllCertifications("Bearer $bearerToken", currentTenantId)

                when(certificationsResult){
                    is Resource.Success -> {
                        certificationsList = certificationsResult.data?.toList()!!
                    }
                    is Resource.Error -> {
                        certificationsList = emptyList()
                        Log.e("Sites","Error al traer los certificados: ${certificationsResult.message}")
                    }
                    else -> {Log.e("Sites","No se pudo traer los certificados")}
                }

                val servicesResult = certificationServicesController.getAllServices("Bearer $bearerToken", currentTenantId)

                when(servicesResult){
                    is Resource.Success -> {
                        servicesList = servicesResult.data?.toList()!!
                    }

                    is Resource.Error -> {
                        Log.e("Sites","Error al traer los servicios: ${servicesResult.message}")
                    }

                    else ->{ Log.e("Sites","No se pudo traer los servicios") }
                }

                loadUpcomingInvoice(
                    bearerToken, currentTenantId,invoiceController,
                    onSuccess = {
                        invoiceAmount = it.total
                        dueDateInvoice = it.dueDate
                    },
                    onError = { errorMsg -> Log.e("home screen invoice", errorMsg)  }
                )
            }
        }


    }

    if( !currentTenantId.isNullOrEmpty()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = BasBackground)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 6.dp)
                    .align(Alignment.Start),
                text = "Bienvenido Cliente, ",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BasYellow)
                    .padding(top = 30.dp, bottom = 30.dp)
            ){
                Image(
                    painter = painterResource(R.drawable.baslogo),
                    contentDescription = "Logo de bas"
                )
            }

            Spacer(modifier = Modifier.padding(top = 10.dp))

            titleTexts(  modifier = Modifier.align(Alignment.Start), "Nuestros Servicios:"  )

            Spacer(modifier = Modifier.padding(top = 10.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                //verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                items(servicesList){ service ->
                    ServiceBadgeItems(
                        Modifier.fillMaxWidth().width(160.dp).height(200.dp),
                        service
                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 30.dp))

            titleTexts(  modifier = Modifier.align(Alignment.Start), "Facturación:"  )

            Spacer(modifier = Modifier.padding(top = 10.dp))

            BillingComposable(
                modifier.padding(horizontal = 2.dp),
                invoiceAmount,
                dueDateInvoice,
                onBillingClicked = { onBillingClicked() }
            )

            Spacer(modifier = Modifier.padding(top = 30.dp))

            titleTexts(  modifier = Modifier.align(Alignment.Start), "Certificaciones y Permisos:"  )

            LazyRow(
                modifier = Modifier.fillMaxWidth().height(220.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                items(certificationsList){ certification ->
                    CertificationBadgeItems(
                        Modifier.width(160.dp).height(200.dp),
                        certification)
                }
            }

            Spacer(modifier = Modifier.height(90.dp) )

        }
    }else{
        TenantInvitationBadge(
            modifier = Modifier.fillMaxWidth(),
            onCodeChanged = {
                currentTypedToken -> verificationCode = currentTypedToken   },
            isButtonEnabled = true,
            codeTyped = verificationCode,
            onAcceptInvitation = {

                processInvitationAccepted(
                    "Bearer ${bearerToken}",
                    verificationCode,
                    userId,
                    tenantInvitationController, acceptTokenResponse, { newTenantId->
                        currentTenantId = newTenantId
                    }
                )

            }
        )
    }

}


@Composable
fun BillingComposable(
    modifier: Modifier,  amount: String,
    lastPayment: String, onBillingClicked: () -> Unit = {}
){

    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd" )
    val date = LocalDate.parse(LocalDate.now().toString(), apiFormat)


    Surface(
        modifier = modifier.clickable(
            onClick = onBillingClicked
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BasGray),
        color = Color.Transparent
    ){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = BasYellow)
                    .height(50.dp)
                    .width(10.dp)
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Siguiente Pago",
                        fontSize = 14.sp
                    )

                    Text(
                        modifier = Modifier,
                        text = "${date.month.toString().uppercase()} ${date.year}",
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.padding(top = 10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = amount,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.padding(top = 10.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Último Pago: $lastPayment",
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun titleTexts(modifier: Modifier, text: String){
    Text(
        modifier = modifier
            .padding(start = 10.dp),
        text = text,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        textAlign = TextAlign.Start
    )
}


//functions to accept tenant invitation
@Composable
fun TenantInvitationBadge(
    modifier: Modifier = Modifier,
    onCodeChanged: (String) -> Unit = {},
    isButtonEnabled: Boolean = true,
    codeTyped: String,
    onAcceptInvitation: () -> Unit = {},
){

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                // Franja superior amarilla
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            color = BasYellow,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                )


                // Contenido principal centrado
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                color = BasBackground,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                            tint = BasGray
                        )
                    }


                    Text(
                        text = "Ingresa el código de la organización",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = codeTyped,
                        onValueChange = onCodeChanged,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp),
                        placeholder = { Text(text="Código:") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true, maxLines = 1,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                //button send code - accept invitation
                    Button(
                        onClick = onAcceptInvitation,
                        modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(50.dp) ,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(isButtonEnabled) Color.Black else Color.Gray ,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White.copy(alpha = 0.6f)
                        ),
                        enabled = isButtonEnabled

                    )
                    {
                        Text(
                            text = "Continuar",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 30.sp
                        )
                    }
                }


                // Franja inferior amarilla
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            color = BasYellow,
                            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                        )
                )
            }
        }
    }
}


private fun processInvitationAccepted(bearerToken: String, verificationCode: String,
    userId: String, controller: TenantInvitationController, acceptTokenResponse: AcceptTokenResponse,
    onUpdateTenantId: (String) -> Unit = {} ){
    CoroutineScope(Dispatchers.IO).launch {
        Log.i("ACCEPT","${bearerToken}\n ${verificationCode}\n, $userId")
        val result = controller.acceptTenantInvitation(bearerToken, verificationCode, userId)
        withContext(Dispatchers.Main){
            when(result){

                is Resource.Success -> {
                    Log.i("TenantInvitation","Success: ${  !acceptTokenResponse.id.isNullOrEmpty()  }")
                    onUpdateTenantId( result.data!!.id  )
                }
                is Resource.Error -> {
                    Log.e("TenantInvitation","Error: ${  result.message  }")
                }
                else -> {
                    Log.e("TenantInvitation","No se pudo procesar la invitación")
                }

            }
        }
    }
}


private fun loadUpcomingInvoice(
    bearerToken: String, tenantId: String,
    invoiceController: InvoiceController,
    onSuccess: (AllInvoicesRespData) -> Unit,
    onError: (String) -> Unit
){

    CoroutineScope(Dispatchers.IO).launch{
        if(!bearerToken.isNullOrEmpty() || !tenantId.isNullOrEmpty()){
            val result = invoiceController.getAllInvoices("Bearer $bearerToken", tenantId)

            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Success -> { onSuccess(result.data!!.sortedBy { it.dueDate }[0]) }
                    is Resource.Error -> { onError(result.message.toString()) }
                    else -> { onError(result.message.toString()) }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                onError("token o tenantId inválidos")
            }
        }
    }


}
