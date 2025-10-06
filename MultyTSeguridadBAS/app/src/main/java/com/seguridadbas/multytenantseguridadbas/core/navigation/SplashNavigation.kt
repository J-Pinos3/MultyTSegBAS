package com.seguridadbas.multytenantseguridadbas.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.seguridadbas.multytenantseguridadbas.view.DemoHome
import com.seguridadbas.multytenantseguridadbas.view.LoginScreen
import com.seguridadbas.multytenantseguridadbas.view.RegisterScreen
import com.seguridadbas.multytenantseguridadbas.view.ResetPasswordScreen
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

                { navController.navigate(Home) },
                { navController.navigate(ResetPasswordSc) },
                { navController.navigate(Register) }
            )
        }

        composable <Home>{
            DemoHome()
        }

        composable <Register>{
            RegisterScreen()
        }

        composable<ResetPasswordSc>{
            ResetPasswordScreen()
        }
    }

}