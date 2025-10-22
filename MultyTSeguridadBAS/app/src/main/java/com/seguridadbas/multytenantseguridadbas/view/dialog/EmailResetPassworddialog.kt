package com.seguridadbas.multytenantseguridadbas.view.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seguridadbas.multytenantseguridadbas.view.titleTexts

@Composable
fun EmailResetPasswordDialog( email: String ,onClose: () -> Unit) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = onClose,
        title ={ Text(text = "Recuperar contraseña") },
        text = { Text(  text = "Hemos enviado un correo a:\n $email \nRevisa tu correo para continuar" ) },
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