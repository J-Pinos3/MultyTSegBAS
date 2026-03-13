package com.seguridadbas.multytenantseguridadbas.view

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBlue
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import com.seguridadbas.multytenantseguridadbas.view.dialog.EmailResetPasswordDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//@Preview(showSystemUi = true)
@Composable
fun ResetPasswordScreen(
    authController: AuthController
){

    var emailText by remember { mutableStateOf("") }
    val validEmail = emailText.isNotEmpty() && emailText.contains("@") && emailText.contains(".")

    var showResetScreenDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color= BasBackground)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasBlue)
                .padding(top =  30.dp, bottom = 30.dp)
        ){
            Image(
                painter = painterResource(R.drawable.cguardimage),
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

        if(!validEmail){
            showResetScreenDialog = false
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

        ResetButton(
            modifier = Modifier,
            enabled = validEmail,
            onResetClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val result = authController.sendResetPasswordEmail(emailText)

                    withContext(Dispatchers.Main){
                        when(result){
                            is Resource.Success ->{
                                showResetScreenDialog = true
                                Toast.makeText(context, "Correo enviado: ${result.data.toString()}", Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Error ->{
                                showResetScreenDialog = false
                                Toast.makeText(context, "Error el enviar el correo: ${result.message.toString()}", Toast.LENGTH_SHORT).show()
                            }
                            else ->{
                                showResetScreenDialog = false
                                Toast.makeText(context, "Error el enviar el correo: ${result.message.toString()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }


                }
            }
        )


        if( showResetScreenDialog ){
            EmailResetPasswordDialog(
                email = emailText,
                onClose = {
                    showResetScreenDialog = false
                    emailText = ""
                }
            )
        }



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


