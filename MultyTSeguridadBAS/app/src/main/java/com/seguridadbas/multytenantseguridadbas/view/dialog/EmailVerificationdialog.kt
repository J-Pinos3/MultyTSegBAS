package com.seguridadbas.multytenantseguridadbas.view.dialog


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun EmailVerificationDialog(email:String, onClose: () -> Unit){

    AlertDialog(
        modifier = Modifier,
        onDismissRequest = onClose,
        title ={ Text(text = "Verificación") },
        text = { Text(  text = "Hemos enviado un correo de verificación a:\n $email \nRevisa tu correo para continuar" ) },
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