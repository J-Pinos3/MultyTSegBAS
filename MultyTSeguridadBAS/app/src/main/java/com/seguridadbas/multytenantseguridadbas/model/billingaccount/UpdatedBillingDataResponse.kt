package com.seguridadbas.multytenantseguridadbas.model.billingaccount

import com.google.gson.annotations.SerializedName

data class UpdatedBillingDataResponse(

    @SerializedName("bill")
    val bill: List<Any?> = emptyList<Any>(),
    @SerializedName("clientsInvoiced")
    val clientsInvoiced: ClientsInvoiced,
    @SerializedName("clientsInvoicedId")
    val clientsInvoicedId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("invoiceNumber")
    val invoiceNumber: String,
    @SerializedName("lastPaymentDate")
    val lastPaymentDate: String? = "",
    @SerializedName("montoPorPagar")
    val montoPorPagar: String? = "",
    @SerializedName("nextPaymentDate")
    val nextPaymentDate: String? = "",
    @SerializedName("status")
    val status: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = ""
)