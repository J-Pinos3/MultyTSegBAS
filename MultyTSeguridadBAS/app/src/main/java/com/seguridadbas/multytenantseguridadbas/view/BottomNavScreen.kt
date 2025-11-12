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
import com.seguridadbas.multytenantseguridadbas.controllers.stationscontroller.StationsController
import com.seguridadbas.multytenantseguridadbas.core.navigation.BottomNavBar
import com.seguridadbas.multytenantseguridadbas.core.navigation.Business
import com.seguridadbas.multytenantseguridadbas.core.navigation.NavItemList

@Composable
fun BottonNavScreen(navController: NavController, authController: AuthController,
                    stationsController: StationsController){
    //var selectedIndex by remember{ mutableStateOf(0) }
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    val navigateToBusiness: (String) -> Unit = {
        siteName ->
        navController.saveState()
        navController.navigate(Business(siteName = siteName) )
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
            authController = authController,
            stationsController = stationsController
        )
    }
}


@Composable
fun ContentScreen(
    selectedIndex: Int,
    modifier: Modifier,
    navigateToBusiness: (String) -> Unit = {},
    authController: AuthController,
    stationsController: StationsController
){
    when(selectedIndex){
        0-> HomeScreen(Modifier, authController)

        1 -> PostSitesScreen(Modifier, navigateToBusiness, stationsController)

        2 -> MyAccountScreen(Modifier, authController)

        3 -> MoreScreen()
    }
}