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

object NavItemList{
    val navItemList = listOf(

        NavItem("Inicio",  R.drawable.ic_home  ),
        NavItem("Mi Negocio", R.drawable.ic_business ),
        NavItem("MI Cuenta", R.drawable.ic_person),

    )
}