package com.seguridadbas.multytenantseguridadbas.model

data class Tenant(
    val createdAt: String,
    val createdById: Any,
    val deletedAt: Any,
    val id: String,
    val invitationToken: Any,
    val roles: List<Any?>,
    val status: String,
    val tenant: TenantX,
    val tenantId: String,
    val updatedAt: String,
    val updatedById: Any,
    val userId: String
)