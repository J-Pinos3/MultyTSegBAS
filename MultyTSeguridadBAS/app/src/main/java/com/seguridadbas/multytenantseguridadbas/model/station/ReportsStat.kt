package com.seguridadbas.multytenantseguridadbas.model.station

import com.google.gson.annotations.SerializedName

data class ReportsStat(

    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("generatedDate")
    val generatedDate: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("stationId")
    val stationId: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("updatedById")
    val updatedById: String? = ""

)
