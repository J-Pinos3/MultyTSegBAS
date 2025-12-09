package com.seguridadbas.multytenantseguridadbas.controllers.guardshiftscontroller

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.network.NoNetworkException
import com.seguridadbas.multytenantseguridadbas.controllers.repository.GuardShiftsRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.guard_shifts.AllGuardShiftsResponse
import com.seguridadbas.multytenantseguridadbas.model.guard_shifts.GuardShift
import com.seguridadbas.multytenantseguridadbas.model.guard_shifts.GuardShiftsDataResponse
import com.seguridadbas.multytenantseguridadbas.model.guard_shifts.ShortGuardShift
import com.seguridadbas.multytenantseguridadbas.model.guard_shifts.StationName
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class GuardShiftsController @Inject constructor(
    private val guardShiftsRepository: GuardShiftsRepository
): ViewModel() {


    suspend fun getGuardShifts(
        token: String,
        tenantId: String,
    ): Resource<List<ShortGuardShift>>{
        val response = guardShiftsRepository.getAllGuardShiftsRepo(token, tenantId)

        return try{


            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!
                val guardShiftsResponse = parseAllGuardShiftsResponse(jsonBody as JsonObject)

                Resource.Success(
                    guardShiftsResponse.rows.map {
                        ShortGuardShift(

                            guardName = it.guardName ?: "",
                            id = it.id ,
                            numberOfIncidents = it.numberOfIncidentsDurindShift,
                            numberOfPatrols = it.numberOfPatrolsDuringShift,
                            tenantId = it.tenantId,
                            stationName = it.stationName.stationName
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



     suspend fun getGuardShiftsDetail(token: String, tenantId: String, id: String): Resource<GuardShift>{
        val response = guardShiftsRepository.getShiftsByGuardRepo(token, tenantId, id)

         return try {

             if(response.isSuccessful){
                 val guardShift = response.body()
                 Resource.Success(

                     GuardShift(
                         completeInventoryCheck = guardShift.isNullBooleanField("completeInventoryCheck"),
                         dailyIncidents = guardShift?.getAsJsonArray("dailyIncidents")?.toList() ?: emptyList(),
                         guardName = guardShift.isNullStringField("guardName"),
                         guardNameId = guardShift.isNullStringField("guardNameId"),
                         id = guardShift.isNullStringField("id"),
                         numberOfIncidentsDuringShift = guardShift?.get("numberOfIncidentsDurindShift")?.asInt ?: 0,
                         numberOfPatrolsDuringShift = guardShift?.get("numberOfPatrolsDuringShift")?.asInt ?: 0,
                         observations = guardShift?.get("observations")?.asString ?: "",
                         patrolsDone = guardShift?.getAsJsonArray("patrolsDone")?.toList() ?: emptyList(),
                         punchInTime = guardShift?.get("punchInTime")?.asString ?: "",
                         punchOutTime = guardShift?.get("punchOutTime")?.asString ?: "",
                         shiftSchedule = guardShift?.get("shiftSchedule")?.asString ?: "",
                         tenantId = guardShift?.get("tenantId")?.asString ?: "",
                         latitude = guardShift?.get("stationName")?.asJsonObject?.get("latitud")?.asString ?: "",
                         longitude = guardShift?.get("stationName")?.asJsonObject?.get("longitud")?.asString ?: "",
                         numberOfGuardsInStation = guardShift?.get("stationName")?.asJsonObject?.get("numberOfGuardsInStation")?.asInt ?: 0,
                         stationName = guardShift?.get("stationName")?.asJsonObject?.get("stationName")?.asString ?: "",

                         startingTimeInDay = guardShift?.get("stationName")?.asJsonObject?.get("startingTimeInDay")?.asString ?: "",
                         finishTimeInDay = guardShift?.get("stationName")?.asJsonObject?.get("finishTimeInDay")?.asString ?: "",

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



    private fun parseAllGuardShiftsResponse(jsonObject: JsonObject): AllGuardShiftsResponse{

        val count = jsonObject.get("count").asInt
        val rowsArray = jsonObject.get("rows").asJsonArray

        val rows = rowsArray.map{ rowElement ->
            val row = rowElement.asJsonObject
            val stationObj = row.getAsJsonObject("stationName")

            GuardShiftsDataResponse(
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
                stationName = StationName(
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
                stationNameId = row.get("stationNameId").asString,
                tenantId = row.get("tenantId").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.isNullStringField("updatedById")
            )

        }
        return AllGuardShiftsResponse(count, rows)

    }

    private fun JsonObject?.isNullStringField(field: String): String{
        return if( this?.get(field)?.isJsonNull == false ) this.get(field).asString else ""
    }

    private fun JsonObject?.isNullBooleanField(field: String): Boolean{
        return if( this?.get(field)?.isJsonNull == false ) this.get(field).asBoolean else false
    }

}