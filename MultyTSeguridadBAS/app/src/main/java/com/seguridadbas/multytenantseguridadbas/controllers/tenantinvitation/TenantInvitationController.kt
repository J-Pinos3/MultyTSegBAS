package com.seguridadbas.multytenantseguridadbas.controllers.tenantinvitation

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.TenantInvitationRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.station.ShortStation
import com.seguridadbas.multytenantseguridadbas.model.tenantinvitation.AcceptInvitationBody
import com.seguridadbas.multytenantseguridadbas.model.tenantinvitation.AcceptTokenResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class TenantInvitationController @Inject constructor(
   private val tenantInvitationRepository: TenantInvitationRepository
): ViewModel() {

    suspend fun acceptTenantInvitation(
        token: String, invitationToken: String, userId: String
    ): Resource<AcceptTokenResponse> {
        val acceptInvitationBody = AcceptInvitationBody(userId)
        val response = tenantInvitationRepository.acceptTenantInvitationRepo(token, invitationToken, acceptInvitationBody)

        return try{


            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!


                Resource.Success(
                    AcceptTokenResponse(
                        address = jsonBody.get("address").asString,
                        businessTitle = jsonBody.get("businessTitle").asString,
                        createdAt = jsonBody.get("createdAt").asString,
                        createdById = jsonBody.isNullStringField("createdById"),
                        deletedAt = jsonBody.isNullStringField("deletedAt"),
                        email = jsonBody.get("email").asString,
                        extraLines = jsonBody.get("extraLines").asString,
                        id = jsonBody.get("id").asString,
                        licenseNumber = jsonBody.get("licenseNumber").asString,
                        logoId = jsonBody.isNullStringField("logoId"),
                        name = jsonBody.get("name").asString,
                        phone = jsonBody.get("phone").asString,
                        plan = jsonBody.get("plan").asString,
                        planStatus = jsonBody.get("planStatus").asString,
                        planStripeCustomerId = jsonBody.isNullStringField("planStripeCustomerId"),
                        planUserId = jsonBody.isNullStringField("planUserId"),
                        taxNumber = jsonBody.get("taxNumber").asString,
                        timezone = jsonBody.get("timezone").asString,
                        updatedAt = jsonBody.get("updatedAt").asString,
                        updatedById = jsonBody.isNullStringField("updatedById"),
                        url = jsonBody.get("url").asString,
                        website = jsonBody.get("website").asString
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

    private fun JsonObject?.isNullStringField(field: String): String{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asString else ""
    }


}