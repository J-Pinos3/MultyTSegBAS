package com.seguridadbas.multytenantseguridadbas.model.postsite

import com.google.gson.annotations.SerializedName

data class AllPostSiteData(
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("address")
    val address: String,
    @SerializedName("categoryIds")
    val categoryIds: List<Any?> = emptyList(),
    @SerializedName("city")
    val city: String,
    @SerializedName("client")
    val client: Client,
    @SerializedName("clientAccount")
    val clientAccount: ClientAccount,
    @SerializedName("clientAccountId")
    val clientAccountId: String,
    @SerializedName("clientAccountName")
    val clientAccountName: String,
    @SerializedName("clientId")
    val clientId: String,
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("contactEmail")
    val contactEmail: String,
    @SerializedName("contactPhone")
    val contactPhone: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? ="",
    @SerializedName("description")
    val description: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("latitud")
    val latitud: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("logo")
    val logo: List<Any?> = emptyList(),
    @SerializedName("longitud")
    val longitud: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("postalCode")
    val postalCode: String,
    @SerializedName("secondAddress")
    val secondAddress: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String
)