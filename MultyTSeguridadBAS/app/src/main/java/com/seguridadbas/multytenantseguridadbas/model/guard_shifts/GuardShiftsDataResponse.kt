package com.seguridadbas.multytenantseguridadbas.model.guard_shifts

import com.google.gson.annotations.SerializedName

//todo replace List<any> for a proper datatype
data class GuardShiftsDataResponse(
    @SerializedName("completeInventoryCheck")
    val completeInventoryCheck: Boolean? = null,
    @SerializedName("completeInventoryCheckId")
    val completeInventoryCheckId: String? = "",
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String? = "",
    @SerializedName("dailyIncidents")
    val dailyIncidents: List<Any?> = emptyList<Any>(),
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("guardName")
    val guardName: String? = "",
    @SerializedName("guardNameId")
    val guardNameId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = "",
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
    val stationName: StationName,
    @SerializedName("stationNameId")
    val stationNameId: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = ""
)







