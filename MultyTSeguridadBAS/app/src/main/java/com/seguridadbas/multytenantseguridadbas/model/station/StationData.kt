package com.seguridadbas.multytenantseguridadbas.model.station

data class StationData(
    val assignedGuards: List<Any>  = emptyList(),
    val finishTimeInDay: String,
    val id: String,
    val incidents: List<Any> = emptyList(),
    val latitude: String,
    val longitude: String,
    val stationName: String,
    val stationSchedule: String,
    val tasks: List<Any> = emptyList()
)
