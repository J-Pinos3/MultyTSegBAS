package com.seguridadbas.multytenantseguridadbas.di

import android.content.Context
import android.icu.util.TimeUnit
import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import com.seguridadbas.multytenantseguridadbas.controllers.network.LiveNetworkMonitor
import com.seguridadbas.multytenantseguridadbas.controllers.network.NetworkMonitor
import com.seguridadbas.multytenantseguridadbas.controllers.network.NetworkMonitorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideNetworkMonitor(
        @ApplicationContext appContext: Context
    ): NetworkMonitor{
        return LiveNetworkMonitor(appContext)
    }


    @Provides
    @Singleton
    fun provideRetrofit(
        liveNetworkMonitor: NetworkMonitor
    ): Retrofit{
        val monitorClient = OkHttpClient.Builder()
            .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS )
            .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor( NetworkMonitorInterceptor(liveNetworkMonitor) )
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.cguardpro.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(monitorClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiClient(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)

    }
}