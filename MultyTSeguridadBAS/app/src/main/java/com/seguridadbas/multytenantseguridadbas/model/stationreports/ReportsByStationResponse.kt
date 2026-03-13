package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName

data class ReportsByStationResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<ReportsByStationData>
)