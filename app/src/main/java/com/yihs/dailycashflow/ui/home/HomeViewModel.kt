package com.yihs.dailycashflow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yihs.dailycashflow.data.model.RangeDateFilter
import com.yihs.dailycashflow.data.model.Summary
import com.yihs.dailycashflow.data.model.TransactionResponse
import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    //list filter range date
    val filterRangeDateOptions = listOf(
        RangeDateFilter("all", "Semua"),
        RangeDateFilter("daily", "Hari ini"),
        RangeDateFilter("weekly", "Minggu ini"),
        RangeDateFilter("monthly", "Bulan Ini")
    )

    //set data filter range default to monthly
    private val _selectedFilterRangeDate = MutableLiveData(filterRangeDateOptions[3])
    val selectedFilterRangeDate: LiveData<RangeDateFilter> = _selectedFilterRangeDate

    fun changeSelectedFilterRangeDate(value :RangeDateFilter){
        _selectedFilterRangeDate.value = value
    }


    //pie chart
    val exampleDataPieChart = Summary(income = 200000f, expense = 60000f)

    private val _transactionState = MutableStateFlow<Resource<TransactionResponse>>(Resource.Loading)
    val transactionState : StateFlow<Resource<TransactionResponse>> = _transactionState

    fun getTransaction(){
        viewModelScope.launch {
            repository.transaction().collect {
                _transactionState.value = it
            }
        }
    }

    init {
        getTransaction()
    }


}