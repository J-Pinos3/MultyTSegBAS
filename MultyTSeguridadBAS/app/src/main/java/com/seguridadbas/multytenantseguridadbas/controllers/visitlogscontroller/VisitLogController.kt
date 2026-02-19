package com.seguridadbas.multytenantseguridadbas.controllers.visitlogscontroller

import androidx.lifecycle.ViewModel
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.VisitLogRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.visitorlogs.Payload
import com.seguridadbas.multytenantseguridadbas.model.visitorlogs.VisitLogResponse
import com.seguridadbas.multytenantseguridadbas.model.visitorlogs.VisitorLogRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class VisitLogController @Inject constructor(
    private val visitLogRepository: VisitLogRepository
): ViewModel() {

    suspend fun sendVisitLog(
        bearerToken: String, tenantId: String, requestData: VisitorLogRequestBody
    ): Resource<VisitLogResponse>{
        val response = visitLogRepository.sendVisitLogRepo(bearerToken, tenantId, requestData)

        return try{

            if(response.isSuccessful && response.body() != null){
                val jsonBody = response.body()!!
                //val payloadObj = jsonBody.getAsJsonObject("payload")
                //val idPhotoResponseObj = payloadObj.getAsJsonObject("idPhoto")

                Resource.Success(
                    VisitLogResponse(
                        code = jsonBody.get("code").asInt,
                        message = jsonBody.get("message").asString,
                    )
                )
            }else{
                Resource.Error(response.message().toString() + "--" + response.raw().message )
            }


        }catch (e: SocketTimeoutException){
            Resource.Error("La conexión ha tardado mucho tiempo")
        }catch (ex: NoNetworkException){
            when(ex){
                is NoNetworkException -> { Resource.Error(ex.message.toString()) }
                is IOException -> { Resource.Error(ex.message.toString()) }
            }
        }

    }

}