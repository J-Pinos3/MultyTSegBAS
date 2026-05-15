package com.seguridadbas.multytenantseguridadbas.controllers.repository.customerpository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class AuthenticateCustomerRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun getMeCustomerAccountRepo(token: String)
     = apiClient.getMeCustomerAccountApi(token)

}