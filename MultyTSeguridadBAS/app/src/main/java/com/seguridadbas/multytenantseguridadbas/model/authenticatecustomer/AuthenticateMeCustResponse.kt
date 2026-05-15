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
    val address: String? = "",

    @SerializedName("city")
    val city: String? = "",

    @SerializedName("stations")
    val stations: List<StationsCust> = emptyList()
)

data class StationsCust(
    @SerializedName("stationName")
    val stationName: String? = "",
)


data class ClientAccountCust(
    @SerializedName("id")
    val id: String? = "",

    @SerializedName("name")
    val name: String? = "",

    @SerializedName("email")
    val email:String? = "",
)