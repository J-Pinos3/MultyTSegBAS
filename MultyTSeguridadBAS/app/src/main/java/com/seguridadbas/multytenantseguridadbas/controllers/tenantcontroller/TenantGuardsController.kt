package com.seguridadbas.multytenantseguridadbas.controllers.tenantcontroller

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.TenantGuardsRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.AllGuardsResponse
import com.seguridadbas.multytenantseguridadbas.model.Guard
import com.seguridadbas.multytenantseguridadbas.model.GuardDataResponse
import com.seguridadbas.multytenantseguridadbas.model.SecurityGuardProfile
import com.seguridadbas.multytenantseguridadbas.model.autocompletGuardsResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel

import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class TenantGuardsController @Inject constructor(
    private val tenantGuardsRepository: TenantGuardsRepository
): ViewModel() {

    private fun parseAllGuardsResponse(jsonObject: JsonObject):AllGuardsResponse {

        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map { rowElement ->
            val row = rowElement.asJsonObject

            val guardObj = row.getAsJsonObject("guard")

            GuardDataResponse(
                academicInstruction = row.get("academicInstruction").asString,
                address = row.get("address").asString,
                birthDate = row.get("birthDate").asString,
                birthPlace = row.get("birthPlace").asString,
                bloodType = row.get("bloodType").asString,
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdAt").asString,
                credentialImage = row.getAsJsonArray("credentialImage").toList(),
                deletedAt = row.get("deletedAt").asString,
                fullName = row.get("fullName").asString,
                gender = row.get("gender").asString,
                governmentId = row.get("governmentId").asString,
                guard = Guard(
                    email = guardObj.get("email").asString,
                    firstName = guardObj.get("firstName").asString,
                    id = guardObj.get("id").asString,
                    lastName = guardObj.get("lastName")?.asString ?: ""
                ),
                guardCredentials = row.get("guardCredentials").asString,
                guardId = row.get("guardId").asString,
                hiringContractDate = row.get("hiringContractDate").asString,
                id = row.get("id").asString,
                importHash = row.get("importHash").asString,
                isOnDuty = row.get("isOnDuty").asBoolean,
                maritalStatus = row.get("maritalStatus").asString,
                memos = row.getAsJsonArray("memos").toList(),
                profileImage = row.getAsJsonArray("profileImage").toList(),
                recordPolicial = row.getAsJsonArray("recordPolicial").toList(),
                requests = row.getAsJsonArray("requests").toList(),
                tenantId = row.get("tenantId").asString,
                tutoriales = row.getAsJsonArray("tutoriales").toList(),
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.get("updatedById").asString
            )
        }

        return AllGuardsResponse(count, rows)
    }


    suspend fun getSecGuards(
        token: String,
        tenantID: String,
        filter: Map<String, String>,
        limit: Int,
        offset: Int,
        orderBy: String? = ""
    ): Resource<List<Guard>> {
        val response = tenantGuardsRepository.getSecGuardsRepo(token, tenantID, filter,limit, offset, orderBy)

        return try{

            if(response.isSuccessful && response.body()!=null){

                val jsonBody  = response.body()!!
                val guardsResponse = parseAllGuardsResponse(jsonBody as JsonObject)
                Resource.Success(//response.body()?.asJsonArray ?: JsonArray()

                    guardsResponse.rows.map {
                        Guard(
                            email = it.guard.email,
                            firstName =  it.guard.firstName,
                            id = it.guard.id,
                            lastName = it.guard.lastName
                        )
                    }


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
                val guardBodyResponse = response.body()
                Resource.Success(

                    SecurityGuardProfile(
                        guardBodyResponse?.get("id").toString(),
                        guardBodyResponse?.get("governmentId").toString(),
                        guardBodyResponse?.get("isOnDuty")?.asBoolean ?: false,
                        guardBodyResponse?.get("fullName").toString(),
                        guardBodyResponse?.get("bloodType").toString(),
                        guardBodyResponse?.get("academicInstruction").toString(),
                        guardBodyResponse?.get("maritalStatus").toString(),
                        guardBodyResponse?.get("gender").toString(),
                        guardBodyResponse?.get("birthDate").toString(),
                        guardBodyResponse?.get("birthPlace").toString()
                    )

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

                    response.body()?.map{ it ->
                        Log.i("sds","que jesto  ${it}")
                        autocompletGuardsResponseItem(
                            it.asJsonObject.get("id").toString(),
                            it.asJsonObject.get("label").toString()

                        )
                    } ?: emptyList()
                )
            }else{
                Resource.Error(
                    response.message().toString() + " -- " + response.raw().toString()
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