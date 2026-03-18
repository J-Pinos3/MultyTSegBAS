package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class TenantRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getCurrentTenant(
        token: String, tenantId: String
    ) = apiClient.getCurrentTenantApi(autho_token = token, tenantId = tenantId)
}