package com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.StationReportsRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.Guard
import com.seguridadbas.multytenantseguridadbas.model.station.StationsDataResponse
import com.seguridadbas.multytenantseguridadbas.model.stationreports.GuardShiftByStationData
import com.seguridadbas.multytenantseguridadbas.model.stationreports.GuardShiftByStationResponse
import com.seguridadbas.multytenantseguridadbas.model.stationreports.IncidentsByStationData
import com.seguridadbas.multytenantseguridadbas.model.stationreports.IncidentsbyStationResponse
import com.seguridadbas.multytenantseguridadbas.model.stationreports.PatrolByStationData
import com.seguridadbas.multytenantseguridadbas.model.stationreports.PatrolByStationResponse
import com.seguridadbas.multytenantseguridadbas.model.stationreports.ReportsByStationData
import com.seguridadbas.multytenantseguridadbas.model.stationreports.ReportsByStationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class StationReportsController  @Inject constructor(
    private val stationReportsRepository: StationReportsRepository
): ViewModel() {

    suspend fun getGuardShiftsByStation(
        token: String,
        tenantId: String, stationId: String?, guardId: String?
    ): Resource<List<GuardShiftByStationData>>{
        val response = stationReportsRepository.getGuardShiftsByStationRepo(token, tenantId, stationId, guardId)

        return try{

            if(response.isSuccessful && response.body() != null ){
                val jsonBody = response.body()!!
                val guardShiftsByStationResponse = parseGuardShiftsByStationResponse(jsonBody as JsonObject)

                Resource.Success(
                    guardShiftsByStationResponse.rows
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


    suspend fun getReportsByStation(
        token: String,
        tenantId: String, stationId: String?, generatedDateRange: List<String>?
    ): Resource<List<ReportsByStationData>> {
        val response = stationReportsRepository.getReportsByStationRepo(token, tenantId, stationId, generatedDateRange)

        return try{

            if(response.isSuccessful && response.body() != null ){
                val jsonBody = response.body()!!
                val reportsByStation = parseReportsByStationResponse(jsonBody as JsonObject)

                Resource.Success(
                    reportsByStation.rows
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

    suspend fun getIncidents(
        token: String, tenantId: String,
        title: String?, dateRange: List<String>?
    ): Resource<List<IncidentsByStationData>> {
        val response = stationReportsRepository.getIncidentsRepo(token, tenantId, title, dateRange)
        return try{

            if(response.isSuccessful && response.body() != null ){
                val jsonBody = response.body()!!
                val incidentsByStation = parseIncidentsByStationResponse(jsonBody as JsonObject)

                Resource.Success(
                    incidentsByStation.rows
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

    suspend fun getPatrolsByStation(
        token: String, tenantId: String, stationId: String
    ):Resource<List<PatrolByStationData>>  {
        val response = stationReportsRepository.getPatrolsByStationRepo(token, tenantId, stationId)

        return try{

            if(response.isSuccessful && response.body() != null ){
                val jsonBody = response.body()!!
                val patrolsByStation = parsePatrolsByStation(jsonBody as JsonObject)

                Resource.Success(
                    patrolsByStation.rows
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

    private fun parseGuardShiftsByStationResponse(jsonObject: JsonObject): GuardShiftByStationResponse{
        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map{ rowElement ->
            val row = rowElement.asJsonObject
            val stationObj = row.getAsJsonObject("stationName")

            GuardShiftByStationData(
                completeInventoryCheck = row.get("completeInventoryCheck").asBoolean,
                completeInventoryCheckId = row.get("completeInventoryCheckId").asString,
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                dailyIncidents = row.getAsJsonArray("dailyIncidents").toList(),
                deletedAt = row.get("deletedAt").asString,
                guardName = row.get("guardName").asString,
                guardNameId = row.get("guardNameId").asString,
                id = row.get("id").asString,
                importHash = row.get("importHash").asString,
                numberOfIncidentsDurindShift = row.get("numberOfIncidentsDurindShift").asInt,
                numberOfPatrolsDuringShift = row.get("numberOfPatrolsDuringShift").asInt,
                observations = row.get("observations").asString,
                patrolsDone = row.getAsJsonArray("patrolsDone").toList(),
                punchInTime = row.get("punchInTime").asString,
                punchOutTime = row.get("punchOutTime").asString,
                shiftSchedule = row.get("shiftSchedule").asString,
                stationName = StationsDataResponse(
                    assignedGuards = stationObj.getAsJsonArray("assignedGuards").toList(),
                    checkpoints = stationObj.getAsJsonArray("checkpoints").toList(),
                    createdAt = stationObj.get("createdAt").asString,
                    createdById = stationObj.get("createdById").asString,
                    deletedAt = stationObj.get("deletedAt").asString,
                    finishTimeInDay = stationObj.get("finishTimeInDay").asString,
                    id = stationObj.get("id").asString,
                    importHash = stationObj.get("importHash").asString,
                    incidents = stationObj.getAsJsonArray("incidents").toList(),
                    latitud = stationObj.get("latitud").asString,
                    longitud = stationObj.get("longitud").asString,
                    numberOfGuardsInStation = stationObj.get("numberOfGuardsInStation").asString,
                    patrol = stationObj.getAsJsonArray("patrol").toList(),
                    reports = stationObj.getAsJsonArray("reports").toList(),
                    shift = stationObj.getAsJsonArray("shift").toList(),
                    startingTimeInDay = stationObj.get("startingTimeInDay").asString,
                    stationName = stationObj.get("stationName").asString,
                    stationOrigin = stationObj.get("stationOrigin").asString,
                    stationOriginId = stationObj.get("stationOriginId").asString,
                    stationSchedule = stationObj.get("stationSchedule").asString,
                    tasks = stationObj.getAsJsonArray("tasks").toList(),
                    tenantId = stationObj.get("tenantId").asString,
                    updatedAt = stationObj.get("updatedAt").asString,
                    updatedById = stationObj.get("updatedById").asString
                ),
                stationNameId  = row.get("stationNameId").asString,
                tenantId  = row.get("tenantId").asString,
                updatedAt  = row.get("updatedAt").asString,
                updatedById  = row.get("updatedById").asString,
            )
        }


        return GuardShiftByStationResponse(count, rows)

    }


    private fun parseReportsByStationResponse(jsonObject: JsonObject): ReportsByStationResponse {
        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map { rowElement ->

            val row = rowElement.asJsonObject
            val stationObj = row.getAsJsonObject("station")

            ReportsByStationData(
                content = row.get("content").asString,
                        createdAt = row.get("createdAt").asString,
                        createdById = row.get("createdById").asString,
                        deletedAt = row.get("deletedAt").asString,
                        generatedDate = row.get("generatedDate").asString,
                        id = row.get("id").asString,
                        importHash = row.get("importHash").asString,
                        station = StationsDataResponse(
                            assignedGuards = stationObj.getAsJsonArray("assignedGuards").toList(),
                            checkpoints = stationObj.getAsJsonArray("checkpoints").toList(),
                            createdAt = stationObj.get("createdAt").asString,
                            createdById = stationObj.get("createdById").asString,
                            deletedAt = stationObj.get("deletedAt").asString,
                            finishTimeInDay = stationObj.get("finishTimeInDay").asString,
                            id = stationObj.get("id").asString,
                            importHash = stationObj.get("importHash").asString,
                            incidents = stationObj.getAsJsonArray("incidents").toList(),
                            latitud = stationObj.get("latitud").asString,
                            longitud = stationObj.get("longitud").asString,
                            numberOfGuardsInStation = stationObj.get("numberOfGuardsInStation").asString,
                            patrol = stationObj.getAsJsonArray("patrol").toList(),
                            reports = stationObj.getAsJsonArray("reports").toList(),
                            shift = stationObj.getAsJsonArray("shift").toList(),
                            startingTimeInDay = stationObj.get("startingTimeInDay").asString,
                            stationName = stationObj.get("stationName").asString,
                            stationOrigin = stationObj.get("stationOrigin").asString,
                            stationOriginId = stationObj.get("stationOriginId").asString,
                            stationSchedule = stationObj.get("stationSchedule").asString,
                            tasks = stationObj.getAsJsonArray("tasks").toList(),
                            tenantId = stationObj.get("tenantId").asString,
                            updatedAt = stationObj.get("updatedAt").asString,
                            updatedById = stationObj.get("updatedById").asString
                        ),
                        stationId = row.get("stationId").asString,
                        tenantId = row.get("tenantId").asString,
                        title = row.get("title").asString,
                        updatedAt = row.get("updatedAt").asString,
                        updatedById = row.get("updatedById").asString,
            )

        }

        return ReportsByStationResponse(count, rows)

    }


    private fun parseIncidentsByStationResponse(jsonObject: JsonObject): IncidentsbyStationResponse {

        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray


        val rows = rowsArray.map{ rowElement ->
            val row = rowElement.asJsonObject

            IncidentsByStationData(
                createdAt = row.get("createdAt").asString,
                        createdById = row.get("createdById").asString,
                        date = row.get("date").asString,
                        deletedAt = row.get("deletedAt").asString,
                        description = row.get("description").asString,
                        id = row.get("id").asString,
                        imageUrl = row.getAsJsonArray("imageUrl").toList(),
                        importHash = row.get("importHash").asString,
                        stationIncidents = row.get("stationIncidents").asString,
                        stationIncidentsId = row.get("stationIncidentsId").asString,
                        tenantId = row.get("tenantId").asString,
                        title = row.get("title").asString,
                        updatedAt = row.get("updatedAt").asString,
                        updatedById = row.get("updatedById").asString,
                        wasRead = row.get("wasRead").asBoolean,
            )
        }


        return IncidentsbyStationResponse(count, rows)
    }


    private fun parsePatrolsByStation(jsonObject: JsonObject): PatrolByStationResponse{
        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map { rowElement ->
            val row = rowElement.asJsonObject
            val stationObj = row.getAsJsonObject("station")
            val guardObj = row.getAsJsonObject("assignedGuard")

            PatrolByStationData(
                assignedGuard = Guard(
                    email = guardObj.get("email").asString,
                    firstName = guardObj.get("firstName").asString,
                    id = guardObj.get("id").asString,
                    lastName = guardObj.get("lastName").asString
                ),
                assignedGuardId = row.get("assignedGuardId").asString,
                checkpoints = row.getAsJsonArray("checkpoints").toList(),
                completed = row.get("completed").asBoolean,
                completionTime = row.get("completionTime").asString,
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                deletedAt = row.get("deletedAt").asString,
                id = row.get("id").asString,
                importHash = row.get("importHash").asString,
                logs = row.getAsJsonArray("logs").toList(),
                scheduledTime = row.get("scheduledTime").asString,
                station = StationsDataResponse(
                    assignedGuards = stationObj.getAsJsonArray("assignedGuards").toList(),
                    checkpoints = stationObj.getAsJsonArray("checkpoints").toList(),
                    createdAt = stationObj.get("createdAt").asString,
                    createdById = stationObj.get("createdById").asString,
                    deletedAt = stationObj.get("deletedAt").asString,
                    finishTimeInDay = stationObj.get("finishTimeInDay").asString,
                    id = stationObj.get("id").asString,
                    importHash = stationObj.get("importHash").asString,
                    incidents = stationObj.getAsJsonArray("incidents").toList(),
                    latitud = stationObj.get("latitud").asString,
                    longitud = stationObj.get("longitud").asString,
                    numberOfGuardsInStation = stationObj.get("numberOfGuardsInStation").asString,
                    patrol = stationObj.getAsJsonArray("patrol").toList(),
                    reports = stationObj.getAsJsonArray("reports").toList(),
                    shift = stationObj.getAsJsonArray("shift").toList(),
                    startingTimeInDay = stationObj.get("startingTimeInDay").asString,
                    stationName = stationObj.get("stationName").asString,
                    stationOrigin = stationObj.get("stationOrigin").asString,
                    stationOriginId = stationObj.get("stationOriginId").asString,
                    stationSchedule = stationObj.get("stationSchedule").asString,
                    tasks = stationObj.getAsJsonArray("tasks").toList(),
                    tenantId = stationObj.get("tenantId").asString,
                    updatedAt = stationObj.get("updatedAt").asString,
                    updatedById = stationObj.get("updatedById").asString
                ),
                stationId = row.get("stationId").asString,
                status = row.get("status").asString,
                tenantId = row.get("tenantId").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.get("updatedById").asString
            )
        }

        return PatrolByStationResponse(count, rows)
    }


}