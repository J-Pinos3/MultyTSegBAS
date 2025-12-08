package com.seguridadbas.multytenantseguridadbas.controllers.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.model.AllGuardsResponse
import com.seguridadbas.multytenantseguridadbas.model.AuthMeResponse
import com.seguridadbas.multytenantseguridadbas.model.GuardDataResponse
import com.seguridadbas.multytenantseguridadbas.model.SignInResponse
import com.seguridadbas.multytenantseguridadbas.model.User
import com.seguridadbas.multytenantseguridadbas.model.UserProfileRequest
import com.seguridadbas.multytenantseguridadbas.model.autocompletGuardsResponse
import com.seguridadbas.multytenantseguridadbas.model.autocompletGuardsResponseItem
import com.seguridadbas.multytenantseguridadbas.model.oldNewPasswords
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiClient {

    @POST("auth/sign-up")
    suspend fun signUpApi(@Body user: User): Response<ResponseBody>

    @PUT("auth/verify-email")
    suspend fun verifyEmailApi(@Body token: String): Response<ResponseBody>

    @POST("auth/sign-in")
    suspend fun
            signInApi(@Body user: User): Response<JsonObject>

    @POST("auth/send-email-address-verification-email")
    suspend fun sendEmailVerificationApi(
        @Header("Authorization") auth_token: String,
        @Body email: String
    ): Response<ResponseBody>

    @POST("auth/send-password-reset-email")
    suspend fun sendResetPasswordApi(@Body email: String): Response<ResponseBody>

    @GET("auth/me")
    suspend fun authenticateMeApi(
        @Header("Authorization") auth_token: String
    ): Response<JsonObject>

    @PUT("auth/change-password")
    suspend fun changePasswordApi(
        @Header("Authorization") auth_token: String,
        @Body passwords: oldNewPasswords
    ): Response<JsonObject>


    @PUT("auth/password-reset")
    suspend fun resetPasswordApi(@Body email: String): Response<User>

    @PUT("auth/profile")
    suspend fun updateProfileApi(
        @Header("Authorization") auth_token: String,
        @Body data: UserProfileRequest
    ): Response<ResponseBody>




    /**  🧑‍✈️ ENDPOINTS FOR GUARDS  */
    //lISTA DE GUARDIAS DE SEGURIDAD
    @GET("tenant/{tenantId}/security-guard")
    suspend fun getSecurityGuardsApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Query("filter[governmentId]") governmentId: String? = null,
        @Query("filter[fullName]") fullName: String? = null,
    ): Response<JsonObject>


    //DETALLES DE UN GUARDIA
    @GET("tenant/{tenantId}/security-guard/{id}")
    suspend fun getSecGuardDetailsApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Path("id") id: String
    ): Response<JsonObject>


    //BUSCAR GUARDIA
    @GET("tenant/{tenantId}/security-guard/autocomplete")
    suspend fun searchSecGuardApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Query("query") query: String,
        @Query("limit") limit: Int
    ): Response<JsonArray>



    /**🧑‍✈️ ENDPOINTS FOR GUARD SHIFTS*/
    //TURNOS DE GUARDIAS
    @GET("tenant/{tenantId}/guard-shift")
    suspend fun getAllGuardsShiftsApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
    ): Response<JsonObject>


    //TURNOS DE UN GUARDIA EN ESPECÍFICO
    @GET("tenant/{tenantId}/guard-shift/{id}")
    suspend fun getShiftsByGuardApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Path("id") id: String,
    ): Response<JsonObject>


    //BUSCAR TURNOS corregir
    @GET("tenant/{tenantId}/guard-shift/autocomplete")
    suspend fun searchGuardsShiftsApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Query("limit") limit: Int,
        @Query("query") query: String?,
    ): Response<JsonArray>




    /**⏱️️ ENDPOINTS FOR SHIFTS */
    //TODOS LOS TURNOS CREADOS
    @GET("tenant/{tenantId}/shift")
    suspend fun getAllShiftsApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @QueryMap filter: Map<String, String>,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("orderBy") orderBy: String? = null
    ): Response<JsonArray>


    //BUSCAR TURNOS CREADOS POR ID
    @GET("tenant/{tenantId}/shift/{id}")
    suspend fun shiftDetailApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantID: String,
        @Path("id") id: String
    ): Response<JsonObject>


    //AUTOCOMPLETAR TURNOS corregir
    //@GET("tenant/{tenantId}/shift/autocomplete")



    /** 🌍 ENDPOINTS FOR STATIONS */
    @GET("tenant/{tenantId}/station")
    suspend fun getAllStationsApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
    ): Response<JsonObject>


    @GET("tenant/{tenantId}/station/{id}")
    suspend fun getStationDetailsApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Path("id") id: String
    ): Response<JsonObject>



    /** ENDPOINTS FOR CERTIRFICATION AND SERVICES*/
    @GET("tenant/{tenantId}/certification")
    suspend fun getAllCertificationsApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
    ): Response<JsonObject>


    @GET("tenant/{tenantId}/service")
    suspend fun getAllServicesApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
    ): Response<JsonObject>


    /** 🏠 ENDPOINTS FOR REPORTS FOR A GIVEN STATION*/
    @GET("tenant/{tenantId}/guard-shift")
    suspend fun getGuardShiftsByStationApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Query("filter[stationName]") stationId: String? = null,
        @Query("filter[guardName]") guardId: String? = null
    ): Response<JsonObject>


    @GET("tenant/{tenantId}/guard-shift/{id}")
    suspend fun getGrdShiftByStationDetApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Path("id") id: String
    ): Response<JsonObject>


    @GET("tenant/{tenantId}/report")
    suspend fun getReportsByStationApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Query("filter[station]") stationId: String? = null,
        @Query("filter[generatedDateRange][]") generatedDateRange: List<String>? = null
    ): Response<JsonObject>


   @GET("tenant/{tenantId}/report/{id}")
   suspend fun getReportByStationDetApi(
       @Header("Authorization") auth_token: String,
       @Path("tenantId") tenantId: String,
       @Path("id") id: String
   ): Response<JsonObject>



    @GET("tenant/{tenantId}/incident")
    suspend fun getIncidentsApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Query("filter[title]") title: String? = null,
        @Query("filter[dateRange][]") dateRange: List<String>? = null,
    ): Response<JsonObject>


    @GET("tenant/{tenantId}/patrol")
    suspend fun getPatrolsByStationApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Query("filter[station]") stationId: String
    ): Response<JsonObject>


    @GET("tenant/{tenantId}/inventory")
    suspend fun getInventoryByStationApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Query("filter[belongsToStation]") stationName: String? = null
    ): Response<JsonObject>


    @GET("tenant/{tenantId}/inventory/{id}")
    suspend fun getInventoryByStationDetApi(
        @Header("Authorization") auth_token: String,
        @Path("tenantId") tenantId: String,
        @Path("id") id: String
    ): Response<JsonObject>


}


