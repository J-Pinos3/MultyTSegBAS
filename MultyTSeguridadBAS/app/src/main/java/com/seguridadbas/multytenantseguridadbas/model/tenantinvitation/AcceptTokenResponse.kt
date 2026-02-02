package com.seguridadbas.multytenantseguridadbas.model.tenantinvitation

import com.google.gson.annotations.SerializedName

data class AcceptTokenResponse(
    @SerializedName("address")
    var address: String = "",
    @SerializedName("businessTitle")
    var businessTitle: String = "",
    @SerializedName("createdAt")
    var createdAt: String = "",
    @SerializedName("createdById")
    var createdById: String? = "",
    @SerializedName("deletedAt")
    var deletedAt: String? = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("extraLines")
    var extraLines: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("licenseNumber")
    var licenseNumber: String = "",
    @SerializedName("logoId")
    var logoId: String? = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("plan")
    var plan: String = "",
    @SerializedName("planStatus")
    var planStatus: String = "",
    @SerializedName("planStripeCustomerId")
    var planStripeCustomerId: String? = "",
    @SerializedName("planUserId")
    var planUserId: String? = "",
    @SerializedName("taxNumber")
    var taxNumber: String = "",
    @SerializedName("timezone")
    var timezone: String = "",
    @SerializedName("updatedAt")
    var updatedAt: String = "",
    @SerializedName("updatedById")
    var updatedById: String? = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("website")
    var website: String = ""
)