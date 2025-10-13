package com.yihs.dailycashflow.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.repository.Repository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    fun removeSession(){
        viewModelScope.launch {
            repository.removeSession()
        }
    }

}