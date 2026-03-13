package com.seguridadbas.multytenantseguridadbas.controllers.stationreportscontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonArray
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
import com.seguridadbas.multytenantseguridadbas.model.stationreports.InventoryByStationData
import com.seguridadbas.multytenantseguridadbas.model.stationreports.InventoryByStationResponse
import com.seguridadbas.multytenantseguridadbas.model.stationreports.PatrolByStationData
import com.seguridadbas.multytenantseguridadbas.model.stationreports.PatrolByStationResponse
import com.seguridadbas.multytenantseguridadbas.model.stationreports.ReportsByStationData
import com.seguridadbas.multytenantseguridadbas.model.stationreports.ReportsByStationResponse
import com.seguridadbas.multytenantseguridadbas.model.stationreports.StationObj
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


    suspend fun getGrdShiftByStationDetail(
        token: String, tenantId:String, id: String
    ): Resource<GuardShiftByStationData>{
        val response = stationReportsRepository.getGrdShiftByStationDetRepo( token, tenantId, id)

        return try {

            if(response.isSuccessful && response.body() != null){
                val jsonBody = response.body()!!
                val stationObj = jsonBody.getAsJsonObject("stationName")

                Resource.Success(
                    GuardShiftByStationData(
                        completeInventoryCheck = jsonBody.isNullBooleanField("completeInventoryCheck"),
                        completeInventoryCheckId = jsonBody.isNullStringField("completeInventoryCheckId"),
                        createdAt = jsonBody.isNullStringField("createdAt"),
                        createdById = jsonBody.isNullStringField("createdById"),

                        dailyIncidents = jsonBody.isNullArrayField("dailyIncidents"),
                        deletedAt = jsonBody.isNullStringField("deletedAt"),
                        guardName = jsonBody.isNullStringField("guardName"),
                        guardNameId = jsonBody.isNullStringField("guardNameId"),
                        id = jsonBody.isNullStringField("id"),
                        importHash = jsonBody.isNullStringField("importHash"),
                        numberOfIncidentsDurindShift = jsonBody.isNullIntField("numberOfIncidentsDurindShift"),
                        numberOfPatrolsDuringShift = jsonBody.isNullIntField("numberOfPatrolsDuringShift"),
                        observations = jsonBody.isNullStringField("observations"),

                        patrolsDone = jsonBody.isNullArrayField("patrolsDone"),
                        punchInTime = jsonBody.isNullStringField("punchInTime"),
                        punchOutTime = jsonBody.isNullStringField("punchOutTime"),
                        shiftSchedule = jsonBody.isNullStringField("shiftSchedule"),
                        stationName = StationObj(
                            createdAt = stationObj.isNullStringField("createdAt"),
                            createdById = stationObj.isNullStringField("createdById"),
                            deletedAt = stationObj.isNullStringField("deletedAt"),
                            finishTimeInDay = stationObj.isNullStringField("finishTimeInDay"),
                            id = stationObj.isNullStringField("id"),
                            importHash = stationObj.isNullStringField("importHash"),
                            latitud = stationObj.isNullStringField("latitud"),
                            longitud = stationObj.isNullStringField("longitud"),
                            numberOfGuardsInStation = stationObj.isNullStringField("numberOfGuardsInStation"),
                            startingTimeInDay = stationObj.isNullStringField("startingTimeInDay"),
                            stationName = stationObj.isNullStringField("stationName"),
                            stationOriginId = stationObj.isNullStringField("stationOriginId"),
                            stationSchedule = stationObj.isNullStringField("stationSchedule"),
                            tenantId = stationObj.isNullStringField("tenantId"),
                            updatedAt = stationObj.isNullStringField("updatedAt"),
                            updatedById = stationObj.isNullStringField("updatedById")
                        ),
                        stationNameId  = jsonBody.isNullStringField("stationNameId"),
                        tenantId  = jsonBody.isNullStringField("tenantId"),
                        updatedAt  = jsonBody.isNullStringField("updatedAt"),
                        updatedById  = jsonBody.isNullStringField("updatedById"),
                    )
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


    suspend fun getReportByStationDetail(
        token: String, tenantId: String, id: String
    ): Resource<ReportsByStationData>{
        val response = stationReportsRepository.getReportByStationDetRepo( token,   tenantId,   id )

        return try {

            if( response.isSuccessful && response.body() != null ){
                val jsonBody = response.body()!!
                val stationObj = jsonBody.getAsJsonObject("station")

                Resource.Success(

                    ReportsByStationData(
                        content = jsonBody.get("content").asString,
                        createdAt = jsonBody.get("createdAt").asString,
                        createdById = jsonBody.get("createdById").asString,
                        deletedAt = jsonBody.isNullStringField("deletedAt"),
                        generatedDate = jsonBody.get("generatedDate").asString,
                        id = jsonBody.get("id").asString,
                        importHash = jsonBody.isNullStringField("importHash"),
                        station = StationObj(
                            createdAt = stationObj.get("createdAt").asString,
                            createdById = stationObj.get("createdById").asString,
                            deletedAt = stationObj.isNullStringField("deletedAt"),
                            finishTimeInDay = stationObj.get("finishTimeInDay").asString,
                            id = stationObj.get("id").asString,
                            importHash = stationObj.isNullStringField("importHash"),
                            latitud = stationObj.get("latitud").asString,
                            longitud = stationObj.get("longitud").asString,
                            numberOfGuardsInStation = stationObj.get("numberOfGuardsInStation").asString,
                            startingTimeInDay = stationObj.get("startingTimeInDay").asString,
                            stationName = stationObj.get("stationName").asString,
                            stationOriginId = stationObj.isNullStringField("stationOriginId"),
                            stationSchedule = stationObj.get("stationSchedule").asString,
                            tenantId = stationObj.get("tenantId").asString,
                            updatedAt = stationObj.get("updatedAt").asString,
                            updatedById = stationObj.isNullStringField("updatedById")
                        ),
                        stationId = jsonBody.get("stationId").asString,
                        tenantId = jsonBody.get("tenantId").asString,
                        title = jsonBody.get("title").asString,
                        updatedAt = jsonBody.get("updatedAt").asString,
                        updatedById = jsonBody.isNullStringField("updatedById"),
                    )

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


    suspend fun getInventoryByStation(
        token: String, tenantId: String, stationName: String?
    ): Resource<List<InventoryByStationData>> {
        val response = stationReportsRepository.getInventoryByStationRepo(token, tenantId, stationName)

        return try{

            if(response.isSuccessful && response.body() != null ){
                val jsonBody = response.body()!!
                val inventory = parseInventoryByStationResponse(jsonBody as JsonObject)

                Resource.Success(
                    inventory.rows
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



    suspend fun getInventoryByStationDetail(
        token: String, tenantId: String, id: String
    ):Resource<InventoryByStationData> {

        val response  = stationReportsRepository.getInventoryByStationDetRepo(token, tenantId, id)

        return try{

            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!

                Resource.Success(
                    InventoryByStationData(
                        armor = jsonBody.get("armor").asBoolean,
                        armorSerialNumber = jsonBody.get("armorSerialNumber").asString,
                        armorType = jsonBody.get("armorType").asString,
                        belongsTo = jsonBody.isNullStringField("belongsTo"),
                        belongsToId = jsonBody.isNullStringField("belongsToId"),
                        belongsToStation = jsonBody.isNullStringField("belongsToStation"),
                        caseta = jsonBody.get("caseta").asBoolean,
                        cintoCompleto = jsonBody.get("cintoCompleto").asBoolean,
                        createdAt = jsonBody.get("createdAt").asString,
                        createdById = jsonBody.get("createdById").asString,
                        deletedAt = jsonBody.isNullStringField("deletedAt"),
                        detectorDeMetales = jsonBody.get("detectorDeMetales").asBoolean,
                        gun = jsonBody.get("gun").asBoolean,
                        gunSerialNumber = jsonBody.get("gunSerialNumber").asString,
                        gunType = jsonBody.get("gunType").asString,
                        id = jsonBody.get("id").asString,
                        importHash = jsonBody.isNullStringField("importHash"),
                        linterna = jsonBody.get("linterna").asBoolean,
                        observations = jsonBody.get("observations").asString,
                        pito = jsonBody.get("pito").asBoolean,
                        ponchoDeAguas = jsonBody.get("ponchoDeAguas").asBoolean,
                        radio = jsonBody.get("radio").asBoolean,
                        radioSerialNumber = jsonBody.get("radioSerialNumber").asString,
                        radioType = jsonBody.get("radioType").asString,
                        tenantId = jsonBody.get("tenantId").asString,
                        tolete = jsonBody.get("tolete").asBoolean,
                        transportation = jsonBody.get("transportation").asString,
                        updatedAt = jsonBody.get("updatedAt").asString,
                        updatedById = jsonBody.isNullStringField("updatedById"),
                        vitacora = jsonBody.get("vitacora").asBoolean
                    )
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
                completeInventoryCheck = row.isNullBooleanField("completeInventoryCheck"),
                completeInventoryCheckId = row.isNullStringField("completeInventoryCheckId"),
                createdAt = row.get("createdAt").asString,
                createdById = row.isNullStringField("createdById"),
                dailyIncidents = row.getAsJsonArray("dailyIncidents").toList(),
                deletedAt = row.isNullStringField("deletedAt"),
                guardName = row.isNullStringField("guardName"),
                guardNameId = row.isNullStringField("guardNameId"),
                id = row.isNullStringField("id"),
                importHash = row.isNullStringField("importHash"),
                numberOfIncidentsDurindShift = row.get("numberOfIncidentsDurindShift").asInt,
                numberOfPatrolsDuringShift = row.get("numberOfPatrolsDuringShift").asInt,
                observations = row.get("observations").asString,
                patrolsDone = row.getAsJsonArray("patrolsDone").toList(),
                punchInTime = row.get("punchInTime").asString,
                punchOutTime = row.get("punchOutTime").asString,
                shiftSchedule = row.get("shiftSchedule").asString,
                stationName = StationObj(
                    createdAt = stationObj.get("createdAt").asString,
                    createdById = stationObj.get("createdById").asString,
                    deletedAt = stationObj.isNullStringField("deletedAt"),
                    finishTimeInDay = stationObj.get("finishTimeInDay").asString,
                    id = stationObj.get("id").asString,
                    importHash = stationObj.isNullStringField("importHash"),
                    latitud = stationObj.get("latitud").asString,
                    longitud = stationObj.get("longitud").asString,
                    numberOfGuardsInStation = stationObj.get("numberOfGuardsInStation").asString,
                    startingTimeInDay = stationObj.get("startingTimeInDay").asString,
                    stationName = stationObj.get("stationName").asString,
                    stationOriginId = stationObj.isNullStringField("stationOriginId"),
                    stationSchedule = stationObj.get("stationSchedule").asString,
                    tenantId = stationObj.get("tenantId").asString,
                    updatedAt = stationObj.get("updatedAt").asString,
                    updatedById = stationObj.isNullStringField("updatedById")
                ),
                stationNameId  = row.get("stationNameId").asString,
                tenantId  = row.get("tenantId").asString,
                updatedAt  = row.get("updatedAt").asString,
                updatedById  = row.isNullStringField("updatedById"),
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
                        deletedAt = row.isNullStringField("deletedAt"),
                        generatedDate = row.get("generatedDate").asString,
                        id = row.get("id").asString,
                        importHash = row.isNullStringField("importHash"),
                        station = StationObj(
                            createdAt = stationObj.get("createdAt").asString,
                            createdById = stationObj.get("createdById").asString,
                            deletedAt = stationObj.isNullStringField("deletedAt"),
                            finishTimeInDay = stationObj.get("finishTimeInDay").asString,
                            id = stationObj.get("id").asString,
                            importHash = stationObj.isNullStringField("importHash"),
                            latitud = stationObj.get("latitud").asString,
                            longitud = stationObj.get("longitud").asString,
                            numberOfGuardsInStation = stationObj.get("numberOfGuardsInStation").asString,
                            startingTimeInDay = stationObj.get("startingTimeInDay").asString,
                            stationName = stationObj.get("stationName").asString,
                            stationOriginId = stationObj.isNullStringField("stationOriginId"),
                            stationSchedule = stationObj.get("stationSchedule").asString,
                            tenantId = stationObj.get("tenantId").asString,
                            updatedAt = stationObj.get("updatedAt").asString,
                            updatedById = stationObj.isNullStringField("updatedById")
                        ),
                        stationId = row.get("stationId").asString,
                        tenantId = row.get("tenantId").asString,
                        title = row.get("title").asString,
                        updatedAt = row.get("updatedAt").asString,
                        updatedById = row.isNullStringField("updatedById"),
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
                        deletedAt = row.isNullStringField("deletedAt"),
                        description = row.get("description").asString,
                        id = row.get("id").asString,
                        imageUrl = row.getAsJsonArray("imageUrl").toList(),
                        importHash = row.isNullStringField("importHash"),
                        stationIncidents = row.isNullStringField("stationIncidents"),
                        stationIncidentsId = row.isNullStringField("stationIncidentsId"),
                        tenantId = row.get("tenantId").asString,
                        title = row.get("title").asString,
                        updatedAt = row.get("updatedAt").asString,
                        updatedById = row.isNullStringField("updatedById"),
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
                    id = guardObj.isNullStringField("id"),
                    lastName = guardObj.isNullStringField("lastName")
                ),
                assignedGuardId = row.get("assignedGuardId").asString,
                checkpoints = row.getAsJsonArray("checkpoints").toList(),
                completed = row.get("completed").asBoolean,
                completionTime = row.isNullStringField("completionTime"),
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                deletedAt = row.isNullStringField("deletedAt"),
                id = row.get("id").asString,
                importHash = row.isNullStringField("importHash"),
                logs = row.getAsJsonArray("logs").toList(),
                scheduledTime = row.get("scheduledTime").asString,
                station = StationObj(
                    createdAt = stationObj.get("createdAt").asString,
                    createdById = stationObj.get("createdById").asString,
                    deletedAt = stationObj.isNullStringField("deletedAt"),
                    finishTimeInDay = stationObj.get("finishTimeInDay").asString,
                    id = stationObj.get("id").asString,
                    importHash = stationObj.isNullStringField("importHash"),
                    latitud = stationObj.get("latitud").asString,
                    longitud = stationObj.get("longitud").asString,
                    numberOfGuardsInStation = stationObj.get("numberOfGuardsInStation").asString,
                    startingTimeInDay = stationObj.get("startingTimeInDay").asString,
                    stationName = stationObj.get("stationName").asString,
                    stationOriginId = stationObj.isNullStringField("stationOriginId"),
                    stationSchedule = stationObj.get("stationSchedule").asString,
                    tenantId = stationObj.get("tenantId").asString,
                    updatedAt = stationObj.get("updatedAt").asString,
                    updatedById = stationObj.isNullStringField("updatedById")
                ),
                stationId = row.get("stationId").asString,
                status = row.get("status").asString,
                tenantId = row.get("tenantId").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.isNullStringField("updatedById")
            )
        }

        return PatrolByStationResponse(count, rows)
    }


    private fun parseInventoryByStationResponse(jsonObject: JsonObject): InventoryByStationResponse {
        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map { rowElement ->
            val row = rowElement.asJsonObject
            InventoryByStationData(
                armor = row.get("armor").asBoolean,
                armorSerialNumber = row.get("armorSerialNumber").asString,
                armorType = row.get("armorType").asString,
                belongsTo = row.isNullStringField("belongsTo"),
                belongsToId = row.isNullStringField("belongsToId"),
                belongsToStation = row.isNullStringField("belongsToStation"),
                caseta = row.get("caseta").asBoolean,
                cintoCompleto = row.get("cintoCompleto").asBoolean,
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                deletedAt = row.isNullStringField("deletedAt"),
                detectorDeMetales = row.get("detectorDeMetales").asBoolean,
                gun = row.get("gun").asBoolean,
                gunSerialNumber = row.get("gunSerialNumber").asString,
                gunType = row.get("gunType").asString,
                id = row.get("id").asString,
                importHash = row.isNullStringField("importHash"),
                linterna = row.get("linterna").asBoolean,
                observations = row.get("observations").asString,
                pito = row.get("pito").asBoolean,
                ponchoDeAguas = row.get("ponchoDeAguas").asBoolean,
                radio = row.get("radio").asBoolean,
                radioSerialNumber = row.get("radioSerialNumber").asString,
                radioType = row.get("radioType").asString,
                tenantId = row.get("tenantId").asString,
                tolete = row.get("tolete").asBoolean,
                transportation = row.get("transportation").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.isNullStringField("updatedById"),
                vitacora = row.get("vitacora").asBoolean
            )
        }

        return InventoryByStationResponse(count, rows)
    }


    private fun JsonObject?.isNullStringField(field: String): String{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asString else ""
    }

    private fun JsonObject?.isNullBooleanField(field: String): Boolean{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asBoolean else false
    }


    private fun JsonObject?.isNullArrayField(field: String): List<Any>{
        return if(this?.get(field)?.isJsonNull == false) this.asJsonArray.toList() else emptyList()
    }

    private fun JsonObject?.isNullIntField(field: String): Int{
        return if( this?.get(field)?.isJsonNull == false ) this.get(field).asInt else 0
    }
}