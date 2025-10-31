package com.seguridadbas.multytenantseguridadbas.model

data class SecurityGuardProfile(
    val id: String,
    val governmentID: String,
    val isOnDuty: Boolean,
    val fullName: String,
    val bloodType: String,
    val academicInstruction: String,
    val maritalStatus: String,
    val gender: String,
    val birthDate: String,
    val birthPlace: String,
)
