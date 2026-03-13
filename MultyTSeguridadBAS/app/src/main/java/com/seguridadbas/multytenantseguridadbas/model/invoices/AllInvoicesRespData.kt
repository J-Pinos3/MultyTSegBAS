package com.seguridadbas.multytenantseguridadbas.model.invoices

import com.google.gson.annotations.SerializedName

data class AllInvoicesRespData(
    @SerializedName("client")
    val client: Client,
    @SerializedName("clientId")
    val clientId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("dueDate")
    val dueDate: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("invoiceNumber")
    val invoiceNumber: String,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("notes")
    val notes: String,
    @SerializedName("payments")
    val payments: List<Payment>,
    @SerializedName("poSoNumber")
    val poSoNumber: String,
    @SerializedName("postSite")
    val postSite: PostSite,
    @SerializedName("postSiteId")
    val postSiteId: String,
    @SerializedName("sentAt")
    val sentAt: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("subtotal")
    val subtotal: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("total")
    val total: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String
)