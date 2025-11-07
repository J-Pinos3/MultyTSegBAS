package com.seguridadbas.multytenantseguridadbas.model.shifts

import android.provider.MediaStore

data class ShiftsData (
    val endTime: String,
    val startTime: String,
    val guardEmail: String,
    val guardFirstName: String,
    val guardId: String,
    val guardLastName: String,
    val shiftId: String,
    val finishTimeInDay: String,
    val latitude: String,
    val longitude: String,
    val numberOfGuardsInStation: String,
    val startingTimeInDay: String,
    val stationName: String,
    val stationSchedule: String,
    val tenantId: String

)