package com.seguridadbas.multytenantseguridadbas.core.navigation

import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.core.navigation.models.NavItem

@Composable
fun BottomNavBar(
    navItemList: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
){
    NavigationBar {
        navItemList.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    onItemSelected(index)
                },
                icon = {
                    Icon(
                        painterResource(navItem.icon),
                        "Icono"
                    )
                },
                label = {
                    Text(text = navItem.label)
                }
            )

        }
    }
}