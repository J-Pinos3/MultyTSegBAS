package com.seguridadbas.multytenantseguridadbas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.certifservicescontroller.CertificationServicesController
import com.seguridadbas.multytenantseguridadbas.controllers.stationscontroller.StationsController
import com.seguridadbas.multytenantseguridadbas.core.navigation.SplashNavigation
import com.seguridadbas.multytenantseguridadbas.ui.theme.MultyTenantSeguridadBASTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authController by viewModels<AuthController>()
    private val stationsController by viewModels<StationsController>()
    private val certificationServicesController by viewModels<CertificationServicesController>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultyTenantSeguridadBASTheme {
                SplashNavigation(authController, stationsController, certificationServicesController)
                //SplashScreen()
            }
        }
    }
}




/*
*
Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Greeting(
        name = "Android",
        modifier = Modifier.padding(innerPadding)
    )
}
* */