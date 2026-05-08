package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.seguridadbas.multytenantseguridadbas.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Color principal del toolbar y acentos
val ToolbarBlue = Color(0xFF0D47A1)
val CardBackgroundBlue = ToolbarBlue.copy(alpha = 0.85f)
val ActionCardAlpha = Color.White.copy(alpha = 0.2f)
val GradientBackground = Brush.verticalGradient(
    colors = listOf(ToolbarBlue.copy(alpha = 0.8f), ToolbarBlue)
)

// Modelo para el guardia
data class GuardiaEnServicio(
    val fullName: String,
    val isActive: Boolean,
    val gender: String,
    val station: String,
    val idNumber: String
)

@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MySecurityScreen() {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    // Datos reactivos (simulados)
    var guardiasList by remember {
        mutableStateOf(
            listOf(
                GuardiaEnServicio("Nombre Apellido", true, "Masculino", "Bravo 1", "1719257642"),
                GuardiaEnServicio("Maria Lopez", false, "Femenino", "Delta 2", "1719257643")
            )
        )
    }
    var incidentesList by remember { mutableStateOf(listOf("No hay nuevas novedades en su empresa. Revisa más tarde.")) }

    // Función de refresh simulando API
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                delay(2000) // Simulación de carga de red
                // Actualización de datos
                guardiasList = listOf(
                    GuardiaEnServicio("Nombre Apellido Actualizado", true, "Masculino", "Bravo 1", "1719257642"),
                    GuardiaEnServicio("Maria Lopez Actualizada", true, "Femenino", "Delta 2", "1719257643")
                )
                isRefreshing = false
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo persistente
        Image(
            painter = painterResource(id = R.drawable.splashscreenbackground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Toolbar fijo
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Seguridad",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ToolbarBlue)
            )

            // Contenedor con Pull to Refresh y Scroll Vertical
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Card principal
                    MainImageCard(imageUrl = "https://via.placeholder.com/600x300")

                    // Card "Guardia en turno" con scroll horizontal funcional
                    GuardiaEnTurnoCard(guardiasList)

                    // Card "Incidentes recientes" con scroll horizontal funcional
                    IncidentesRecientesCard(incidentesList)

                    // Card "Acciones rápidas"
                    AccionesRapidasCard()

                    Spacer(modifier = Modifier.height(80.dp))
                }

                // Animación de Pull to Refresh
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
fun MainImageCard(imageUrl: String) {
    Card(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Banner Seguridad",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.miseguridad_txt1)
        )
    }
}

@Composable
fun GuardiaEnTurnoCard(guardias: List<GuardiaEnServicio>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundBlue)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Guardia en turno",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Scroll horizontal independiente
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(guardias) { guardia ->
                    GuardiaItem(
                        fullName = guardia.fullName,
                        isActive = guardia.isActive,
                        gender = guardia.gender,
                        station = guardia.station,
                        idNumber = guardia.idNumber
                    )
                }
            }
        }
    }
}

@Composable
fun GuardiaItem(
    fullName: String,
    isActive: Boolean = true,
    gender: String,
    station: String,
    idNumber: String
) {
    Surface(
        modifier = Modifier
            .width(170.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Fondo: imagen guardItembackground
            Image(
                painter = painterResource(id = R.drawable.guarditembackground), // Usamos splashscreenbackground como fallback si guardItembackground no existe
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            // Contenido overlay
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 2.1 Iniciales del guardia (Cuadrado en la parte superior)
                val initials = fullName.split(" ")
                    .filter { it.isNotBlank() }
                    .take(2)
                    .map { it.first().uppercase() }
                    .joinToString("")

                Surface(
                    modifier = Modifier.size(75.dp),
                    shape = RectangleShape, // Bordes NO redondeados
                    color = Color(0xFF1E88E5), // Tono azul claro configurable
                    border = BorderStroke(2.dp, Color(0xFF90CAF9)) // Borde más claro
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

                // 2.2 Nombre completo
                Text(
                    text = fullName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )

                // 2.3 Género
                Text(
                    text = gender,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )

                // 2.4 Estado (status) badge rectangular
                val statusBg = if (isActive) Color(0xFF003300) else Color(0xFF330000)
                val statusBorder = if (isActive) Color(0xFF00E676) else Color(0xFFFF5252)
                val statusText = if (isActive) "EN TURNO" else "EN DESCANSO"

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp),
                    shape = RectangleShape, // Badge rectangular sin bordes redondeados
                    color = statusBg,
                    border = BorderStroke(1.dp, statusBorder)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Pequeño cuadrado indicador
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(statusBorder)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = statusText,
                            color = statusBorder,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 2.5 Estación
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_place),
                        contentDescription = null,
                        tint = Color(0xFFE53935), // Rojo tipo ubicación
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

                // 2.6 Cédula
                Text(
                    text = "CI: $idNumber",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun IncidentesRecientesCard(incidentes: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "INCIDENTES RECIENTES",
                color = ToolbarBlue.copy(alpha = 0.7f),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(incidentes) { texto ->
                    IncidenteItem(texto)
                }
            }
        }
    }
}

@Composable
fun IncidenteItem(text: String) {
    Box(
        modifier = Modifier
            .width(280.dp)
            .height(120.dp)
            .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun AccionesRapidasCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.background(GradientBackground).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "ACCIONES RÁPIDAS",
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionItem(Modifier.weight(1f), "Guardias Asignados", R.drawable.ic_groups)
                QuickActionItem(Modifier.weight(1f), "Dotación del Personal", R.drawable.ic_business)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickActionItem(Modifier.weight(1f), "Consignas", R.drawable.ic_orders)
                QuickActionItem(Modifier.weight(1f), "Historial de Rondas", R.drawable.ic_tracking)
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                QuickActionItem(
                    modifier = Modifier.fillMaxWidth(0.485f),
                    title = "Contactar al Servicio al Cliente",
                    iconRes = R.drawable.ic_person
                )
            }
        }
    }
}

@Composable
fun QuickActionItem(modifier: Modifier = Modifier, title: String, iconRes: Int) {
    Card(
        modifier = modifier.height(110.dp).clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ActionCardAlpha),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}
