package com.seguridadbas.multytenantseguridadbas.model

import com.google.gson.annotations.SerializedName

data class SendEmailVerificationRequest(
    @SerializedName("email")
    val email: String = ""
)
