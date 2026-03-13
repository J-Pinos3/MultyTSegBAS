package com.seguridadbas.multytenantseguridadbas.model.billingaccount

import com.google.gson.annotations.SerializedName

data class ClientAccountDataResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("addressComplement")
    val addressComplement: String? = "",
    @SerializedName("zipCode")
    val zipCode: String? = "",
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("useSameAddressForBilling")
    val useSameAddressForBilling: Boolean,
    @SerializedName("website")
    val website: String? = "",
    @SerializedName("latitude")
    val latitude: String? = "",
    @SerializedName("longitude")
    val longitude: String? = "",
    @SerializedName("categoryIds")
    val categoryIds: List<Any?> = emptyList<Any>(),
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("categories")
    val categories: List<Any?> = emptyList<Any>(),
    @SerializedName("createdAt")//∯∯∯
    val createdAt: String,
    @SerializedName("createdById")//∯∯∯
    val createdById: String,
    @SerializedName("deletedAt")//∯∯∯
    val deletedAt: String? = "",
    @SerializedName("email")//∯∯∯
    val email: String,
    @SerializedName("faxNumber")
    val faxNumber: String? = "",
    @SerializedName("id")//∯∯∯
    val id: String,
    @SerializedName("importHash")//∯∯∯
    val importHash: String? ="",
    @SerializedName("phoneNumber")//∯∯∯
    val phoneNumber: String,
    @SerializedName("name")//∯∯∯
    val name: String,
    @SerializedName("lastName")//∯∯∯
    val lastName: String? = "",
    @SerializedName("tenantId")//∯∯∯
    val tenantId: String,
    @SerializedName("updatedAt")//∯∯∯
    val updatedAt: String,
    @SerializedName("updatedById")//∯∯∯
    val updatedById: String? = ""
)