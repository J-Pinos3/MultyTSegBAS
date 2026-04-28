package com.seguridadbas.multytenantseguridadbas.model.invoices

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("id")
    val id: String,
    @SerializedName("line")
    val line: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("rate")
    val rate: Int,
    @SerializedName("taxName")
    val taxName: String = "",
    @SerializedName("taxRate")
    val taxRate: Int,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("lineTotal")
    val lineTotal: Int,
    @SerializedName("taxAmount")
    val taxAmount: Int,

)