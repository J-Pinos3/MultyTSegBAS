package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import com.seguridadbas.multytenantseguridadbas.model.User
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val apiClient: ApiClient
){

    suspend fun signUpRepo(user: User) = apiClient.signUpApi(user)
    //suspend fun signUp(user: User) = RetrofitHelper.consumeAPI.signUp(p = user)
    suspend fun verifyEmailRepo(token: String) = apiClient.verifyEmailApi(token = token)

    suspend fun signInRepo(user: User)= apiClient.signInApi(user = user)

    suspend fun sendEmailVerificationRepo(email: String, auth_token: String)
            = apiClient.sendEmailVerificationApi(auth_token = auth_token,email = email)


    suspend fun sendResetPasswordRepo(email: String)
            = apiClient.sendResetPasswordApi(email = email)


    suspend fun changePasswordRepo(auth_token: String, oldPassword: String, newPassword: String)
    = apiClient.changePasswordApi(auth_token = auth_token,  oldPassword = oldPassword, newPassword = newPassword)


    suspend fun authenticateMeRepo(auth_token: String) = apiClient.authenticateMeApi(auth_token = auth_token)


    suspend fun resetPasswordRepo(email: String) = apiClient.resetPasswordApi(email = email)

    suspend fun updateProfileRepo(email: String) = apiClient.updateProfileApi(email = email)


}