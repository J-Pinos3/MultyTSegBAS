package com.seguridadbas.multytenantseguridadbas.view

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller.DataStoreController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.core.util.validators
import com.seguridadbas.multytenantseguridadbas.model.UserDataStore
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
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
    authController: AuthController
){

    var passwordVisible by remember { mutableStateOf(false) }
    var passwordText by remember { mutableStateOf("") }

    var showPasswordError by remember { mutableStateOf(true) }
    var passwordErrorMessage by remember { mutableStateOf("") }

    var emailText by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    var tenantid by remember { mutableStateOf("") }

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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BasBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasYellow)
                .padding(top = 50.dp, bottom = 30.dp)
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


        Spacer(modifier = Modifier.padding(top = 8.dp))

        if(showPasswordError){
            Text(
                text = passwordErrorMessage,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp)
                    .align(Alignment.Start),
                fontSize = 16.sp

            )

            Spacer(modifier = Modifier.padding(top = 8.dp))
        }

        if(!showMailError){
            Text(
                text = "El formato de correo no es válido",
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp)
                    .align(Alignment.Start),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))
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
                                firstName = result.data?.user?.firstName.toString(),
                                lastName = result.data?.user?.lastName.toString() ?: ""
                            )
                            val token = result.data?.token.toString().replace("\"","").trim()
                            val resultTenant = authController.authenticateProfileME("Bearer $token")
                                when(resultTenant){
                                    is Resource.Success -> {

                                        tenantid = resultTenant.data?.tenantId.toString().replace("\"","").trim() ?: ""
                                        dataStoreController.saveTenantId( tenantid   )

                                        Log.i("Login","tenantId GUARDADO $tenantid")
                                    }

                                    is Resource.Error -> {
                                        Log.e("Login",result.message.toString())
                                    }

                                    else -> {
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
                            Log.e("LOGIN SCREEN", "error login: ${result.message.toString()}")
                        }

                        else ->{
                            loading = false
                            Log.e("LOGIN SCREEN", "error al iniciar sesion")
                        }

                    }
                }

                //onLoginClicked()
            }
        )

        if(loading){
            Spacer(modifier = Modifier.padding(top = 24.dp))
            Text("Cargandoo")
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








