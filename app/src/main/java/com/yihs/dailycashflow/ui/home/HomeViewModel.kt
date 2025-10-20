package com.yihs.dailycashflow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.data.Result
import com.yihs.dailycashflow.data.model.RangeDateFilter
import com.yihs.dailycashflow.data.model.Summary
import com.yihs.dailycashflow.data.model.TransactionResponse
import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.utils.Constant
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _transactionHistoryState = MutableLiveData<Result<TransactionResponse>>()
    val transactionHistoryState: MutableLiveData<Result<TransactionResponse>> = _transactionHistoryState

    fun getTransactionHistory(){
        viewModelScope.launch {
            repository.getTransactionDashboard().collect { result ->
                _transactionHistoryState.value = result
            }
        }
    }


    //set data filter range default to monthly
    private val _selectedFilterRangeDate = MutableLiveData(Constant.filterRangeDateOptions[3])
    val selectedFilterRangeDate: LiveData<RangeDateFilter> = _selectedFilterRangeDate

    fun changeSelectedFilterRangeDate(value :RangeDateFilter){
        _selectedFilterRangeDate.value = value
    }

    //pie chart
    val exampleDataPieChart = Summary(income = 200000f, expense = 60000f)


    init {
        getTransactionHistory()
    }


}