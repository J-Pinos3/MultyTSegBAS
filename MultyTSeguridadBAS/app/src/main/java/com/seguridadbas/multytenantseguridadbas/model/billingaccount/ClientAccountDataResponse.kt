package com.seguridadbas.multytenantseguridadbas.model.billingaccount

import com.google.gson.annotations.SerializedName

data class ClientAccountDataResponse(
    @SerializedName("address")
    val address: String,
    @SerializedName("billingInvoices")
    val billingInvoices: List<Any?> = emptyList<Any>(),
    @SerializedName("commercialName")
    val commercialName: String,
    @SerializedName("contractDate")
    val contractDate: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("email")
    val email: String,
    @SerializedName("faxNumber")
    val faxNumber: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? ="",
    @SerializedName("logoUrl")
    val logoUrl: List<Any>,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("placePictureUrl")
    val placePictureUrl: List<Any?> = emptyList<Any>(),
    @SerializedName("purchasedServices")
    val purchasedServices: List<Any?> = emptyList<Any>(),
    @SerializedName("pushNotifications")
    val pushNotifications: List<Any?> = emptyList<Any>(),
    @SerializedName("representante")
    val representante: String? = "",
    @SerializedName("representanteId")
    val representanteId: String? = "",
    @SerializedName("rucNumber")
    val rucNumber: String,
    @SerializedName("stations")
    val stations: List<Any?> = emptyList<Any>(),
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = ""
)