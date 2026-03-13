package com.seguridadbas.multytenantseguridadbas.model.shifts

import com.google.gson.annotations.SerializedName

data class ShiftsDataResponse(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String? = "",
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("guard")
    val guard: Guard,
    @SerializedName("guardId")
    val guardId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("station")
    val station: Station,
    @SerializedName("stationId")
    val stationId: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = ""
)