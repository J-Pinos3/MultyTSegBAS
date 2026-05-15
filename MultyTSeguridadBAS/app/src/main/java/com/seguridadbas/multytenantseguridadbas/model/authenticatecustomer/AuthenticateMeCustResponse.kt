package com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer

import com.google.gson.annotations.SerializedName

data class AuthenticateMeCustResponse (
    @SerializedName("clientAccount")
    val clientAccount: ClientAccountCust = ClientAccountCust(),

    @SerializedName("postSites")
    val postSites: List<PostSitesCust> = emptyList()



)

data class PostSitesCust(
    @SerializedName("address")
    val address: String? = null.toString(),

    @SerializedName("city")
    val city: String? = null.toString(),

    @SerializedName("stations")
    val stations: List<StationsCust> = emptyList()
)

data class StationsCust(
    @SerializedName("stationName")
    val stationName: String? = null.toString(),
)


data class ClientAccountCust(
    @SerializedName("id")
    val id: String? = null.toString(),

    @SerializedName("name")
    val name: String? = null.toString(),

    @SerializedName("email")
    val email:String? = null.toString(),
)