package com.seguridadbas.multytenantseguridadbas.view.badgeshome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.seguridadbas.multytenantseguridadbas.model.services.ServiceDataResponse
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray

//@Preview(showSystemUi = true)
@Composable
fun ServiceBadgeItems(
    modifier: Modifier = Modifier,
    service: ServiceDataResponse

){

    Box(
        modifier = modifier
            .padding(8.dp)
            .width(180.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(12))
            .border(
                width = 0.5.dp,
                color = BasGray,
                shape = RoundedCornerShape(12.dp)
            )
            .shadow(2.dp, shape = RoundedCornerShape(12.dp))
            .background(Color.White)
            .aspectRatio(0.85f)
    ){


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier.weight(0.4f)
            ){
                Text(
                    text = service.title,
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
                    text = service.price + "$",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            /*
            //descripcion
            Text(
                modifier = Modifier.weight(0.3f).padding(top = 5.dp),
                text = service.specifications,
                fontSize = 12.sp,
                maxLines = 3,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Clip
            )
            */

            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxSize()
                    .padding(top = 5.dp),
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    model = if( service.iconImage.isNullOrEmpty() ) "https://picsum.photos/300/200" else service.iconImage[0],
                    contentDescription = "service image",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

}
