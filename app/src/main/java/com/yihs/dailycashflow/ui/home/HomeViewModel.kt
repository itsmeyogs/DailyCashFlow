package com.yihs.dailycashflow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.data.Result
import com.yihs.dailycashflow.data.model.RangeDateFilter
import com.yihs.dailycashflow.data.model.SummaryResponse
import com.yihs.dailycashflow.data.model.TransactionResponse
import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.utils.Constant
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _transactionHistoryState = MutableLiveData<Result<TransactionResponse>>()
    val transactionHistoryState: MutableLiveData<Result<TransactionResponse>> = _transactionHistoryState

    private val _summaryTransactionState = MutableLiveData<Result<SummaryResponse>>()
    val summaryTransactionState: MutableLiveData<Result<SummaryResponse>> = _summaryTransactionState

    private val _cashFlowSummaryTransactionState = MutableLiveData<Result<SummaryResponse>>()
    val cashFlowSummaryTransactionState: MutableLiveData<Result<SummaryResponse>> = _cashFlowSummaryTransactionState


    fun getCashFlowSummaryTransaction(){
        viewModelScope.launch {
            repository.getCashFlowSummary().collect { result ->
                _cashFlowSummaryTransactionState.value = result
            }
        }
    }
    fun getSummaryTransaction(filterRange: RangeDateFilter){
        viewModelScope.launch {
            val range = filterRange.key
            repository.getSummary(range).collect { result ->
                _summaryTransactionState.value = result
            }
        }
    }

    fun getTransactionHistory(){
        viewModelScope.launch {
            repository.getTransactionDashboard().collect { result ->
                _transactionHistoryState.value = result
            }
        }
    }


    //set data filter range default to monthly
    private val _selectedFilterRangeDate = MutableLiveData(Constant.filterRangeDateOptions.last())
    val selectedFilterRangeDate: LiveData<RangeDateFilter> = _selectedFilterRangeDate

    fun changeSelectedFilterRangeDate(value :RangeDateFilter){
        getSummaryTransaction(value)
        _selectedFilterRangeDate.value = value
    }


    fun getData(){
        getCashFlowSummaryTransaction()
        selectedFilterRangeDate.value?.let { getSummaryTransaction(it) }
        getTransactionHistory()
    }

    init {
       getData()
    }


}