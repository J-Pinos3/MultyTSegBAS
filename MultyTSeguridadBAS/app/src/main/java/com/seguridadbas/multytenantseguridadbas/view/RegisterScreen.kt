package com.seguridadbas.multytenantseguridadbas.view

import android.widget.Toast
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.core.util.validators
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import com.seguridadbas.multytenantseguridadbas.view.dialog.EmailVerificationDialog

@Preview(showSystemUi = true)
@Composable
fun RegisterScreen(

){

    /** AÑADIR VOLVER AL  LOGIN
    */
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordText by remember { mutableStateOf("") }

    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }

    var emailText by remember { mutableStateOf("") }

    var passwordsMatch by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }



    val showMailError = emailText.isNotEmpty() && emailText.contains("@") && emailText.contains(".")

    var showVerificationDialog by remember { mutableStateOf(false) }


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
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.Start),
            text = "Ingrese su correo",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        EmailRegisterField (
            modifier = Modifier,
            emailText,
            onEmailChange = {
                    currentText -> emailText = currentText
            }
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.Start),
            text = "Ingrese su contraseña",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        PasswordRegisterField(
            modifier = Modifier,  password = passwordText,
            isPasswordVisible = passwordVisible,
            onPasswordCHange = {
                    newPassword ->  passwordText = newPassword

                passwordErrorMessage = validators.validatePassword(passwordText)
                if( passwordErrorMessage.isNullOrEmpty() ){
                    showPasswordError = false
                }else{
                    showPasswordError = true
                }
            },
            onPasswordVisibilityChange = {
                    newVisibility ->  passwordVisible = newVisibility
            }
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.Start),
            text = "Confirme su contraseña",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )


        Spacer(modifier = Modifier.padding(top = 16.dp))

        PasswordRegisterField(
            modifier = Modifier,
            password = confirmPassword,
            isPasswordVisible = confirmPasswordVisible,
            onPasswordCHange = {
                    newPassword ->
                confirmPassword = newPassword

                passwordsMatch = (passwordText == confirmPassword) && passwordText.isNotEmpty()

                print("Claves iguales: $passwordsMatch")
            },
            onPasswordVisibilityChange = {
                    newVisibility ->
                confirmPasswordVisible = newVisibility
            },
        )

        Spacer(modifier = Modifier.padding(top = 24.dp))

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

        if(!passwordsMatch){
            Text(
                text = "Las contraseñas no coinciden",
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp)
                    .align( Alignment.Start ),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.padding(top = 24.dp))
        }

        if(showPasswordError){
            Text(
                text = passwordErrorMessage,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp)
                    .align( Alignment.Start ),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.padding(top = 24.dp))
        }

        RegisterButton(
            modifier = Modifier,
            enabled = passwordsMatch && showMailError,
            onRegisterClick = {
                if(passwordsMatch && showMailError){
                    showVerificationDialog = true
                }else{
                    showVerificationDialog = false
                }
            }
        )

        if(showVerificationDialog){
            EmailVerificationDialog(
                email = emailText,
                onClose = {

                    /** logica para enviar correos*/

                    showVerificationDialog = false
                    emailText = ""
                    passwordText = ""
                    confirmPassword = ""
                }
            )
        }


    }


}





@Composable
fun EmailRegisterField(modifier: Modifier, email:String, onEmailChange: (String) -> Unit){
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
fun PasswordRegisterField(modifier: Modifier,
                  password:String, isPasswordVisible: Boolean, onPasswordCHange: (String) -> Unit,
                  onPasswordVisibilityChange: (Boolean) -> Unit,

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
fun RegisterButton(
    modifier: Modifier,
    enabled: Boolean,
    onRegisterClick:() -> Unit

){
    Button(
        onClick = onRegisterClick,
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
            text = "REGISTRARSE",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
    }
}






