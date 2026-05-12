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
import com.seguridadbas.multytenantseguridadbas.core.navigation.*

@Composable
fun BottonNavScreen(
    navController: NavController,
    tenantId: String
){
    var selectedIndex by remember{ mutableStateOf(0) }

    // --- NUEVAS ACCIONES DE NAVEGACIÓN ---
    val navigateToAssignedGuards = { navController.navigate(AssignedGuardsList) }
    val navigateToPersonalStaffing = { navController.navigate(PersonalStaffingScreen) }
    val navigateToConsignas = { navController.navigate(Consignas) }
    val navigateToRoundsHistory = { navController.navigate(RoundsHistoryScreen) }
    val navigateToHelp = { navController.navigate(HelpCustomerService) }
    val navigateToGuardDetail: (String) -> Unit = { id ->
        navController.navigate(AssignedGuardDetails(id))
    }

    // --- LOGICA ANTERIOR (Comentada o Desacoplada) ---
    /*
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
    */

    Scaffold(
       modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            BottomNavBar(
                navItemList = NavItemList.navItemList, // Ahora usa la lista de 3 items
                selectedIndex = selectedIndex,
                onItemSelected = { index -> selectedIndex = index }
            )
        }
    ){ it ->
        ContentScreen(
            selectedIndex = selectedIndex,
            modifier = Modifier.padding(it),
            tenantId = tenantId,
            onAssignedGuardsClick = navigateToAssignedGuards,
            onPersonalStaffingClick = navigateToPersonalStaffing,
            onConsignasClick = navigateToConsignas,
            onRoundsHistoryClick = navigateToRoundsHistory,
            onHelpCustomerServiceClick = navigateToHelp,
            onGuardClick = navigateToGuardDetail
        )
    }
}

@Composable
fun ContentScreen(
    selectedIndex: Int,
    modifier: Modifier,
    tenantId: String,
    onAssignedGuardsClick: () -> Unit = {},
    onPersonalStaffingClick: () -> Unit = {},
    onConsignasClick: () -> Unit = {},
    onRoundsHistoryClick: () -> Unit = {},
    onHelpCustomerServiceClick: () -> Unit = {},
    onGuardClick: (String) -> Unit = {}
){
    when(selectedIndex){
        0 -> HomeScreen(modifier, tenantId = tenantId)

        1 -> MySecurityScreen(
            onAssignedGuardsClick = onAssignedGuardsClick,
            onPersonalStaffingClick = onPersonalStaffingClick,
            onConsignasClick = onConsignasClick,
            onRoundsHistoryClick = onRoundsHistoryClick,
            onHelpCustomerServiceClick = onHelpCustomerServiceClick,
            onGuardClick = onGuardClick
        )

        2 -> UserAccountScreen(modifier)

        // --- PESTAÑAS ANTERIORES COMENTADAS ---
        /*
        0-> HomeScreen(Modifier, tenantId = tenantId, onBillingClicked= navigateToBilling)

        1 -> PostSitesScreen(Modifier, navigateToStations)

        2 -> MyAccountScreen(Modifier)

        3 -> MoreScreen(Modifier,
            navigateToGuardsScreen,
            navigateToGuardShiftScreen,
            navigateToShiftScreen,
            navigateToVisitLog)
        */
    }
}