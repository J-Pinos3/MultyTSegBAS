package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName

data class GuardName(
    @SerializedName("academicInstruction")
    val academicInstruction: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("availability")
    val availability: String? = "",
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("birthPlace")
    val birthPlace: String,
    @SerializedName("bloodType")
    val bloodType: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("governmentId")
    val governmentId: String,
    @SerializedName("guardCredentials")
    val guardCredentials: String,
    @SerializedName("guardId")
    val guardId: String,
    @SerializedName("hiringContractDate")
    val hiringContractDate: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = "",
    @SerializedName("isOnDuty")
    val isOnDuty: Boolean,
    @SerializedName("maritalStatus")
    val maritalStatus: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String
)