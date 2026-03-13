package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class BillingAccountRepository @Inject constructor(
    private val apiClient: ApiClient
){


    suspend fun getBillingRepo(
        token:String,
        tenantId: String,
        clientId: String? = null
    ) = apiClient.getBillingApi(token, tenantId, clientId)


    suspend fun getClientAccountRepo(
        token: String, tenantId: String
    ) = apiClient.getClientAccountApi(token, tenantId)


    suspend fun processPaymentRepo(
        token: String, tenantId: String, plan: String
    ) = apiClient.processPaymentApi(token, tenantId, plan)
}