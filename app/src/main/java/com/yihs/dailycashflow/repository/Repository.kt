package com.yihs.dailycashflow.repository

import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.data.model.User
import com.yihs.dailycashflow.data.preferences.UserPreference
import com.yihs.dailycashflow.data.remote.ApiService
import com.yihs.dailycashflow.utils.Constant
import com.yihs.dailycashflow.utils.Helper
import kotlinx.coroutines.flow.Flow

class Repository(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun saveSession(data: LoginResponse) = userPreference.saveSession(data)

    fun getSession() : Flow<User> = userPreference.getSession()

    suspend fun removeSession() = userPreference.removeSession()


    fun login(email: String, password: String) = Helper.apiCall { apiService.login(email, password) }
    fun register(name: String, email: String, password: String)  = Helper.apiCall { apiService.register(name, email, password) }

    fun getTransactionDashboard(orderBy: String = Constant.ORDER_BY_NEWEST, type: String = Constant.CATEGORY_TYPE_ALL, page: Int = 1) = Helper.apiCall { apiService.getTransaction(orderBy, type, page) }

    fun getSummary(range: String) = Helper.apiCall { apiService.getSummary(range) }

}