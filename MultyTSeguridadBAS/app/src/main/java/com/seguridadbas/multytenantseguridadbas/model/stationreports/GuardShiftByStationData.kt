package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName
import com.seguridadbas.multytenantseguridadbas.model.station.StationData
import com.seguridadbas.multytenantseguridadbas.model.station.StationsDataResponse

data class GuardShiftByStationData(
    @SerializedName("completeInventoryCheck")
    val completeInventoryCheck: Boolean? = null,
    @SerializedName("completeInventoryCheckId")
    val completeInventoryCheckId: String? = null,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String? = null,
    @SerializedName("dailyIncidents")
    val dailyIncidents: List<Any?> = emptyList<Any>(),
    @SerializedName("deletedAt")
    val deletedAt: String? = null,
    @SerializedName("guardName")
    val guardName: String? = null,
    @SerializedName("guardNameId")
    val guardNameId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = null,
    @SerializedName("numberOfIncidentsDurindShift")
    val numberOfIncidentsDurindShift: Int,
    @SerializedName("numberOfPatrolsDuringShift")
    val numberOfPatrolsDuringShift: Int,
    @SerializedName("observations")
    val observations: String,
    @SerializedName("patrolsDone")
    val patrolsDone: List<Any?> = emptyList<Any>(),
    @SerializedName("punchInTime")
    val punchInTime: String,
    @SerializedName("punchOutTime")
    val punchOutTime: String,
    @SerializedName("shiftSchedule")
    val shiftSchedule: String,
    @SerializedName("stationName")
    val stationName: StationObj,
    @SerializedName("stationNameId")
    val stationNameId: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = null
)