package com.seguridadbas.multytenantseguridadbas.model.shifts

data class Guard(
    val email: String,
    val firstName: String,
    val id: String,
    val lastName: String? = ""
)