package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class PostSiteRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getAllPostSitesRepo(
        authToken: String, tenantId: String
    ) = apiClient.getAllPostSitesApi(authToken, tenantId)

}