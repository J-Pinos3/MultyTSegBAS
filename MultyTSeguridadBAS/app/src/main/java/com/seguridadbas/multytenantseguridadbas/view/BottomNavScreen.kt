package com.seguridadbas.multytenantseguridadbas.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.certifservicescontroller.CertificationServicesController
import com.seguridadbas.multytenantseguridadbas.controllers.invoicescontroller.InvoiceController
import com.seguridadbas.multytenantseguridadbas.controllers.stationscontroller.StationsController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantcontroller.TenantGuardsController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantinvitation.TenantInvitationController
import com.seguridadbas.multytenantseguridadbas.core.navigation.BillingListScreen
import com.seguridadbas.multytenantseguridadbas.core.navigation.BottomNavBar
import com.seguridadbas.multytenantseguridadbas.core.navigation.Business
import com.seguridadbas.multytenantseguridadbas.core.navigation.GuardsScreen
import com.seguridadbas.multytenantseguridadbas.core.navigation.GuardsShiftScreen
import com.seguridadbas.multytenantseguridadbas.core.navigation.NavItemList
import com.seguridadbas.multytenantseguridadbas.core.navigation.ShiftsScreen
import com.seguridadbas.multytenantseguridadbas.core.navigation.VisitorLogsScreen

@Composable
fun BottonNavScreen(
    navController: NavController,
    authController: AuthController,
    stationsController: StationsController,
    certificationServicesController: CertificationServicesController,
    tenantInvitationController: TenantInvitationController,
    invoiceController: InvoiceController,
    tenantId: String
){
    var selectedIndex by remember{ mutableStateOf(0) }


    val navigateToBusiness: (String) -> Unit = {
        siteName ->
        navController.saveState()
        navController.navigate(Business(siteName = siteName) )
    }

    val navigateToGuards: () -> Unit = {
        navController.saveState()
        navController.navigate(GuardsScreen )
    }

    val navigateToGuardShift: ()-> Unit = {
        navController.saveState()
        navController.navigate( GuardsShiftScreen )
    }

    val navigateToShift: () -> Unit = {
        navController.saveState()
        navController.navigate(ShiftsScreen)
    }

    val navigateToBilling: () -> Unit = {
        navController.saveState()
        navController.navigate(BillingListScreen )
    }


    val navigateToVisitLog: () -> Unit = {
        navController.saveState()
        navController.navigate(VisitorLogsScreen)
    }

    Scaffold(
       modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            BottomNavBar(
                navItemList = NavItemList.navItemList,
                selectedIndex = selectedIndex,
                onItemSelected = { index -> selectedIndex = index }
            )
        }
    ){ it ->
        ContentScreen(
            selectedIndex,
            Modifier.padding(it),
            navigateToBusiness = navigateToBusiness,
            navigateToGuardsScreen = navigateToGuards,
            navigateToGuardShiftScreen = navigateToGuardShift,
            navigateToShiftScreen = navigateToShift,
            navigateToBilling = navigateToBilling,
            navigateToVisitLog = navigateToVisitLog,
            authController = authController,
            stationsController = stationsController,
            certificationServicesController =certificationServicesController,
            tenantInvitationController = tenantInvitationController,
            invoiceController = invoiceController,
            tenantId = tenantId
        )
    }
}


@Composable
fun ContentScreen(
    selectedIndex: Int,
    modifier: Modifier,
    navigateToBusiness: (String) -> Unit = {},
    navigateToGuardsScreen: () -> Unit = {},
    navigateToGuardShiftScreen: () -> Unit ,
    navigateToShiftScreen: () -> Unit,
    navigateToBilling: () -> Unit,
    navigateToVisitLog: () -> Unit,
    authController: AuthController,
    stationsController: StationsController,
    certificationServicesController: CertificationServicesController,
    tenantInvitationController: TenantInvitationController,
    invoiceController: InvoiceController,
    tenantId: String
){
    when(selectedIndex){
        0-> HomeScreen(Modifier, certificationServicesController, tenantInvitationController, invoiceController,tenantId, navigateToBilling)

        1 -> PostSitesScreen(Modifier, navigateToBusiness, stationsController)

        2 -> MyAccountScreen(Modifier, authController)

        3 -> MoreScreen(Modifier,
            navigateToGuardsScreen,
            navigateToGuardShiftScreen,
            navigateToShiftScreen,
            navigateToVisitLog)
    }
}