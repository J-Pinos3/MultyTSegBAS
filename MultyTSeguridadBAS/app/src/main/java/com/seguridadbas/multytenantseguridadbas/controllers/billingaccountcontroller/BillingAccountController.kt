package com.seguridadbas.multytenantseguridadbas.controllers.billingaccountcontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.BillingAccountRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.AllBillingResponse
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.AllBillingResponseUpdated
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.AllClientAccountResponse
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.BillingDataResponse
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.ClientAccountDataResponse
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.ClientsInvoiced
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.UpdatedBillingDataResponse
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
        val response = billingAccountRepository.getClientAccountRepo(token, tenantId)

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


    suspend fun getAllBilling(
        token: String, tenantId: String, clientId:String? = null
    ): Resource<List<UpdatedBillingDataResponse>>{
        val response = billingAccountRepository.getBillingRepo(token, tenantId, clientId)


        return try {

            if( response.isSuccessful && response.body() != null ){
                val jsonBody = response.body()!!
                val billingResponse  = parseBillingResponse(jsonBody)
                Resource.Success(
                    billingResponse.rows
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



    suspend fun processPayment(
        token: String, tenantId: String, plan: String
    ): Resource<String>{
        val response  = billingAccountRepository.processPaymentRepo(token, tenantId, plan)


        return try{

            if(response.isSuccessful && response.body() != null){
                val field = response.body()!!.getAsJsonObject("id")
                Resource.Success(
                    response.body().isNullStringField(field.isNullStringField("description") )
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
                        addressComplement = row.isNullStringField("addressComplement"),
                        zipCode = row.isNullStringField("zipCode"),
                        city = row.get("city").asString,
                        country = row.get("country").asString,
                        useSameAddressForBilling = row.get("useSameAddressForBilling").asBoolean,
                        website = row.get("website").asString,
                        latitude = row.get("latitude").asString,
                        longitude = row.get("longitude").asString,
                        categoryIds = row.isNullStringField("categoryIds"),
                        active=row.get("active").asBoolean,
                        categories = row.getAsJsonArray("categories").toList(),
                        createdAt = row.get("createdAt").asString,
                        createdById = row.get("createdById").asString,
                        deletedAt = row.isNullStringField("deletedAt"),
                        email = row.get("email").asString,
                        faxNumber = row.isNullStringField("faxNumber"),
                        id = row.get("id").asString,
                        importHash = row.isNullStringField("importHash"),
                        phoneNumber = row.get("phoneNumber").asString,
                        tenantId = row.get("tenantId").asString,
                        updatedAt = row.get("updatedAt").asString,
                        name = row.get("name").asString,
                        lastName = row.isNullStringField("lastName"),
                        updatedById = row.isNullStringField("updatedById")


            )
        }

        return AllClientAccountResponse(count, rows)
    }



    private fun parseBillingResponse(jsonObject: JsonObject): AllBillingResponseUpdated{

        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map{ rowElement ->
            val row= rowElement.asJsonObject
            val clientsInvoicedObj = row.getAsJsonObject("clientsInvoiced")

            UpdatedBillingDataResponse(
                bill = row.getAsJsonArray("bill").toList(),
                clientsInvoiced = ClientsInvoiced(
                    active = clientsInvoicedObj.get("active").asBoolean,
                    address = clientsInvoicedObj.get("address").asString,
                    addressComplement = clientsInvoicedObj.isNullStringField("addressComplement"),
                    categoryIds = clientsInvoicedObj.isNullStringField("categoryIds"),
                    city = clientsInvoicedObj.get("city").asString,
                    country = clientsInvoicedObj.get("country").asString,
                    createdAt = clientsInvoicedObj.get("createdAt").asString,
                    createdById = clientsInvoicedObj.get("createdById").asString,
                    deletedAt = clientsInvoicedObj.isNullStringField("deletedAt"),
                    email = clientsInvoicedObj.get("email").asString,
                    faxNumber = clientsInvoicedObj.isNullStringField("faxNumber"),
                    id = clientsInvoicedObj.isNullStringField("id"),
                    importHash = clientsInvoicedObj.isNullStringField("importHash"),
                    lastName = clientsInvoicedObj.get("lastName").asString,
                    latitude = clientsInvoicedObj.isNullStringField("latitude"),
                    longitude = clientsInvoicedObj.isNullStringField("longitude"),
                    name = clientsInvoicedObj.get("name").asString,
                    phoneNumber = clientsInvoicedObj.get("phoneNumber").asString,
                    tenantId = clientsInvoicedObj.get("tenantId").asString,
                    updatedAt = clientsInvoicedObj.get("updatedAt").asString,
                    updatedById = clientsInvoicedObj.isNullStringField("updatedById"),
                    useSameAddressForBilling = clientsInvoicedObj.get("useSameAddressForBilling").asBoolean,
                    website = clientsInvoicedObj.isNullStringField("website"),
                    zipCode = clientsInvoicedObj.isNullStringField("zipCode")
                ),
                clientsInvoicedId = row.get("clientsInvoicedId").asString,
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                deletedAt = row.isNullStringField("deletedAt"),
                description = row.get("description").asString,
                id = row.get("id").asString,
                importHash = row.isNullStringField("importHash"),
                invoiceNumber = row.get("invoiceNumber").asString,
                lastPaymentDate = row.isNullStringField("lastPaymentDate"),
                montoPorPagar = row.isNullStringField("montoPorPagar"),
                nextPaymentDate = row.isNullStringField("nextPaymentDate"),
                status = row.get("status").asString,
                tenantId = row.get("tenantId").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.isNullStringField("updatedById")
            )
        }

        return AllBillingResponseUpdated(count, rows)
    }



    private fun JsonObject?.isNullStringField(field: String):  String{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asString else ""
    }

}