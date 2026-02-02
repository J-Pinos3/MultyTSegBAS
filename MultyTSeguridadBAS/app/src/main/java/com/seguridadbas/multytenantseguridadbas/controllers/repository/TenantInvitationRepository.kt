package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import javax.inject.Inject

class TenantInvitationRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun acceptTenantInvitationRepo(
        authToken: String, invitationToken: String, userId: String
    ) = apiClient.acceptTenantInvitationApi(
        authToken, invitationToken, userId
    )

}