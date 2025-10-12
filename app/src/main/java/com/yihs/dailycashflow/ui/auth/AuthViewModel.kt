package com.yihs.dailycashflow.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository) : ViewModel() {
    private  val _loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.Idle)
    val loginState : StateFlow<Resource<LoginResponse>> = _loginState

    fun login(email: String, password: String){
        viewModelScope.launch {
            repository.login(email, password).collect {
                _loginState.value = it
            }
        }
    }


     fun saveSession(data: LoginResponse){
        viewModelScope.launch {
            repository.saveSession(data)
        }
    }

}