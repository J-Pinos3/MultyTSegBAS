package com.seguridadbas.multytenantseguridadbas.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.NumberPicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chargemap.compose.numberpicker.NumberPicker
import com.seguridadbas.multytenantseguridadbas.R
import com.seguridadbas.multytenantseguridadbas.controllers.visitlogscontroller.VisitLogController
import com.seguridadbas.multytenantseguridadbas.model.visitorlogs.IdPhoto
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasBackground
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow

@Preview(showSystemUi = true)
@Composable
fun VisitLogScreen(
    //visitLogController: VisitLogController
){

    var visitDate by remember { mutableStateOf("") }
    var exitTime by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    var numPeople by remember { mutableStateOf(1) }

    //file data
    val photoList: ArrayList<IdPhoto> = arrayListOf()


    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = BasBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ){

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BasYellow)
                .padding(top = 50.dp, bottom = 50.dp)
        ){
            Image(
                painter = painterResource(R.drawable.baslogo),
                contentDescription = "Logo de bas"
            )
        }

        Spacer(modifier = Modifier.padding(top=16.dp))

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start),
            text = "Ingrese el nombre:",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top=16.dp))

        VisitLogTextField(
            modifier = Modifier,
            typedText = firstName,
            onTextChange = {
                currentText -> firstName = currentText
            },
            placeholder = "Nombre:",
            isSingleLine = true, maxLines = 1
        )

        Spacer(modifier = Modifier.padding(top=16.dp))

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start),
            text = "Ingrese el apellido:",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top=16.dp))

        VisitLogTextField(
            modifier = Modifier,
            typedText = lastName,
            onTextChange = {
                    currentText -> lastName = currentText
            },
            placeholder = "Apellido:",
            isSingleLine = true, maxLines = 1
        )

        Spacer(modifier = Modifier.padding(top=16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ){
            Column(
                modifier = Modifier.weight(0.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Text(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.Start),
                    text = "Ingrese la cédula:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.padding(top=16.dp))

                VisitLogTextField(
                    modifier = Modifier,
                    typedText = idNumber,
                    onTextChange = {
                            currentText -> idNumber = currentText
                    },
                    placeholder = "Cédula:",
                    isSingleLine = true, maxLines = 1
                )
            }

            NumberPicker(
                modifier = Modifier.padding(end = 15.dp),
                value = numPeople,
                range = 1..20,
                onValueChange = {
                    numPeople = it
                }
            )
        }

        Spacer(modifier = Modifier.padding(top=16.dp))

        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.Start),
            text = "Razón de ingreso:",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.padding(top=16.dp))

        VisitLogTextField(
            modifier = Modifier,
            typedText = reason,
            onTextChange = {
                    currentText -> reason = currentText
            },
            placeholder = "Razón:",
            isSingleLine = false, maxLines = 3
        )
    }

}


@Composable
private fun VisitLogTextField(
    modifier: Modifier, typedText: String,
    onTextChange: (String) -> Unit, placeholder: String, isSingleLine: Boolean, maxLines: Int
){
    TextField(
        value = typedText,
        onValueChange = onTextChange,
        modifier = modifier.fillMaxWidth().padding(horizontal = 15.dp),
        placeholder = { Text(text=placeholder) },
        singleLine = isSingleLine, maxLines = maxLines,
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
private fun TakePhotoButton(
    modifier: Modifier,
    onButtonClicked: () -> Unit
){
    Button(
        onClick = onButtonClicked,
        modifier = modifier.padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Text(
            text = "Tomar foto",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}



private fun hasCameraPermission(context: Context) = ContextCompat.checkSelfPermission(
    context, Manifest.permission.CAMERA
) == PackageManager.PERMISSION_GRANTED



private fun requestCameraPermissions(context: Context){
    var permissionsArray: Array<String>
    if( !hasCameraPermission( context ) ){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permissionsArray = arrayOf(
                Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES
            )
        }else{
            permissionsArray = arrayOf(
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        ActivityCompat.requestPermissions(
            context as Activity, permissionsArray, 0
        )
    }
}