package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("address")
    val address: String,
    @SerializedName("addressComplement")
    val addressComplement: String,
    @SerializedName("categoryIds")
    val categoryIds: List<Any?> = emptyList(),
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("documentNumber")
    val documentNumber: String? = "",
    @SerializedName("email")
    val email: String,
    @SerializedName("faxNumber")
    val faxNumber: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("landline")
    val landline: String? = "",
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("personType")
    val personType: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String,
    @SerializedName("useSameAddressForBilling")
    val useSameAddressForBilling: Boolean,
    @SerializedName("website")
    val website: String,
    @SerializedName("zipCode")
    val zipCode: String
)