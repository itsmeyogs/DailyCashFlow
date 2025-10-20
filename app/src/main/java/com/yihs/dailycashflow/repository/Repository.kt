package com.yihs.dailycashflow.repository

import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.data.model.TransactionResponse
import com.yihs.dailycashflow.data.model.User
import com.yihs.dailycashflow.data.preferences.UserPreference
import com.yihs.dailycashflow.data.remote.ApiService
import com.yihs.dailycashflow.utils.Constant
import com.yihs.dailycashflow.utils.Helper
import com.yihs.dailycashflow.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun saveSession(data: LoginResponse) = userPreference.saveSession(data)

    fun getSession() : Flow<User> = userPreference.getSession()

    suspend fun removeSession() = userPreference.removeSession()


    fun login(email: String, password: String) = Helper.apiCall { apiService.login(email, password) }
    fun register(name: String, email: String, password: String)  = Helper.apiCall { apiService.register(name, email, password) }


    fun transaction(orderBy: String = Constant.ORDER_BY_NEWEST, type: String = Constant.CATEGORY_TYPE_ALL, page: Int = 1) : Flow<Resource<TransactionResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getCashFlows(orderBy, type, page)
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