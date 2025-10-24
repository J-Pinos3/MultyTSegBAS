package com.seguridadbas.multytenantseguridadbas.model

data class UserProfile(
    val id: String = "",
    val fullName: String ="",
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val provider: String? = null,
    val phoneNumber: String? = null
)