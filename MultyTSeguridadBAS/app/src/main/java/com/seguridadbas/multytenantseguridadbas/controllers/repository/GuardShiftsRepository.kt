package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class GuardShiftsRepository @Inject constructor(
    private val apiClient: ApiClient
) {


    suspend fun getAllGuardShiftsRepo(
        token: String,
        filters: Map<String, String>,
        limit: Int
    ) = apiClient.getAllGuardsShiftsApi(token, filters, limit)


    suspend fun getShiftsByGuardRepo(
        token: String,
        tenantId: String,
        id: String
    ) = apiClient.getShiftsByGuardApi(token, tenantId, id)


    suspend fun searchGuardsShiftsRepo(
        token: String,
        tenantId: String,
        query: String,
        limit: Int
    ) = apiClient.searchGuardsShiftsApi(token, tenantId, query, limit)











}