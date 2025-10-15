package com.seguridadbas.multytenantseguridadbas.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.core.navigation.models.NavItem
import kotlinx.serialization.Serializable

@Serializable
object Splash


@Serializable
object Login

@Serializable
object Home

@Serializable
object ResetPasswordSc

@Serializable
object Register

@Serializable
object Sites

@Serializable
object Business

object NavItemList{
    val navItemList = listOf(

        NavItem("Inicio",  R.drawable.ic_home  ),
        NavItem("Sitios", R.drawable.ic_place),
        //NavItem("Mi Negocio", R.drawable.ic_business ),
        NavItem("Mi Cuenta", R.drawable.ic_person),


    )
}