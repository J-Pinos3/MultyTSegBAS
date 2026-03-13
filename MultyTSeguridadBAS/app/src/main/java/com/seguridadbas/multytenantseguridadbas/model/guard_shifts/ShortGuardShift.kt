package com.seguridadbas.multytenantseguridadbas.model.guard_shifts

data class ShortGuardShift(
    val id: String,
    val guardName: String,
    val stationName: String,
    val numberOfPatrols: Int,
    val numberOfIncidents: Int,
    val tenantId: String
)
