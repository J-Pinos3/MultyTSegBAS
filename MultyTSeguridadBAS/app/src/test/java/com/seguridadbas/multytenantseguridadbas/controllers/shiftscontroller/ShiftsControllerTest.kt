package com.seguridadbas.multytenantseguridadbas.controllers.shiftscontroller

import android.hardware.Sensor
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.repository.ShiftsRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ShiftsControllerTest {

    private  val fakeTenantId = "asdkajdljahsd"
    private val fakeBearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijk5ZjA0NGM1LTYxZjItNGM1NS04NjQzLWZjZjg3YWYwYWMzZSIsImlhdCI6MTc2ODk0MzE5MCwiZXhwIjoxNzY5NTQ3OTkwfQ.9Ix847666sZuey_EpIQAoYflhomHM8B4cy4Lqc_X9Ao"

    @RelaxedMockK
    private lateinit var shiftsRepository: ShiftsRepository

    lateinit var shiftsController: ShiftsController

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        shiftsController = ShiftsController(shiftsRepository)
    }


    @Test
    fun `the api returns an empty list of shifts and the app doesn't break`()= runBlocking {
        val jsonResponse = JsonObject().apply {
            addProperty("count",0)
            add("rows", JsonArray())
        }

        val apiResponse = Response.success<JsonObject>(jsonResponse)


        //GIVEN
        coEvery { shiftsRepository.getAllShiftsRepo(fakeBearerToken, fakeTenantId, emptyList()) } returns apiResponse

        //WHEN
        val result = shiftsController.getAllShifts(fakeBearerToken, fakeTenantId, emptyList())

        //THEN
        print("Result: ${result.data}")
        assert( result is Resource.Success )
        assert( (result as Resource.Success).data!!.count() == 0 )
    }


    @Test
    fun` the api returns a list of shifts within a startTimeRange `() = runBlocking {
        val guardObj = JsonObject().apply {
            addProperty("email","Jhonny@mail.com")
            addProperty("firstName","Jhon")
            addProperty("id","2")
            addProperty("lastName","Petruccinator")
        }

        val stationObjeto = JsonObject().apply {
            addProperty( "createdAt", "")
            addProperty( "createdById", "")
            addProperty( "deletedAt", "")
            addProperty( "finishTimeInDay", "")
            addProperty( "id", "")
            addProperty( "importHash", "")
            addProperty( "latitud", "")
            addProperty( "longitud", "")
            addProperty( "numberOfGuardsInStation", "")
            addProperty( "startingTimeInDay", "")
            addProperty( "stationName", "")
            addProperty( "stationOriginId", "")
            addProperty( "stationSchedule", "")
            addProperty( "tenantId", "")
            addProperty( "updatedAt", "")
            addProperty( "updatedById", "")
        }

        val datesList = listOf("2024-01-14T00:00:00.000Z","2024-01-15T23:59:00.000Z")

        val jeisonObject1 = JsonObject().apply {
            addProperty( "createdAt","")
            addProperty( "createdById","")
            addProperty( "deletedAt","")
            addProperty( "endTime","2024-01-15T23:57:00.000Z")
            add( "guard",guardObj)
            addProperty( "guardId","")
            addProperty( "id","")
            addProperty( "importHash","")
            addProperty( "startTime","2024-01-14T00:01:00.000Z")
            add( "station",stationObjeto)
            addProperty( "stationId","")
            addProperty( "tenantId","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
        }

        val jeisonObject2 = JsonObject().apply {
            addProperty( "createdAt","")
            addProperty( "createdById","")
            addProperty( "deletedAt","")
            addProperty( "endTime","2024-01-15T20:59:00.000Z")
            add( "guard",guardObj)
            addProperty( "guardId","")
            addProperty( "id","")
            addProperty( "importHash","")
            addProperty( "startTime","2024-01-15T00:00:00.000Z")
            add( "station",stationObjeto)
            addProperty( "stationId","")
            addProperty( "tenantId","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
        }

        val jeisonObject3 = JsonObject().apply {
            addProperty( "createdAt","")
            addProperty( "createdById","")
            addProperty( "deletedAt","")
            addProperty( "endTime","2024-01-18T23:59:00.000Z")
            add( "guard",guardObj)
            addProperty( "guardId","")
            addProperty( "id","")
            addProperty( "importHash","")
            addProperty( "startTime","2024-01-17T00:00:00.000Z")
            add( "station",stationObjeto)
            addProperty( "stationId","")
            addProperty( "tenantId","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
        }

        val jeisonArray = JsonArray().apply {
            add(jeisonObject1)
            add(jeisonObject2)
            add(jeisonObject3)
        }

        val jsonResponse = JsonObject().apply {
            addProperty("count",3)
            add("rows",jeisonArray)
        }

        val response = Response.success(jsonResponse)


        //GIVEN
        coEvery { shiftsRepository.getAllShiftsRepo(fakeBearerToken, fakeTenantId, datesList) } returns response

        //WHEN
        val result = shiftsController.getAllShifts(fakeBearerToken, fakeTenantId, datesList)

        //THEN
        print("data ${result.data}")
        assert( result is Resource.Success )
        assert( (result as Resource.Success).data!![2].startDate > datesList[1] )

    }



}