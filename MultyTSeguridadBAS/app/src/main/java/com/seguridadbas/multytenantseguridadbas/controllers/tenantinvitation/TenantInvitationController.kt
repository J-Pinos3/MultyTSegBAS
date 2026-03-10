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
                        address = jsonBody.isNullStringField("address"),
                        businessTitle = jsonBody.isNullStringField("businessTitle"),
                        createdAt = jsonBody.isNullStringField("createdAt"),
                        createdById = jsonBody.isNullStringField("createdById"),
                        deletedAt = jsonBody.isNullStringField("deletedAt"),
                        email = jsonBody.isNullStringField("email"),
                        extraLines = jsonBody.isNullStringField("extraLines"),
                        id = jsonBody.isNullStringField("id"),
                        licenseNumber = jsonBody.isNullStringField("licenseNumber"),
                        logoId = jsonBody.isNullStringField("logoId"),
                        name = jsonBody.isNullStringField("name"),
                        phone = jsonBody.isNullStringField("phone"),
                        plan = jsonBody.isNullStringField("plan"),
                        planStatus = jsonBody.isNullStringField("planStatus"),
                        planStripeCustomerId = jsonBody.isNullStringField("planStripeCustomerId"),
                        planUserId = jsonBody.isNullStringField("planUserId"),
                        taxNumber = jsonBody.isNullStringField("taxNumber"),
                        timezone = jsonBody.isNullStringField("timezone"),
                        updatedAt = jsonBody.isNullStringField("updatedAt"),
                        updatedById = jsonBody.isNullStringField("updatedById"),
                        url = jsonBody.isNullStringField("url"),
                        website = jsonBody.isNullStringField("website")
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