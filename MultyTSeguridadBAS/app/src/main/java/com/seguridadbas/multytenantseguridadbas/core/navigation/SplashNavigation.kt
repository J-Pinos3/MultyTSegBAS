package com.seguridadbas.multytenantseguridadbas.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.stationscontroller.StationsController
import com.seguridadbas.multytenantseguridadbas.view.BottonNavScreen
import com.seguridadbas.multytenantseguridadbas.view.BusinessScreen
import com.seguridadbas.multytenantseguridadbas.view.LoginScreen
import com.seguridadbas.multytenantseguridadbas.view.PostSitesScreen
import com.seguridadbas.multytenantseguridadbas.view.RegisterScreen
import com.seguridadbas.multytenantseguridadbas.view.ResetPasswordScreen
import com.seguridadbas.multytenantseguridadbas.view.SplashScreen

@Composable
fun SplashNavigation(authController: AuthController,
                     stationsController: StationsController) {

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
            BottonNavScreen(navController, authController, stationsController, home.tenantId)
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
                stationsController = stationsController)
        }
    }

}