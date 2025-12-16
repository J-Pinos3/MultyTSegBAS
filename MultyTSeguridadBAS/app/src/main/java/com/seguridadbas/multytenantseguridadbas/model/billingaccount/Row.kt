package com.seguridadbas.multytenantseguridadbas.model.billingaccount

data class Row(
    val bill: List<Any>,
    val clientsInvoiced: Any,
    val clientsInvoicedId: Any,
    val createdAt: String,
    val createdById: String,
    val deletedAt: Any,
    val description: String,
    val id: String,
    val importHash: Any,
    val invoiceNumber: String,
    val lastPaymentDate: String,
    val montoPorPagar: String,
    val nextPaymentDate: String,
    val status: String,
    val tenantId: String,
    val updatedAt: String,
    val updatedById: Any
)