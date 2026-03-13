package com.seguridadbas.multytenantseguridadbas.model

import com.google.gson.annotations.SerializedName

data class autocompletGuardsResponseItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("label")
    val label: String
)