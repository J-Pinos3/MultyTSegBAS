package com.seguridadbas.multytenantseguridadbas.model

data class TenantX(
    val createdAt: String,
    val createdById: String,
    val deletedAt: Any,
    val id: String,
    val name: String,
    val plan: String,
    val planStatus: String,
    val planStripeCustomerId: Any,
    val planUserId: Any,
    val settings: List<Setting>,
    val updatedAt: String,
    val updatedById: String,
    val url: String
)