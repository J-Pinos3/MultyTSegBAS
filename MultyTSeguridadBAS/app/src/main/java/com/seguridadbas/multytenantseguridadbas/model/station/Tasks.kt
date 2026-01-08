package com.seguridadbas.multytenantseguridadbas.model.station

import com.google.gson.annotations.SerializedName

data class Tasks(
    @SerializedName("id")
    val id: String,
    @SerializedName("taskToDo")
    val taskToDo: String,
    @SerializedName("wasItDone")
    val wasItDone: Boolean,
    @SerializedName("dateToDoTheTask")
    val dateToDoTheTask: String,
    @SerializedName("dateCompletedTask")
    val dateCompletedTask: String? = "",
    @SerializedName("importHash")
    val importHash: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = "",
    @SerializedName("taskBelongsToStationId")
    val taskBelongsToStationId: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("updatedById")
    val updatedById: String? = ""
)
