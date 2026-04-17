package com.seguridadbas.multytenantseguridadbas.core.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
import com.seguridadbas.multytenantseguridadbas.view.AllGuardShiftsScreen
import com.seguridadbas.multytenantseguridadbas.view.AllGuardsScreen
import com.seguridadbas.multytenantseguridadbas.view.AllShiftScreen
import com.seguridadbas.multytenantseguridadbas.view.BillingScreen
import com.seguridadbas.multytenantseguridadbas.view.BottonNavScreen
import com.seguridadbas.multytenantseguridadbas.view.BusinessScreen
import com.seguridadbas.multytenantseguridadbas.view.GuardShiftByStationDetail
import com.seguridadbas.multytenantseguridadbas.view.GuardShiftByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.GuardShiftsDetailSc
import com.seguridadbas.multytenantseguridadbas.view.IncidentsByStationDetail
import com.seguridadbas.multytenantseguridadbas.view.IncidentsByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.InventoryByStationDetail
import com.seguridadbas.multytenantseguridadbas.view.InventoryByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.LoginScreen
import com.seguridadbas.multytenantseguridadbas.view.PatrolsByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.PostSitesScreen
import com.seguridadbas.multytenantseguridadbas.view.StationsScreen
import com.seguridadbas.multytenantseguridadbas.view.RegisterScreen
import com.seguridadbas.multytenantseguridadbas.view.ReportsByStationDetail
import com.seguridadbas.multytenantseguridadbas.view.ReportsByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.ResetPasswordScreen
import com.seguridadbas.multytenantseguridadbas.view.SecGuardDetail
import com.seguridadbas.multytenantseguridadbas.view.ShiftDetailScreen
import com.seguridadbas.multytenantseguridadbas.view.SplashScreen
import com.seguridadbas.multytenantseguridadbas.view.VisitLogScreen
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun SplashNavigation( billingAccountController: BillingAccountController,
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
                deepLinkIntentFlow = deepLinkIntentFlowSocial,
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
                billingAccountController = billingAccountController
            )
        }

        composable<VisitorLogsScreen>{
            VisitLogScreen(
                Modifier,
                navigateBackToMore = { navController.popBackStack() },
            )

        }

    }

}