package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class IncidentsDetailRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getIncidentDetailRepo(
        token: String, tenantId: String, incidentID: String
    ) = apiClient.getIncidentDetailApi(token, tenantId, incidentID)

}