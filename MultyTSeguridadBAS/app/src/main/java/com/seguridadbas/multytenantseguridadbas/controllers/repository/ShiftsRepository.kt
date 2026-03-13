package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class ShiftsRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getAllShiftsRepo(
        token:String,
        tenantId: String,
        startTimeRange: List<String>? = null
    ) = apiClient.getAllShiftsApi(token, tenantId,  startTimeRange)


    suspend fun shiftDetailRepo(
        token: String,
        tenantId: String,
        id: String
    ) = apiClient.shiftDetailApi(token, tenantId, id)





}