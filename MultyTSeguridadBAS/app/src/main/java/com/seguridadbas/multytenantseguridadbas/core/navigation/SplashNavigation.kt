package com.seguridadbas.multytenantseguridadbas.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seguridadbas.multytenantseguridadbas.view.LoginScreen
import com.seguridadbas.multytenantseguridadbas.view.SplashScreen

@Composable
fun SplashNavigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Splash){
        composable<Splash>{
            SplashScreen(
                "BAS",   {  navController.navigate(Login)      }
            )
        }

        composable <Login>{
            LoginScreen(
                ///todo create empty home screen
                //{ navController.navigate() }
                { navController.navigate(ResetPasswordScreen) },
                { navController.navigate(RegisterScreen) }
            )
        }

        composable <Register>{
            RegisterScreen()
        }

        composable<ResetPasswordScreen>{
            ResetPasswordScreen()
        }
    }

}