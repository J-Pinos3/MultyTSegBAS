package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName

data class IncidentsByStationData(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = null,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: List<Any?> = emptyList<Any>(),
    @SerializedName("importHash")
    val importHash: String? = null,
    @SerializedName("stationIncidents")
    val stationIncidents: String? = null,
    @SerializedName("stationIncidentsId")
    val stationIncidentsId: String? = null,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = null,
    @SerializedName("wasRead")
    val wasRead: Boolean
)