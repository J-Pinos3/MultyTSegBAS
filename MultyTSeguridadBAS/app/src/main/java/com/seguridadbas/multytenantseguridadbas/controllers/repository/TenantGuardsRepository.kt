package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class TenantGuardsRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getSecGuardsRepo(
        token: String,
        tenantId: String,
        filter: Map<String, String>,
        limit: Int,
        offset: Int,
        orderBy: String? = ""
    ) = apiClient.getSecurityGuardsApi(auth_token = token, tenantId = tenantId, filter=filter, limit = limit, offset = offset, orderBy = orderBy)


    suspend fun getSecGuardDetailsRepo(
        token: String,
        tenantId: String,
        id: String
    ) = apiClient.getSecGuardDetailsApi(token, tenantId, id)


    suspend fun searchSecGuardRepo(
        token: String,
        tenantId: String,
        query: String,
        limit: Int
    ) = apiClient.searchSecGuardApi(token, tenantId, query, limit)




}