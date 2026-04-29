package com.seguridadbas.multytenantseguridadbas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.billingaccountcontroller.BillingAccountController
import com.seguridadbas.multytenantseguridadbas.controllers.certifservicescontroller.CertificationServicesController
import com.seguridadbas.multytenantseguridadbas.controllers.guardshiftscontroller.GuardShiftsController
import com.seguridadbas.multytenantseguridadbas.controllers.invoicescontroller.InvoiceController
import com.seguridadbas.multytenantseguridadbas.controllers.postsitecontrollers.PostSiteController
import com.seguridadbas.multytenantseguridadbas.controllers.shiftscontroller.ShiftsController
import com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller.StationReportsController
import com.seguridadbas.multytenantseguridadbas.controllers.stationscontroller.StationsController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantguardscontroller.TenantGuardsController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantinfocontroller.TenantInfoController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantinvitation.TenantInvitationController
import com.seguridadbas.multytenantseguridadbas.controllers.visitlogscontroller.VisitLogController
import com.seguridadbas.multytenantseguridadbas.core.navigation.SplashNavigation
import com.seguridadbas.multytenantseguridadbas.ui.theme.MultyTenantSeguridadBASTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    val deepLinkIntentFlowSocial = MutableSharedFlow<Intent>(replay = 1)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        intent?.let {
            deepLinkIntentFlowSocial.tryEmit(it)
        }

        setContent {
            MultyTenantSeguridadBASTheme {
                SplashNavigation(
                    deepLinkIntentFlowSocial,
                )
                //SplashScreen()
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.let {
            setIntent(it)
            deepLinkIntentFlowSocial.tryEmit(it)

            Log.d("MainActivity", "onNewIntent emitted: $it")
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