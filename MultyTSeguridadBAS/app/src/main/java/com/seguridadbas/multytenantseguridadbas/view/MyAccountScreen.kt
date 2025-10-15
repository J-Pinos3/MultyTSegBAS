package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow

@Preview(showSystemUi = true)
@Composable
fun MyAccountScreen(
    modifier: Modifier = Modifier
) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize()
            .background(color = BasBackground)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasYellow)
                .padding(top =  50.dp, bottom = 30.dp)
        ){
            Image(
                painter = painterResource(R.drawable.baslogo),
                contentDescription = "Logo de bas"
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterHorizontally),
            text = "Mi Cuenta",
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start),
            textAlign = TextAlign.Start,
            text = "Nombre del cliente",
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))

        TextFieldsProfile(
            modifier = Modifier,
            textData = name,
            onTextDataChange = {},
            placeholder = "Nombre: ",
            keyboardTypes = KeyboardType.Text
        )


        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start),
            textAlign = TextAlign.Start,
            text = "Correo Electrónico",
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))

        TextFieldsProfile(
            modifier = Modifier,
            textData = email,
            onTextDataChange = {},
            placeholder = "correo electronico: ",
            keyboardTypes = KeyboardType.Email
        )


        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start),
            textAlign = TextAlign.Start,
            text = "Número de Teléfono",
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))

        TextFieldsProfile(
            modifier = Modifier,
            textData = phone,
            onTextDataChange = {},
            placeholder = "numero de telefono: ",
            keyboardTypes = KeyboardType.Phone
        )


        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start),
            textAlign = TextAlign.Start,
            text = "Dirección del Cliente",
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))

        TextFieldsProfile(
            modifier = Modifier,
            textData = address,
            onTextDataChange = {},
            placeholder = "Direccion del cliente: ",
            keyboardTypes = KeyboardType.Text
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        UpdateProfileButton(
            modifier = Modifier,
            enabled = false,
            onUpdateProfileClick = {

            }
        )
    }


}


@Composable
fun TextFieldsProfile(
    modifier: Modifier,
    textData: String,
    onTextDataChange: (String) -> Unit,
    placeholder: String,
    keyboardTypes: KeyboardType
){

    TextField(

        value = textData,
        onValueChange = onTextDataChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        placeholder = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardTypes),
        singleLine = true, maxLines = 4,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )

    )

}



@Composable
fun UpdateProfileButton(
    modifier: Modifier,
    enabled: Boolean,
    onUpdateProfileClick: () -> Unit
){

    Button(
        onClick = onUpdateProfileClick,
        modifier.padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(50.dp) ,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) Color.Black else Color.Gray,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        ),
        enabled = enabled
    )
    {
        Text(
            text = "Actualizar",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
    }

}
