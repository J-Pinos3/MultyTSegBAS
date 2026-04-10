package com.seguridadbas.multytenantseguridadbas.model

import android.text.SpannedString
import com.google.gson.annotations.SerializedName

data class UserSignInResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("firstName")
    val firstName: String = "",

    @SerializedName("lastName")
    val lastName: String?  = "",

    @SerializedName("tenant")
    val tenant: TenantLogin
)


data class TenantLogin(
    @SerializedName("roles")
    val roles: List<String> = listOf(),

)