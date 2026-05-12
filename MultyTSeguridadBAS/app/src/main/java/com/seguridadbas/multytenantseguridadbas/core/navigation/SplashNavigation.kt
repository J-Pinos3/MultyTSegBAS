package com.seguridadbas.multytenantseguridadbas.core.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seguridadbas.multytenantseguridadbas.view.*
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun SplashNavigation(
                     deepLinkIntentFlowSocial: SharedFlow<Intent>
                     ) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Splash){
        composable<Splash>{
            SplashScreen(
                "Cguard",   {  navController.navigate(Login)      },
            )
        }

        composable <Login>{
            LoginScreen(

                {it-> navController.navigate(Home(tenantId = it)) },
                { navController.navigate(ResetPasswordSc) },
                { navController.navigate(Register) },
            )
        }

        composable <Home>{ backstackEntry ->
            val home =backstackEntry.toRoute<Home>()
            BottonNavScreen(navController, home.tenantId)
        }

        composable <Register>{
            RegisterScreen()
        }

        composable<ResetPasswordSc>{
            ResetPasswordScreen()
        }

        // --- NUEVAS RUTAS DE NAVEGACIÓN ---

        composable<AssignedGuardsList> {
            AssignedGuardsList(
                onBackClick = { navController.popBackStack() },
                onGuardClick = { guardId -> navController.navigate(AssignedGuardDetails(guardId)) }
            )
        }

        composable<AssignedGuardDetails> { backStackEntry ->
            val guardId = backStackEntry.toRoute<AssignedGuardDetails>().guardId
            // Usando el ID para la pantalla de detalle (Placeholder o implementación real)
            AssignedGuardDetailsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<PersonalStaffingScreen> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Personal Staffing Screen (Placeholder)")
            }
        }

        composable<RoundsHistoryScreen> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Rounds History Screen (Placeholder)")
            }
        }

        composable<HelpCustomerService> {
            HelpCustomerService(
                onBackClick = { navController.popBackStack() }
            )
        }

        // --- RUTAS ANTERIORES (Mantenidas para compatibilidad) ---

        composable <Stations>{ backStackEntry ->
            val postSiteId = backStackEntry.toRoute<Stations>()
            StationsScreen(
                Modifier,
                onStationClicked = { siteName -> navController.navigate(Business(siteName = siteName)) },
                postSiteId = postSiteId.postSiteId,
                navigateBackToPostSites = {navController.popBackStack()}
            )
        }


        composable<PostSites>{
            PostSitesScreen(
                Modifier,
                onPostSiteClicked = { postSiteId -> navController.navigate(Stations(postSiteId =postSiteId)) },
            )
        }


        composable <Business>{ backStackEntry ->
            val business = backStackEntry.toRoute<Business>()
            BusinessScreen(
                siteId = business.siteName,
                navigateBackToPostSites = { navController.popBackStack() },
                onGuardShiftByStationClicked = {siteId -> navController.navigate(GuarShiftByStationReport(siteId)) },
                onReportsByStationClicked = { siteId ->navController.navigate(ReportsByStation(siteId)) },
                onIncidentsByStationClicked = { navController.navigate(IncidentsByStationScreen) },
                onPatrolsByStationClicked = { siteId -> navController.navigate(PatrolsByStation(siteId)) },
                onInventoryByStationClicked = { navController.navigate(InventoryByStation) },)
        }

        composable <GuarShiftByStationReport>{ backStackEntry ->
            val guarsh = backStackEntry.toRoute<GuarShiftByStationReport>()
            GuardShiftByStationScreen(
                guarsh.siteId,
                Modifier,
                navigateBackToBusiness = { navController.popBackStack() },
                onGuardShiftReportClicked = { reportId -> navController.navigate(GuarShiftStationReprDetail(reportId)) },
            )
        }



        composable <GuarShiftStationReprDetail>{ backStackEntry ->
            val report = backStackEntry.toRoute<GuarShiftStationReprDetail>()

            GuardShiftByStationDetail(
                Modifier,
                reportId = report.reportId,
                navigateBackToGuardShiftsByStation = {navController.popBackStack()},
            )
        }


        composable <ReportsByStation>{  backStackEntry ->
            val report = backStackEntry.toRoute<ReportsByStation>()

            ReportsByStationScreen(
                report.siteId,
                Modifier,
                navigateBackToBusiness = { navController.popBackStack() },
                // ↓ reportId -> navController.navigate(GuarShiftStationReprDetail(reportId))
                onStationReportClicked = { reportId -> navController.navigate(ReportsByStationDetail(reportId))  },

            )

        }

        composable <ReportsByStationDetail>{ backStackEntry ->
            val report = backStackEntry.toRoute<ReportsByStationDetail>()

            ReportsByStationDetail(
                Modifier,
                reportId = report.reportId,
                navigateBackToReportsByStation = {navController.popBackStack()},
            )
        }

        composable <IncidentsByStationScreen>{
            IncidentsByStationScreen(
                Modifier,
                navigateBackToBusiness = {navController.popBackStack()},
                onIncidentClicked = { incidentId -> navController.navigate(IncidentsDetail(incidentId)) },
            )
        }


        composable <IncidentsDetail>{ backStackEntry ->
            val incidentId = backStackEntry.toRoute<IncidentsDetail>().incidentId
            IncidentsByStationDetail(
                Modifier,
                navigateBackToIncidents = {navController.popBackStack()},
                incidentID = incidentId
            )
        }


        composable <PatrolsByStation>{ backStackEntry ->
            val report = backStackEntry.toRoute<PatrolsByStation>()

            PatrolsByStationScreen(
                Modifier,
                siteId = report.siteId,
                navigateBackToBusiness = {navController.popBackStack()},
            )
        }

        composable <InventoryByStation>{
            InventoryByStationScreen(
                Modifier,
                navigateBackToBusiness = {navController.popBackStack()},
                //reportId -> navController.navigate(ReportsByStationDetail(reportId))
                onInventoryReportClicked = { reportiD -> navController.navigate( InventoryByStationDet(reportiD) ) },
            )
        }

        composable <InventoryByStationDet>{ backStackEntry ->
            val report = backStackEntry.toRoute<InventoryByStationDet>()
            InventoryByStationDetail(
                Modifier,
                reportId = report.reportId,
                navigateBackToInventoryByStat = {navController.popBackStack()},
            )
        }

        composable <GuardsScreen>{
            AllGuardsScreen(
                Modifier,
                navigateBackToMore = {navController.popBackStack()},
                onGuardClicked = { guardId -> navController.navigate(GuardsScreenDetail(guardId)) },
            )
        }

        composable <GuardsScreenDetail>{backStackEntry ->
            val guard = backStackEntry.toRoute<GuardsScreenDetail>()
            SecGuardDetail(
                Modifier, guardId = guard.guardId,
                navigateBackToAllGuards = { navController.popBackStack() },
            )
        }

        composable <GuardsShiftScreen> {
            AllGuardShiftsScreen(
                Modifier,
                navigateBackToMore = { navController.popBackStack() },
                onGuardShiftClicked = { guardShiftId -> navController.navigate(GuardsShiftDetail(guardShiftId)) },
            )
        }

        composable <GuardsShiftDetail>{ backStackEntry ->
            val id = backStackEntry.toRoute<GuardsShiftDetail>()
            GuardShiftsDetailSc(
                Modifier,
                id.guardShiftId,
                navigateBackToAllGuardsShifts = { navController.popBackStack() },
            )
        }


        composable<ShiftsScreen>{
            AllShiftScreen(
                Modifier,
                navigateBackToMore = { navController.popBackStack()  },
                onShiftClicked = { shiftId -> navController.navigate(ShiftDetailsSc(shiftId)) },
            )
        }

        composable <ShiftDetailsSc>{ backStackEntry ->
            val id = backStackEntry.toRoute<ShiftDetailsSc>().shiftId
            ShiftDetailScreen(
                Modifier,
                id,
                navigateBackToShifts = { navController.popBackStack() },
            )
        }


        composable <BillingListScreen>{
            BillingScreen(
                Modifier,
                navigateBackToHome = { navController.popBackStack() },
            )
        }

        composable<VisitorLogsScreen>{
            VisitLogScreen(
                Modifier,
                navigateBackToMore = { navController.popBackStack() },
            )

        }

        composable<Consignas>{
            ConsignasScreen(
                onBackClick = { navController.popBackStack() },
            )
        }

    }

}