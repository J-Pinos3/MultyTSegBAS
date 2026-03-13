package com.seguridadbas.multytenantseguridadbas.controllers.network

import android.net.http.NetworkException
import android.os.Build
import android.os.Build.*
import android.os.ext.SdkExtensions
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import javax.inject.Inject
import kotlin.jvm.Throws

class NetworkMonitorInterceptor @Inject constructor(
    private val liveNetworkMonitor: NetworkMonitor
): Interceptor {


    @Throws(NoNetworkException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request: Request = chain.request()

        if( liveNetworkMonitor.isConnected() ){
            return chain.proceed(request)
        }else{
            throw NoNetworkException("No se pudo conectar al servidor")
        }

    }


}


class NoNetworkException(message: String): IOException(message)