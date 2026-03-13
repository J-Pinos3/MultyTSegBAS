package com.seguridadbas.multytenantseguridadbas.model

import com.google.gson.annotations.SerializedName

data class oldNewPasswords(

    @SerializedName("oldPassword")
    val oldPassword: String,
    @SerializedName("newPassword")
    val newPassword: String
)
