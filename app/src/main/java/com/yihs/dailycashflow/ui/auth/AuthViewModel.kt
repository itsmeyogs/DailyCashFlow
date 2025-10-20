package com.yihs.dailycashflow.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.data.Result
import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.data.model.RegisterResponse
import com.yihs.dailycashflow.data.model.User
import com.yihs.dailycashflow.repository.Repository
import kotlinx.coroutines.launch


class AuthViewModel(private val repository: Repository) : ViewModel() {


    private val _loginState = MutableLiveData<Result<LoginResponse>>()
    val loginState: MutableLiveData<Result<LoginResponse>> = _loginState

    private val _registerState = MutableLiveData<Result<RegisterResponse>>()
    val registerState: MutableLiveData<Result<RegisterResponse>> = _registerState


    fun login(email: String, password: String){
        viewModelScope.launch {
            repository.login(email, password).collect { result->
                _loginState.value = result
            }
        }
    }

    fun register(name: String, email:String, password:String){
        viewModelScope.launch {
            repository.register(name, email, password).collect { result ->
                _registerState.value = result
            }
        }
    }

    fun saveSession(data: LoginResponse){
        viewModelScope.launch {
            repository.saveSession(data)
        }
    }

    fun getSession(): LiveData<User>{
        return repository.getSession().asLiveData()
    }








}