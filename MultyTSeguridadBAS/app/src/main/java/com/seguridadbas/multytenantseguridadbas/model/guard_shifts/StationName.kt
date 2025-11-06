package com.seguridadbas.multytenantseguridadbas.model.guard_shifts

data class StationName(
    val createdAt: String,
    val createdById: String,
    val deletedAt: String? = "",
    val finishTimeInDay: String,
    val id: String,
    val importHash: String? = "",
    val latitud: String,
    val longitud: String,
    val numberOfGuardsInStation: String,
    val startingTimeInDay: String,
    val stationName: String,
    val stationOriginId: String? = "",
    val stationSchedule: String,
    val tenantId: String,
    val updatedAt: String,
    val updatedById: String? = ""
)