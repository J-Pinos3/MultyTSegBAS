package com.seguridadbas.multytenantseguridadbas.signin

import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.authcontroller.AuthController
import com.seguridadbas.multytenantseguridadbas.controllers.repository.AuthenticationRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class AuthControllerSignInTest {

    private val repository = mockk<AuthenticationRepository>()
    private val controller = AuthController(repository)

    @Test
    fun `signIn returns success when backend role is customer`() = runTest {
        coEvery { repository.signInRepo(any()) } returns Response.success(
            signInJson(role = "customer")
        )

        val result = controller.signIn("john@doe.com", "Password1!")

        assertTrue(result is Resource.Success)
        val success = result as Resource.Success
        assertEquals("abc-token", success.data?.token?.replace("\"", ""))
        assertEquals("customer", success.data?.user?.tenant?.roles?.firstOrNull())
    }

    @Test
    fun `signIn returns error when role is not customer`() = runTest {
        coEvery { repository.signInRepo(any()) } returns Response.success(
            signInJson(role = "guard")
        )

        val result = controller.signIn("john@doe.com", "Password1!")

        assertTrue(result is Resource.Error)
        assertEquals("Por favor use la app de guardias", (result as Resource.Error).message)
    }

    @Test
    fun `signIn returns error without crashing when roles array is empty`() = runTest {
        coEvery { repository.signInRepo(any()) } returns Response.success(
            signInJson(role = null)
        )

        val result = controller.signIn("john@doe.com", "Password1!")

        assertTrue(result is Resource.Error)
        assertEquals("Por favor use la app de guardias", (result as Resource.Error).message)
    }

    @Test
    fun `signIn returns backend error message when response is unsuccessful`() = runTest {
        val errorBody =
            "{\"message\":\"Credenciales invalidas\"}".toResponseBody("application/json".toMediaType())
        coEvery { repository.signInRepo(any()) } returns Response.error(401, errorBody)

        val result = controller.signIn("john@doe.com", "Password1!")

        assertTrue(result is Resource.Error)
        assertEquals("Credenciales invalidas", (result as Resource.Error).message)
    }

    private fun signInJson(role: String?): JsonObject {
        val json = JsonObject()
        val user = JsonObject()
        val tenant = JsonObject()
        val roles = com.google.gson.JsonArray()

        if (role != null) {
            roles.add(role)
        }

        tenant.add("roles", roles)
        user.addProperty("id", "1")
        user.addProperty("email", "john@doe.com")
        user.addProperty("firstName", "John")
        user.addProperty("lastName", "Doe")
        user.addProperty("clientAccountId", "ca-1")
        user.add("tenant", tenant)

        json.addProperty("token", "abc-token")
        json.add("user", user)
        return json
    }
}

