package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import com.seguridadbas.multytenantseguridadbas.controllers.retrohelper.RetrofitHelper
import com.seguridadbas.multytenantseguridadbas.model.User
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val apiClient: ApiClient
){

    suspend fun signUp(user: User) = apiClient.signUp(user)
    //suspend fun signUp(user: User) = RetrofitHelper.consumeAPI.signUp(p = user)

    suspend fun signIn(user: User) = apiClient.signIn(user = user)

    suspend fun authenticateMe() = apiClient.authenticateMe()

    suspend fun changePassword(email: String) = apiClient.changePassword(email = email)

    suspend fun sendEmailVerification(email: String)
        = apiClient.sendEmailVerification(email = email)

    suspend fun sendResetPassword(email: String)
        = apiClient.sendResetPassword(email = email)

    suspend fun resetPassword(email: String) = apiClient.resetPassword(email = email)

    suspend fun verifyEmail(email: String) = apiClient.verifyEmail(email = email)

    suspend fun updateProfile(email: String) = apiClient.updateProfile(email = email)


}