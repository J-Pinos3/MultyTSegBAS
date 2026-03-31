package com.seguridadbas.multytenantseguridadbas.controllers.incidentsdetailcontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.IncidentsDetailRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.stationreports.Client
import com.seguridadbas.multytenantseguridadbas.model.stationreports.GuardName
import com.seguridadbas.multytenantseguridadbas.model.stationreports.IncidentDetailResponse
import com.seguridadbas.multytenantseguridadbas.model.stationreports.IncidentType
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class IncidentDetailController @Inject constructor(
    private val incidentDetailRepository: IncidentsDetailRepository
): ViewModel(){

    suspend fun getIncidentDetail(token: String, tenantId: String, incidentId: String): Resource<IncidentDetailResponse> {
        val response = incidentDetailRepository.getIncidentDetailRepo("Bearer $token", tenantId, incidentId)

        return try {

            if(response.isSuccessful){
                val incidentDetail = response.body()!!
                val clientObj = incidentDetail.getAsJsonObject("client")
                val guardNameObj = incidentDetail.getAsJsonObject("guardName")
                val incidentTypeObj = incidentDetail.getAsJsonObject("incidentType")

                Resource.Success(
                    IncidentDetailResponse(
                        action = incidentDetail.isNullStringField("action"),
                        actionsTaken = incidentDetail.isNullStringField("actionsTaken"),
                        callerName = incidentDetail.get("callerName").asString,
                        callerType = incidentDetail.get("callerType").asString,
                        client = Client(
                            active = clientObj.get("active").asBoolean,
                            address = clientObj.get("address").asString,
                            addressComplement = clientObj.get("addressComplement").asString,
                            categoryIds = clientObj.getAsJsonArray("categoryIds").toList(),
                            city = clientObj.get("city").asString,
                            country = clientObj.get("country").asString,
                            createdAt = clientObj.get("createdAt").asString,
                            createdById = clientObj.get("createdById").asString,
                            deletedAt = clientObj.isNullStringField("deletedAt"),
                            documentNumber = clientObj.isNullStringField("documentNumber"),
                            email = clientObj.get("email").asString,
                            faxNumber = clientObj.get("faxNumber").asString,
                            id = clientObj.get("id").asString,
                            importHash = clientObj.isNullStringField("importHash"),
                            landline = clientObj.isNullStringField("landline"),
                            lastName = clientObj.get("lastName").asString,
                            latitude = clientObj.get("latitude").asString,
                            longitude = clientObj.get("longitude").asString,
                            name = clientObj.get("name").asString,
                            personType = clientObj.get("personType").asString,
                            phoneNumber = clientObj.get("phoneNumber").asString,
                            tenantId = clientObj.get("tenantId").asString,
                            updatedAt = clientObj.get("updatedAt").asString,
                            updatedById = clientObj.get("updatedById").asString,
                            useSameAddressForBilling = clientObj.get("useSameAddressForBilling").asBoolean,
                            website = clientObj.get("website").asString,
                            zipCode = clientObj.get("zipCode").asString,
                        ),
                        clientId = incidentDetail.get("clientId").asString,
                        comments = incidentDetail.isNullStringField("comments"),
                        content = incidentDetail.get("content").asString,
                        createdAt = incidentDetail.get("createdAt").asString,
                        createdById = incidentDetail.get("createdById").asString,
                        date = incidentDetail.get("date").asString,
                        dateTime = incidentDetail.isNullStringField("dateTime"),
                        deletedAt = incidentDetail.isNullStringField("deletedAt"),
                        description = incidentDetail.get("description").asString,
                        guardName = GuardName(
                            academicInstruction = guardNameObj.get("academicInstruction").asString,
                            address = guardNameObj.get("address").asString,
                            availability = guardNameObj.isNullStringField("availability"),
                            birthDate = guardNameObj.get("birthDate").asString,
                            birthPlace = guardNameObj.get("birthPlace").asString,
                            bloodType = guardNameObj.get("bloodType").asString,
                            createdAt = guardNameObj.get("createdAt").asString,
                            createdById = guardNameObj.get("createdById").asString,
                            deletedAt = guardNameObj.isNullStringField("deletedAt"),
                            fullName = guardNameObj.get("fullName").asString,
                            gender = guardNameObj.get("gender").asString,
                            governmentId = guardNameObj.get("governmentId").asString,
                            guardCredentials = guardNameObj.get("guardCredentials").asString,
                            guardId = guardNameObj.get("guardId").asString,
                            hiringContractDate = guardNameObj.get("hiringContractDate").asString,
                            id = guardNameObj.get("id").asString,
                            importHash = guardNameObj.isNullStringField("importHash"),
                            isOnDuty = guardNameObj.get("isOnDuty").asBoolean,
                            maritalStatus = guardNameObj.get("maritalStatus").asString,
                            tenantId = guardNameObj.get("tenantId").asString,
                            updatedAt = guardNameObj.get("updatedAt").asString,
                            updatedById = guardNameObj.get("updatedById").asString,
                        ),
                        guardNameId = incidentDetail.get("guardNameId").asString,
                        id = incidentDetail.get("id").asString,
                        imageUrl = incidentDetail.getAsJsonArray("imageUrl").toList(),
                        importHash = incidentDetail.isNullStringField("importHash"),
                        incidentAt = incidentDetail.get("incidentAt").asString,
                        incidentType = IncidentType(
                            active = incidentTypeObj.get("active").asBoolean,
                                    createdAt = incidentTypeObj.get("createdAt").asString,
                                    createdById = incidentTypeObj.get("createdById").asString,
                                    deletedAt = incidentTypeObj.isNullStringField("deletedAt"),
                                    id = incidentTypeObj.get("id").asString,
                                    importHash = incidentTypeObj.isNullStringField("importHash"),
                                    name = incidentTypeObj.get("name").asString,
                                    tenantId = incidentTypeObj.get("tenantId").asString,
                                    updatedAt = incidentTypeObj.get("updatedAt").asString,
                                    updatedById = incidentTypeObj.get("updatedById").asString,
                        ),
                        incidentTypeId = incidentDetail.get("incidentTypeId").asString,
                        internalNotes = incidentDetail.isNullStringField("internalNotes"),
                        location = incidentDetail.get("location").asString,
                        postSiteId = incidentDetail.isNullStringField("postSiteId"),
                        priority = incidentDetail.get("priority").asString,
                        site = incidentDetail.isNullStringField("site"),
                        siteId = incidentDetail.isNullStringField("siteId"),
                        station = incidentDetail.isNullStringField("station"),
                        stationId = incidentDetail.get("stationId").asString,
                        stationIncidents = incidentDetail.isNullStringField("stationIncidents"),
                        stationIncidentsId = incidentDetail.isNullStringField("stationIncidentsId"),
                        status = incidentDetail.get("status").asString,
                        subject = incidentDetail.get("subject").asString,
                        tenantId = incidentDetail.get("tenantId").asString,
                        title = incidentDetail.get("title").asString,
                        updatedAt = incidentDetail.get("updatedAt").asString,
                        updatedById = incidentDetail.get("updatedById").asString,
                        wasRead = incidentDetail.get("wasRead").asBoolean,

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
        return if( this?.get(field)?.isJsonNull == false ) this.get(field).asString else ""
    }

    private fun JsonObject?.isNullIntField(field: String): Int{
        return if( this?.get(field)?.isJsonNull == false ) this.get(field).asInt else 0
    }

    private fun JsonObject?.isNullBooleanField(field: String): Boolean{
        return if( this?.get(field)?.isJsonNull == false ) this.get(field).asBoolean else false
    }


}