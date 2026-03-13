package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import com.seguridadbas.multytenantseguridadbas.model.tenantinvitation.AcceptInvitationBody
import javax.inject.Inject

class TenantInvitationRepository @Inject constructor(
    private val apiClient: ApiClient
) {

    suspend fun acceptTenantInvitationRepo(
        authToken: String, invitationToken: String, userId: AcceptInvitationBody
    ) = apiClient.acceptTenantInvitationApi(
        authToken, invitationToken, userId
    )

}