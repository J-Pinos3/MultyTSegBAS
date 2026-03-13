package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import com.seguridadbas.multytenantseguridadbas.model.visitorlogs.VisitorLogRequestBody
import javax.inject.Inject

class VisitLogRepository @Inject constructor(
    private val apiClient: ApiClient
) {


    suspend fun sendVisitLogRepo(
        bearerToken: String,
        tenantId: String,
        requestData: VisitorLogRequestBody
    ) = apiClient.sendVisitLogApi(bearerToken, tenantId, requestData)

}