package com.seguridadbas.multytenantseguridadbas.model.invoices

import com.google.gson.annotations.SerializedName

data class PostSite(
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("companyName")
    val companyName: String,
    @SerializedName("contactEmail")
    val contactEmail: String,
    @SerializedName("contactPhone")
    val contactPhone: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)