package com.seguridadbas.multytenantseguridadbas.model.guard_shifts

import com.google.gson.annotations.SerializedName

data class AllGuardShiftsResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<GuardShiftsDataResponse>
)