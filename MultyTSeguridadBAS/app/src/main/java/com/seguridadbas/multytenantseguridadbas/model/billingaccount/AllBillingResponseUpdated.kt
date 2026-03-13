package com.seguridadbas.multytenantseguridadbas.model.billingaccount

import com.google.gson.annotations.SerializedName

data class AllBillingResponseUpdated(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<UpdatedBillingDataResponse>
)