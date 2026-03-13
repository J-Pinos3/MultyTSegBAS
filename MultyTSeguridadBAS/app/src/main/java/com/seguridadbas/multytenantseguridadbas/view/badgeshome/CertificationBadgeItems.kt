package com.seguridadbas.multytenantseguridadbas.view.badgeshome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.seguridadbas.multytenantseguridadbas.model.certifications.CertificationDataResponse
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray

//@Preview(showSystemUi = true)
@Composable
fun CertificationBadgeItems(
    modifier: Modifier = Modifier,
    certification: CertificationDataResponse

){

    Box(
        modifier = modifier
            .padding(8.dp)
            .width(180.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 0.5.dp,
                color = BasGray,
                shape = RoundedCornerShape(12.dp)
            )
            .shadow(2.dp, shape = RoundedCornerShape(12.dp))
            .background(Color.White)
            .aspectRatio(0.85f) // Hacerlo más cuadrado (ancho/alto = 0.85)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Sección superior: Título y código
            Column(
                modifier = Modifier.weight(0.4f)
            ){
                Text(
                    text = certification.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 6.dp),
                    thickness = 1.dp,
                    color = Color(0f, 0f, 0f, 0.14f)
                )

                Text(
                    text = certification.code,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    color = Color.Gray,
                    overflow = TextOverflow.Clip,
                )
            }

            // Sección media: Descripción
            Text(
                modifier = Modifier.weight(0.3f).padding(top = 5.dp),
                text = certification.description,
                fontSize = 12.sp,
                maxLines = 3, // Limitar a 3 líneas máximo
                lineHeight = 14.sp,
                overflow = TextOverflow.Clip // Puntos suspensivos si es muy largo
            )

            // Sección inferior: Imagen
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxSize()
                    .padding(top = 5.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    model = if (certification.icon.isNullOrEmpty())
                        "https://picsum.photos/100"
                    else
                        certification.icon,
                    contentDescription = "certification image",
                    contentScale = ContentScale.Crop // Cambiar a Crop para llenar el espacio
                )
            }
        }
    }
}

