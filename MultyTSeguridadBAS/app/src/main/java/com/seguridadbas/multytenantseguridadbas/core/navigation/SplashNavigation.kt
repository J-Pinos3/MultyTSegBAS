package com.seguridadbas.multytenantseguridadbas.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.certifservicescontroller.CertificationServicesController
import com.seguridadbas.multytenantseguridadbas.controllers.guardshiftscontroller.GuardShiftsController
import com.seguridadbas.multytenantseguridadbas.controllers.shiftscontroller.ShiftsController
import com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller.StationReportsController
import com.seguridadbas.multytenantseguridadbas.controllers.stationscontroller.StationsController
import com.seguridadbas.multytenantseguridadbas.controllers.tenantcontroller.TenantGuardsController
import com.seguridadbas.multytenantseguridadbas.view.AllGuardShiftsScreen
import com.seguridadbas.multytenantseguridadbas.view.AllGuardsScreen
import com.seguridadbas.multytenantseguridadbas.view.AllShiftScreen
import com.seguridadbas.multytenantseguridadbas.view.BottonNavScreen
import com.seguridadbas.multytenantseguridadbas.view.BusinessScreen
import com.seguridadbas.multytenantseguridadbas.view.GuardShiftByStationDetail
import com.seguridadbas.multytenantseguridadbas.view.GuardShiftByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.GuardShiftsDetailSc
import com.seguridadbas.multytenantseguridadbas.view.IncidentsByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.InventoryByStationDetail
import com.seguridadbas.multytenantseguridadbas.view.InventoryByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.LoginScreen
import com.seguridadbas.multytenantseguridadbas.view.PatrolsByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.PostSitesScreen
import com.seguridadbas.multytenantseguridadbas.view.RegisterScreen
import com.seguridadbas.multytenantseguridadbas.view.ReportsByStationDetail
import com.seguridadbas.multytenantseguridadbas.view.ReportsByStationScreen
import com.seguridadbas.multytenantseguridadbas.view.ResetPasswordScreen
import com.seguridadbas.multytenantseguridadbas.view.SecGuardDetail
import com.seguridadbas.multytenantseguridadbas.view.SplashScreen

@Composable
fun SplashNavigation(authController: AuthController,
                     stationsController: StationsController,
                     certificationServicesController: CertificationServicesController,
                     stationsReportsController: StationReportsController,
                     tenantGuardsController: TenantGuardsController,
                     guardShiftsController: GuardShiftsController,
                     shiftsController: ShiftsController
                     ) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Splash){
        composable<Splash>{
            SplashScreen(
                "BAS",   {  navController.navigate(Login)      }
            )
        }

        composable <Login>{
            LoginScreen(

                {it-> navController.navigate(Home(tenantId = it)) },
                { navController.navigate(ResetPasswordSc) },
                { navController.navigate(Register) },
                authController = authController
            )
        }

        composable <Home>{ backstackEntry ->
            val home =backstackEntry.toRoute<Home>()
            BottonNavScreen(navController, authController, stationsController, certificationServicesController,home.tenantId)
        }

        composable <Register>{
            RegisterScreen(authController = authController)
        }

        composable<ResetPasswordSc>{
            ResetPasswordScreen(authController = authController)
        }

        composable <Sites>{
            PostSitesScreen(
                Modifier,
                onPostSiteClicked = { siteName -> navController.navigate(Business(siteName = siteName)) },
                stationsController = stationsController
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
                onInventoryByStationClicked = { navController.navigate(InventoryByStation) },
                stationsController = stationsController)
        }

        composable <GuarShiftByStationReport>{ backStackEntry ->
            val guarsh = backStackEntry.toRoute<GuarShiftByStationReport>()
            GuardShiftByStationScreen(
                guarsh.siteId,
                Modifier,
                navigateBackToBusiness = { navController.popBackStack() },
                onGuardShiftReportClicked = { reportId -> navController.navigate(GuarShiftStationReprDetail(reportId)) },
                stationsReportsController = stationsReportsController
            )
        }



        composable <GuarShiftStationReprDetail>{ backStackEntry ->
            val report = backStackEntry.toRoute<GuarShiftStationReprDetail>()

            GuardShiftByStationDetail(
                Modifier,
                reportId = report.reportId,
                navigateBackToGuardShiftsByStation = {navController.popBackStack()},
                stationReportsController = stationsReportsController
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
                stationsReportsController = stationsReportsController

            )

        }

        composable <ReportsByStationDetail>{ backStackEntry ->
            val report = backStackEntry.toRoute<ReportsByStationDetail>()

            ReportsByStationDetail(
                Modifier,
                reportId = report.reportId,
                navigateBackToReportsByStation = {navController.popBackStack()},
                stationReportsController = stationsReportsController
            )
        }

        composable <IncidentsByStationScreen>{
            IncidentsByStationScreen(
                Modifier,
                navigateBackToBusiness = {navController.popBackStack()},
                stationsReportsController = stationsReportsController
            )
        }

        composable <PatrolsByStation>{ backStackEntry ->
            val report = backStackEntry.toRoute<PatrolsByStation>()

            PatrolsByStationScreen(
                Modifier,
                siteId = report.siteId,
                navigateBackToBusiness = {navController.popBackStack()},
                stationsReportsController = stationsReportsController
            )
        }

        composable <InventoryByStation>{
            InventoryByStationScreen(
                Modifier,
                navigateBackToBusiness = {navController.popBackStack()},
                //reportId -> navController.navigate(ReportsByStationDetail(reportId))
                onInventoryReportClicked = { reportiD -> navController.navigate( InventoryByStationDet(reportiD) ) },
                stationsReportsController
            )
        }

        composable <InventoryByStationDet>{ backStackEntry ->
            val report = backStackEntry.toRoute<InventoryByStationDet>()
            InventoryByStationDetail(
                Modifier,
                reportId = report.reportId,
                navigateBackToInventoryByStat = {navController.popBackStack()},
                stationsReportsController = stationsReportsController
            )
        }

        composable <GuardsScreen>{
            AllGuardsScreen(
                Modifier,
                navigateBackToMore = {navController.popBackStack()},
                onGuardClicked = { guardId -> navController.navigate(GuardsScreenDetail(guardId)) },
                tenantGuardsController = tenantGuardsController
            )
        }

        composable <GuardsScreenDetail>{backStackEntry ->
            val guard = backStackEntry.toRoute<GuardsScreenDetail>()
            SecGuardDetail(
                Modifier, guardId = guard.guardId,
                navigateBackToAllGuards = { navController.popBackStack() },
                tenantGuardsController = tenantGuardsController
            )
        }

        composable <GuardsShiftScreen> {
            AllGuardShiftsScreen(
                Modifier,
                navigateBackToMore = { navController.popBackStack() },
                onGuardShiftClicked = { guardShiftId -> navController.navigate(GuardsShiftDetail(guardShiftId)) },
                guardShiftsController = guardShiftsController
            )
        }

        composable <GuardsShiftDetail>{ backStackEntry ->
            val id = backStackEntry.toRoute<GuardsShiftDetail>()
            GuardShiftsDetailSc(
                Modifier,
                id.guardShiftId,
                navigateBackToAllGuardsShifts = { navController.popBackStack() },
                guardShiftsController

            )
        }


        composable<ShiftsScreen>{
            AllShiftScreen(
                Modifier,
                navigateBackToMore = { navController.popBackStack()  },
                onShiftClicked = {},
                shiftsController = shiftsController
            )
        }
    }

}