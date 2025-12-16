package com.seguridadbas.multytenantseguridadbas.controllers.billingaccountcontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.BillingAccountRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.AllClientAccountResponse
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.ClientAccountDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class BillingAccountController @Inject constructor(
    private val billingAccountRepository: BillingAccountRepository
): ViewModel() {

    suspend fun getAllClientAccounts(
        token: String, tenantId: String
    ): Resource<List<ClientAccountDataResponse>>{
        val response = billingAccountRepository.getClientAccountApi(token, tenantId)

        return try{

            if(response.isSuccessful && response.body() != null){
                val jsonBody = response.body()!!
                val clientAccountResponse = parseClientAccountsResponse(jsonBody)
                Resource.Success(
                    clientAccountResponse.rows
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


    private fun parseClientAccountsResponse(jsonObject: JsonObject): AllClientAccountResponse {
        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map{ rowElement ->
            val row = rowElement.asJsonObject
            ClientAccountDataResponse(
                address = row.get("address").asString,
                        billingInvoices = row.getAsJsonArray("billingInvoices").toList(),
                        commercialName = row.get("commercialName").asString,
                        contractDate = row.get("contractDate").asString,
                        createdAt = row.get("createdAt").asString,
                        createdById = row.get("createdById").asString,
                        deletedAt = row.isNullStringField("deletedAt"),
                        email = row.get("email").asString,
                        faxNumber = row.get("faxNumber").asString,
                        id = row.get("id").asString,
                        importHash = row.isNullStringField("importHash"),
                        logoUrl = row.getAsJsonArray("logoUrl").toList(),
                        phoneNumber = row.get("phoneNumber").asString,
                        placePictureUrl = row.getAsJsonArray("placePictureUrl").toList(),
                        purchasedServices = row.getAsJsonArray("purchasedServices").toList(),
                        pushNotifications = row.getAsJsonArray("pushNotifications").toList(),
                        representante = row.isNullStringField("representante"),
                        representanteId = row.isNullStringField("representanteId"),
                        rucNumber = row.get("rucNumber").asString,
                        stations = row.getAsJsonArray("stations").toList(),
                        tenantId = row.get("tenantId").asString,
                        updatedAt = row.get("updatedAt").asString,
                        updatedById = row.isNullStringField("updatedById")


            )
        }

        return AllClientAccountResponse(count, rows)
    }


    private fun JsonObject?.isNullStringField(field: String):  String{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asString else ""
    }

}