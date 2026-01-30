package com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.repository.StationReportsRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class StationReportsControllerTest {

    private  val fakeTenantId = "asdkajdljahsd"
    private val fakeBearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijk5ZjA0NGM1LTYxZjItNGM1NS04NjQzLWZjZjg3YWYwYWMzZSIsImlhdCI6MTc2ODk0MzE5MCwiZXhwIjoxNzY5NTQ3OTkwfQ.9Ix847666sZuey_EpIQAoYflhomHM8B4cy4Lqc_X9Ao"

    @RelaxedMockK
    private lateinit var stationReportsRepository: StationReportsRepository

    lateinit var stationReportsController: StationReportsController


    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        stationReportsController = StationReportsController(stationReportsRepository)
    }

    @Test
    fun `when api returns an empty list of guard shifts and the app doesn't break`() = runBlocking {
        val jsonResponse = JsonObject().apply {
            addProperty("count",0)
            add("rows", JsonArray())
        }

        val apiResponse = Response.success<JsonObject>(jsonResponse)

        coEvery { stationReportsRepository.getGuardShiftsByStationRepo(fakeBearerToken, fakeTenantId, null, null) } returns apiResponse

        val result = stationReportsController.getGuardShiftsByStation(fakeBearerToken, fakeTenantId, null, null)

        print("data: ${result.data}")
        assert(result is Resource.Success)
        assert( (result as Resource.Success).data!!.count() == 0 )
    }


    @Test
    fun `given a valid guard shift object if, return an object containing its data`()=runBlocking {
        val guardShiftId = "FJF.rjgrIkSD4Tgjsgj409434"

        val stationObject = JsonObject().apply {
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

        val jsonObject = JsonObject().apply {
            addProperty( "completeInventoryCheck", false)
            addProperty( "completeInventoryCheckId", "")
            addProperty( "createdAt", "")
            addProperty( "createdById", "")
            add( "dailyIncidents", JsonArray())
            addProperty( "deletedAt", "")
            addProperty( "guardName", "")
            addProperty( "guardNameId", "")
            addProperty( "id", guardShiftId)
            addProperty( "importHash", "")
            addProperty( "numberOfIncidentsDurindShift", 0)
            addProperty( "numberOfPatrolsDuringShift", 0)
            addProperty( "observations", "")
            add( "patrolsDone", JsonArray())
            addProperty( "punchInTime", "")
            addProperty( "punchOutTime", "")
            addProperty( "shiftSchedule", "")
            add("stationName", stationObject)
            addProperty( "stationNameId", "")
            addProperty( "tenantId", "")
            addProperty( "updatedAt", "")
            addProperty( "updatedById", "")
        }

        val response = Response.success(jsonObject)


        coEvery { stationReportsRepository.getGrdShiftByStationDetRepo(fakeBearerToken, fakeTenantId, guardShiftId) } returns response

        val result = stationReportsController.getGrdShiftByStationDetail(fakeBearerToken, fakeTenantId, guardShiftId)


        assert(result is Resource.Success)
        assert( (result as Resource.Success).data!!.id == guardShiftId )

    }


    @Test
    fun `return a list of reports by station given a valid stationId`() = runBlocking {
        val stationId = "FJF.rjgrIkSD4Tgjsgj409434"

        val stationObj = JsonObject().apply {
            addProperty( "createdAt", "")
            addProperty( "createdById", "")
            addProperty( "deletedAt", "")
            addProperty( "finishTimeInDay", "")
            addProperty( "id", stationId)
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

        val jsonObject1 = JsonObject().apply {
            addProperty( "content", "")
            addProperty( "createdAt", "")
            addProperty( "createdById", "")
            addProperty( "deletedAt", "")
            addProperty( "generatedDate", "")
            addProperty( "id", "")
            addProperty( "importHash", "")
            add("station", stationObj)
            addProperty( "stationId",stationId)
            addProperty( "tenantId","")
            addProperty( "title","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
        }
        val jsonObject2 = JsonObject().apply {
            addProperty( "content", "")
            addProperty( "createdAt", "")
            addProperty( "createdById", "")
            addProperty( "deletedAt", "")
            addProperty( "generatedDate", "")
            addProperty( "id", "")
            addProperty( "importHash", "")
            add("station", stationObj)
            addProperty( "stationId",stationId)
            addProperty( "tenantId","")
            addProperty( "title","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
        }
        val jsonObject3 = JsonObject().apply {
            addProperty( "content", "")
            addProperty( "createdAt", "")
            addProperty( "createdById", "")
            addProperty( "deletedAt", "")
            addProperty( "generatedDate", "")
            addProperty( "id", "")
            addProperty( "importHash", "")
            add("station", stationObj)
            addProperty( "stationId",stationId)
            addProperty( "tenantId","")
            addProperty( "title","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
        }

        val jsonArray = JsonArray().apply {
            add(jsonObject1)
            add(jsonObject2)
            add(jsonObject3)
        }

        val jsonResponse = JsonObject().apply {
            addProperty("count",3)
            add("rows", jsonArray)
        }

        val response = Response.success(jsonResponse)

        coEvery { stationReportsRepository.getReportsByStationRepo(fakeBearerToken, fakeTenantId, stationId, null) } returns response

        val result = stationReportsController.getReportsByStation(fakeBearerToken, fakeTenantId, stationId, null)


        assert(result is Resource.Success)
        var count = 0
        for (k in 0 until jsonArray.size()){
            if( (result as Resource.Success).data!![k].stationId == stationId ){
                count++
            }
        }
        print("result ${result.data}  size ${count}")
        assert( count == jsonArray.size() )


    }


    @Test
    fun `when there is no detail for a given object or the object id went to api was wrong, return error message`()=runBlocking {
        val errorResponse = Response.error<JsonObject>(
            404,
            okhttp3.ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "{\"error\":\"Not found\"}"
            )
        )

        coEvery { stationReportsRepository.getGrdShiftByStationDetRepo(fakeBearerToken, fakeTenantId, "") } returns errorResponse

        val result = stationReportsController.getGrdShiftByStationDetail(fakeBearerToken, fakeTenantId, "")

        assert(result is Resource.Error)
    }


    @Test
    fun `return empty object with fields as null, so the app doesn't crash`()=runBlocking {
        val emptyObject = JsonObject() // Objeto completamente vacío

        val response = Response.success(emptyObject)

        coEvery { stationReportsRepository.getGrdShiftByStationDetRepo(fakeBearerToken, fakeTenantId, "1") } returns response

        val result = stationReportsController.getGrdShiftByStationDetail(fakeBearerToken, fakeTenantId, "1")

        assert(result is Resource.Success)
        // Verifica que los campos tengan valores por defecto
        assert(result.data?.guardName == "")
        assert(result.data?.stationName?.stationName == "")
    }


    @Test
    fun `return the detail object of a list of reports by station`()=runBlocking {
        val reportByStationId = "dfk vlknsdfsD"

        val jsonObject = JsonObject().apply {
            addProperty( "content","")
            addProperty( "createdAt","")
            addProperty( "createdById","")
            addProperty( "deletedAt","")
            addProperty( "generatedDate","")
            addProperty( "id",reportByStationId)
            addProperty( "importHash","")

            val stationObj = JsonObject().apply {
                addProperty( "createdAt","")
                addProperty( "createdById","")
                addProperty( "deletedAt","")
                addProperty( "finishTimeInDay","")
                addProperty( "id","")
                addProperty( "importHash","")
                addProperty( "latitud","")
                addProperty( "longitud","")
                addProperty( "numberOfGuardsInStation",8)
                addProperty( "startingTimeInDay","")
                addProperty( "stationName","")
                addProperty( "stationOriginId","")
                addProperty( "stationSchedule","")
                addProperty( "tenantId","")
                addProperty( "updatedAt","")
                addProperty( "updatedById","")
            }

            add("station", stationObj)
            addProperty( "stationId","")
            addProperty( "tenantId","")
            addProperty( "title","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
        }

        val response = Response.success(jsonObject)

        coEvery { stationReportsRepository.getReportByStationDetRepo(fakeBearerToken, fakeTenantId, reportByStationId) } returns response

        val result = stationReportsController.getReportByStationDetail(fakeBearerToken, fakeTenantId, reportByStationId)

        assert(result is Resource.Success)
        assert( (result as Resource.Success).data!!.id == reportByStationId )

    }


    @Test
    fun `return the incidents that had place in the station within a given range of dates`()=runBlocking {

        val datesList = listOf("2024-01-14T00:00:00.000Z","2024-01-15T23:59:00.000Z")

        val incidentsObj1 =JsonObject().apply {
            addProperty( "createdAt","")
            addProperty( "createdById","")
            addProperty( "date","2024-01-15T23:57:00.000Z")
            addProperty( "deletedAt","")
            addProperty( "description","")
            addProperty( "id","")
            add( "imageUrl",JsonArray())
            addProperty( "importHash","")
            addProperty( "stationIncidents","")
            addProperty( "stationIncidentsId","")
            addProperty( "tenantId","")
            addProperty( "title","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
            addProperty( "wasRead",true)
        }
        val incidentsObj2 =JsonObject().apply {
            addProperty( "createdAt","")
            addProperty( "createdById","")
            addProperty( "date","2024-01-14T23:57:00.000Z")
            addProperty( "deletedAt","")
            addProperty( "description","")
            addProperty( "id","")
            add( "imageUrl",JsonArray())
            addProperty( "importHash","")
            addProperty( "stationIncidents","")
            addProperty( "stationIncidentsId","")
            addProperty( "tenantId","")
            addProperty( "title","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
            addProperty( "wasRead",true)
        }
        val incidentsObj3 =JsonObject().apply {
            addProperty( "createdAt","")
            addProperty( "createdById","")
            addProperty( "date","2024-01-14T13:57:00.000Z")
            addProperty( "deletedAt","")
            addProperty( "description","")
            addProperty( "id","")
            add( "imageUrl",JsonArray())
            addProperty( "importHash","")
            addProperty( "stationIncidents","")
            addProperty( "stationIncidentsId","")
            addProperty( "tenantId","")
            addProperty( "title","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
            addProperty( "wasRead",true)
        }
        val incidentsObj4 =JsonObject().apply {
            addProperty( "createdAt","")
            addProperty( "createdById","")
            addProperty( "date","2024-01-18T23:57:00.000Z")
            addProperty( "deletedAt","")
            addProperty( "description","")
            addProperty( "id","")
            add( "imageUrl",JsonArray())
            addProperty( "importHash","")
            addProperty( "stationIncidents","")
            addProperty( "stationIncidentsId","")
            addProperty( "tenantId","")
            addProperty( "title","")
            addProperty( "updatedAt","")
            addProperty( "updatedById","")
            addProperty( "wasRead",true)
        }

        val jsonArray = JsonArray().apply {
            add(incidentsObj1)
            add(incidentsObj2)
            add(incidentsObj3)
            add(incidentsObj4)
        }

        val jsonResponse = JsonObject().apply {
            addProperty("count",4)
            add("rows", jsonArray)
        }

        val response = Response.success(jsonResponse)


        coEvery { stationReportsRepository.getIncidentsRepo(fakeBearerToken, fakeTenantId, null, datesList) } returns response

        val result = stationReportsController.getIncidents(fakeBearerToken, fakeTenantId, null, datesList)

        assert( result is Resource.Success )
        assert( (result as Resource.Success).data!![2].date <= datesList[1] )
    }


    @Test
    fun `the case when there is no patrols in the selected station`()=runBlocking {
        val jsonResponse = JsonObject().apply {
            addProperty("count", 0)
            add("rows", JsonArray())
        }

        val apiResponse = Response.success<JsonObject>(jsonResponse)


        coEvery { stationReportsRepository.getGuardShiftsByStationRepo(fakeBearerToken, fakeTenantId, null, null) } returns apiResponse

        val result = stationReportsController.getGuardShiftsByStation(fakeBearerToken, fakeTenantId, null, null)

        assert(result is Resource.Success)
        assert( (result as Resource.Success).data!!.count() == 0 )

    }







}


