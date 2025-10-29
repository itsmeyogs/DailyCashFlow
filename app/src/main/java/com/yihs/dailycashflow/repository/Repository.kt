package com.yihs.dailycashflow.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.data.model.Transaction
import com.yihs.dailycashflow.data.model.User
import com.yihs.dailycashflow.data.preferences.UserPreference
import com.yihs.dailycashflow.data.remote.ApiService
import com.yihs.dailycashflow.data.remote.TransactionPagingSource
import com.yihs.dailycashflow.utils.Constant
import com.yihs.dailycashflow.utils.Helper
import kotlinx.coroutines.flow.Flow

class Repository(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun saveSession(data: LoginResponse) = userPreference.saveSession(data)

    fun getSession() : Flow<User> = userPreference.getSession()

    suspend fun removeSession() = userPreference.removeSession()


    fun login(email: String, password: String) = Helper.apiCall { apiService.login(email, password) }
    fun register(name: String, email: String, password: String)  = Helper.apiCall { apiService.register(name, email, password) }

    fun getCashFlowSummary() = Helper.apiCall { apiService.getSummary(Constant.filterRangeDateOptions.last().key) }

    fun getSummary(range: String) = Helper.apiCall { apiService.getSummary(range) }

    fun getTransaction(type: String, range: String, page: Int = 1) = Helper.apiCall { apiService.getTransaction(type, range, page) }

    fun getTransactionPaging(type: String, range: String) : Flow<PagingData<Transaction>>{
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                TransactionPagingSource(apiService, type, range)
            }
        ).flow
    }


}