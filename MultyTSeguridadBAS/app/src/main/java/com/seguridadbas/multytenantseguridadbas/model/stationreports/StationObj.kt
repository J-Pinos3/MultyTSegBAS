package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName

data class StationObj(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("stationName")
    val stationName: String = "",
    @SerializedName("latitud")
    val latitud: String = "",
    @SerializedName("longitud")
    val longitud: String = "",
    @SerializedName("numberOfGuardsInStation")
    val numberOfGuardsInStation: String = "",
    @SerializedName("stationSchedule")
    val stationSchedule: String = "",
    @SerializedName("startingTimeInDay")
    val startingTimeInDay: String = "",
    @SerializedName("finishTimeInDay")
    val finishTimeInDay: String = "",
    @SerializedName("importHash")
    val importHash: String = "",
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("deletedAt")
    val deletedAt: String = "",
    @SerializedName("stationOriginId")
    val stationOriginId: String = "",
    @SerializedName("tenantId")
    val tenantId: String = "",
    @SerializedName("createdById")
    val createdById: String = "",
    @SerializedName("updatedById")
    val updatedById: String = "",
)