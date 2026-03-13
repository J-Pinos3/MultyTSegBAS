package com.seguridadbas.multytenantseguridadbas.model.station

import com.google.gson.annotations.SerializedName

data class StationsDataResponse(
    @SerializedName("assignedGuards")
    val assignedGuards: List<Any?> = emptyList<Any>(),
    @SerializedName("checkpoints")
    val checkpoints: List<Any?> = emptyList<Any>(),
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String? = "",
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("finishTimeInDay")
    val finishTimeInDay: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("incidents")
    val incidents: List<Any?> = emptyList<Any>(),
    @SerializedName("latitud")
    val latitud: String,
    @SerializedName("longitud")
    val longitud: String,
    @SerializedName("")
    val numberOfGuardsInStation: String,
    @SerializedName("patrol")
    val patrol: List<Any?> = emptyList<Any>(),
    @SerializedName("reports")
    val reports: List<Any?> = emptyList<Any>(),
    @SerializedName("shift")
    val shift: List<Any?> = emptyList<Any>(),
    @SerializedName("startingTimeInDay")
    val startingTimeInDay: String,
    @SerializedName("stationName")
    val stationName: String,
    @SerializedName("stationOrigin")
    val stationOrigin: String? = "",
    @SerializedName("stationOriginId")
    val stationOriginId: String? = "",
    @SerializedName("stationSchedule")
    val stationSchedule: String,
    @SerializedName("tasks")
    val tasks: List<Any?> = emptyList<Any>(),
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById:String? = ""
)