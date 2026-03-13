package com.seguridadbas.multytenantseguridadbas.controllers.invoicescontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.InvoiceRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.invoices.AllInvoicesRespData
import com.seguridadbas.multytenantseguridadbas.model.invoices.AllInvoicesResponse
import com.seguridadbas.multytenantseguridadbas.model.invoices.Client
import com.seguridadbas.multytenantseguridadbas.model.invoices.Item
import com.seguridadbas.multytenantseguridadbas.model.invoices.Payment
import com.seguridadbas.multytenantseguridadbas.model.invoices.PostSite
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class InvoiceController @Inject constructor(
    private val invoiceRepository: InvoiceRepository
): ViewModel() {



    suspend fun getAllInvoices(
        token: String, tenantId: String
    ): Resource<List<AllInvoicesRespData>>{
        val response = invoiceRepository.getAllInvoicesRepo(token, tenantId)

        return try{

            if(response.isSuccessful && response.body() != null){
                val jsonBody = response.body()!!
                val invoicesResponse = parseAllInvoicesResponse(jsonBody as JsonObject)

                Resource.Success(
                    invoicesResponse.rows
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


    suspend fun parseAllInvoicesResponse(jsonObject: JsonObject): AllInvoicesResponse{
        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map { rowElement ->
            val row = rowElement.asJsonObject
            val clientObj = row.getAsJsonObject("client")
            val itemsObj = row.getAsJsonArray("items")
            val items = itemsObj.map { itemElement ->
                val item = itemElement.asJsonObject
                Item(
                    description = item.get("description").asString,
                    id =  item.get("description").asString,
                    quantity =  item.get("quantity").asInt,
                    total =  item.get("total").asDouble,
                    unitPrice =  item.get("unitPrice").asDouble,
                )
            }
            val paymentObj = row.getAsJsonArray("payments")
            val paymentItem = paymentObj.map { itemElement ->
                val item = itemElement.asJsonObject

                Payment(
                    amount = item.get("amount").asDouble,
                    date = item.get("date").asString ,
                    id = item.get("id").asString ,
                    method = item.get("method").asString ,
                )
            }


            val postSiteObj = row.getAsJsonObject("postSite")

            AllInvoicesRespData(
                client = Client(
                    address = clientObj.get("address").asString,
                    email = clientObj.get("email").asString,
                    id = clientObj.get("id").asString,
                    name = clientObj.get("name").asString,
                    phone = clientObj.get("phone").asString
                ),
                clientId = row.get("clientId").asString,
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                date = row.get("date").asString,
                dueDate = row.get("dueDate").asString,
                id = row.get("id").asString,
                invoiceNumber = row.get("invoiceNumber").asString,
                items = items,
                notes = row.get("notes").asString,
                payments = paymentItem,
                poSoNumber = row.get("poSoNumber").asString,
                postSite = PostSite(
                    address = postSiteObj.get("address").asString,
                            city = postSiteObj.get("city").asString,
                            companyName = postSiteObj.get("companyName").asString,
                            contactEmail = postSiteObj.get("contactEmail").asString,
                            contactPhone = postSiteObj.get("contactPhone").asString,
                            country = postSiteObj.get("country").asString,
                            id = postSiteObj.get("id").asString,
                            name = postSiteObj.get("name").asString,
                ),
                postSiteId = row.get("postSiteId").asString ,
                sentAt = row.get("sentAt").asString ,
                status = row.get("status").asString ,
                subtotal = row.get("subtotal").asString ,
                summary = row.get("summary").asString ,
                tenantId = row.get("tenantId").asString ,
                title = row.get("title").asString ,
                total = row.get("total").asString ,
                updatedAt = row.get("updatedAt").asString ,
                updatedById = row.get("updatedById").asString ,
            )
        }

        return AllInvoicesResponse(count,rows)
    }

}