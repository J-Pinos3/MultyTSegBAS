package com.seguridadbas.multytenantseguridadbas.controllers.certifservicescontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.CertificationServicesRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.certifications.AllCertificationsResponse
import com.seguridadbas.multytenantseguridadbas.model.certifications.CertificationDataResponse
import com.seguridadbas.multytenantseguridadbas.model.guard_shifts.ShortGuardShift
import com.seguridadbas.multytenantseguridadbas.model.services.AllServicesResponse
import com.seguridadbas.multytenantseguridadbas.model.services.ServiceDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class CertificationServicesController @Inject constructor(
    private val certificationServicesRepository: CertificationServicesRepository
): ViewModel(){

    suspend fun getAllCertifications(
        token: String,
        tenantId: String
    ): Resource<List<CertificationDataResponse>>{

        val response = certificationServicesRepository.getAllCertificationsRepo(token, tenantId)

        return try{
            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!
                val certificationsResponse = parseAllCertificationsResponse(jsonBody as JsonObject)

                Resource.Success(
                    certificationsResponse.rows.map {
                        CertificationDataResponse(
                            acquisitionDate  = it.acquisitionDate,
                            code = it.code,
                            createdAt = it.createdAt,
                            createdById = it.createdById,
                            deletedAt = it.deletedAt,
                            description = it.description,
                            expirationDate = it.expirationDate,
                            icon = it.icon,
                            id = it.id,
                            image = it.image,
                            importHash = it.importHash,
                            tenantId = it.tenantId,
                            title = it.title,
                            updatedAt = it.updatedAt,
                            updatedById = it.updatedById,
                        )
                    }
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


    suspend fun getAllServices(
        token: String,
        tenantId: String
    ): Resource<List<ServiceDataResponse>>{
        val response = certificationServicesRepository.getAllServicesrepo(token, tenantId)

        return try{
            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!
                val servicesResponse = parseAllServicesResponse(jsonBody as JsonObject)

                Resource.Success(
                    servicesResponse.rows.map {
                        ServiceDataResponse(
                            createdAt = it.createdAt,
                            createdById = it.createdById,
                            deletedAt = it.deletedAt,
                            description = it.description,
                            iconImage = it.iconImage,
                            id = it.id,
                            importHash = it.importHash,
                            price = it.price,
                            serviceImages = it.serviceImages,
                            specifications = it.specifications,
                            subtitle = it.subtitle,
                            tenantId = it.tenantId,
                            title = it.title,
                            updatedAt = it.updatedAt,
                            updatedById = it.updatedById
                        )
                    }
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


    private fun parseAllCertificationsResponse(jsonObject: JsonObject): AllCertificationsResponse {

        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map { rowElement ->
            val row = rowElement.asJsonObject

            CertificationDataResponse(

                acquisitionDate = row.get("acquisitionDate").asString,
                code = row.get("code").asString,
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                deletedAt = row.isNullField("deletedAt"),
                description = row.get("description").asString,
                expirationDate = row.get("expirationDate").asString,
                icon = row.getAsJsonArray("icon").toList(),
                id = row.get("id").asString,
                image = row.getAsJsonArray("image").toList(),
                importHash = row.isNullField("importHash") ,
                tenantId = row.get("tenantId").asString,
                title = row.get("title").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.isNullField("updatedBy")
            )

        }

        return AllCertificationsResponse(count, rows)

    }


    private fun parseAllServicesResponse(jsonObject: JsonObject): AllServicesResponse {

        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map { rowElement ->
            val row = rowElement.asJsonObject

            ServiceDataResponse(
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                deletedAt = row.isNullField("deletedAt"),
                description = row.get("description").asString,
                iconImage = row.getAsJsonArray("iconImage").toList(),
                id = row.get("id").asString,
                importHash = row.isNullField("importHash"),
                price = row.get("price").asString,
                serviceImages = row.getAsJsonArray("serviceImages").toList(),
                specifications = row.get("specifications").asString ,
                subtitle = row.isNullField("subtitle"),
                tenantId = row.get("tenantId").asString,
                title = row.get("title").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.isNullField("updatedById")
            )

        }



        return AllServicesResponse(count, rows)

    }



    private fun JsonObject?.isNullField(field: String): String{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asString else ""
    }

}