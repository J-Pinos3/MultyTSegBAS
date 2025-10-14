package com.seguridadbas.multytenantseguridadbas.view

import android.graphics.drawable.shapes.Shape
import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.core.util.validators
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasGray
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow

@Preview(showSystemUi = true)
@Composable
fun LoginScreen(
    onLoginClicked:() -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {},
    onCreateAccount: () -> Unit = {}
){

    var passwordVisible by remember { mutableStateOf(false) }
    var passwordText by remember { mutableStateOf("") }

    var showPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }

    var emailText by remember { mutableStateOf("") }

    val showMailError = emailText.isNotEmpty() && emailText.contains("@") && emailText.contains(".")

    Column(
        modifier = Modifier.fillMaxSize()
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
            text = "Bienvenido",
            fontWeight = FontWeight.W900,
            fontSize = 40.sp
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

                passwordErrorMessage = validators.validatePassword(passwordText)
                //println("Error: $passwordErrorMessage")
                if( passwordErrorMessage.isNullOrEmpty() ){
                    showPasswordError = false
                }else{
                    //println("Error: $passwordErrorMessage")
                    showPasswordError = true
                }
            },
            onPasswordVisibilityChange = {
                newVisibility ->
                passwordVisible = newVisibility
            }
        )


        Spacer(modifier = Modifier.padding(top = 16.dp))

        if(showPasswordError){
            Text(
                text = passwordErrorMessage,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp)
                    .align(Alignment.Start),
                fontSize = 16.sp

            )

            Spacer(modifier = Modifier.padding(top = 16.dp))
        }

        if(!showMailError){
            Text(
                text = "El formato de correo no es válido",
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp)
                    .align( Alignment.Start ),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.padding(top = 24.dp))
        }

        LoginButton(modifier = Modifier,
            enabled = !showPasswordError && showMailError,
            onLoginButtonClicked = onLoginClicked
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        ForgotPassword(
            Modifier
                .padding(end = 20.dp)
                .align(Alignment.End),
            
            _onForgotPasswordClicked = onForgotPasswordClicked
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        CreateAccount(
            Modifier
                .padding(end = 20.dp)
                .align(Alignment.End),

            _onCreateAccountClicked = onCreateAccount
        )
    }


}


@Composable
fun EmailField(modifier: Modifier, email:String, onEmailChange: (String) -> Unit){
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
fun PasswordField(modifier: Modifier,
          password:String, isPasswordVisible: Boolean, onPasswordCHange: (String) -> Unit,  onPasswordVisibilityChange: (Boolean) -> Unit
                  ){
    TextField(
        value = password,
        onValueChange = onPasswordCHange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        placeholder = { Text(text="Contraseña:") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true, maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
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
fun ForgotPassword(modifier: Modifier, _onForgotPasswordClicked: () -> Unit ){
    Text(
        text = "Olvidaste tu contraseña?",
        modifier.clickable {  _onForgotPasswordClicked()   },
         fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
    )
}


@Composable
fun CreateAccount(modifier: Modifier, _onCreateAccountClicked: () -> Unit){
    Text(
        text = "No tienes cuenta?, crea una.",
        modifier.clickable {  _onCreateAccountClicked()   },
         fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
    )
}


@Composable
fun LoginButton(
    modifier: Modifier,
    enabled: Boolean,
    onLoginButtonClicked: () -> Unit
){
    Button(
        onClick = { onLoginButtonClicked()  },
        modifier.padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(50.dp) ,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(enabled) Color.Black else Color.Gray ,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        ),
        enabled = enabled

    )
    {
        Text(
            text = "INICIAR SESIÓN",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
    }
}