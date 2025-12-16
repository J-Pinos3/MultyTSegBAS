package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class BillingAccountRepository @Inject constructor(
    private val apiClient: ApiClient
){


    suspend fun getBillingRepo(
        token:String,
        tenantId: String
    ) = apiClient.getBillingApi(token, tenantId)


    suspend fun getClientAccountApi(
        token: String, tenantId: String
    ) = apiClient.getClientAccountApi(token, tenantId)



}