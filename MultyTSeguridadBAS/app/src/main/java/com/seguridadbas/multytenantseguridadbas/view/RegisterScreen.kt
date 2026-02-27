package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.network.ApiClient
import com.seguridadbas.multytenantseguridadbas.controllers.repository.AuthenticationRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.core.util.validators
import com.seguridadbas.multytenantseguridadbas.model.SendEmailVerificationRequest
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import com.seguridadbas.multytenantseguridadbas.view.dialog.EmailVerificationDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java

//@Preview(showSystemUi = true)
@Composable
fun RegisterScreen(
    authController: AuthController
){

    /** AÑADIR VOLVER AL  LOGIN
    */

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var passwordText by remember { mutableStateOf("") }

    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }

    var emailText by remember { mutableStateOf("") }

    var passwordsMatch by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }

    var loading by remember {mutableStateOf(false)}

    var user_token: String = ""

    val showMailError = emailText.isNotEmpty() && emailText.contains("@") && emailText.contains(".")

    var showVerificationDialog by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = BasBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
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
            text = "Ingrese su nombre",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        FullNameRegisterField(
            modifier = Modifier,
            fullName = firstName,
            onFullNameChange = {
                currentText -> firstName = currentText
            },
            placeholder = "Nombre:"
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.Start),
            text = "Ingrese su apellido",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        FullNameRegisterField(
            modifier = Modifier,
            fullName = lastName,
            onFullNameChange = {
                    currentText -> lastName = currentText
            },
            placeholder = "Apellido:"
        )


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
                    .padding( start = 20.dp)
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
                    .padding( start = 20.dp)
                    .align( Alignment.Start ),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.padding(top = 10.dp))
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

                    CoroutineScope(Dispatchers.IO).launch {
                        loading = true
                        val result = authController.signUp(emailText, passwordText,
                            "$firstName $lastName", firstName, lastName, "$firstName $lastName"
                        )
                        withContext(Dispatchers.Main){
                            loading  =false
                            when(result){
                                is Resource.Success -> {
                                    showVerificationDialog = true
                                    user_token = "Bearer " + result.data.toString()
                                    Log.i("REGISTER SCREEN", "registro exitoso, token: ${user_token}")

                                    //verifyEmailWithToken(token, authController)
                                }

                                is Resource.Error->{
                                    showVerificationDialog = false
                                    Log.e("REGISTER SCREEN", "error registro: ${result.message.toString()}")
                                }
                                else ->{
                                    showVerificationDialog = false
                                    Log.e("REGISTER SCREEN", "error al registrarse")
                                }
                            }
                        }
                    }
                    //authController.signUp(emailText, passwordText)


                }else{
                    showVerificationDialog = false
                }
            }
        )

        if(loading){
            Spacer(modifier = Modifier.padding(top = 14.dp))
            Text("Cargandoo")
        }

        if(showVerificationDialog){
            EmailVerificationDialog(
                email = emailText,
                onClose = {

                    verifyEmailWithToken(user_token, authController, emailText)


                    showVerificationDialog = false
                    emailText = ""
                    passwordText = ""
                    confirmPassword = ""
                }
            )
        }


        Spacer(Modifier.height(40.dp))
    }


}


@Composable
fun FullNameRegisterField(modifier: Modifier, fullName: String, onFullNameChange: (String) -> Unit, placeholder: String){
    TextField(
        value = fullName,
        onValueChange = onFullNameChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        placeholder = { Text(text=placeholder) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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


fun verifyEmailWithToken(token: String, controller: AuthController, email:String){
    CoroutineScope(Dispatchers.IO).launch {
        val result = controller.sendVerificationEmail(token, SendEmailVerificationRequest(email))
        withContext(Dispatchers.Main){
            when(result){
                is Resource.Success -> {
                    Log.i("REGISTER SCREEN", "correo verificado exitosamente, token: ${result.data}")
                }

                is Resource.Error->{
                    Log.e("REGISTER SCREEN", "error verificacion: ${result.message.toString()}")
                }
                else ->{
                    Log.e("REGISTER SCREEN", "error al verificar el email")
                }
            }
        }
    }
}



