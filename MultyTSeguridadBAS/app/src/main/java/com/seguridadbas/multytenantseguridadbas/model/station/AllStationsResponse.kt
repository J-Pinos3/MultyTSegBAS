package com.seguridadbas.multytenantseguridadbas.model.station

import com.google.gson.annotations.SerializedName

data class AllStationsResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<StationsDataResponse>
)