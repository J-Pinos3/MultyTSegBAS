package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantinfocontroller.TenantInfoController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    enterprise: String = "C Guard Pro",
    onSplashComplete: () -> Unit = {},
    tenantInfoController: TenantInfoController = hiltViewModel()
) {
    var token by remember { mutableStateOf("") }
    var tenantId by remember { mutableStateOf("") }
    var tenantUrlForImage by remember { mutableStateOf("") }
    var tenantBusinessName by remember { mutableStateOf(enterprise) }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            token = storedData.token.replace("\"", "").trim()
            tenantId = dataStoreController.getTenantId().first().replace("\"", "").trim()

            if (token.isNotEmpty() && tenantId.isNotEmpty()) {
                val result = tenantInfoController.getCurrentTenant("Bearer $token", tenantId)
                when (result) {
                    is Resource.Success -> {
                        tenantUrlForImage = result.data?.logoId ?: ""
                        tenantBusinessName = result.data?.name ?: enterprise
                        Log.i("splash", "Tenant cargado: ${result.data?.name}")
                    }
                    is Resource.Error -> Log.e("splash tenant", "Error: ${result.message}")
                    else -> Log.e("splash tenant", "Error desconocido")
                }
            }
        }
    }

    // 🔥 Fondo con imagen que cubre toda la pantalla
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo (degradado personalizado)
        Image(
            painter = painterResource(id = R.drawable.splashscreenbackground), // 👈 Reemplaza con tu drawable
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ajusta la imagen sin deformarla
        )

        // Contenido centrado vertical y horizontalmente
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo circular
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.White), // Fondo blanco opcional para contraste
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.miseguridadshield), // 👈 Escudo
                    contentDescription = "Logo de seguridad",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título "Mi Seguridad"
            Text(
                text = "Mi Seguridad",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtítulo "by C Guard Pro"
            Text(
                text = "by C Guard Pro",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Copyright (opcional: se puede omitir si molesta)
            Text(
                text = "© 2026 C Guard Pro — Mi Seguridad",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }

    // Navegación automática después de cargar datos
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000) // 2 segundos de splash
        onSplashComplete()
    }
}



/*
package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantinfocontroller.TenantInfoController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.tenantinfo.TenantInfoResponse
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGrayDark
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//@Preview(showSystemUi = true)
@Composable
fun SplashScreen(
    enterprise: String ,
    onSplashComplete: () -> Unit  = {},
    tenantInfoController: TenantInfoController = hiltViewModel()
){
    var token by remember { mutableStateOf("") }
    var tenantId by remember { mutableStateOf("") }

    var tenantData  by remember{ mutableStateOf<TenantInfoResponse?>(null)  }

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    var tenantUrlForImage by remember { mutableStateOf("") }
    var tenantBusinessName by remember { mutableStateOf(enterprise) }


    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val storedData = dataStoreController.getDataFromStore().first()
            token = storedData.token
            token = token.replace("\"","").trim()

            tenantId = dataStoreController.getTenantId().first()
            tenantId = tenantId.replace("\"","").trim()

            if( !token.isNullOrEmpty() && !tenantId.isNullOrEmpty() ){

                val result = tenantInfoController.getCurrentTenant("Bearer $token", tenantId)

                when(result){
                   is Resource.Success -> {
                       tenantData = result.data
                        tenantUrlForImage = result.data!!.logoId ?: ""
                       tenantBusinessName = result.data.name

                       Log.i("splash", result.data.name)
                   }

                   is Resource.Error -> { Log.e("splash tenant", "no se pudbo obtener el tenant") }

                   else -> { Log.e("splash tenant", "no se pudbo obtener el tenant") }
                }
            }else{
                tenantUrlForImage = ""
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BasBackground)
            .statusBarsPadding()
            .clickable{
                onSplashComplete()
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasGrayDark)
                .padding(top = 40.dp, bottom = 40.dp)
        )

        Spacer(
            modifier= Modifier.padding(top = 5.dp, bottom = 5.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasGray)
                .padding(top = 15.dp, bottom = 15.dp)
        )

        Spacer(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )

        // dibujo
        if( tenantUrlForImage.isNullOrEmpty() ){
            Image(
                modifier = Modifier.height(300.dp).fillMaxWidth(),
                painter = painterResource(R.drawable.miseguridadshield),
                contentDescription = "Escudo de bas"
            )
        }else{
            AsyncImage(
                model = tenantUrlForImage,
                contentDescription = "Escudo de bas")
        }


        Spacer(
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        Text(
            text = tenantBusinessName,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )

        Spacer(
            modifier = Modifier.weight(0.4f)
        )

        Text(
            modifier = Modifier.padding(bottom = 40.dp),
            text = "© 2021 Copyright, Brigadas Antirrobos Seguridad"
        )

    }


}

*/