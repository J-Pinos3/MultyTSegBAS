package com.seguridadbas.multytenantseguridadbas.controllers.network

import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiClient {

    @POST("auth/sign-up")
    suspend fun signUp(@Body user: User): Response<String>

    @POST("auth/sign-in")
    suspend fun signIn(@Body user: User): Response<JsonObject>

    @GET("auth/me")
    suspend fun authenticateMe(): Response<User>

    @PUT("auth/change-password")
    suspend fun changePassword(@Body email: String): Response<JsonObject>

    @POST("auth/send-email-address-verification-email")
    suspend fun sendEmailVerification(@Body email: String): Response<JsonObject>

    @POST("auth/send-password-reset-email")
    suspend fun sendResetPassword(@Body email: String): Response<JsonObject>

    @PUT("auth/password-reset")
    suspend fun resetPassword(@Body email: String): Response<User>

    @PUT("auth/verify-email")
    suspend fun verifyEmail(@Body email: String): Response<User>

    @PUT("auth/profile")
    suspend fun updateProfile(@Body email: String): Response<User>





}