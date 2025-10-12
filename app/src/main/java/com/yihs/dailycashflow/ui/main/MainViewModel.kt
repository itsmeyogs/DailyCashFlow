package com.yihs.dailycashflow.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.data.model.User
import com.yihs.dailycashflow.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {

    fun getSession(): LiveData<User>{
        return repository.getSession().asLiveData()
    }

    fun removeSession(){
        viewModelScope.launch {
            repository.removeSession()
        }
    }

}