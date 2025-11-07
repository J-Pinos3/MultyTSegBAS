package com.seguridadbas.multytenantseguridadbas.model.shifts

data class ShortShiftData(
    val guardName: String,
    val stationName: String,
    val id: String,
    val tenantId: String,
    val stationSchedule: String
)
