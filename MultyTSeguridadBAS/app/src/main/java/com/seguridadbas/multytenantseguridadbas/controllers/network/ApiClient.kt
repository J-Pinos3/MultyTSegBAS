package com.seguridadbas.multytenantseguridadbas.controllers.network

import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.model.SignInResponse
import com.seguridadbas.multytenantseguridadbas.model.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiClient {

    @POST("auth/sign-up")
    suspend fun signUpApi(@Body user: User): Response<ResponseBody>

    @PUT("auth/verify-email")
    suspend fun verifyEmailApi(@Body token: String): Response<ResponseBody>

    @POST("auth/sign-in")
    suspend fun signInApi(@Body user: User): Response<JsonObject>

    @POST("auth/send-email-address-verification-email")
    suspend fun sendEmailVerificationApi(
        @Header("Authorization") auth_token: String,
        @Body email: String
    ): Response<ResponseBody>

    @POST("auth/send-password-reset-email")
    suspend fun sendResetPasswordApi(@Body email: String): Response<ResponseBody>

    @GET("auth/me")
    suspend fun authenticateMeApi(): Response<User>

    @PUT("auth/change-password")
    suspend fun changePasswordApi(@Body email: String): Response<JsonObject>


    @PUT("auth/password-reset")
    suspend fun resetPasswordApi(@Body email: String): Response<User>

    @PUT("auth/profile")
    suspend fun updateProfileApi(@Body email: String): Response<User>





}