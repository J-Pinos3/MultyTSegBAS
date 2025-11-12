package com.seguridadbas.multytenantseguridadbas.controllers.stationscontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.StationsRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.shifts.ShortShiftData
import com.seguridadbas.multytenantseguridadbas.model.station.AllStationsResponse
import com.seguridadbas.multytenantseguridadbas.model.station.ShortStation
import com.seguridadbas.multytenantseguridadbas.model.station.StationData
import com.seguridadbas.multytenantseguridadbas.model.station.StationsDataResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.JsonElement
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class StationsController @Inject constructor(
    private val stationsRepository: StationsRepository
): ViewModel() {

    suspend fun getAllStations(token:String, tenantId: String): Resource<List<ShortStation>>{
        val response = stationsRepository.getAllStationsRepo(token, tenantId)

        return try{


            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!
                val stationsResponse = parseAllStationsResponse(jsonBody as JsonObject)

                Resource.Success(
                    stationsResponse.rows.map {
                        ShortStation(
                            stationId = it.id,
                            stationName = it.stationName,
                            stationSchedule = it.stationSchedule
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


    suspend fun getStationDetail(token: String, tenantId: String, id: String): Resource<StationData>{
        val response = stationsRepository.stationDetailRepo(token, tenantId, id)

        return try{


            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!

                Resource.Success(

                    StationData(
                assignedGuards = jsonBody.getAsJsonArray("assignedGuards").toList(),
                        finishTimeInDay = jsonBody.get("finishTimeInDay").asString ?: "",
                        id = jsonBody.get("id").asString ?: "",
                        incidents = jsonBody.getAsJsonArray("incidents").toList(),
                        latitude = jsonBody.get("latitud").asString,
                        longitude = jsonBody.get("longitud").asString,
                        stationName = jsonBody.get("stationName").asString,
                        stationSchedule = jsonBody.get("stationSchedule").asString,
                        tasks = jsonBody.getAsJsonArray("tasks").toList(),
                        numberOfGuardsInStation = jsonBody.get("numberOfGuardsInStation").asInt
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

    private fun parseAllStationsResponse(jsonObject: JsonObject): AllStationsResponse{


        val count =jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map{ rowElement ->

            val row = rowElement.asJsonObject

            StationsDataResponse(
                assignedGuards = row.getAsJsonArray("assignedGuards").toList(),
                checkpoints = row.getAsJsonArray("checkpoints").toList(),
                createdAt = row.get("createdAt").asString,
                createdById = row.get("createdById").asString,
                deletedAt =  if( !row.get("deletedAt").isJsonNull  ) row.get("deletedAt").asString else "" ,
                finishTimeInDay = row.get("finishTimeInDay").asString,
                id = row.get("id").asString,
                importHash = if( !row.get("importHash").isJsonNull  ) row.get("importHash").asString else "",
                incidents = row.getAsJsonArray("incidents").toList(),
                latitud = row.get("latitud").asString,
                longitud = row.get("longitud").asString,
                numberOfGuardsInStation = row.get("numberOfGuardsInStation").asString,
                patrol = row.getAsJsonArray("patrol").toList(),
                reports = row.getAsJsonArray("reports").toList(),
                shift = row.getAsJsonArray("shift").toList(),
                startingTimeInDay = row.get("startingTimeInDay").asString,
                stationName = row.get("stationName").asString,
                stationOrigin = row.isNullField("stationOrigin"),
                stationOriginId = row.isNullField("stationOriginId"),
                stationSchedule = row.get("stationSchedule").asString,
                tasks = row.getAsJsonArray("tasks").toList(),
                tenantId = row.get("tenantId").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.isNullField("updatedById")
            )

        }

        return AllStationsResponse(count, rows)
    }


    private fun JsonObject?.isNullField(field: String):String{
        return if(this?.get(field)?.isJsonNull == false) this.get(field).asString  else ""
    }

}