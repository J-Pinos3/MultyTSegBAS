package com.seguridadbas.multytenantseguridadbas.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    val id: String = "",
    val fullName: String ="",
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val provider: String? = null,
    val phoneNumber: String? = null
)

data class UserProfileRequest(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)