package com.seguridadbas.multytenantseguridadbas.model.billingaccount

import com.google.gson.annotations.SerializedName


data class AllClientAccountResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<ClientAccountDataResponse>
)