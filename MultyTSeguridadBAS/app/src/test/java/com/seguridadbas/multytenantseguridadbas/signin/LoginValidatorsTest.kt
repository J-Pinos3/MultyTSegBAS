package com.seguridadbas.multytenantseguridadbas.signin

import com.seguridadbas.multytenantseguridadbas.core.util.validators
import org.junit.Assert.assertEquals
import org.junit.Test

class LoginValidatorsTest {

    @Test
    fun `validatePassword returns empty string for a valid password`() {
        val result = validators.validatePassword("Valid1!")

        assertEquals("", result)
    }

    @Test
    fun `validatePassword returns uppercase error when missing uppercase`() {
        val result = validators.validatePassword("valid1!")

        assertEquals("\nLa contrasena debe contener al menos una mayuscula", normalize(result))
    }

    @Test
    fun `validatePassword returns digit error when missing digit`() {
        val result = validators.validatePassword("ValidPass!")

        assertEquals("\nLa contrasena debe contener al menos un numero", normalize(result))
    }

    @Test
    fun `validatePassword returns special-character error when missing special character`() {
        val result = validators.validatePassword("ValidPass1")

        assertEquals("\nLa contrasena debe contener al menos un caracter especial", normalize(result))
    }

    @Test
    fun `validatePassword returns length error when all other rules pass but length is invalid`() {
        val result = validators.validatePassword("Aa1!")

        assertEquals("\nLa contrasena debe tener entre 7 y 18 caracteres", normalize(result))
    }

    private fun normalize(message: String): String {
        return message
            .replace("ñ", "n")
            .replace("ú", "u")
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
    }
}

