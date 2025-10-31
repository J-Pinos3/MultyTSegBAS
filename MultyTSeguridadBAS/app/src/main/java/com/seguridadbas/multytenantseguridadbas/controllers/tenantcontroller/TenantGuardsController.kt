package com.seguridadbas.multytenantseguridadbas.controllers.tenantcontroller

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.TenantGuardsRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.Guard
import com.seguridadbas.multytenantseguridadbas.model.SecurityGuardProfile
import com.seguridadbas.multytenantseguridadbas.model.autocompletGuardsResponseItem

import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class TenantGuardsController @Inject constructor(
    private val tenantGuardsRepository: TenantGuardsRepository
) {

    suspend fun getSecGuards(token:String, tenantID: String): Resource<List<Guard>> {
        val response = tenantGuardsRepository.getSecGuardsRepo(token, tenantID)

        return try{

            if(response.isSuccessful){
                Resource.Success(//response.body()?.asJsonArray ?: JsonArray()

                    response.body()?.rows?.map {
                        Guard(
                            email = it.guard.email,
                            firstName =  it.guard.firstName,
                            id = it.guard.id,
                            lastName = it.guard.lastName
                        )
                    } ?: emptyList()
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



    suspend fun getSecurityGuardDetails(token: String, tenantID: String, id: String): Resource<SecurityGuardProfile>{
        val response = tenantGuardsRepository.getSecGuardDetailsRepo(token, tenantID, id)

        return try {

            if( response.isSuccessful ){
                Resource.Success(
                    response.body()?.toPresentation_SecurityGuardProfile() ?:
                    SecurityGuardProfile(
                        "","",false,"",
                        "","",
                        "","","",""   )

                )
            }else{
                Resource.Error(response.message().toString() + " -- " +
                        response.raw().toString()
                )
            }

        }catch (e: SocketTimeoutException){
            Resource.Error("La conexión ha tardado mucho tiempo")
        }catch(ex: NoNetworkException){
            when(ex){
                is NoNetworkException -> { Resource.Error(ex.message.toString()) }
                is IOException -> { Resource.Error(ex.message.toString()) }

            }
        }
    }


    suspend fun searchSecurityGuard(token: String, tenantID: String, query: String, limit: Int): Resource<List<autocompletGuardsResponseItem>>{
        val response = tenantGuardsRepository.searchSecGuardRepo(token, tenantID, query, limit)

        return try{

            if(response.isSuccessful ){
                Resource.Success(

                    response.body()?.map{
                        autocompletGuardsResponseItem(
                            id = it.id,
                            label = it.label
                        )
                    } ?: listOf(
                        autocompletGuardsResponseItem(
                            id = "",
                            label = "Sin guardias"
                        )
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


}