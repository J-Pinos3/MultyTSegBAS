package com.seguridadbas.multytenantseguridadbas.controllers.authcontroller

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

    private val _bearerToken = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val bearerToken = _bearerToken.asStateFlow()



     fun signUp(email:String, password: String){
        val user = User(email, password)
        viewModelScope.launch {     _bearerToken.emit(Resource.Loading())      }
        var errorMessage: String = ""
        viewModelScope.launch {
            val response = authenticationRepository.signUp(user = user)
            if( response.isSuccessful && response.body()?.length != null ){
                response!!.body()?.let { token ->
                    _bearerToken.emit(Resource.Success(token))
                }

            }else{
                errorMessage = response.errorBody().toString()
                _bearerToken.emit(Resource.Error(errorMessage))
            }
        }

    }

}