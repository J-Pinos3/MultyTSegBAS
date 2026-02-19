package com.seguridadbas.multytenantseguridadbas.model.visitorlogs

import kotlinx.serialization.SerialName

data class IdPhotoResponse(
    @SerialName("createdAt")
    val createdAt: String  ="",
    @SerialName("downloadUrl")
    val downloadUrl: String  ="",
    @SerialName("id")
    val id: String  ="",
    @SerialName("name")
    val name: String  ="",
    @SerialName("publicUrl")
    val publicUrl: String? = "",
    @SerialName("sizeInBytes")
    val sizeInBytes: Int = 0,
    @SerialName("storageKey")
    val storageKey: String = ""
)