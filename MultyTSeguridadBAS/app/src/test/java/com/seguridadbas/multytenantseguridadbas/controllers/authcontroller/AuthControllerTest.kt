package com.seguridadbas.multytenantseguridadbas.controllers.authcontroller

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.repository.AuthenticationRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.User
import com.seguridadbas.multytenantseguridadbas.model.UserProfile
import com.seguridadbas.multytenantseguridadbas.model.oldNewPasswords
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthControllerTest {

    private val testUser = User("Reverb1@outlook.es", "Reverbpass")
    private val fakeBearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijk5ZjA0NGM1LTYxZjItNGM1NS04NjQzLWZjZjg3YWYwYWMzZSIsImlhdCI6MTc2ODk0MzE5MCwiZXhwIjoxNzY5NTQ3OTkwfQ.9Ix847666sZuey_EpIQAoYflhomHM8B4cy4Lqc_X9Ao"

    @RelaxedMockK
    private lateinit var authRepository: AuthenticationRepository

    lateinit var authController: AuthController

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        authController = AuthController(authRepository)
    }


    @Test
    fun `when the email or passwords are empty or the have incorrect format`() = runBlocking {

        val jsonError = JsonObject().apply {
            addProperty("error","error al iniciar sesión")
            addProperty("message","fatalerror")
        }

        val errorResponse = Response.error<JsonObject>(
            400,
            okhttp3.ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                jsonError.toString()
            )
        )

        //Given
        coEvery { authRepository.signInRepo(testUser) } returns errorResponse

        //When
        val result = authController.signIn(testUser.mail, testUser.password)


        print("seccess: ${authRepository.signInRepo(testUser).isSuccessful}   ${result.message}")

        assert(result is Resource.Error)


    }


    @Test
    fun `case when both passwords are correct when the user is changing his password`() = runBlocking {
        val testPasswords = oldNewPasswords("oldPassword", "oldPassword")

        val jsonSuccess = JsonObject().apply {
            addProperty("message", "success")
        }
        val response: Response<JsonObject> = Response.success(
            200,
            jsonSuccess
        )
        //GIVEN
        coEvery { authRepository.changePasswordRepo(fakeBearerToken, testPasswords) } returns response


        //WHEN
        val result = authController.changePassword(fakeBearerToken, testPasswords)


        //THEN
        assert(result is Resource.Success)
        assertTrue(testPasswords.oldPassword == testPasswords.newPassword)
    }


    @Test
    fun `given a valid bearer token, return users profile por auth`() = runBlocking {
        val tenantsArray = JsonArray().apply {
            add(JsonObject().apply {
                addProperty("tenantId", "ssssdddlllk")
            })
        }

        val jsonUserObj = JsonObject().apply{
            addProperty("id", "111-111-2")
            addProperty("fullName", "Pepe pinos")
            addProperty("firstName", "Pepe")
            addProperty("lastName", "Pinos")
            addProperty("email", "pepe@mail.com")
            addProperty("provider", "google")
            addProperty("phoneNumber", "123456789")
            add("tenants", tenantsArray) // ← ARRAY, no tenantId directo
        }

        val response = Response.success( jsonUserObj )

        //GIVEN
        coEvery { authRepository.authenticateMeRepo(fakeBearerToken) } returns response


        //WHEN
        val result = authController.authenticateProfileME(fakeBearerToken)


        //THEN
        assertEquals("ssssdddlllk", (result as Resource.Success).data!!.tenantId.replace("\"", ""))
        assertEquals("pepe@mail.com", (result as Resource.Success).data!!.email.replace("\"", ""))

    }

}