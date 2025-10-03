package com.seguridadbas.multytenantseguridadbas.view

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow

@Preview(showSystemUi = true)
@Composable
fun LoginScreen(){

    var passwordVisible by remember { mutableStateOf(false) }
    var passwordText by remember { mutableStateOf("") }

    var emailText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = BasBackground)
            .paddingFromBaseline(top=70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasYellow)
                .padding(top =  30.dp, bottom = 30.dp)
        ){
            Image(
                painter = painterResource(R.drawable.baslogo),
                contentDescription = "Logo de bas"
            )
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            text = "Bienvenido",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        EmailField(
            modifier = Modifier,
            emailText,
            onEmailChange = {
                currentText -> emailText = currentText
            }
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        PasswordField(
            modifier = Modifier,
            password = passwordText,
            isPasswordVisible = passwordVisible,
            onPasswordCHange = {
                newPassword ->
                passwordText = newPassword
            },
            onPasswordVisibilityChange = {
                newVisibility ->
                passwordVisible = newVisibility
            }
        )


        Spacer(modifier = Modifier.padding(top = 16.dp))

        LoginButton(modifier = Modifier)
    }


}


@Composable
fun EmailField(modifier: Modifier, email:String, onEmailChange: (String) -> Unit){
    TextField(
        value = email,
        onValueChange = onEmailChange,
        modifier = modifier.padding(horizontal = 30.dp),
        placeholder = { Text(text="Correo:") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true, maxLines = 1
    )
}

@Composable
fun PasswordField(modifier: Modifier,
          password:String, isPasswordVisible: Boolean, onPasswordCHange: (String) -> Unit,  onPasswordVisibilityChange: (Boolean) -> Unit
                  ){
    TextField(
        value = password,
        onValueChange = onPasswordCHange,
        modifier = modifier.padding(horizontal = 30.dp),
        placeholder = { Text(text="Contraseña:") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true, maxLines = 1,
        visualTransformation = if(isPasswordVisible)
            VisualTransformation.None else PasswordVisualTransformation(),

        trailingIcon = {
            val image = if(isPasswordVisible){
                R.drawable.ic_visibilty_on
            }else{
                R.drawable.ic_visibility_off
            }

            val description = if(isPasswordVisible) "Ocultar Contraseña"
            else "Mostrar Contraseña"

            IconButton(
                onClick = {
                    onPasswordVisibilityChange(!isPasswordVisible)
                }
            ){
                Icon(
                    painter = painterResource(id = image),
                    description
                )
            }

        }
    )

}


@Composable
fun LoginButton(modifier: Modifier){
    Button(
        onClick = {},
        modifier.padding(horizontal = 20.dp)
            .background(Color.Black)
            .fillMaxWidth()
            .height(50.dp) ,
        shape = RoundedCornerShape(2.dp)
    )
    {
        Text(
            text = "Iniciar Sesión",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
    }
}