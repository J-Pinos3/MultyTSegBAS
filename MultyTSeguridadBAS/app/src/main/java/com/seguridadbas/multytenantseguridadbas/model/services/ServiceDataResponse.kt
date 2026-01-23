package com.seguridadbas.multytenantseguridadbas.model.services

import com.google.gson.annotations.SerializedName

data class ServiceDataResponse(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("description")
    val description: String,
    @SerializedName("iconImage")
    val iconImage: List<Any?> = emptyList<Any?>(),
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("price")
    val price: String,
    @SerializedName("serviceImages")
    val serviceImages: List<Any?> = emptyList<Any>(),
    @SerializedName("taxId")
    val taxId: String? = "",
    @SerializedName("taxName")
    val taxName: String? = "",
    @SerializedName("taxRate")
    val taxRate: String? = "",
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = ""
)