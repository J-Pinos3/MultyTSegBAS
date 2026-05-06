package com.seguridadbas.multytenantseguridadbas.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.core.util.validators
import com.seguridadbas.multytenantseguridadbas.model.UserDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun LoginScreen(
    onLoginClicked: (String) -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    authController: AuthController = hiltViewModel()
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordText by remember { mutableStateOf("") }
    var emailText by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var tenantId by remember { mutableStateOf("") }

    // Estado para el popup de error
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Validaciones
    var showPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }
    val showMailError = emailText.isNotEmpty() && !(emailText.contains("@") && emailText.contains("."))

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Fondo con imagen personalizada
        Image(
            painter = painterResource(id = R.drawable.splashscreenbackground),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido centrado con espacio en los bordes
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), // ← Margen lateral para el card
            contentAlignment = Alignment.Center
        ) {
            // Card con el formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, bottom = 32.dp, start = 8.dp, end = 8.dp), // ← Padding superior para dar espacio al círculo
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título "Mi Seguridad"
                    Text(
                        text = "Mi Seguridad",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )

                    // Subtítulo "by C Guard Pro"
                    Text(
                        text = "by C Guard Pro",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF42A5F5)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // "Bienvenido"
                    Text(
                        text = "Bienvenido",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo de correo electrónico
                    OutlinedTextField(
                        maxLines = 1,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_mail_login),
                                contentDescription = "icon mail",
                                tint = Color(0xFF1565C0)
                            )
                        },
                        value = emailText,
                        onValueChange = { emailText = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Correo Electrónico") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1565C0),
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de contraseña
                    OutlinedTextField(
                        maxLines = 1,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_lock_login),
                                contentDescription = "icon password",
                                tint = Color(0xFF1565C0)
                            )
                        },
                        value = passwordText,
                        onValueChange = {
                            passwordText = it
                            passwordErrorMessage = validators.validatePassword(passwordText)
                            showPasswordError = passwordErrorMessage.isNotEmpty()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Contraseña") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1565C0),
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                        ),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        if (passwordVisible) R.drawable.ic_visibilty_on else R.drawable.ic_visibility_off
                                    ),
                                    contentDescription = if (passwordVisible) "Ocultar" else "Mostrar"
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de iniciar sesión
                    Button(
                        onClick = {
                            if (emailText.isBlank() || passwordText.isBlank()) {
                                errorMessage = "Por favor complete todos los campos"
                                showErrorDialog = true
                                return@Button
                            }
                            if (showMailError) {
                                errorMessage = "Formato de correo inválido"
                                showErrorDialog = true
                                return@Button
                            }
                            if (showPasswordError) {
                                errorMessage = passwordErrorMessage
                                showErrorDialog = true
                                return@Button
                            }

                            loading = true
                            CoroutineScope(Dispatchers.IO).launch {
                                val result = authController.signIn(emailText, passwordText)
                                withContext(Dispatchers.Main) {
                                    loading = false
                                    when (result) {
                                        is Resource.Success -> {
                                            val userDataStore = UserDataStore(
                                                token = result.data?.token.toString(),
                                                id = result.data?.user?.id.toString(),
                                                email = result.data?.user?.email.toString(),
                                                clientAccountId = result.data?.user?.clientAccountId.toString(),
                                                firstName = result.data?.user?.firstName.toString(),
                                                lastName = result.data?.user?.lastName.toString() ?: ""
                                            )
                                            val token = result.data?.token.toString().replace("\"", "").trim()
                                            val resultTenant = authController.authenticateProfileME("Bearer $token")
                                            when (resultTenant) {
                                                is Resource.Success -> {
                                                    tenantId = resultTenant.data?.tenantId.toString().replace("\"", "").trim()
                                                    dataStoreController.saveTenantId(tenantId)
                                                    dataStoreController.saveToDataStore(userDataStore)
                                                    onLoginClicked(tenantId)
                                                }
                                                is Resource.Error -> {
                                                    errorMessage = resultTenant.message ?: "Error al obtener perfil"
                                                    showErrorDialog = true
                                                }
                                                else -> {
                                                    errorMessage = "Error al obtener perfil"
                                                    showErrorDialog = true
                                                }
                                            }
                                        }
                                        is Resource.Error -> {
                                            errorMessage = result.message ?: "Error al iniciar sesión"
                                            showErrorDialog = true
                                        }
                                        else -> {
                                            errorMessage = "Error desconocido"
                                            showErrorDialog = true
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1565C0),
                            contentColor = Color.White
                        ),
                        enabled = !loading
                    ) {
                        if (loading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                        } else {
                            Text("INICIAR SESIÓN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Olvidaste contraseña
                    Text(
                        text = "Olvidaste tu contraseña?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1565C0),
                        modifier = Modifier.clickable { onForgotPasswordClicked() }
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            // Círculo con el logo (encima del card, en el borde superior)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (70).dp) // ← Mitad del círculo fuera, mitad dentro
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .shadow(1.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.miseguridadshield),
                    contentDescription = "Logo",
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        // Copyright en la parte inferior
        Text(
            text = "© 2026 C Guard Pro — Mi Seguridad",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }

    // Popup de error semi-transparente
    if (showErrorDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { showErrorDialog = false },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(alpha = 0.85f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icono de error
                    Icon(
                        painter = painterResource(R.drawable.ic_error),
                        contentDescription = "Error",
                        tint = Color(0xFFF44344),
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mensaje de error
                    Text(
                        text = errorMessage,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de aceptar
                    Button(
                        onClick = { showErrorDialog = false },
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44344),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Aceptar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}






/*

package com.seguridadbas.multytenantseguridadbas.view

import android.content.Context
import android.net.Uri
import android.util.Log
import android.content.Intent
import kotlinx.coroutines.flow.*

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.core.util.validators
import com.seguridadbas.multytenantseguridadbas.model.UserDataStore
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//@Preview(showSystemUi = true)
@Composable
fun LoginScreen(
    onLoginClicked:(String) -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    deepLinkIntentFlow: SharedFlow<Intent>,
    authController: AuthController = hiltViewModel()
){

    var passwordVisible by remember { mutableStateOf(false) }
    var passwordText by remember { mutableStateOf("") }

    var showPasswordError by remember { mutableStateOf(true) }
    var passwordErrorMessage by remember { mutableStateOf("") }

    var showLoginErrorMessage by remember { mutableStateOf(false) }

    var emailText by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    var tenantid by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val showMailError = emailText.isNotEmpty() && emailText.contains("@") && emailText.contains(".")

    val context = LocalContext.current
    val dataStoreController = DataStoreController(context)

    var isProcessingDeepLink by remember { mutableStateOf(false) }


    LaunchedEffect(deepLinkIntentFlow) {
        deepLinkIntentFlow.collect { intent ->
            val uri = intent.data
            if (uri == null) return@collect

            Log.d("Deep Link", "url : $uri")
            val uriToken = uri.getQueryParameter("authToken")
            val errorCode = uri.getQueryParameter("socialErrorCode")

            if(uriToken != null){
                Log.i("DeepLink","Token jwt recibido: ${uriToken.take(4)}...")
                isProcessingDeepLink = true

                val userDataStore = UserDataStore(
                    token = uriToken,
                    id = "",
                    email = "",
                    firstName = "",
                    lastName = ""
                )
                dataStoreController.saveToDataStore(userDataStore)

                CoroutineScope(Dispatchers.IO).launch{
                    val resultTenant = authController.authenticateProfileME("Bearer $uriToken")

                    when(resultTenant){
                        is Resource.Success -> {
                            val tenantIdSocial  = resultTenant.data?.tenantId.toString().replace("\"","").trim()

                            dataStoreController.saveTenantId(tenantIdSocial)
                            Log.i("Deep_Link","tenantId GUARDADO $tenantIdSocial")

                            withContext(Dispatchers.Main){
                                onLoginClicked(tenantIdSocial)
                            }
                        }

                        is Resource.Error -> {
                            Log.e("Deep_Link","Error al obtener tenant: ${resultTenant.message}")
                        }

                        else -> {
                            Log.e("Deep_Link","No se pudo obtener el tenant")
                        }
                    }
                    isProcessingDeepLink = false
                }
            }else if (errorCode != null){
                Log.e("DeepLink","Error al iniciar sesion con redes sociales")
            }
        }
        /*
        snapshotFlow { (context as ComponentActivity).intent }
            .filter { intent->
                intent.action == Intent.ACTION_VIEW && intent.data != null
            }
            .collect{  intent ->
                val uri = intent.data!!
                Log.d("DeepLink", "url : $uri")

                val token = uri.getQueryParameter("authToken")
                val errorCode = uri.getQueryParameter("socialErrorCode")

                if( token != null ){

                }else if( errorCode != null ){

                    Toast.makeText(context, "Error con redes sociales", Toast.LENGTH_SHORT).show()
                }

                (context as ComponentActivity).intent = Intent()
            }
        */

    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ){ paddingVals ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVals)
                .background(color = BasBackground)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BasBackground)
                    .padding(top = 5.dp, bottom = 5.dp)
            ){
                Image(
                    modifier = Modifier.height(30.dp).fillMaxWidth(),
                    painter = painterResource(R.drawable.miseguridad_txt1),
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


            Spacer(modifier = Modifier.padding(top = 6.dp))

            if(showPasswordError){
                Text(
                    text = passwordErrorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(top = 6.dp, start = 20.dp)
                        .align(Alignment.Start),
                    fontSize = 14.sp

                )

                Spacer(modifier = Modifier.padding(top = 6.dp))
            }

            if(!showMailError){
                Text(
                    text = "El formato de correo no es válido",
                    color = Color.Red,
                    modifier = Modifier
                        .padding(top = 6.dp, start = 20.dp)
                        .align(Alignment.Start),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.padding(top = 6.dp))
            }

            LoginButton(modifier = Modifier,
                enabled = !showPasswordError && showMailError,
                onLoginButtonClicked = {
                    CoroutineScope(Dispatchers.IO).launch {
                        loading = true
                        val result = authController.signIn(emailText, passwordText)


                        when(result){
                            is Resource.Success -> {
                                loading = false
                                val userDataStore = UserDataStore(
                                    token = result.data?.token.toString(),
                                    id = result.data?.user?.id.toString(),
                                    email = result.data?.user?.email.toString(),
                                    clientAccountId = result.data?.user?.clientAccountId.toString(),
                                    firstName = result.data?.user?.firstName.toString(),
                                    lastName = result.data?.user?.lastName.toString() ?: ""
                                )
                                val token = result.data?.token.toString().replace("\"","").trim()
                                val resultTenant = authController.authenticateProfileME("Bearer $token")
                                when(resultTenant){
                                    is Resource.Success -> {

                                        tenantid = resultTenant.data?.tenantId.toString().replace("\"","").trim() ?: ""
                                        dataStoreController.saveTenantId( tenantid   )

                                        showLoginErrorMessage = false
                                        Log.i("Login","tenantId GUARDADO $tenantid")
                                    }

                                    is Resource.Error -> {

                                        showLoginErrorMessage = true
                                        Log.e("Login",result.message.toString())
                                    }

                                    else -> {

                                        showLoginErrorMessage = true
                                        Log.e("Login","No se pudo traer el perfil del usuario")
                                    }
                                }

                                withContext(Dispatchers.Main){
                                    dataStoreController.saveToDataStore(userDataStore)
                                    onLoginClicked(tenantid)
                                }

                                Log.i("LOGIN SCREEN", "login exitoso, ${result.data?.token } ${result.data?.user?.firstName}")
                            }

                            is Resource.Error->{
                                loading = false
                                showLoginErrorMessage = true
                                errorMessage = result.message.toString()
                                Log.e("LOGIN SCREEN", "error login: ${result.message.toString()}")
                            }

                            else ->{
                                loading = false
                                showLoginErrorMessage = false
                                errorMessage = result.message.toString()
                                Log.e("LOGIN SCREEN", "error al iniciar sesion")
                            }

                        }
                    }

                    //onLoginClicked()
                }
            )

            if(loading){
                Spacer(modifier = Modifier.padding(top = 12.dp))
                Text("Cargando...")
            }

            if(showLoginErrorMessage){
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(top = 6.dp, start = 10.dp)
                        .align(Alignment.Start),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.padding(top = 1.dp))
            }

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

            Spacer(modifier = Modifier.padding(top = 16.dp))

            SocialMediaButton(
                Modifier,
                "Iniciar Sesión con Google",
                painterResource(R.drawable.google),
                {
                    openCustomTab(context,"https://unabetted-edison-unparallel.ngrok-free.dev/api/auth/social/google/")
                }

            )
            Spacer(modifier = Modifier.padding(top = 16.dp))

            SocialMediaButton(
                Modifier,
                "Iniciar Sesión con Facebook",
                painterResource(R.drawable.facebook),
                {
                    openCustomTab(context,"https://unabetted-edison-unparallel.ngrok-free.dev/api/auth/social/facebook/")

                }
            )

        }
    }

    if(isProcessingDeepLink){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier.width(20.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
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
fun SocialMediaButton(
    modifier: Modifier,
    text: String,
    icon: Painter,
    onClick: () -> Unit = {}
){

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
        //colors = ButtonDefaults.outlinedButtonColors(   containerColor = Color.White     )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Espacio antes del icono
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = icon,
                contentDescription = "social media provider",
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre icono y texto
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }


}




@Composable
fun LoginButton(
    modifier: Modifier,
    enabled: Boolean,
    onLoginButtonClicked: () -> Unit
){
    Button(
        onClick = onLoginButtonClicked,
        modifier
            .padding(horizontal = 20.dp)
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

private fun openCustomTab(context: Context, url: String){
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}

*/






