package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class ShiftsRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getAllShiftsRepo(
        token:String,
        tenantId: String,
        limit: Int,
        offset: Int,
        orderBy: String? = ""
    ) = apiClient.getAllShiftsApi(token, tenantId, limit, offset, orderBy)


    suspend fun shiftDetailRepo(
        token: String,
        tenantId: String,
        id: String
    ) = apiClient.shiftDetailApi(token, tenantId, id)





}