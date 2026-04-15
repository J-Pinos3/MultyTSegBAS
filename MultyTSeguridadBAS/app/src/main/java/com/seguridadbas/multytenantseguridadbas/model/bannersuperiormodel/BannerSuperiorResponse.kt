package com.seguridadbas.multytenantseguridadbas.model.bannersuperiormodel

import com.google.gson.annotations.SerializedName

data class BannerSuperiorResponse(
    @SerializedName("title")
    val title: String? = null.toString(),

    @SerializedName("downloadUrl")
    val downloadUrl: String? = null.toString()

)
