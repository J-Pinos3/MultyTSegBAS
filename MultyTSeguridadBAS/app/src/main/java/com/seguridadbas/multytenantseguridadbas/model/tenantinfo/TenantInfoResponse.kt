package com.seguridadbas.multytenantseguridadbas.model.tenantinfo

import com.google.gson.annotations.SerializedName

data class TenantInfoResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("addressLine2")
    val addressLine2: String? = "",
    @SerializedName("businessTitle")
    val businessTitle: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String? = "",
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("email")
    val email: String,
    @SerializedName("extraLines")
    val extraLines: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("latitude")
    val latitude: String? = "",
    @SerializedName("licenseNumber")
    val licenseNumber: String,
    @SerializedName("logoId")
    val logoId: String? = "",
    @SerializedName("longitude")
    val longitude: String? = "",
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("plan")
    val plan: String,
    @SerializedName("planStatus")
    val planStatus: String,
    @SerializedName("planStripeCustomerId")
    val planStripeCustomerId: String? = "",
    @SerializedName("planUserId")
    val planUserId: String? = "",
    @SerializedName("postalCode")
    val postalCode: String,
    @SerializedName("settings")
    val settings: List<Any> = emptyList(),
    @SerializedName("taxNumber")
    val taxNumber: String,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = "",
    @SerializedName("url")
    val url: String? = "",
    @SerializedName("website")
    val website: String
)