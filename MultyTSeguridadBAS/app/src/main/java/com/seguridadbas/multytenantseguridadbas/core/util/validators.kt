package com.seguridadbas.multytenantseguridadbas.core.util

object validators {

    private val specialCharacters = "!@#$%^&*()_+{}:\"<>?,./;'[]\\|`~"

    fun validatePassword(password: String): String{
        var message: String = ""

        if(password.length !in (7..18) ){
            message += "\nLa contraseña debe tener entre 7 y 18 caracteres"
        }

        val hasUpperCase = password.any {
            it.isUpperCase()
        }

        if(!hasUpperCase){
            message = "\nLa contraseña debe contener al menos una mayúscula"
        }

        val hasDigits = password.any {
            it.isDigit()
        }

        if(!hasDigits) {
            message = "\nLa contraseña debe contener al menos un número"
        }

        val hasSpecialCharacters = password.any {
            specialCharacters.contains(it)
        }

        if(!hasSpecialCharacters){
            message = "\nLa contraseña debe contener al menos un carácter especial"
        }



        return message
    }

}