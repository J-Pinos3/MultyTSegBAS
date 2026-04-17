package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.certifservicescontroller.CertificationServicesController
import com.seguridadbas.multytenantseguridadbas.controllers.invoicescontroller.InvoiceController
import com.seguridadbas.multytenantseguridadbas.controllers.postsitecontrollers.PostSiteController
import com.seguridadbas.multytenantseguridadbas.controllers.stationscontroller.StationsController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantinvitation.TenantInvitationController
import com.seguridadbas.multytenantseguridadbas.core.navigation.BillingListScreen
import com.seguridadbas.multytenantseguridadbas.core.navigation.BottomNavBar
import com.seguridadbas.multytenantseguridadbas.core.navigation.Business
import com.seguridadbas.multytenantseguridadbas.core.navigation.GuardsScreen
import com.seguridadbas.multytenantseguridadbas.core.navigation.GuardsShiftScreen
import com.seguridadbas.multytenantseguridadbas.core.navigation.NavItemList
import com.seguridadbas.multytenantseguridadbas.core.navigation.ShiftsScreen
import com.seguridadbas.multytenantseguridadbas.core.navigation.Stations
import com.seguridadbas.multytenantseguridadbas.core.navigation.VisitorLogsScreen

@Composable
fun BottonNavScreen(
    navController: NavController,
    tenantId: String
){
    var selectedIndex by remember{ mutableStateOf(0) }


    val navigateToStations: (String) -> Unit = {
        postSiteId ->
        navController.saveState()
        navController.navigate(Stations(postSiteId = postSiteId))
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
            navigateToStations = navigateToStations,
            navigateToGuardsScreen = navigateToGuards,
            navigateToGuardShiftScreen = navigateToGuardShift,
            navigateToShiftScreen = navigateToShift,
            navigateToBilling = navigateToBilling,
            navigateToVisitLog = navigateToVisitLog,
            tenantId = tenantId
        )
    }
}


@Composable
fun ContentScreen(
    selectedIndex: Int,
    modifier: Modifier,
    navigateToStations: (String) -> Unit = {},
    navigateToGuardsScreen: () -> Unit = {},
    navigateToGuardShiftScreen: () -> Unit ,
    navigateToShiftScreen: () -> Unit,
    navigateToBilling: () -> Unit,
    navigateToVisitLog: () -> Unit,
    tenantId: String
){
    when(selectedIndex){
        0-> HomeScreen(Modifier, tenantId = tenantId, onBillingClicked= navigateToBilling)

        1 -> PostSitesScreen(Modifier, navigateToStations)

        2 -> MyAccountScreen(Modifier)

        3 -> MoreScreen(Modifier,
            navigateToGuardsScreen,
            navigateToGuardShiftScreen,
            navigateToShiftScreen,
            navigateToVisitLog)
    }
}