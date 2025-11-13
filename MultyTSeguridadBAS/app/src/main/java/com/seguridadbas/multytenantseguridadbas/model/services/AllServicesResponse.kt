package com.seguridadbas.multytenantseguridadbas.model.services

import com.google.gson.annotations.SerializedName

data class AllServicesResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<ServiceDataResponse>
)