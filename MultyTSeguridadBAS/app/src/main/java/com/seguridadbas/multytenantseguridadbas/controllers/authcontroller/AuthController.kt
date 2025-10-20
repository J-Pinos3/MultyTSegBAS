package com.seguridadbas.multytenantseguridadbas.controllers.authcontroller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seguridadbas.multytenantseguridadbas.controllers.repository.AuthenticationRepository
import com.seguridadbas.multytenantseguridadbas.core.util.Resource
import com.seguridadbas.multytenantseguridadbas.model.User
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


    suspend fun signUp(email: String, password: String): Resource<String>{
        val user = User(email, password)

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