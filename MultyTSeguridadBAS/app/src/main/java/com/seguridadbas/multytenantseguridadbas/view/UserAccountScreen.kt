package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBlue
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow

@Preview(showBackground = true)
@Composable
fun UserAccountScreen(
    modifier: Modifier = Modifier,
    userName: String = "Michael Urresta",
    userRole: String = "Manager",
    profileImageUrl: String? = null,
    onAccountDetailsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onBillingClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    toolbarBackgroundColor: Color = BasBlue,
    menuContainerColor: Color = Color.Transparent
) {
    Box(modifier = modifier.fillMaxSize()) {
        // 3. Fondo (Misma imagen que HomeScreen, LoginScreen y SplashScreen)
        Image(
            painter = painterResource(id = R.drawable.splashscreenbackground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // 2. Toolbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(toolbarBackgroundColor)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Mi Cuenta",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // 4.1 Perfil - Avatar en contenedor cuadrado con bordes redondeados
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImageUrl != null) {
                        AsyncImage(
                            model = profileImageUrl,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize().padding(1.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Ícono por defecto (usuario)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person_add),
                            contentDescription = null,
                            modifier = Modifier.size(90.dp),
                            tint = BasYellow
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre del usuario (blanco, bold)
                Text(
                    text = userName,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                
                // Rol (texto normal, color más tenue)
                Text(
                    text = userRole,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(48.dp))

                // 4.2 Menú de opciones
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(menuContainerColor)
                ) {
                    AccountOptionItem(
                        icon = R.drawable.ic_person,
                        title = "Detalles de la Cuenta",
                        description = "Nombre de usuario, cargo, etc.",
                        onClick = onAccountDetailsClick
                    )
                    
                    AccountOptionItem(
                        icon = R.drawable.ic_settings,
                        title = "Configuración",
                        description = "Configure sus preferencias en notificaciones y más",
                        onClick = onSettingsClick
                    )
                    
                    AccountOptionItem(
                        icon = R.drawable.ic_pay_money,
                        title = "Facturación",
                        description = "Encuentre información sobre facturación",
                        onClick = onBillingClick
                    )
                }

                //Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(32.dp))

                // 5. Botón de cerrar sesión
                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = BasBlue // Letras azules
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun AccountOptionItem(
    icon: Int,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono a la izquierda
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = Color.Black.copy(alpha = 0.4f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            // Título (negro, bold)
            Text(
                text = title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
            // Descripción (más tenue)
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp
            )
        }

        // Flecha > a la derecha
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_forward),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Black.copy(alpha = 0.2f)
        )
    }
}
