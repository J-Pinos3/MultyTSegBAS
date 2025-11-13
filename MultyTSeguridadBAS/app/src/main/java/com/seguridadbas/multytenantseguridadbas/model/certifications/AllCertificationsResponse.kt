package com.seguridadbas.multytenantseguridadbas.model.certifications

import com.google.gson.annotations.SerializedName

data class AllCertificationsResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("rows")
    val rows: List<CertificationDataResponse>
)