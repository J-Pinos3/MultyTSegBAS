package com.seguridadbas.multytenantseguridadbas.controllers.postsitecontrollers

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.PostSiteRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.postsite.AllPostSiteData
import com.seguridadbas.multytenantseguridadbas.model.postsite.AllPostSiteResponse
import com.seguridadbas.multytenantseguridadbas.model.postsite.Client
import com.seguridadbas.multytenantseguridadbas.model.postsite.ClientAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class PostSiteController @Inject constructor(
    private val postSiteRepository: PostSiteRepository
): ViewModel(){

    suspend fun getAllPostSites(
        token: String,
        tenantId: String
    ): Resource<List<AllPostSiteData>>{


        val response = postSiteRepository.getAllPostSitesRepo(token, tenantId)


        return try{
            if(response.isSuccessful && response.body()!=null){
                val jsonBody = response.body()!!
                val allPostSites = parseAllPostSitesResponse(jsonBody as JsonObject)

                Resource.Success(
                    allPostSites.rows
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






    private fun parseAllPostSitesResponse(jsonObject: JsonObject): AllPostSiteResponse{
        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map { rowElement ->
            val row = rowElement.asJsonObject
            val clientAccountObj = row.getAsJsonObject("clientAccount")
            val clientObj = row.getAsJsonObject("client")

            AllPostSiteData(
                active = row.get("active").asBoolean,
                address = row.get("address").asString,
                city = row.get("city").asString,
                client = Client(
                    email = clientObj.get("email").asString,
                            id = clientObj.get("id").asString,
                            lastName = clientObj.get("lastName").asString,
                            name = clientObj.get("name").asString,
                ),
                clientAccount = ClientAccount(
                    active = clientAccountObj.get("active").asBoolean,
                    address = clientAccountObj.get("address").asString,
                    addressComplement = clientAccountObj.get("addressComplement").asString,
                    categoryIds = clientAccountObj.getAsJsonArray("categoryIds").toList(),
                    city = clientAccountObj.get("city").asString,
                    country = clientAccountObj.get("country").asString,
                    createdAt = clientAccountObj.get("createdAt").asString,
                    createdById = clientAccountObj.get("createdById").asString,
                    deletedAt = clientAccountObj.isNullStringField("deletedAt"),
                    email = clientAccountObj.get("email").asString,
                    faxNumber = clientAccountObj.isNullStringField("faxNumber"),
                    id = clientAccountObj.get("id").asString,
                    importHash = clientAccountObj.isNullStringField("importHash"),
                    lastName = clientAccountObj.isNullStringField("lastName"),
                    latitude = clientAccountObj.isNullStringField("latitude"),
                    longitude = clientAccountObj.isNullStringField("longitude"),
                    name = clientAccountObj.isNullStringField("name"),
                    phoneNumber = clientAccountObj.isNullStringField("phoneNumber"),
                    tenantId = clientAccountObj.isNullStringField("tenantId"),
                    updatedAt = clientAccountObj.isNullStringField("updatedAt"),
                    updatedById = clientAccountObj.isNullStringField("updatedById"),
                    useSameAddressForBilling = clientAccountObj.get("useSameAddressForBilling").asBoolean,
                    website = clientAccountObj.isNullStringField("website"),
                    zipCode = clientAccountObj.isNullStringField("zipCode"),
                ),
                clientAccountId = row.get("clientAccountId").asString,
                clientAccountName = row.get("clientAccountName").asString,
                clientId = row.get("clientId").asString,
                companyName = row.get("companyName").asString,
                contactEmail = row.get("contactEmail").asString,
                contactPhone = row.get("contactPhone").asString,
                country = row.get("country").asString,
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                deletedAt = row.isNullStringField("deletedAt"),
                description = row.get("description").asString,
                email = row.get("email").asString,
                id = row.get("id").asString,
                importHash = row.isNullStringField("importHash"),
                latitud = row.get("latitud").asString,
                latitude = row.get("latitude").asString,
                logo = row.getAsJsonArray("logo").toList(),
                longitud = row.get("longitud").asString,
                longitude = row.get("longitude").asString,
                name = row.get("name").asString,
                phone = row.get("phone").asString,
                postalCode = row.get("postalCode").asString,
                secondAddress = row.get("secondAddress").asString,
                tenantId = row.get("tenantId").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.isNullStringField("updatedById"),
            )
        }


        return AllPostSiteResponse(count, rows)
    }




    private fun JsonObject?.isNullStringField(field: String): String{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asString else ""
    }
}