package com.seguridadbas.multytenantseguridadbas.model

import com.google.gson.annotations.SerializedName

//todo replace List<any> for a proper datatype
data class GuardDataResponse(
    @SerializedName("academicInstruction")
    val academicInstruction: String,
    @SerializedName("address")
    val address: String? = "",
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("birthPlace")
    val birthPlace: String,
    @SerializedName("bloodType")
    val bloodType: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String? = "",
    @SerializedName("credentialImage")
    val credentialImage: List<Any?> = emptyList<Any>(),
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("governmentId")
    val governmentId: String,
    @SerializedName("guard")
    val guard: Guard,
    @SerializedName("guardCredentials")
    val guardCredentials: String,
    val guardId: String,
    val hiringContractDate: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("isOnDuty")
    val isOnDuty: Boolean,
    @SerializedName("maritalStatus")
    val maritalStatus: String,
    @SerializedName("memos")
    val memos: List<Any?> = emptyList<Any>(),
    @SerializedName("profileImage")
    val profileImage: List<Any?> = emptyList<Any>(),
    @SerializedName("recordPolicial")
    val recordPolicial: List<Any?> = emptyList<Any>(),
    @SerializedName("requests")
    val requests: List<Any?> = emptyList<Any>(),
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("tutoriales")
    val tutoriales: List<Any?> = emptyList<Any>(),
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = ""
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