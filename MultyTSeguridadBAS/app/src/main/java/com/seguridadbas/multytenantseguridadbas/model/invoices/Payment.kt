package com.seguridadbas.multytenantseguridadbas.model.invoices

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("date")
    val date: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("method")
    val method: String
)