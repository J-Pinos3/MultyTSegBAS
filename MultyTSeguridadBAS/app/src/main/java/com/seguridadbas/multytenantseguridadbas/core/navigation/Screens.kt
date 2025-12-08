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
data class Home(val tenantId: String)

@Serializable
object ResetPasswordSc

@Serializable
object Register

@Serializable
object Sites

@Serializable
object More

@Serializable
data class GuarShiftByStationReport(val siteId: String)

@Serializable
data class  GuarShiftStationReprDetail(val reportId: String)

@Serializable
data class ReportsByStation(val siteId: String)


@Serializable
data class ReportsByStationDetail(val reportId: String)


@Serializable
data class PatrolsByStation(val siteId: String)


@Serializable
object InventoryByStation

@Serializable
data class InventoryByStationDet(val reportId: String)

@Serializable
object IncidentsByStationScreen

@Serializable
data class Business(val siteName: String)


@Serializable
object GuardsScreen


@Serializable
data class GuardsScreenDetail(val guardId: String)

@Serializable
object GuardsShiftScreen

object NavItemList{
    val navItemList = listOf(

        NavItem("Inicio",  R.drawable.ic_home  ),
        NavItem("Sitios", R.drawable.ic_place),
        //NavItem("Mi Negocio", R.drawable.ic_business ),
        NavItem("Mi Cuenta", R.drawable.ic_person),
        NavItem("Más",  R.drawable.ic_more_features)


    )
}