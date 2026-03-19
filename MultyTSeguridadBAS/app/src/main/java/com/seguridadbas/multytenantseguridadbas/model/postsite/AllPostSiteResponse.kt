package com.seguridadbas.multytenantseguridadbas.model.postsite

import com.google.gson.annotations.SerializedName

data class AllPostSiteResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<AllPostSiteData>
)