package com.seguridadbas.multytenantseguridadbas.model

data class User(
    val mail: String,
    val password: String,
    val invitationToken: String = "",
    val tenantID: String = ""
)
