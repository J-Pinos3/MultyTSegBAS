package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class InvoiceRepository @Inject constructor(
    private val apiClient: ApiClient
){

    suspend fun getAllInvoicesRepo(
        token: String, tenantId: String
    ) = apiClient.getAllInvoicesApi(token, tenantId)

}