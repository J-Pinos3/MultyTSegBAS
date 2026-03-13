package com.seguridadbas.multytenantseguridadbas.model

data class AuthMeResponse(
    val avatars: List<Any?>,
    val createdAt: String,
    val createdById: Any,
    val deletedAt: Any,
    val email: String,
    val emailVerificationTokenExpiresAt: String,
    val emailVerified: Boolean,
    val firstName: String,
    val fullName: String,
    val id: String,
    val importHash: Any,
    val jwtTokenInvalidBefore: String,
    val lastName: Any,
    val passwordResetTokenExpiresAt: String,
    val phoneNumber: Any,
    val provider: Any,
    val providerId: Any,
    val tenants: List<Tenant>,
    val updatedAt: String,
    val updatedById: String
){
    fun toPresentation_UserProfile(): UserProfile{
        return UserProfile(
            id = id,
            fullName = fullName,
            firstName = firstName,
            lastName = lastName.toString(),
            email = email,
            provider = provider.toString(),
            phoneNumber = phoneNumber.toString()
        )
    }
}