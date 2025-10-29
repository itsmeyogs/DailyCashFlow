package com.yihs.dailycashflow.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.data.Result
import com.yihs.dailycashflow.data.model.DropDownItemModel
import com.yihs.dailycashflow.data.model.SummaryResponse
import com.yihs.dailycashflow.data.model.TransactionResponse
import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.utils.Constant
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: Repository) : ViewModel() {
    private val _selectedSpinnerFilterRange = MutableLiveData(Constant.filterRangeDateOptions.last())
    val selectedSpinnerFilterRange : LiveData<DropDownItemModel> = _selectedSpinnerFilterRange

    private val _selectedSpinnerTypeTransaction = MutableLiveData(Constant.filterTypeTransactionOptions.first())
    val selectedSpinnerTypeTransaction: LiveData<DropDownItemModel> = _selectedSpinnerTypeTransaction

    private val _summaryTransactionState = MutableLiveData<Result<SummaryResponse>>()
    val summaryTransactionState: LiveData<Result<SummaryResponse>> = _summaryTransactionState


    private val _transactionState = MutableLiveData<Result<TransactionResponse>>()
    val transactionState: LiveData<Result<TransactionResponse>> = _transactionState



    private fun getTransaction(type: String, filterRange: String, page: Int = 1) {
        viewModelScope.launch {
            repository.getTransaction(type, filterRange, page).collect { result->
                _transactionState.value = result
            }
        }
    }



    private fun getSummaryTransaction(filterRange: String) {
        viewModelScope.launch {
            repository.getSummary(filterRange).collect { item ->
                _summaryTransactionState.value = item
            }
        }
    }



    fun changeSelectedSpinnerFilterRange(value: DropDownItemModel){
        _selectedSpinnerFilterRange.value = value
        getData()
    }

    fun changeSelectedSpinnerTypeTransaction(value: DropDownItemModel){
        _selectedSpinnerTypeTransaction.value = value
    }

    fun getData(){
        _selectedSpinnerFilterRange.value?.let { getSummaryTransaction(it.key) }
    }

}