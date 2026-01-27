package com.seguridadbas.multytenantseguridadbas.controllers.certificationservicescontroller

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.certifservicescontroller.CertificationServicesController
import com.seguridadbas.multytenantseguridadbas.controllers.repository.CertificationServicesRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CertificationServicesControllerTest {

    private val testUser = User("Reverb1@outlook.es", "Reverbpass")
    private  val fakeTenantId = "asdkajdljahsd"
    private val fakeBearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijk5ZjA0NGM1LTYxZjItNGM1NS04NjQzLWZjZjg3YWYwYWMzZSIsImlhdCI6MTc2ODk0MzE5MCwiZXhwIjoxNzY5NTQ3OTkwfQ.9Ix847666sZuey_EpIQAoYflhomHM8B4cy4Lqc_X9Ao"


    @RelaxedMockK
    private lateinit var certServRepository: CertificationServicesRepository

    lateinit var certServController: CertificationServicesController

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        certServController = CertificationServicesController(certServRepository)
    }



    @Test
fun `when api does not return the list of certifications, make sure it returns empty list-array`() = runBlocking {
        val jsonResponse = JsonObject().apply {
            addProperty("count", 0)
            add("rows", JsonArray())

        }

        val apiResponse = Response.success<JsonObject>(jsonResponse)


        //GIVEN
        coEvery {
            certServRepository.getAllCertificationsRepo(
                fakeBearerToken,
                fakeTenantId
            )
        } returns apiResponse

        //WHEN
        val result = certServController.getAllCertifications(fakeBearerToken, fakeTenantId)

        //THEN
        assert(result is Resource.Success)
        assert((result as Resource.Success).data!!.count() == 0)


    }


    @Test
    fun `the case when api returns a complete list of services`() = runBlocking {

        val jsonObject = JsonObject().apply {
            addProperty("createdAt","")
            addProperty("createdById","")
            addProperty("deletedAt","")
            addProperty("description","")
            add("iconImage", JsonArray())
            addProperty("id","")
            addProperty("importHash","")
            addProperty("price","")
            add("serviceImages", JsonArray())
            addProperty("taxId","")
            addProperty("taxName","")
            addProperty("taxRate","")
            addProperty("tenantId","")
            addProperty("title","")
            addProperty("updatedAt","")
            addProperty("updatedById","")
        }

        val jeisonArreglo = JsonArray().apply {
            add(jsonObject)
            add(jsonObject)
            add(jsonObject)
        }

        val jsonResponse = JsonObject().apply {
            addProperty("count",3)
            add("rows", jeisonArreglo)
        }

        val response = Response.success(jsonResponse)


        //GIVEN
        coEvery { certServRepository.getAllServicesrepo(fakeBearerToken, fakeTenantId) } returns response

        //WHEN
        val result = certServController.getAllServices(fakeBearerToken, fakeTenantId)

        //THEN
        assert(result is Resource.Success)
        assert((result as Resource.Success).data!!.count() == jeisonArreglo.size())


    }

}