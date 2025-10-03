package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow

@Preview(showSystemUi = true)
@Composable
fun SplashScreen( enterprise: String = "Tu empresa" ){

    Column(
        modifier = Modifier.background(color = BasBackground)
            .paddingFromBaseline(top=30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasYellow)
                .padding(top = 40.dp, bottom = 40.dp)
        )

        Spacer(
            modifier= Modifier.padding(top = 5.dp, bottom = 5.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasGray)
                .padding(top = 15.dp, bottom = 15.dp)
        )

        Spacer(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )

        // dibujo

        Image(
            painter = painterResource(R.drawable.basimage),
            contentDescription = "Escudo de bas"
        )

        Spacer(
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        Text(
            text = enterprise,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )

        Spacer(
            modifier = Modifier.weight(0.4f)
        )

        Text(
            modifier = Modifier.padding(bottom = 40.dp),
            text = "© 2021 Copyright, Brigadas Antirrobos Seguridad"
        )

    }


}