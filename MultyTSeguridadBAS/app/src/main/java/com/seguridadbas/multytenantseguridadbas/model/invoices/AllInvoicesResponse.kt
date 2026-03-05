package com.seguridadbas.multytenantseguridadbas.model.invoices

import com.google.gson.annotations.SerializedName

data class AllInvoicesResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<AllInvoicesRespData>
)