package com.yihs.dailycashflow.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.data.ResultForm
import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.data.model.RegisterResponse
import com.yihs.dailycashflow.data.model.User
import com.yihs.dailycashflow.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository) : ViewModel() {

    private  val _loginState = MutableStateFlow<ResultForm<LoginResponse>>(ResultForm.Idle)
    val loginState : StateFlow<ResultForm<LoginResponse>> = _loginState
    private val _registerState = MutableStateFlow<ResultForm<RegisterResponse>> (ResultForm.Idle)
    val registerState : StateFlow<ResultForm<RegisterResponse>> = _registerState


    fun getSession(): Flow<User> {
        return repository.getSession()
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            repository.login(email, password).collect {
                if(it is ResultForm.Success) {
                    repository.saveSession(it.data)
                }
                _loginState.value = it
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