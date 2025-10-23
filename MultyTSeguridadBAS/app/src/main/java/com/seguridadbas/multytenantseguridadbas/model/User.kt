package com.seguridadbas.multytenantseguridadbas.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")
    val mail: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("fullName")
    val fullName: String = "",
    @SerializedName("tenantId")
    val tenantID: String = ""
)
