package com.seguridadbas.multytenantseguridadbas.model.station

import com.google.gson.annotations.SerializedName

data class IncidentsStat(
    @SerializedName("id")
    val id: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("wasRead")
    val wasRead: Boolean,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("stationIncidentsId")
    val stationIncidentsId: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("updatedById")
    val updatedById: String? = "",
)