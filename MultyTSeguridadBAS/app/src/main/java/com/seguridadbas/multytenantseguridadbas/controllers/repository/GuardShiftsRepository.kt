package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class GuardShiftsRepository @Inject constructor(
    private val apiClient: ApiClient
) {


    suspend fun getAllGuardShiftsRepo(
        token: String,
        tenantID: String,
        filter: Map<String, String>,
        limit: Int,
        offset: Int,
        orderBy: String? = ""
    ) = apiClient.getAllGuardsShiftsApi(token, tenantID,filter,limit, offset, orderBy )


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
    ) = apiClient.searchGuardsShiftsApi(token, tenantId, limit, query)











}