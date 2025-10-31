package com.seguridadbas.multytenantseguridadbas.model

data class GuardDataResponse(
    val academicInstruction: String,
    val address: Any,
    val birthDate: String,
    val birthPlace: String,
    val bloodType: String,
    val createdAt: String,
    val createdById: Any,
    val credentialImage: List<Any>,
    val deletedAt: Any,
    val fullName: String,
    val gender: String,
    val governmentId: String,
    val guard: Guard,
    val guardCredentials: String,
    val guardId: String,
    val hiringContractDate: String,
    val id: String,
    val importHash: Any,
    val isOnDuty: Boolean,
    val maritalStatus: String,
    val memos: List<Any>,
    val profileImage: List<Any>,
    val recordPolicial: List<Any>,
    val requests: List<Any>,
    val tenantId: String,
    val tutoriales: List<Any>,
    val updatedAt: String,
    val updatedById: Any
){
    fun toPresentation_SecurityGuardProfile(): SecurityGuardProfile{
        return SecurityGuardProfile(
            id = id,
            governmentID = governmentId,
            isOnDuty = isOnDuty,
            fullName = fullName,
            bloodType = bloodType,
            academicInstruction = academicInstruction,
            maritalStatus = maritalStatus,
            gender = gender,
            birthDate = birthDate,
            birthPlace = birthPlace
        )
    }
}