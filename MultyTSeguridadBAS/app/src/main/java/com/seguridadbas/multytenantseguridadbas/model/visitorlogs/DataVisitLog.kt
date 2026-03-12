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
    @SerializedName("clientId")
    val clientId: String = "",
    @SerializedName("guardId")
    val guardId: String= "",
    @SerializedName("postSiteId")
    val postSiteId: String= "",
    @SerializedName("placeType")
    val placeType: String= "",
    @SerializedName("visitDate")
    val visitDate: String,
    @SerializedName("exitTime")
    val exitTime: String? = null
)