package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.certifservicescontroller.CertificationServicesController
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.certifications.CertificationDataResponse
import com.seguridadbas.multytenantseguridadbas.model.services.ServiceDataResponse
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
    tenantId: String
){

    var bearerToken by remember { mutableStateOf("") }

    var certificationsList by remember{ mutableStateOf<List<CertificationDataResponse>>( emptyList() ) }
    var servicesList by remember { mutableStateOf<List<ServiceDataResponse>>( emptyList() ) }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            bearerToken = storedData.token
            bearerToken = bearerToken.replace("\"","").trim()

            Log.i("Home","token: ${bearerToken.substring(0,6)} and tenantId: $tenantId")

            if( !bearerToken.isNullOrEmpty() && !tenantId.isNullOrEmpty() ){
                val certificationsResult = certificationServicesController.getAllCertifications("Bearer $bearerToken", tenantId)

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

                val servicesResult = certificationServicesController.getAllServices("Bearer $bearerToken", tenantId)

                when(servicesResult){
                    is Resource.Success -> {
                        servicesList = servicesResult.data?.toList()!!
                    }

                    is Resource.Error -> {
                        Log.e("Sites","Error al traer los servicios: ${servicesResult.message}")
                    }

                    else ->{ Log.e("Sites","No se pudo traer los servicios") }
                }
            }
        }


    }

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
            fontSize = 40.sp,
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
            "$ 12312.00",
            "20/01/23"
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

}


@Composable
fun BillingComposable(modifier: Modifier, amount: String, lastPayment: String){

    val apiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd" )
    val date = LocalDate.parse(LocalDate.now().toString(), apiFormat)


    Surface(
        modifier = modifier,
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