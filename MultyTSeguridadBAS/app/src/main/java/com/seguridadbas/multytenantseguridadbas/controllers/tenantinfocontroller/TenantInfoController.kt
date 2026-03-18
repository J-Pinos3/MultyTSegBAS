package com.seguridadbas.multytenantseguridadbas.controllers.tenantinfocontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.TenantRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.tenantinfo.TenantInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class TenantInfoController @Inject constructor(
    private val tenantRepository: TenantRepository
): ViewModel(){

    suspend fun getCurrentTenant(
        token: String, tenantId: String
    ): Resource<TenantInfoResponse> {

        val response = tenantRepository.getCurrentTenant(token, tenantId)


        return try {

            if(response.isSuccessful && response.body()!=null){

                val jsonBody = response.body()!!
                Resource.Success(
                    TenantInfoResponse(
                        address = jsonBody.get("address").asString,
                        addressLine2 = jsonBody.isNullStringField("addressLine2"),
                        businessTitle = jsonBody.get("businessTitle").asString,
                        city = jsonBody.get("city").asString,
                        country = jsonBody.get("country").asString,
                        createdAt = jsonBody.get("createdAt").asString,
                        createdById = jsonBody.isNullStringField("createdById"),
                        deletedAt = jsonBody.isNullStringField("deletedAt"),
                        email = jsonBody.get("email").asString,
                        extraLines = jsonBody.get("extraLines").asString,
                        id = jsonBody.get("id").asString,
                        latitude = jsonBody.isNullStringField("latitude"),
                        licenseNumber = jsonBody.get("licenseNumber").asString,
                        logoId = jsonBody.isNullStringField("logoId"),
                        longitude = jsonBody.isNullStringField("longitude"),
                        name = jsonBody.get("name").asString,
                        phone = jsonBody.get("phone").asString,
                        plan = jsonBody.get("plan").asString,
                        planStatus = jsonBody.get("planStatus").asString,
                        planStripeCustomerId = jsonBody.isNullStringField("planStripeCustomerId"),
                        planUserId = jsonBody.isNullStringField("planUserId"),
                        postalCode = jsonBody.get("postalCode").asString,
                        settings = jsonBody.getAsJsonArray("settings").toList(),
                        taxNumber = jsonBody.get("taxNumber").asString,
                        timezone = jsonBody.get("timezone").asString,
                        updatedAt = jsonBody.get("updatedAt").asString,
                        updatedById = jsonBody.isNullStringField("updatedById"),
                        url = jsonBody.get("url").asString,
                        website = jsonBody.get("website").asString
                    )
                )

            }else{
                Resource.Error(
                    response.message().toString() + " -- " +
                            response.raw().toString()
                )
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


    private fun JsonObject?.isNullStringField(field: String): String{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asString else ""
    }

}