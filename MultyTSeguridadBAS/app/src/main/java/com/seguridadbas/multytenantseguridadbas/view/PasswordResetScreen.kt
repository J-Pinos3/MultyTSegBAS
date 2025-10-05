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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow

@Preview(showSystemUi = true)
@Composable
fun ResetPasswordScreen( ){

    var emailText by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize()
            .background(color= BasBackground)
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
                .padding(start = 20.dp)
                .align(Alignment.Start),
            text = "Ingrese su correo para recuperar la contraseña",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        EmailRSField(
            modifier = Modifier,
            email = emailText,
            onEmailChange = {
                currentEmail -> emailText = currentEmail
            }
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        ResetButton(
            modifier = Modifier,
            enabled = emailText.isNotEmpty(),
            onResetClick = {
                //LOGIC TO SEND MAIL
            }
        )




    }

}


@Composable
fun EmailRSField(modifier: Modifier, email:String, onEmailChange: (String) -> Unit){
    TextField(
        value = email,
        onValueChange = onEmailChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        placeholder = { Text(text="Correo:") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true, maxLines = 1,
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
fun ResetButton(
    modifier: Modifier,
    enabled: Boolean,
    onResetClick:() -> Unit

){
    Button(
        onClick = onResetClick,
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
            text = "ACEPTAR",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
    }
}


