package com.seguridadbas.multytenantseguridadbas.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.compose.rememberNavController
import com.seguridadbas.multytenantseguridadbas.core.navigation.BottomNavBar
import com.seguridadbas.multytenantseguridadbas.core.navigation.NavItemList

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottonNavScreen(){
    var selectedIndex by remember{ mutableStateOf(0) }

    Scaffold(
       modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            BottomNavBar(
                navItemList = NavItemList.navItemList,
                selectedIndex = selectedIndex,
                onItemSelected = { index -> selectedIndex = index }
            )
        }
    ){
        ContentScreen(selectedIndex)
    }
}


@Composable
fun ContentScreen(selectedIndex: Int){
    when(selectedIndex){
        0-> HomeScreen()

        1 -> BusinessScreen()

        2 -> MyAccountScreen()
    }
}