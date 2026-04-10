package com.seguridadbas.multytenantseguridadbas.model.station

data class ShortStation(
    val stationId: String,
    val stationName: String,
    val stationSchedule: String,
    val latitud: String,
    val longitud: String
)
