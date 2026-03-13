package com.seguridadbas.multytenantseguridadbas.model

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("token")
    val token: String ="No Token",
    @SerializedName("user")
    val user: UserSignInResponse = UserSignInResponse("--","--","--","--")
)
