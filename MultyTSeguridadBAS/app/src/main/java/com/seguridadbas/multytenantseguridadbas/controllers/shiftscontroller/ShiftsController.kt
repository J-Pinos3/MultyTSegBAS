package com.seguridadbas.multytenantseguridadbas.controllers.shiftscontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.ShiftsRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.shifts.AllShiftsResponse
import com.seguridadbas.multytenantseguridadbas.model.shifts.Guard
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


    suspend fun getAllShifts(token: String, tenantId: String,
       limit: Int, offset: Int, orderBy: String? = ""): Resource<List<ShortShiftData>> {

        val response = shiftsRepository.getAllShiftsRepo(token, tenantId, limit, offset, orderBy)

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
                            stationSchedule = it.station.stationSchedule
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


    private fun parseAllShiftsResponse(jsonObject: JsonObject): AllShiftsResponse {

        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map{ rowElement ->
            val row = rowElement.asJsonObject
            val guardObj = row.getAsJsonObject("guard")
            val stationObj = row.getAsJsonObject("station")


            ShiftsDataResponse(
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                deletedAt = row.get("deletedAt").asString,
                endTime = row.get("endTime").asString,
                guard = Guard(
                    email  = guardObj.get("email").asString,
                    firstName = guardObj.get("firstName").asString,
                    id = guardObj.get("id").asString,
                    lastName = guardObj.get("lastName").asString
                ),
                guardId = row.get("guardId").asString,
                id = row.get("id").asString,
                importHash = row.get("importHash").asString,
                startTime = row.get("startTime").asString,
                station = Station(
                    createdAt = stationObj.get("createdAt").asString,
                    createdById = stationObj.get("createdById").asString,
                    deletedAt = stationObj.get("deletedAt").asString,
                    finishTimeInDay = stationObj.get("finishTimeInDay").asString,
                    id = stationObj.get("id").asString,
                    importHash = stationObj.get("importHash").asString,
                    latitud = stationObj.get("latitud").asString,
                    longitud = stationObj.get("longitud").asString,
                    numberOfGuardsInStation = stationObj.get("numberOfGuardsInStation").asString,
                    startingTimeInDay = stationObj.get("startingTimeInDay").asString,
                    stationName = stationObj.get("stationName").asString,
                    stationOriginId = stationObj.get("stationOriginId").asString,
                    stationSchedule = stationObj.get("stationSchedule").asString,
                    tenantId = stationObj.get("tenantId").asString,
                    updatedAt = stationObj.get("updatedAt").asString,
                    updatedById = stationObj.get("updatedById").asString
                ),
                stationId = row.get("stationId").asString,
                tenantId = row.get("tenantId").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.get("updatedById").asString,
            )
        }

        return AllShiftsResponse(count, rows)

    }

}