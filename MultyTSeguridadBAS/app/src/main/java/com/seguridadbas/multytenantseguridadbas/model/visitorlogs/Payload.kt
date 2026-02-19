package com.seguridadbas.multytenantseguridadbas.model.visitorlogs

import com.google.gson.annotations.SerializedName

data class Payload(
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("createdById")
    val createdById: String = "",
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("exitTime")
    val exitTime: String? = "",
    @SerializedName("firstName")
    val firstName: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("idNumber")
    val idNumber: String = "",
    @SerializedName("idPhoto")
    val idPhoto: List<IdPhotoResponse> = emptyList(),
    @SerializedName("lastName")
    val lastName: String = "",
    @SerializedName("numPeople")
    val numPeople: Int = 0,
    @SerializedName("reason")
    val reason: String = "",
    @SerializedName("tenantId")
    val tenantId: String = "",
    @SerializedName("updatedAt")
    val updatedAt: String = "",
    @SerializedName("updatedById")
    val updatedById: String = "",
    @SerializedName("visitDate")
    val visitDate: String = ""
)