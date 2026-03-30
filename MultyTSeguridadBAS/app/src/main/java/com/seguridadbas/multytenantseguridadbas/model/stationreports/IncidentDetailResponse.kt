package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName

data class IncidentDetailResponse(
    @SerializedName("action")
    val action: String? = "",
    @SerializedName("actionsTaken")
    val actionsTaken: String? = "",
    @SerializedName("callerName")
    val callerName: String,
    @SerializedName("callerType")
    val callerType: String,
    @SerializedName("client")
    val client: Client,
    @SerializedName("clientId")
    val clientId: String,
    @SerializedName("comments")
    val comments: String? = "",
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("dateTime")
    val dateTime: String? = "",
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("description")
    val description: String,
    @SerializedName("guardName")
    val guardName: GuardName,
    @SerializedName("guardNameId")
    val guardNameId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: List<Any?> = emptyList(),
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("incidentAt")
    val incidentAt: String,
    @SerializedName("incidentType")
    val incidentType: IncidentType,
    @SerializedName("incidentTypeId")
    val incidentTypeId: String,
    @SerializedName("internalNotes")
    val internalNotes: String? = "",
    @SerializedName("location")
    val location: String,
    @SerializedName("postSiteId")
    val postSiteId: String? = "",
    @SerializedName("priority")
    val priority: String,
    @SerializedName("site")
    val site: String? = "",
    @SerializedName("siteId")
    val siteId: String? = "",
    @SerializedName("station")
    val station: String? = "",
    @SerializedName("stationId")
    val stationId: String,
    @SerializedName("stationIncidents")
    val stationIncidents: String? = "",
    @SerializedName("stationIncidentsId")
    val stationIncidentsId: String? = "",
    @SerializedName("status")
    val status: String,
    @SerializedName("subject")
    val subject: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String,
    @SerializedName("wasRead")
    val wasRead: Boolean
)