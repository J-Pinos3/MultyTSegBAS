package com.seguridadbas.multytenantseguridadbas.view

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.core.util.validators
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//@Preview(showSystemUi = true)
@Composable
fun MyAccountScreen(
    modifier: Modifier = Modifier,
    authController: AuthController
) {

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var showOldPasswordError by remember { mutableStateOf(false) }
    var showNewPasswordError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }


    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    //GETTING TOKEN FROM DATASTORE PREFERENCES


    LaunchedEffect(Unit) {

        CoroutineScope(Dispatchers.IO).launch {
            val result = authController.authenticateProfileME("Bearer ey")

            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Success -> {
                        fullName = result.data?.fullName.toString()
                        email = result.data?.email.toString()
                        phone = result.data?.phoneNumber.toString()
                    }

                    is Resource.Error -> {
                        fullName = "---"
                        email = "---"
                        phone = "---"
                    }

                    else -> {
                        Log.e("MyAccount","No se pudo traer el perfil del usuario")
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
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
                .padding(start = 10.dp)
                .align(Alignment.CenterHorizontally),
            text = "Mi Perfil",
            textDecoration = TextDecoration.Underline,
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
            textData = fullName,
            onTextDataChange = {
                newName -> fullName = newName
            },
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
            onTextDataChange = {
                    newEmail -> email = newEmail
            },
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
            onTextDataChange = {
                newPhone -> phone = newPhone
            },
            placeholder = "numero de telefono: ",
            keyboardTypes = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        UpdateProfileButton(
            modifier = Modifier,
            enabled = true,
            onUpdateProfileClick = {

            }
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterHorizontally),
            text = "Contraseña",
            textDecoration = TextDecoration.Underline,
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )

        //contraseña vieja
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start),
            textAlign = TextAlign.Start,
            text = "Contraseña Actual",
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))

        TextFieldsProfile(
            modifier = Modifier,
            textData = oldPassword,
            onTextDataChange = {
                    currentText -> oldPassword = currentText

                passwordErrorMessage = validators.validatePassword(oldPassword)
                if( passwordErrorMessage.isNullOrEmpty() ){
                    showOldPasswordError = false
                }else{
                    showOldPasswordError = true
                }
            },
            placeholder = "Contraseña actual",
            keyboardTypes = KeyboardType.Text
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        if(showOldPasswordError){
            Text(
                text = passwordErrorMessage,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp, bottom =8.dp)
                    .align( Alignment.Start ),
                fontSize = 16.sp
            )
        }

        // nueva contraseña
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start),
            textAlign = TextAlign.Start,
            text = "Contraseña Nueva",
            fontSize = 19.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))

        TextFieldsProfile(
            modifier = Modifier,
            textData = newPassword,
            onTextDataChange = {
                    currentText -> newPassword = currentText

                passwordErrorMessage = validators.validatePassword(newPassword)
                if( passwordErrorMessage.isNullOrEmpty() ){
                    showNewPasswordError = false
                }else{
                    showNewPasswordError = true
                }
            },
            placeholder = "Contraseña nueva",
            keyboardTypes = KeyboardType.Text
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        if(showNewPasswordError){
            Text(
                text = passwordErrorMessage,
                color = Color.Red,
                modifier = Modifier
                    .padding(top = 8.dp, start = 20.dp, bottom =8.dp)
                    .align( Alignment.Start ),
                fontSize = 16.sp
            )
        }

        UpdatePasswordButton(
            modifier = Modifier,
            enabled = !showOldPasswordError && !showNewPasswordError,
            onUpdatePasswordClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val result =authController.changePassword(oldPassword, newPassword)

                    withContext(Dispatchers.Main){
                        when(result){
                            is Resource.Success -> {
                                Log.i("MyAccount", "Contraseña actualizada, USER ID = ${result.data.toString()}")
                            }
                            is Resource.Error -> {
                                Log.e("MyAccount", result.message.toString())
                            }
                            else -> {
                                Log.e("MyAccount", result.message.toString())
                            }
                        }
                    }
                }
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
            text = "Actualizar Perfil",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
    }

}


@Composable
fun UpdatePasswordButton(
    modifier: Modifier,
    enabled: Boolean,
    onUpdatePasswordClick: () -> Unit
){

    Button(
        onClick = onUpdatePasswordClick,
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
            text = "Actualizar Contraseña",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp
        )
    }

}
