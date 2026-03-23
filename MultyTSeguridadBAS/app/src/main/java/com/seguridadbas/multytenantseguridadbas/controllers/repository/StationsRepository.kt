package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class StationsRepository @Inject constructor(
    private val apiClient: ApiClient
){

    suspend fun getAllStationsRepo(
        token: String, tenantId: String, postSiteId: String?
    ) = apiClient.getAllStationsApi(token, tenantId, postSiteId)


    suspend fun stationDetailRepo(
        token: String,
        tenantId: String,
        id: String
    ) = apiClient.getStationDetailsApi(token, tenantId, id)




}