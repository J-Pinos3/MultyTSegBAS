package com.seguridadbas.multytenantseguridadbas.view.dialog

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seguridadbas.multytenantseguridadbas.ui.theme.BasYellow

@Preview(showSystemUi = true)
@Composable
fun LoadingDialog(message: String = "", onClose: ()-> Unit = {}){

    AlertDialog(
        modifier = Modifier,
        onDismissRequest = onClose,
        title ={ Text(text = "Cargando") },
        text = { Text(  text = message ) },
        confirmButton = {
            Button(
                onClick = {
                    onClose()
                }
            ) {

                Text(text = "Aceptar")
            }

        }

    )


}