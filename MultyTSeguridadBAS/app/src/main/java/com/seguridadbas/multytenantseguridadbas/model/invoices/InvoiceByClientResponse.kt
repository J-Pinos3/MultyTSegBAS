package com.seguridadbas.multytenantseguridadbas.model.invoices

import com.google.gson.annotations.SerializedName

data class InvoiceByClientResponse(
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("rows")
    val rows: List<InvoiceByClientObject>? = null
)

data class InvoiceByClientObject(
    @SerializedName("id")
    val id: String? = null.toString(),

    @SerializedName("invoiceNumber")
    val invoiceNumber: String? = null.toString(),

    @SerializedName("dueDate")
    val dueDate: String? = null.toString(),

    @SerializedName("subtotal")
    val subtotal: String? = null.toString(),

    @SerializedName("total")
    val total: String? = null.toString(),

    @SerializedName("payments")
    val payments: List<InvoiceItems>? = null,


)

data class InvoiceItems(
    @SerializedName("name")
    val name:String? = null.toString(),

    @SerializedName("quantity")
    val quantity: Int? = null,

    @SerializedName("rate")
    val rate: Int? = null,

    @SerializedName("taxRate")
    val taxRate: Int? = null,

    @SerializedName("lineTotal")
    val lineTotal: Int? = null,

)





