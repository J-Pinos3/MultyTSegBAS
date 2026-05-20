package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.customercontrollers.AuthenticateCustomerController
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.ActiveShiftCust
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.AuthenticateMeCustResponse
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.ClientAccountCust
import com.seguridadbas.multytenantseguridadbas.model.authenticatecustomer.GuardCust
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// --- COLORES Y ESTILOS ---
val ToolbarBlue = Color(0xFF0D47A1)
val CardBackgroundBlue = ToolbarBlue.copy(alpha = 0.85f)
val ActionCardAlpha = Color.White.copy(alpha = 0.2f)
val GradientBackground = Brush.verticalGradient(
    colors = listOf(ToolbarBlue.copy(alpha = 0.8f), ToolbarBlue)
)

// --- ESTADOS DE LA UI ---
sealed class MySecurityUiState {
    object Loading : MySecurityUiState()
    data class Success(val data: AuthenticateMeCustResponse) : MySecurityUiState()
    data class Error(val message: String) : MySecurityUiState()
    object Empty : MySecurityUiState()
}

// --- MODELO DE UI ---
data class GuardiaEnServicio(
    val fullName: String,
    val isActive: Boolean,
    val gender: String,
    val station: String,
    val idNumber: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MySecurityScreen(
    onAssignedGuardsClick: () -> Unit = {},
    onPersonalStaffingClick: () -> Unit = {},
    onConsignasClick: () -> Unit = {},
    onRoundsHistoryClick: () -> Unit = {},
    onHelpCustomerServiceClick: () -> Unit = {},
    onGuardClick: (String) -> Unit = {},
    controller: AuthenticateCustomerController = hiltViewModel()
) {
    val context = LocalContext.current
    val dataStoreController = remember { DataStoreController(context) }
    val coroutineScope = rememberCoroutineScope()

    var uiState by remember { mutableStateOf<MySecurityUiState>(MySecurityUiState.Loading) }
    var isRefreshing by remember { mutableStateOf(false) }
    val dashboardScrollState = rememberScrollState()

    // Función centralizada para cargar datos
    val loadData = suspend {
        try {
            val userData = dataStoreController.getDataFromStore().first()
            val rawToken = userData.token.replace("\"", "").trim()
            val bearerToken = "Bearer $rawToken"
            Log.i("MYSEC","Token: ${bearerToken}")

            when (val result = controller.getMeCustomerAccount(bearerToken)) {
                is Resource.Success -> {
                    val response = result.data
                    if (response != null && (response.clientAccount.id != "null" || response.postSites.isNotEmpty())) {
                        uiState = MySecurityUiState.Success(response)
                    } else {
                        uiState = MySecurityUiState.Empty
                    }
                }
                is Resource.Error -> {
                    uiState = MySecurityUiState.Error(result.message ?: "Error al obtener datos")
                }
                else -> { uiState = MySecurityUiState.Empty }
            }
        } catch (e: Exception) {
            uiState = MySecurityUiState.Error("Error de conexión: ${e.message}")
        }
    }

    // Carga inicial
    LaunchedEffect(Unit) {
        loadData()
    }

    val refreshEnabled = (uiState !is MySecurityUiState.Success) || dashboardScrollState.value == 0

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                loadData()
                isRefreshing = false
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.splashscreenbackground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Toolbar Dinámico
            TopAppBar(
                title = {
                    val clientName = (uiState as? MySecurityUiState.Success)
                        ?.data
                        ?.clientAccount
                        ?.let { resolveClientDisplayName(it) }
                        ?.takeIf { it.isNotBlank() }
                        ?: "Mi Seguridad"
                    Text(text = clientName, color = Color.White, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ToolbarBlue)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(state = pullRefreshState, enabled = refreshEnabled)
            ) {
                when (val state = uiState) {
                    is MySecurityUiState.Loading -> MySecurityShimmer()
                    is MySecurityUiState.Error -> ErrorContent(state.message) { coroutineScope.launch { uiState = MySecurityUiState.Loading; loadData() } }
                    is MySecurityUiState.Empty -> EmptyContent { coroutineScope.launch { uiState = MySecurityUiState.Loading; loadData() } }
                    is MySecurityUiState.Success -> {
                        SecurityDashboardContent(
                            data = state.data,
                            scrollState = dashboardScrollState,
                            onAssignedGuardsClick = onAssignedGuardsClick,
                            onPersonalStaffingClick = onPersonalStaffingClick,
                            onConsignasClick = onConsignasClick,
                            onRoundsHistoryClick = onRoundsHistoryClick,
                            onHelpCustomerServiceClick = onHelpCustomerServiceClick,
                            onGuardClick = onGuardClick
                        )
                    }
                }

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    backgroundColor = Color.White,
                    contentColor = ToolbarBlue
                )
            }
        }
    }
}

@Composable
fun SecurityDashboardContent(
    data: AuthenticateMeCustResponse,
    scrollState: ScrollState,
    onAssignedGuardsClick: () -> Unit,
    onPersonalStaffingClick: () -> Unit,
    onConsignasClick: () -> Unit,
    onRoundsHistoryClick: () -> Unit,
    onHelpCustomerServiceClick: () -> Unit,
    onGuardClick: (String) -> Unit
) {
    val heroAddress = data.postSites.firstOrNull()?.address.orPlaceholder()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClientHeroHeader(
            clientName = resolveClientDisplayName(data.clientAccount),
            placeImageUrl = data.clientAccount.placeImageUrl.orEmpty(),
            logoUrl = data.clientAccount.logoUrl.orEmpty(),
            address = heroAddress
        )

        val stationNamesById = data.postSites
            .flatMap { site -> site.stations.map { station -> station.id.orEmpty() to station.stationName.orEmpty() } }
            .toMap()

        val mappedGuards = data.activeShifts.map { shift ->
            val matchedGuard = data.guards.firstOrNull { guard ->
                matchesGuardWithShift(guard, shift)
            }

            GuardiaEnServicio(
                fullName = shift.guardName.cleanValue()
                    .ifBlank { matchedGuard?.displayName().orEmpty() }
                    .ifBlank { "Guardia activo" },
                isActive = true,
                gender = "Guardia activo",
                station = resolveStationName(shift, stationNamesById),
                idNumber = matchedGuard?.documentNumber.cleanValue()
                    .ifBlank { shift.guardId.cleanValue() }
                    .ifBlank { "-" }
            )
        }

        Log.d("MYSEC", "Active shifts count: ${data.activeShifts.size}, Mapped guards count: ${mappedGuards.size}")
        mappedGuards.forEach {
            Log.d("MYSEC", "Guard: ${it.fullName} - ${it.station} - ${it.idNumber}")
        }

        if (mappedGuards.isNotEmpty()) {
            GuardiaEnTurnoCard(mappedGuards, onGuardClick)
        } else {
            PlaceholderCard("Guardia en turno", "No hay guardias activos en este momento.")
        }

        // Incidentes (Simulados hasta tener endpoint específico, pero listos para backend)
        IncidentesRecientesCard(listOf("No hay nuevas novedades en su empresa. Revisa más tarde."))

        // Acciones Rápidas
        AccionesRapidasCard(
            onAssignedGuardsClick = onAssignedGuardsClick,
            onPersonalStaffingClick = onPersonalStaffingClick,
            onConsignasClick = onConsignasClick,
            onRoundsHistoryClick = onRoundsHistoryClick,
            onHelpCustomerServiceClick = onHelpCustomerServiceClick
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

// --- HELPER PARA MANEJO DE NULLS ---
fun String?.orPlaceholder(): String {
    return if (this.isNullOrBlank() || this == "null") "Vacío" else this
}

private fun resolveClientDisplayName(clientAccount: ClientAccountCust): String {
    val commercialName = clientAccount.commercialName.cleanValue()
    val companyName = clientAccount.companyName.cleanValue()

    return when {
        commercialName.isNotBlank() -> commercialName
        companyName.isNotBlank() -> companyName
        else -> "Mi Seguridad"
    }
}

private fun String?.cleanValue(): String {
    return this
        ?.trim()
        ?.takeIf { it.isNotEmpty() && !it.equals("null", ignoreCase = true) }
        .orEmpty()
}

private fun GuardCust.displayName(): String {
    val fullName = listOf(firstName.cleanValue(), lastName.cleanValue())
        .filter { it.isNotBlank() }
        .joinToString(" ")
    return fullName.ifBlank { name.cleanValue().ifBlank { "Guardia" } }
}

private fun resolveStationName(shift: ActiveShiftCust?, stationNamesById: Map<String, String>): String {
    if (shift == null) return "Sin estacion"
    val stationById = stationNamesById[shift.stationId.cleanValue()].cleanValue()
    if (stationById.isNotBlank()) return stationById
    return shift.stationName.cleanValue().ifBlank { "Sin estacion" }
}

private fun matchesGuardWithShift(guard: GuardCust, shift: ActiveShiftCust): Boolean {
    val shiftGuardId = shift.guardId.cleanValue()
    if (shiftGuardId.isBlank()) return false

    val guardCandidates = listOf(
        guard.guardId.cleanValue(),
        guard.id.cleanValue()
    ).filter { it.isNotBlank() }

    return guardCandidates.any { it == shiftGuardId }
}

// --- COMPONENTES VISUALES ADAPTADOS ---

@Composable
fun ClientHeroHeader(
    clientName: String,
    placeImageUrl: String,
    logoUrl: String,
    address: String
) {
    Card(
        modifier = Modifier.fillMaxWidth().height(220.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = placeImageUrl.ifBlank { R.drawable.splashscreenbackground },
                contentDescription = "Lugar del cliente",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.splashscreenbackground)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xB3000000))
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LogoAvatar(logoUrl = logoUrl)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = clientName,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        maxLines = 1
                    )
                    if (address.isNotBlank() && address != "Vacío") {
                        Text(
                            text = address,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LogoAvatar(logoUrl: String, shape: Shape = RoundedCornerShape(14.dp)) {
    Surface(
        modifier = Modifier.size(72.dp),
        shape = shape,
        color = Color.White.copy(alpha = 0.96f)
    ) {
        if (logoUrl.isNotBlank()) {
            AsyncImage(
                model = logoUrl,
                contentDescription = "Logo del cliente",
                modifier = Modifier.fillMaxSize().padding(8.dp),
                contentScale = ContentScale.Fit,
                error = painterResource(R.drawable.miseguridadshield)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.miseguridadshield),
                contentDescription = "Logo del cliente",
                modifier = Modifier.fillMaxSize().padding(10.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun PlaceholderCard(title: String, message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundBlue)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
        }
    }
}

@Composable
fun GuardiaEnTurnoCard(guardias: List<GuardiaEnServicio>, onGuardClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundBlue)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Guardia en turno", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            if (guardias.size == 1) {
                // Si hay solo un guardia, mostrar en tamaño completo
                GuardiaItem(
                    fullName = guardias[0].fullName,
                    isActive = guardias[0].isActive,
                    gender = guardias[0].gender,
                    station = guardias[0].station,
                    idNumber = guardias[0].idNumber,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGuardClick(guardias[0].idNumber) }
                )
            } else {
                // Si hay múltiples guardias, mostrar en LazyRow
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(guardias) { guardia ->
                        GuardiaItem(
                            fullName = guardia.fullName,
                            isActive = guardia.isActive,
                            gender = guardia.gender,
                            station = guardia.station,
                            idNumber = guardia.idNumber,
                            modifier = Modifier.clickable { onGuardClick(guardia.idNumber) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GuardiaItem(fullName: String, isActive: Boolean, gender: String, station: String, idNumber: String, modifier: Modifier = Modifier) {
    val baseModifier = if (modifier == Modifier) {
        Modifier.width(170.dp).wrapContentHeight()
    } else {
        modifier.then(Modifier.wrapContentHeight())
    }

    Surface(
        modifier = baseModifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.guarditembackground),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val initials = fullName
                    .split(" ")
                    .filter { it.isNotBlank() }
                    .take(2)
                    .map { it.first().uppercase() }
                    .joinToString("")

                Surface(
                    modifier = Modifier.size(75.dp),
                    shape = RectangleShape,
                    color = Color(0xFF1E88E5),
                    border = BorderStroke(2.dp, Color(0xFF90CAF9))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = initials,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = fullName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )

                Text(
                    text = gender,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )

                val statusBorder = if (isActive) Color(0xFF00E676) else Color(0xFFFF5252)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp),
                    shape = RectangleShape,
                    color = if (isActive) Color(0xFF003300) else Color(0xFF330000),
                    border = BorderStroke(1.dp, statusBorder)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier.size(10.dp).background(statusBorder))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isActive) "EN TURNO" else "EN DESCANSO",
                            color = statusBorder,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_place),
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = station,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }

                Text(
                    text = "ID: $idNumber",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun IncidentesRecientesCard(incidentes: List<String>) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "INCIDENTES RECIENTES", color = ToolbarBlue.copy(alpha = 0.7f), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(incidentes) { texto -> IncidenteItem(texto) }
            }
        }
    }
}

@Composable
fun IncidenteItem(text: String) {
    Box(modifier = Modifier.width(280.dp).height(100.dp).background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
        Text(text = text, textAlign = TextAlign.Center, color = Color.Gray, fontSize = 14.sp)
    }
}

@Composable
fun AccionesRapidasCard(onAssignedGuardsClick: () -> Unit, onPersonalStaffingClick: () -> Unit, onConsignasClick: () -> Unit, onRoundsHistoryClick: () -> Unit, onHelpCustomerServiceClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
        Column(modifier = Modifier.background(GradientBackground).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "ACCIONES RÁPIDAS", color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionItem(Modifier.weight(1f), "Guardias Asignados", R.drawable.ic_groups, onClick = onAssignedGuardsClick)
                QuickActionItem(Modifier.weight(1f), "Dotación del Personal", R.drawable.ic_business, onClick = onPersonalStaffingClick)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionItem(Modifier.weight(1f), "Consignas", R.drawable.ic_orders, onClick = onConsignasClick)
                QuickActionItem(Modifier.weight(1f), "Historial de Rondas", R.drawable.ic_tracking, onClick = onRoundsHistoryClick)
            }
            QuickActionItem(modifier = Modifier.fillMaxWidth(0.5f), title = "Servicio al Cliente", iconRes = R.drawable.ic_person, onClick = onHelpCustomerServiceClick)
        }
    }
}

@Composable
fun QuickActionItem(modifier: Modifier = Modifier, title: String, iconRes: Int, onClick: () -> Unit) {
    Card(modifier = modifier.height(110.dp).clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = ActionCardAlpha), border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(28.dp), tint = Color.White)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
        }
    }
}

// --- SHIMMER Y ESTADOS DE ERROR ---

@Composable
fun MySecurityShimmer() {
    val shimmerColors = listOf(Color.LightGray.copy(alpha = 0.6f), Color.LightGray.copy(alpha = 0.2f), Color.LightGray.copy(alpha = 0.6f))
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim = transition.animateFloat(initialValue = 0f, targetValue = 1000f, animationSpec = infiniteRepeatable(animation = tween(1200, easing = FastOutSlowInEasing)), label = "")
    val brush = Brush.linearGradient(colors = shimmerColors, start = Offset.Zero, end = Offset(x = translateAnim.value, y = translateAnim.value))

    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp).background(brush, RoundedCornerShape(16.dp)))
        Box(modifier = Modifier.fillMaxWidth().height(220.dp).background(brush, RoundedCornerShape(16.dp)))
        Box(modifier = Modifier.fillMaxWidth().height(120.dp).background(brush, RoundedCornerShape(16.dp)))
    }
}

@Composable
fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(painter = painterResource(R.drawable.ic_error), contentDescription = null, tint = Color.Red, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, textAlign = TextAlign.Center, color = Color.DarkGray, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = ToolbarBlue)) { Text("Reintentar") }
    }
}

@Composable
fun EmptyContent(onRefresh: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "No hay datos disponibles", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = "Desliza hacia abajo para actualizar", color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(onClick = onRefresh) { Text("Actualizar Ahora") }
    }
}