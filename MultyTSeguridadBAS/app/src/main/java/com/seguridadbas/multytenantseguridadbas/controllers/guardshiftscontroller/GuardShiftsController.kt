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
        filter: Map<String, String>,
        limit: Int,
        offset: Int,
        orderBy: String? = ""
    ): Resource<List<ShortGuardShift>>{
        val response = guardShiftsRepository.getAllGuardShiftsRepo(token, tenantId, filter,limit, offset, orderBy)

        return try{


            if(response.isSuccessful && response.body() != null){

                val jsonBody = response.body()!!
                val guardShiftsResponse = parseAllGuardShiftsResponse(jsonBody as JsonObject)

                Resource.Success(
                    guardShiftsResponse.rows.map {
                        ShortGuardShift(

                            guardName = it.guardName ?: "",
                            id = it.id,
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
                         completeInventoryCheck = guardShift?.get("completeInventoryCheck")?.asInt ?: 0,
                         dailyIncidents = guardShift?.getAsJsonArray("dailyIncidents")?.toList() ?: emptyList(),
                         guardName = guardShift?.get("guardName")?.asString ?: "",
                         guardNameId = guardShift?.get("guardNameId")?.asString ?: "",
                         id = guardShift?.get("id")?.asString ?: "",
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
                completeInventoryCheck = row.get("completeInventoryCheck").asInt,
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
                stationName = StationName(
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
                stationNameId = row.get("stationNameId").asString,
                tenantId = row.get("tenantId").asString,
                updatedAt = row.get("updatedAt").asString,
                updatedById = row.get("updatedById").asString
            )

        }
        return AllGuardShiftsResponse(count, rows)

    }

}