package com.seguridadbas.multytenantseguridadbas.controller.retrohelper

import com.seguridadbas.multytenantseguridadbas.controller.network.ApiClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofit = Retrofit.Builder()
        .baseUrl("")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val consumeAPI = retrofit.create(ApiClient::class.java)
}