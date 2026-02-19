package com.seguridadbas.multytenantseguridadbas.model.visitorlogs

import kotlinx.serialization.SerialName

data class VisitorLogRequestBody(
    @SerialName("data")
    val data: dataVisitLog
)