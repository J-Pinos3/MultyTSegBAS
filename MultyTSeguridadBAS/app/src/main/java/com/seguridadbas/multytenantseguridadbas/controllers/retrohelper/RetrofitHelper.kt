package com.seguridadbas.multytenantseguridadbas.controllers.retrohelper

import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val consumeAPI = retrofit.create(ApiClient::class.java)
}