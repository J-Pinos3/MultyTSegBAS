package com.seguridadbas.multytenantseguridadbas.model

data class UserDataStore(
    val token: String = "",
    val id: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val tenantId: String = ""
)
