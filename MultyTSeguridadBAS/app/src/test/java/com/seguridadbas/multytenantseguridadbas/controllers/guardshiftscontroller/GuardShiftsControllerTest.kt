package com.seguridadbas.multytenantseguridadbas.controllers.guardshiftscontroller

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.repository.GuardShiftsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class GuardShiftsControllerTest {

    private  val fakeTenantId = "asdkajdljahsd"
    private val fakeBearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijk5ZjA0NGM1LTYxZjItNGM1NS04NjQzLWZjZjg3YWYwYWMzZSIsImlhdCI6MTc2ODk0MzE5MCwiZXhwIjoxNzY5NTQ3OTkwfQ.9Ix847666sZuey_EpIQAoYflhomHM8B4cy4Lqc_X9Ao"

    @RelaxedMockK
    private lateinit var guardShiftRepository: GuardShiftsRepository

    lateinit var guardShiftsController: GuardShiftsController

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        guardShiftsController = GuardShiftsController(guardShiftRepository)
    }

    @Test
    fun `given a list of all guard shifts, choose one object and show guardName`() = runBlocking {
        val jsonObject = JsonObject().apply {
            addProperty("completeInventoryCheck","")
            addProperty("completeInventoryCheckId","")
            addProperty("createdAt","")
            addProperty("createdById","")
            add("dailyIncidents", JsonArray())
            addProperty("deletedAt","")

            val guardNameObj = JsonObject().apply {
                addProperty("fullName","julian")
            }

            add("guardName",guardNameObj)
            addProperty("guardNameId","")
            addProperty("Id","")
            addProperty("importHash","")
            addProperty("numberOfIncidentsDurindShift",0)
            addProperty("numberOfPatrolsDuringShift",0)
            addProperty("observations","")
            add("patrolsDone", JsonArray())
            addProperty("punchInTime","")
            addProperty("punchOutTime","")
            addProperty("shiftSchedule","")

            val stationNameObj = JsonObject().apply {
                addProperty( "createdAt" ,"")
                addProperty( "createdById" ,"")
                addProperty( "deletedAt" ,"")
                addProperty( "finishTimeInDay" ,"")
                addProperty( "id" ,"")
                addProperty( "importHash" ,"")
                addProperty( "latitud" ,"")
                addProperty( "longitud" ,"")
                addProperty( "numberOfGuardsInStation" ,"")
                addProperty( "startingTimeInDay" ,"")
                addProperty( "stationName" ,"")
                addProperty( "stationOriginId" ,"")
                addProperty( "stationSchedule" ,"")
                addProperty( "tenantId" ,"")
                addProperty( "updatedAt" ,"")
                addProperty( "updatedById" ,"")
            }
            add("stationName",stationNameObj)

            addProperty("stationNameId", "")
            addProperty("tenantId","")
            addProperty("updatedAt","")
            addProperty("updatedById","")
        }

        val jsonArray = JsonArray().apply {
            add(jsonObject)
            add(jsonObject)
            add(jsonObject)
        }

        val jsonResponse = JsonObject().apply {
            addProperty("count",3)
            add("rows", jsonArray)
        }

        val response = Response.success(jsonResponse)

        //GIVEN
        coEvery { guardShiftRepository.getAllGuardShiftsRepo(fakeBearerToken, fakeTenantId) } returns response

        //WHEN
        val result = guardShiftsController.getGuardShifts(fakeBearerToken, fakeTenantId)

        //THEN
        assert(result.data!![2].guardName == "julian")


    }


    @Test
    fun `success case when the api returns a non empty Guard Shift Detail object`() = runBlocking {
        val jsonObject = JsonObject().apply {
            addProperty( "completeInventoryCheck","")
            add( "dailyIncidents",JsonArray())
            val guardNameObj = JsonObject().apply {
                addProperty( "hiringContractDate", "")
                addProperty( "birthDate", "")
                addProperty( "id", "")
                addProperty( "governmentId", "")
                addProperty( "fullName", "juan alberto")
                addProperty( "gender", "")
                addProperty( "isOnDuty", true)
                addProperty( "bloodType", "")
                addProperty( "guardCredentials", "")
                addProperty( "birthPlace", "")
                addProperty( "maritalStatus", "")
                addProperty( "academicInstruction", "")
                addProperty( "address", "")
                addProperty( "importHash", "")
                addProperty( "createdAt", "")
                addProperty( "updatedAt", "")
                addProperty( "deletedAt", "")
                addProperty( "guardId", "")
                addProperty( "tenantId", "")
                addProperty( "createdById", "")
                addProperty( "updatedById", "")
            }

            add("guardName",guardNameObj)
            addProperty( "guardNameId",false)
            addProperty( "id","")
            addProperty( "numberOfIncidentsDuringShift",3)
            addProperty( "numberOfPatrolsDuringShift",4)
            addProperty( "observations","")
            add( "patrolsDone",JsonArray())
            addProperty( "punchInTime","")
            addProperty( "punchOutTime","")
            addProperty( "shiftSchedule","")
            addProperty( "tenantId","")
            addProperty( "latitude","")
            addProperty( "longitude","")
            addProperty( "numberOfGuardsInStation","")
            val stationNameObj = JsonObject().apply {
                addProperty( "id","")
                addProperty( "stationName","")
                addProperty( "latitud","")
                addProperty( "longitud","")
                addProperty( "numberOfGuardsInStation",3)
                addProperty( "stationSchedule","")
                addProperty( "startingTimeInDay","")
                addProperty( "finishTimeInDay","")
                addProperty( "importHash","")
                addProperty( "createdAt","")
                addProperty( "updatedAt","")
                addProperty( "deletedAt","")
                addProperty( "stationOriginId","")
                addProperty( "tenantId","")
                addProperty( "createdById","")
                addProperty( "updatedById","")
            }
            add( "stationName",stationNameObj)
            addProperty( "startingTimeInDay","")
            addProperty( "finishTimeInDay","")
            addProperty( "importHash","")
            addProperty( "createdAt","")
            addProperty( "updatedAt","")
            addProperty( "deletedAt","")
            addProperty( "stationNameId","")
            addProperty( "guardNameId","")
            addProperty( "createdById","")
            addProperty( "updatedById","")

        }

        val response = Response.success(jsonObject)

        //GIVEN
        coEvery { guardShiftRepository.getShiftsByGuardRepo(fakeBearerToken, fakeTenantId, "1234") } returns response


        //WHEN
        val result = guardShiftsController.getGuardShiftsDetail(fakeBearerToken, fakeTenantId, "1234")

        //THEN
        print("GuardName: ${result.data!!.guardName}")
        assert(result.data.guardName.isNotEmpty())



    }
}