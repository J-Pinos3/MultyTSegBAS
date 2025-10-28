package com.seguridadbas.multytenantseguridadbas.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("fullName")
    val fullName: String ="",
    @SerializedName("firstName")
    val firstName : String = "",
    @SerializedName("lastName")
    val lastName : String = "",
    @SerializedName("email")
    val email : String = "",
    @SerializedName("provider")
    val provider: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null
)

data class UserProfileRequest(
    @SerializedName("data")
    val data: ProfileData
)

data class ProfileData(
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)