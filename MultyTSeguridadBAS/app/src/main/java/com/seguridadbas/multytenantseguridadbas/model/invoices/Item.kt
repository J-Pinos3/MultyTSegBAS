package com.seguridadbas.multytenantseguridadbas.model.invoices

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("total")
    val total: Double,
    @SerializedName("unitPrice")
    val unitPrice: Double
)