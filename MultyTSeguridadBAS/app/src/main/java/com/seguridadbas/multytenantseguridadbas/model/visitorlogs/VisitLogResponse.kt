package com.seguridadbas.multytenantseguridadbas.model.visitorlogs

import com.google.gson.annotations.SerializedName

data class VisitLogResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("payload")
    val payload: Payload = Payload()
)