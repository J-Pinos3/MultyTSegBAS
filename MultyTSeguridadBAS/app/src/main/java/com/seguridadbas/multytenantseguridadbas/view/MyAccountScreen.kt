package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showSystemUi = true)
@Composable
fun MyAccountScreen(
    modifier: Modifier = Modifier
) {

    Text(
        modifier = Modifier.padding(top = 30.dp),
        textAlign = TextAlign.Center,
        text = "Mi Cuenta",
        fontSize = 40.sp
    )

}