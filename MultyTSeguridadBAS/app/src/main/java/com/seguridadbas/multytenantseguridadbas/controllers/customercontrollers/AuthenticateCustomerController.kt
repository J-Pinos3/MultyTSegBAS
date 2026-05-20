// Archivo: .../controllers/customercontrollers/AuthenticateCustomerController.kt

package com.seguridadbas.multytenantseguridadbas.controllers.customercontrollers

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.customerpository.AuthenticateCustomerRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.AuthenticateMeCustResponse
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.ActiveShiftCust
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.ClientAccountCust
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.GuardCust
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
                val cleanJson = jsonBody?.toString()?.trim()?.replace("\uFEFF", "") // Remover BOM


                Log.d("API_RESPONSE", "Raw JSON: $cleanJson") // Ver los caracteres exactos
                Log.d("API_RESPONSE", "First char code: ${cleanJson?.firstOrNull()?.code}")

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
                    e.toString()
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
        val rootCommercialName = extractStringField(jsonObject, "commercialName", "tradeName", "businessName")
        val rootCompanyName = extractStringField(
            jsonObject,
            "companyName",
            "legalName",
            "razonSocial",
            "clientAccountName",
            "accountName"
        )

        // 1. Parsear ClientAccount
        val clientAccount = if (jsonObject.has("clientAccount") && !jsonObject.get("clientAccount").isJsonNull) {
            val clientObj = jsonObject.getAsJsonObject("clientAccount")
            ClientAccountCust(
                id = if (clientObj.has("id") && !clientObj.get("id").isJsonNull) clientObj.get("id").asString else "",
                name = if (clientObj.has("name") && !clientObj.get("name").isJsonNull) clientObj.get("name").asString else "",
                email = if (clientObj.has("email") && !clientObj.get("email").isJsonNull) clientObj.get("email").asString else "",
                commercialName = extractStringField(clientObj, "commercialName", "tradeName", "businessName").ifBlank { rootCommercialName },
                companyName = extractStringField(clientObj, "companyName", "legalName", "razonSocial", "clientAccountName", "accountName")
                    .ifBlank { rootCompanyName },
                isLegalEntity = extractLegalEntity(clientObj, jsonObject),
                logoUrl = extractFirstMediaUrl(clientObj, "logoUrl", "logo", "clientLogo", "logoImage"),
                placeImageUrl = extractFirstMediaUrl(
                    clientObj,
                    "placePictureUrl",
                    "placeImageUrl",
                    "placeImage",
                    "backgroundImage",
                    "coverImage",
                    "photo",
                    "image"
                ).ifBlank {
                    extractFirstMediaUrl(
                        jsonObject,
                        "placePictureUrl",
                        "placeImageUrl",
                        "placeImage",
                        "backgroundImage",
                        "coverImage",
                        "photo",
                        "image"
                    )
                }
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
                                    stationObj.get("stationName").asString else "",
                                id = extractStringField(stationObj, "id", "stationId")
                            )
                        )
                    }
                }

                postSites.add(
                    PostSitesCust(
                        address = if (siteObj.has("address") && !siteObj.get("address").isJsonNull) siteObj.get("address").asString else "",
                        city = if (siteObj.has("city") && !siteObj.get("city").isJsonNull) siteObj.get("city").asString else "",
                        companyName = extractStringField(siteObj, "companyName", "legalName", "razonSocial"),
                        clientAccountName = extractStringField(siteObj, "clientAccountName", "accountName"),
                        commercialName = extractStringField(siteObj, "commercialName", "tradeName", "businessName"),
                        stations = stations
                    )
                )
            }
        }

        val guards = mutableListOf<GuardCust>()
        if (jsonObject.has("guards") && !jsonObject.get("guards").isJsonNull) {
            jsonObject.getAsJsonArray("guards").forEach { guardElement ->
                val guardObj = guardElement.asJsonObject
                val userObj = guardObj.safeGetObject("user")
                guards.add(
                    GuardCust(
                        id = extractStringField(guardObj, "id"),
                        guardId = extractStringField(guardObj, "guardId", "id"),
                        name = extractStringField(guardObj, "name", "fullName").ifBlank { extractStringField(userObj, "name", "fullName") },
                        firstName = extractStringField(guardObj, "firstName").ifBlank { extractStringField(userObj, "firstName") },
                        lastName = extractStringField(guardObj, "lastName").ifBlank { extractStringField(userObj, "lastName") },
                        documentNumber = extractStringField(guardObj, "documentNumber", "document", "dni"),
                        stationId = extractStringField(guardObj, "stationId")
                    )
                )
            }
        }

        val activeShifts = mutableListOf<ActiveShiftCust>()
        if (jsonObject.has("activeShifts") && !jsonObject.get("activeShifts").isJsonNull) {
            jsonObject.getAsJsonArray("activeShifts").forEach { shiftElement ->
                val shiftObj = shiftElement.asJsonObject
                val guardObj = shiftObj.safeGetObject("guard")
                val guardUserObj = guardObj.safeGetObject("user")
                val stationObj = shiftObj.safeGetObject("station")
                activeShifts.add(
                    ActiveShiftCust(
                        id = extractStringField(shiftObj, "id"),
                        guardId = extractStringField(shiftObj, "guardId").ifBlank {
                            extractStringField(guardObj, "id", "guardId", "userId")
                        },
                        stationId = extractStringField(shiftObj, "stationId").ifBlank { extractStringField(stationObj, "id", "stationId") },
                        guardName = extractStringField(shiftObj, "guardName").ifBlank {
                            val first = extractStringField(shiftObj, "guardFirstName")
                                .ifBlank { extractStringField(guardObj, "firstName") }
                                .ifBlank { extractStringField(guardUserObj, "firstName") }
                            val last = extractStringField(shiftObj, "guardLastName")
                                .ifBlank { extractStringField(guardObj, "lastName") }
                                .ifBlank { extractStringField(guardUserObj, "lastName") }
                            listOf(first, last).filter { it.isNotBlank() }.joinToString(" ").ifBlank {
                                extractStringField(shiftObj, "guardFullName").ifBlank {
                                    extractStringField(guardObj, "name", "fullName").ifBlank {
                                        extractStringField(guardUserObj, "name", "fullName")
                                    }
                                }
                            }
                        },
                        stationName = extractStringField(shiftObj, "stationName").ifBlank {
                            extractStringField(stationObj, "stationName", "name")
                        }
                    )
                )
            }
            Log.d("AUTH_CONTROLLER", "Parsed ${activeShifts.size} active shifts from API response")
            activeShifts.forEach { shift ->
                Log.d("AUTH_CONTROLLER", "Active Shift - Guard: ${shift.guardName}, Station: ${shift.stationName}, ID: ${shift.id}")
            }
        }

        return AuthenticateMeCustResponse(
            clientAccount = clientAccount,
            postSites = postSites,
            guards = guards,
            activeShifts = activeShifts
        )
    }

    private fun JsonObject?.safeGetObject(field: String): JsonObject {
        if (this == null || !this.has(field) || this.get(field).isJsonNull || !this.get(field).isJsonObject) {
            return JsonObject()
        }
        return this.get(field).asJsonObject
    }

    private fun extractFirstMediaUrl(source: JsonObject, vararg candidateFields: String): String {
        candidateFields.forEach { field ->
            if (!source.has(field) || source.get(field).isJsonNull) return@forEach
            val parsedUrl = parseMediaElement(source.get(field))
            if (parsedUrl.isNotBlank()) return parsedUrl
        }
        return ""
    }

    private fun extractStringField(source: JsonObject, vararg candidateFields: String): String {
        candidateFields.forEach { field ->
            if (!source.has(field) || source.get(field).isJsonNull) return@forEach
            val value = source.get(field).asString
            if (value.isNotBlank() && value != "null") return value
        }
        return ""
    }

    private fun extractBooleanField(source: JsonObject, vararg candidateFields: String): Boolean {
        candidateFields.forEach { field ->
            if (!source.has(field) || source.get(field).isJsonNull) return@forEach
            val value = source.get(field)
            return when {
                value.isJsonPrimitive && value.asJsonPrimitive.isBoolean -> value.asBoolean
                value.isJsonPrimitive -> {
                    val raw = value.asString.trim().lowercase()
                    raw == "true" || raw == "1" || raw == "yes"
                }
                else -> false
            }
        }
        return false
    }

    private fun extractLegalEntity(vararg sources: JsonObject): Boolean {
        sources.forEach { source ->
            if (extractBooleanField(source, "isLegalEntity", "isCompany", "personaJuridica")) return true
            val personType = extractStringField(source, "personType", "tipoPersona", "clientType")
            if (personType.equals("JURIDICA", ignoreCase = true) || personType.equals("LEGAL", ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    private fun parseMediaElement(element: JsonElement): String {
        if (element.isJsonNull) return ""
        if (element.isJsonPrimitive) {
            return element.asString.takeIf { it.startsWith("http") } ?: ""
        }
        if (element.isJsonArray) {
            return parseMediaArray(element.asJsonArray)
        }
        if (element.isJsonObject) {
            val mediaObject = element.asJsonObject
            val directField = listOf("downloadUrl", "url", "secureUrl", "location")
                .firstNotNullOfOrNull { key ->
                    mediaObject.get(key)?.takeIf { !it.isJsonNull }?.asString?.takeIf { value -> value.isNotBlank() }
                }
            if (!directField.isNullOrBlank()) return directField
            if (mediaObject.has("rows") && mediaObject.get("rows").isJsonArray) {
                return parseMediaArray(mediaObject.getAsJsonArray("rows"))
            }
        }
        return ""
    }

    private fun parseMediaArray(array: JsonArray): String {
        array.forEach { item ->
            val url = parseMediaElement(item)
            if (url.isNotBlank()) return url
        }
        return ""
    }
}