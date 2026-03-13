package com.seguridadbas.multytenantseguridadbas.model.stationreports

import com.google.gson.annotations.SerializedName

data class InventoryByStationData(
    @SerializedName("armor")
    val armor: Boolean,
    @SerializedName("armorSerialNumber")
    val armorSerialNumber: String,
    @SerializedName("armorType")
    val armorType: String,
    @SerializedName("belongsTo")
    val belongsTo: String? = null,
    @SerializedName("belongsToId")
    val belongsToId: String? = null,
    @SerializedName("belongsToStation")
    val belongsToStation: String,
    @SerializedName("caseta")
    val caseta: Boolean,
    @SerializedName("cintoCompleto")
    val cintoCompleto: Boolean,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdById")
    val createdById: String,
    @SerializedName("deletedAt")
    val deletedAt: String? = null,
    @SerializedName("detectorDeMetales")
    val detectorDeMetales: Boolean,
    @SerializedName("gun")
    val gun: Boolean,
    @SerializedName("gunSerialNumber")
    val gunSerialNumber: String,
    @SerializedName("gunType")
    val gunType: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("importHash")
    val importHash: String? = null,
    @SerializedName("linterna")
    val linterna: Boolean,
    @SerializedName("observations")
    val observations: String,
    @SerializedName("pito")
    val pito: Boolean,
    @SerializedName("ponchoDeAguas")
    val ponchoDeAguas: Boolean,
    @SerializedName("radio")
    val radio: Boolean,
    @SerializedName("radioSerialNumber")
    val radioSerialNumber: String,
    @SerializedName("radioType")
    val radioType: String,
    @SerializedName("tenantId")
    val tenantId: String,
    @SerializedName("tolete")
    val tolete: Boolean,
    @SerializedName("transportation")
    val transportation: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedById")
    val updatedById: String? = null,
    @SerializedName("vitacora")
    val vitacora: Boolean
)