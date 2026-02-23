package com.seguridadbas.multytenantseguridadbas.model.visitorlogs

import com.google.gson.annotations.SerializedName

data class DataVisitLog(
    @SerializedName("firstName")
    val firstName: String? = "",
    @SerializedName("idNumber")
    val idNumber: String,
    @SerializedName("idPhoto")
    val idPhoto: List<IdPhoto>,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("numPeople")
    val numPeople: Int,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("visitDate")
    val visitDate: String,
    @SerializedName("exitTime")
    val exitTime: String? = null
)