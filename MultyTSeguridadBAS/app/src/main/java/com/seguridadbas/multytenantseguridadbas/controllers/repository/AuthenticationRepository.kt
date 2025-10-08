package com.seguridadbas.multytenantseguridadbas.controllers.repository

import com.seguridadbas.multytenantseguridadbas.controllers.retrohelper.RetrofitHelper
import com.seguridadbas.multytenantseguridadbas.model.User

class AuthenticationRepository {

    suspend fun signUp(user: User) = RetrofitHelper.consumeAPI.signUp(p = user)

    suspend fun signIn(user: User) = RetrofitHelper.consumeAPI.signIn(p = user)

    suspend fun authenticateMe() = RetrofitHelper.consumeAPI.authenticateMe()

    suspend fun changePassword(email: String) = RetrofitHelper.consumeAPI.changePassword(email = email)

    suspend fun sendEmailVerification(email: String)
        = RetrofitHelper.consumeAPI.sendEmailVerification(email = email)

    suspend fun sendResetPassword(email: String)
        = RetrofitHelper.consumeAPI.sendResetPassword(email = email)

    suspend fun resetPassword(email: String) = RetrofitHelper.consumeAPI.resetPassword(email = email)

    suspend fun verifyEmail(email: String) = RetrofitHelper.consumeAPI.verifyEmail(email = email)

    suspend fun updateProfile(email: String) = RetrofitHelper.consumeAPI.updateProfile(email = email)


}