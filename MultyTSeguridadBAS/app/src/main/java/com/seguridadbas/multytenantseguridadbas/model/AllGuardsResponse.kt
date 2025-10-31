package com.seguridadbas.multytenantseguridadbas.model

import com.google.gson.annotations.SerializedName

data class AllGuardsResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<GuardDataResponse>
)