// Archivo: .../controllers/customercontrollers/AuthenticateCustomerController.kt

package com.seguridadbas.multytenantseguridadbas.controllers.customercontrollers

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.customerpository.AuthenticateCustomerRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.AuthenticateMeCustResponse
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.ClientAccountCust
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.PostSitesCust
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.StationsCust
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AuthenticateCustomerController @Inject constructor(
    private val authenticateCustomerRepository: AuthenticateCustomerRepository
): ViewModel(){

    suspend fun getMeCustomerAccount(
        token: String
    ): Resource<AuthenticateMeCustResponse> {
        return try {
            val response = authenticateCustomerRepository.getMeCustomerAccountRepo(token)

            if (response.isSuccessful) {
                val jsonBody = response.body()
                if (jsonBody != null) {
                    val authenticateMeCustResponse = parseAuthenticateMeCustResponse(jsonBody)
                    Resource.Success(authenticateMeCustResponse)
                } else {
                    Resource.Error("Respuesta del servidor vacía")
                }
            } else {
                val errorMessage = try {
                    val errorJson = response.errorBody()?.string()
                    JsonParser.parseString(errorJson)
                        .asJsonObject
                        .get("message")
                        ?.asString ?: "Error desconocido"
                } catch (e: Exception) {
                    "Error desconocido"
                }
                Resource.Error(errorMessage)
            }
        } catch (e: SocketTimeoutException) {
            Resource.Error("La conexión ha tardado mucho tiempo")
        } catch (ex: NoNetworkException) {
            Resource.Error(ex.message.toString())
        } catch (ex: IOException) {
            Resource.Error("Error de comunicación: ${ex.message}")
        } catch (e: Exception) {
            Resource.Error("Ocurrió un error inesperado: ${e.message}")
        }
    }

    private fun parseAuthenticateMeCustResponse(jsonObject: JsonObject): AuthenticateMeCustResponse {
        // 1. Parsear ClientAccount
        val clientAccount = if (jsonObject.has("clientAccount") && !jsonObject.get("clientAccount").isJsonNull) {
            val clientObj = jsonObject.getAsJsonObject("clientAccount")
            ClientAccountCust(
                id = if (clientObj.has("id") && !clientObj.get("id").isJsonNull) clientObj.get("id").asString else "",
                name = if (clientObj.has("name") && !clientObj.get("name").isJsonNull) clientObj.get("name").asString else "",
                email = if (clientObj.has("email") && !clientObj.get("email").isJsonNull) clientObj.get("email").asString else ""
            )
        } else {
            ClientAccountCust()
        }

        // 2. Parsear PostSites
        val postSites = mutableListOf<PostSitesCust>()
        if (jsonObject.has("postSites") && !jsonObject.get("postSites").isJsonNull) {
            val sitesArray = jsonObject.getAsJsonArray("postSites")
            sitesArray.forEach { siteElement ->
                val siteObj = siteElement.asJsonObject

                // Parsear estaciones dentro de cada sitio
                val stations = mutableListOf<StationsCust>()
                if (siteObj.has("stations") && !siteObj.get("stations").isJsonNull) {
                    val stationsArray = siteObj.getAsJsonArray("stations")
                    stationsArray.forEach { stationElement ->
                        val stationObj = stationElement.asJsonObject
                        stations.add(
                            StationsCust(
                                stationName = if (stationObj.has("stationName") && !stationObj.get("stationName").isJsonNull)
                                    stationObj.get("stationName").asString else ""
                            )
                        )
                    }
                }

                postSites.add(
                    PostSitesCust(
                        address = if (siteObj.has("address") && !siteObj.get("address").isJsonNull) siteObj.get("address").asString else "",
                        city = if (siteObj.has("city") && !siteObj.get("city").isJsonNull) siteObj.get("city").asString else "",
                        stations = stations
                    )
                )
            }
        }

        return AuthenticateMeCustResponse(
            clientAccount = clientAccount,
            postSites = postSites
        )
    }
}