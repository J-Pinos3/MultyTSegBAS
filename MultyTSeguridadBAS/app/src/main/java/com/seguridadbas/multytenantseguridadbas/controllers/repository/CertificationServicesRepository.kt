package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class CertificationServicesRepository @Inject constructor(
    private val apiClient: ApiClient
){

    suspend fun getAllCertificationsRepo(
        token: String,
        tenantId: String
    ) = apiClient.getAllCertificationsApi(token, tenantId )



    suspend fun getAllServicesrepo(
        token: String,
        tenantId: String
    ) = apiClient.getAllServicesApi(token, tenantId)
}