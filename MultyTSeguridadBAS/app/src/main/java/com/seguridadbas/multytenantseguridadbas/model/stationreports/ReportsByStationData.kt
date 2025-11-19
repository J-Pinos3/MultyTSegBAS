package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName
import com.seguridadbas.multytenantseguridadbas.model.station.StationsDataResponse

data class ReportsByStationData(
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = null,
    @SerializedName("generatedDate")
    val generatedDate: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = null,
    @SerializedName("station")
    val station: StationObj,
    @SerializedName("stationId")
    val stationId: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = null
)