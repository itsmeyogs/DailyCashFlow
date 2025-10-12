package com.yihs.dailycashflow.repository

import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.data.remote.ApiService
import com.yihs.dailycashflow.utils.Helper
import com.yihs.dailycashflow.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository(private val apiService: ApiService) {

    fun login(email: String, password: String) : Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading)
        try{
            val response = apiService.login(email, password)
            if(response.isSuccessful){
                response.body()?.let {
                    emit(Resource.Success(it, response.code()))
                }?: run{
                    emit(Resource.Error("Response Body null", response.code()))
                }
            }else{
                val errorJson = response.errorBody()?.string()
                emit(Resource.Error(Helper.parseErrorMessage(errorJson), response.code()))
            }
        }catch (e: Exception){
            emit(Resource.Error(e.message ?: "An Unknown error occurred", null))
        }
    }



}