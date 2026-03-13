package com.seguridadbas.multytenantseguridadbas.model.shifts

import com.google.gson.annotations.SerializedName

data class AllShiftsResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<ShiftsDataResponse>
)