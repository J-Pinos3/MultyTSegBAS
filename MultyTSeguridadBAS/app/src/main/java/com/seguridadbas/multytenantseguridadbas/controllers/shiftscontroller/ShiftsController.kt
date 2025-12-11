package com.seguridadbas.multytenantseguridadbas.controllers.shiftscontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.ShiftsRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.shifts.AllShiftsResponse
import com.seguridadbas.multytenantseguridadbas.model.shifts.Guard
import com.seguridadbas.multytenantseguridadbas.model.shifts.ShiftsData
import com.seguridadbas.multytenantseguridadbas.model.shifts.ShiftsDataResponse
import com.seguridadbas.multytenantseguridadbas.model.shifts.ShortShiftData
import com.seguridadbas.multytenantseguridadbas.model.shifts.Station
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class ShiftsController @Inject constructor(
    private val shiftsRepository: ShiftsRepository
): ViewModel() {


    suspend fun getAllShifts(
        token: String,
        tenantId: String,
        startTimeRange: List<String>?
    ): Resource<List<ShortShiftData>> {

        val response = shiftsRepository.getAllShiftsRepo(token, tenantId,  startTimeRange)

        return try{

            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!
                val shiftsResponse = parseAllShiftsResponse(jsonBody as JsonObject)

                Resource.Success(
                    shiftsResponse.rows.map {
                        ShortShiftData(
                            guardName = "${it.guard.firstName} ${it.guard.lastName}",
                            stationName = it.station.stationName,
                            id = it.id,
                            tenantId = it.tenantId,
                            stationSchedule = it.station.stationSchedule,
                            startDate = it.startTime
                        )
                    }
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


    suspend fun getShiftDetail(token: String, tenantId: String, id: String): Resource<ShiftsData>{
        val response = shiftsRepository.shiftDetailRepo(token, tenantId, id)


        return try{

            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!
                val guardObj = jsonBody.getAsJsonObject("guard")
                val stationObj = jsonBody.getAsJsonObject("station")


                Resource.Success(
                    ShiftsData(
                        endTime = jsonBody.get("endTime").asString,
                        startTime = jsonBody.get("startTime").asString,
                        guardEmail = guardObj.get("email").asString,
                        guardFirstName = guardObj.isNullStringField("firstName"),
                        guardId = guardObj.get("id").asString,
                        guardLastName = guardObj.isNullStringField("lastName"),
                        shiftId = jsonBody.get("id").asString,
                        finishTimeInDay = stationObj.get("finishTimeInDay").asString,
                        latitude = stationObj.get("latitud").asString,
                        longitude = stationObj.get("longitud").asString,
                        numberOfGuardsInStation = stationObj.get("numberOfGuardsInStation").asString,
                        startingTimeInDay = stationObj.get("startingTimeInDay").asString,
                        stationName = stationObj.get("stationName").asString,
                        stationSchedule = stationObj.get("stationSchedule").asString,
                        tenantId = jsonBody.get("tenantId").asString
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

    private fun parseAllShiftsResponse(jsonObject: JsonObject): AllShiftsResponse {

        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map{ rowElement ->
            val row = rowElement.asJsonObject
            val guardObj = row.getAsJsonObject("guard")
            val stationObj = row.getAsJsonObject("station")


            ShiftsDataResponse(
                createdAt = row.get("createdAt").asString,
                createdById = row.isNullStringField("createdById"),
                deletedAt = row.isNullStringField("deletedAt"),
                endTime = row.get("endTime").asString,
                guard = Guard(
                    email  = guardObj.get("email").asString,
                    firstName = guardObj.get("firstName").asString,
                    id = guardObj.get("id").asString,
                    lastName = guardObj.isNullStringField("lastName")
                ),
                guardId = row.get("guardId").asString,
                id = row.get("id").asString,
                importHash = row.isNullStringField("importHash"),
                startTime = row.get("startTime").asString,
                station = Station(
                    createdAt = stationObj.get("createdAt").asString,
                    createdById = stationObj.isNullStringField("createdById"),
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
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.isNullStringField("updatedById"),
            )
        }

        return AllShiftsResponse(count, rows)

    }

    private fun JsonObject?.isNullStringField(field: String): String{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asString else ""
    }

    private fun JsonObject?.isNullBooleanField(field: String): Boolean{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asBoolean else false
    }

}