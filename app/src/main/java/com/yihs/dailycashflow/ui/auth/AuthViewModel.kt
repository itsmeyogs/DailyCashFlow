package com.yihs.dailycashflow.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.data.model.RegisterResponse
import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository) : ViewModel() {
    private  val _loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.Idle)
    val loginState : StateFlow<Resource<LoginResponse>> = _loginState

    private val _registerState = MutableStateFlow<Resource<RegisterResponse>>(Resource.Idle)
    val registerState : StateFlow<Resource<RegisterResponse>> = _registerState

    fun login(email: String, password: String){
        viewModelScope.launch {
            repository.login(email, password).collect {
                _loginState.value = it
                if(it is Resource.Success){
                    repository.saveSession(it.data)
                }
            }
        }
    }

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            repository.register(name,email,password).collect {
                _registerState.value = it
            }
        }
    }



}