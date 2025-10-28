package com.seguridadbas.multytenantseguridadbas.controllers.authcontroller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.seguridadbas.multytenantseguridadbas.controllers.repository.AuthenticationRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.SignInResponse
import com.seguridadbas.multytenantseguridadbas.model.User
import com.seguridadbas.multytenantseguridadbas.model.UserProfile
import com.seguridadbas.multytenantseguridadbas.model.UserProfileRequest
import com.seguridadbas.multytenantseguridadbas.model.UserSignInResponse
import com.seguridadbas.multytenantseguridadbas.model.oldNewPasswords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthController @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val TAG = "AUTH_CONTROLLER"


    suspend fun signUp(email: String, password: String, fullName: String): Resource<String>{
        val user = User(email, password, fullName)

        val response = authenticationRepository.signUpRepo(user)
        return if(response.isSuccessful){
            val bodyString = response.body()?.string() ?: ""
            Resource.Success(bodyString)
        }else{
            Resource.Error( response.message().toString() +"--"+
            response.body().toString()
            )

        }

    }


    suspend fun signIn(email: String, password: String): Resource<SignInResponse>{
        val user = User(email, password)

        val response = authenticationRepository.signInRepo(user)

        return if( response.isSuccessful ){
            val body = response.body()

            val userResponse = SignInResponse(
                token = body?.get("token").toString(),
                user = UserSignInResponse(
                    id = body?.getAsJsonObject("user")?.get("id").toString(),
                    email = body?.getAsJsonObject("user")?.get("email").toString(),
                    firstName = body?.getAsJsonObject("user")?.get("firstName").toString(),
                    lastName = body?.getAsJsonObject("user")?.get("lastName").toString()
                )
            )

             Resource.Success(userResponse)
        }else{
            Resource.Error( response.message().toString() +"--"+
                    response.errorBody()
            )

        }
    }


    suspend fun sendVerificationEmail(token: String, email: String): Resource<String>{
        val response = authenticationRepository.sendEmailVerificationRepo(auth_token = token, email = email)

        return if( response.isSuccessful ){
            Resource.Success(response.body().toString())
        }else{
            Resource.Error( response.message().toString() +"--"+
                    response.errorBody().toString()
            )

        }
    }


    suspend fun sendResetPasswordEmail(email: String): Resource<String>{
        val response = authenticationRepository.sendResetPasswordRepo(email)

        return if( response.isSuccessful ){
            Resource.Success(response.body().toString())
        }else{
            Resource.Error( response.message().toString() +"--"+
                    response.errorBody().toString()
            )
        }
    }


    suspend fun changePassword( token: String, passwords: oldNewPasswords): Resource<String>{
        val response = authenticationRepository.changePasswordRepo(auth_token = token, passwords = passwords)

        return if(response.isSuccessful){
            Resource.Success( response.body()?.get("id").toString() ?: "" )
        }else{
            Resource.Error( "No se pudo cambiar la contraseña"    )
        }
    }

    suspend fun authenticateProfileME(token: String): Resource<UserProfile>{
        val response = authenticationRepository.authenticateMeRepo(auth_token = token)

        val userProfile = UserProfile(
            id = response.body()?.get("id").toString(),
            fullName = response.body()?.get("fullName").toString(),
            firstName = response.body()?.get("firstName").toString(),
            lastName = response.body()?.get("lastName").toString(),
            email = response.body()?.get("email").toString(),
            provider = response.body()?.get("provider").toString(),
            phoneNumber = response.body()?.get("phoneNumber").toString()
        )

        return if( response.isSuccessful ){
            Resource.Success( userProfile )
        }else{
            Resource.Error( "No se pudo autenticar el usuario: ${response.message()} + ${response.errorBody().toString()}" )
        }
    }


    suspend fun updateProfile(token: String, newUserProfile: UserProfileRequest): Resource<String>{
        val response = authenticationRepository.updateProfileRepo(auth_token = token, data = newUserProfile)

        return if( response.isSuccessful ){
            Resource.Success( response.body().toString() )
        }else{
            Resource.Error( response.message() + response.code() + " " + response.errorBody().toString() )
        }
    }



    /*
    suspend fun verifyEmail(token: String): Resource<String> {
        val response = authenticationRepository.verifyEmailRepo(token)

        return if(response.isSuccessful){
            Resource.Success(response.body().toString()  )
        }else{

            Resource.Error( response.message().toString() +"--"+
                    response.body().toString()
            )

        }
    }
    */

}


/*
     fun signUp2(email:String, password: String){
        val user = User(email, password)
         viewModelScope.launch {
             try {
                 _bearerToken.postValue(Resource.Loading())
                 val response = authenticationRepository.signUpRepo(user = user)

                 if(response.isSuccessful){
                     _bearerToken.postValue( Resource.Success(response.body()!!) )
                 }else{
                     _bearerToken.postValue( Resource.Error(response.errorBody().toString() + response.code()) )
                 }

             }catch (e: Exception){
                 _bearerToken.postValue(Resource.Error(e.message.toString()))
             }
         }

    }

    fun verifyEmail(token: String) {
        viewModelScope.launch {
            try {
                _isEmailVerified.postValue(Resource.Loading())
                val response = authenticationRepository.verifyEmailRepo(token)
                if(response.isSuccessful){
                    _isEmailVerified.postValue(Resource.Success(response.body()!!))
                }else{
                    _isEmailVerified.postValue(Resource.Error(response.errorBody().toString() + response.code()))
                }
            }catch (e: Exception){
                _bearerToken.postValue(Resource.Error(e.message.toString()))
            }
        }
    }



verify email
viewModelScope.launch {   _isEmailVerified.emit(Resource.Loading())    }
var errorMessage: String = ""
viewModelScope.launch {
    val response = authenticationRepository.verifyEmailRepo(token = token)
    if(response.isSuccessful && response.body() != null){
        response!!.body()?.let{ isVerified ->
            _isEmailVerified.emit(Resource.Success(isVerified))
        }
    }else{
        errorMessage = response.errorBody().toString()
        _isEmailVerified.emit(Resource.Error(errorMessage))
        //Log.e(TAG, errorMessage)
    }
}
*/