package com.seguridadbas.multytenantseguridadbas.core.util

object validators {

    private val specialCharacters = "!@#$%^&*()_+{}:\"<>?,./;'[]\\|`~"

    fun validatePassword(password: String): String{
        var message: String = ""

        if( !password.contains("@") ||
            !password.contains(".")
        ){
            message = "El correo debe contener al menos un @ y un ."
        }

        if(password.length !in (7..18) ){
            message = "La contraseña debe tener entre 7 y 18 caracteres"
        }

        val hasUpperCase = password.any {
            it.isLowerCase()
        }

        if(hasUpperCase){
            message = "La contraseña debe contener al menos una mayúscula"
        }

        val hasDigits = password.any {
            it.isDigit()
        }

        if(!hasDigits) {
            message = "La contraseña debe contener al menos un número"
        }

        val hasSpecialCharacters = password.any {
            specialCharacters.contains(it)
        }

        if(!hasSpecialCharacters){
            message = "La contraseña debe contener al menos un carácter especial"
        }



        return message
    }

}