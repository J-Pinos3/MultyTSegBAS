package com.seguridadbas.multytenantseguridadbas.model.postsite

import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("name")
    val name: String
)