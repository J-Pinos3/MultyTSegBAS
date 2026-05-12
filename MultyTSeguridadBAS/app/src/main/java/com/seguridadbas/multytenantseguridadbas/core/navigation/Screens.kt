package com.seguridadbas.multytenantseguridadbas.core.navigation

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
data class Stations(val postSiteId: String)

@Serializable
object PostSites

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
data class IncidentsDetail(val incidentId: String)

@Serializable
data class Business(val siteName: String)

@Serializable
object GuardsScreen

@Serializable
data class GuardsScreenDetail(val guardId: String)

@Serializable
object GuardsShiftScreen

@Serializable
data class GuardsShiftDetail(val guardShiftId: String)

@Serializable
object ShiftsScreen

@Serializable
data class ShiftDetailsSc(val shiftId: String)

@Serializable
object VisitorLogsScreen

@Serializable
object BillingListScreen

@Serializable
object Consignas

// --- NUEVAS RUTAS ---

@Serializable
object MySecurity

@Serializable
object UserAccount

@Serializable
object AssignedGuardsList

@Serializable
data class AssignedGuardDetails(val guardId: String)

@Serializable
object PersonalStaffingScreen

@Serializable
object RoundsHistoryScreen

@Serializable
object HelpCustomerService

object NavItemList{
    val navItemList = listOf(
        NavItem("Inicio",  R.drawable.ic_home),
        NavItem("Mi Seguridad", R.drawable.ic_lock_login), // Asegúrate que este icono exista o cámbialo
        NavItem("Mi Cuenta", R.drawable.ic_person)
    )

    // Mantener la lista anterior por si acaso (opcional, el usuario pidió no borrar)
    val oldNavItemList = listOf(
        NavItem("Inicio",  R.drawable.ic_home  ),
        NavItem("Sitios", R.drawable.ic_place),
        NavItem("Mi Cuenta", R.drawable.ic_person),
        NavItem("Más",  R.drawable.ic_more_features)
    )
}