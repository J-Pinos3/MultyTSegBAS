package com.seguridadbas.multytenantseguridadbas.controllers.billingaccountcontroller

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.repository.BillingAccountRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.ClientsInvoiced
import com.seguridadbas.multytenantseguridadbas.model.billingaccount.UpdatedBillingDataResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class BillingAccountControllerTest {

    private  val fakeTenantId = "asdkajdljahsd"
    private val fakeBearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijk5ZjA0NGM1LTYxZjItNGM1NS04NjQzLWZjZjg3YWYwYWMzZSIsImlhdCI6MTc2ODk0MzE5MCwiZXhwIjoxNzY5NTQ3OTkwfQ.9Ix847666sZuey_EpIQAoYflhomHM8B4cy4Lqc_X9Ao"

    @RelaxedMockK
    private lateinit var billingAccountRepository: BillingAccountRepository

    lateinit var billingAccountController: BillingAccountController

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        billingAccountController = BillingAccountController(billingAccountRepository)
    }



    @Test
    fun `throw error message when there is an error with api`() = runBlocking {
        val jsonResponse = JsonObject().apply {
            addProperty("error","error al traer clientes")
            addProperty("message","no hay cuentas de clientes")
        }

        val apiResponse = Response.error<JsonObject>(
            400,    ResponseBody.create(
                "application/json".toMediaTypeOrNull(),  jsonResponse.toString()
            )
        )

        //GIVEN
        coEvery { billingAccountRepository.getClientAccountRepo(fakeBearerToken, fakeTenantId) } returns apiResponse

        //WHEN
        val result = billingAccountController.getAllClientAccounts(fakeBearerToken, fakeTenantId)

        //THEN
        println("resultado: ${result.message}   ${billingAccountRepository.getClientAccountRepo(fakeBearerToken, fakeTenantId).isSuccessful}")
        assert(result is Resource.Error)
    }

    @Test
    fun `when api does not return the list of client accounts, make sure it returns empty list-array`() = runBlocking {
        val jsonResponse = JsonObject().apply {
            addProperty("count",0)
            add("rows", JsonArray())

        }

        val apiResponse = Response.success<JsonObject>(     jsonResponse      )

        //GIVEN
        coEvery { billingAccountRepository.getClientAccountRepo(fakeBearerToken, fakeTenantId) } returns apiResponse


        //WHEN
        val result = billingAccountController.getAllClientAccounts(fakeBearerToken, fakeTenantId)

        //THEN
        println("resultado: ${result.data}   ${billingAccountRepository.getClientAccountRepo(fakeBearerToken, fakeTenantId).isSuccessful}")
        assert(result is Resource.Success)
        assert((result as Resource.Success).data!!.count() == 0)
    }



    @Test
    fun `return the status of the billing object`() = runBlocking {

        val jsonObject = JsonObject().apply {
            addProperty("id", "")
            addProperty("status", "Pagada")
            addProperty("clientsInvoicedId", "")
            addProperty("createdAt", "")
            addProperty("createdById", "")
            addProperty("deletedAt", "")
            addProperty("description", "")
            addProperty("invoiceNumber", "")
            addProperty("lastPaymentDate", "")
            addProperty("montoPorPagar", "")
            addProperty("nextPaymentDate", "")
            addProperty("tenantId", "")
            addProperty("updatedAt", "")
            addProperty("updatedById", "")
            addProperty("importHash", "")

            // Para clientsInvoiced (objeto JsonObject)
            val clientsInvoicedObj = JsonObject().apply {
                addProperty("active", true)
                addProperty("address", "")
                addProperty("addressComplement", "")
                addProperty("categoryIds", "")
                addProperty("city", "")
                addProperty("country", "")
                addProperty("createdAt", "")
                addProperty("createdById", "")
                addProperty("deletedAt", "")
                addProperty("email", "")
                addProperty("faxNumber", "")
                addProperty("id", "")
                addProperty("importHash", "")
                addProperty("lastName", "")
                addProperty("latitude", "")
                addProperty("longitude", "")
                addProperty("name", "")
                addProperty("phoneNumber", "")
                addProperty("tenantId", "")
                addProperty("updatedAt", "")
                addProperty("updatedById", "")
                addProperty("useSameAddressForBilling", true)
                addProperty("website", "")
                addProperty("zipCode", "")
            }
            add("clientsInvoiced", clientsInvoicedObj)

            // Para bill (JsonArray vacío)
            add("bill", JsonArray())
        }
        val jsonArreglo = JsonArray().apply {
            add( jsonObject )
        }



        val jsonResponse = JsonObject().apply {
            addProperty("count", 1)
            add("rows",   jsonArreglo)
        }

        val response = Response.success(jsonResponse)

        //GIVEN
        coEvery { billingAccountRepository.getBillingRepo(fakeBearerToken, fakeTenantId, null) } returns response

        //WHEN
        val result = billingAccountController.getAllBilling(fakeBearerToken, fakeTenantId, null)

        //THEN
        assert(result is Resource.Success)
        assert(result.data?.get(0)?.status == "Pagada")
    }




}

