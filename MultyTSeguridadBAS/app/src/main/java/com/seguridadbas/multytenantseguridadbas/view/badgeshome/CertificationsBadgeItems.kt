package com.seguridadbas.multytenantseguridadbas.view.badgeshome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray

@Preview(showSystemUi = true)
@Composable
fun ServicesBadgeItems(
    modifier: Modifier = Modifier,
    description: String = "",
    image: String = ""

){

    Box(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(14))
            .border(
                width = 0.5.dp,
                color = BasGray,
                shape = RoundedCornerShape(0,20,0,20)
            )
            .shadow(2.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        AsyncImage(
            model = image,
            contentDescription = "certification image",
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.padding(vertical = 20.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

    }

}
