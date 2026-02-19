package com.seguridadbas.multytenantseguridadbas.model.visitorlogs

import kotlinx.serialization.SerialName

data class IdPhoto(
    @SerialName("downloadUrl")
    val downloadUrl: String? = "",
    @SerialName("id")
    val id: String? = "",//id autogenerado o null
    @SerialName("name")
    val name: String,
    @SerialName("privateUrl")
    val privateUrl: String,
    @SerialName("publicUrl")
    val publicUrl: String? = "",
    @SerialName("sizeInBytes")
    val sizeInBytes: Int,
    @SerialName("storageKey")
    val storageKey: String,
    @SerialName("type")
    val type: String
)