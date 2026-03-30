package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.controllers.incidentsdetailcontroller.IncidentDetailController
import com.seguridadbas.multytenantseguridadbas.model.stationreports.IncidentDetailResponse


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentsByStationDetail(
    modifier: Modifier = Modifier,
    incidentID: String = "",
    navigateBackToIncidents: () -> Unit = {},
    incidentDetailController: IncidentDetailController
){
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val incidentData by remember{ mutableStateOf<IncidentDetailResponse?>(null) }

    val context = LocalContext.current
    val dataStoreController = remember { DataStoreController(context) }


}