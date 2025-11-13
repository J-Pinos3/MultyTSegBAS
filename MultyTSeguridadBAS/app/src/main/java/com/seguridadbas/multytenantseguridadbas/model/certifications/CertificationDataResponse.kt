package com.seguridadbas.multytenantseguridadbas.model.certifications

import com.google.gson.annotations.SerializedName

data class CertificationDataResponse(
    @SerializedName("acquisitionDate")
    val acquisitionDate: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("description")
    val description: String,
    @SerializedName("expirationDate")
    val expirationDate: String,
    @SerializedName("icon")
    val icon: List<Any?> = emptyList<Any>(),
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: List<Any?> = emptyList<Any>(),
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = ""
)