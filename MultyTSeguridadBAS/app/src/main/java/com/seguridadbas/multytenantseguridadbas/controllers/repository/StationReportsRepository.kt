package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class StationReportsRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getGuardShiftsByStationRepo(
        token: String,
        tenantId: String,
        stationId: String?,
        guardId: String?
    ) = apiClient.getGuardShiftsByStationApi(token, tenantId, stationId, guardId)


    suspend fun getGrdShiftByStationDetRepo(
        token: String,
        tenantId: String,
        id: String
    ) = apiClient.getGrdShiftByStationDetApi(token, tenantId, id)

    suspend fun getReportsByStationRepo(
        token: String,
        tenantId: String,
        stationId: String?,
        generatedDateRange: List<String>?
    ) = apiClient.getReportsByStationApi(token, tenantId, stationId, generatedDateRange)


    suspend fun getReportByStationDetRepo(
        token: String, tenantId: String, id: String
    ) = apiClient.getReportByStationDetApi(token, tenantId, id)


    suspend fun getIncidentsRepo(
        token: String,
        tenantId: String,
        title: String?,
        dateRange: List<String>?
    ) = apiClient.getIncidentsApi(token, tenantId, title, dateRange)


    suspend fun getPatrolsByStationRepo(
        token: String,
        tenantId: String,
        stationId: String
    ) = apiClient.getPatrolsByStationApi(token, tenantId, stationId)


    suspend fun getInventoryByStationRepo(
        token: String,
        tenantId: String,
        stationName: String?
    ) = apiClient.getInventoryByStationApi(token, tenantId, stationName)


}







