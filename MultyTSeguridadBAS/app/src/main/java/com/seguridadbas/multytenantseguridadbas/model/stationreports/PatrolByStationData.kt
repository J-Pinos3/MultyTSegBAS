package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName
import com.seguridadbas.multytenantseguridadbas.model.Guard
import com.seguridadbas.multytenantseguridadbas.model.station.StationsDataResponse

data class PatrolByStationData(
    @SerializedName("assignedGuard")
    val assignedGuard: Guard,
    @SerializedName("assignedGuardId")
    val assignedGuardId: String,
    @SerializedName("checkpoints")
    val checkpoints: List<Any?> = emptyList<Any>(),
    @SerializedName("completed")
    val completed: Boolean,
    @SerializedName("completionTime")
    val completionTime: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = null,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = null,
    @SerializedName("logs")
    val logs: List<Any?> = emptyList<Any>(),
    @SerializedName("scheduledTime")
    val scheduledTime: String,
    @SerializedName("station")
    val station: StationsDataResponse,
    @SerializedName("stationId")
    val stationId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = null
)