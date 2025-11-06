package com.seguridadbas.multytenantseguridadbas.model.guard_shifts

data class GuardShift(
    val completeInventoryCheck: Int = 0,
    val dailyIncidents: List<Any?> = emptyList<Any>(),
    val guardName: String = "",
    val guardNameId: String = "",
    val id: String = "",
    val numberOfIncidentsDuringShift: Int = 0,
    val numberOfPatrolsDuringShift: Int = 0,
    val observations: String = "",
    val patrolsDone: List<Any?> = emptyList<Any>(),
    val punchInTime: String = "",
    val punchOutTime: String = "",
    val shiftSchedule: String = "",
    val tenantId: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val numberOfGuardsInStation: Int = 0,
    val stationName: String = ""

)
