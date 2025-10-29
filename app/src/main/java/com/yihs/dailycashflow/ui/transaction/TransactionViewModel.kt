package com.yihs.dailycashflow.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yihs.dailycashflow.data.Result
import com.yihs.dailycashflow.data.model.DropDownItemModel
import com.yihs.dailycashflow.data.model.SummaryResponse
import com.yihs.dailycashflow.data.model.Transaction
import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.utils.Constant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: Repository) : ViewModel() {
    private val _selectedSpinnerFilterRange = MutableStateFlow(Constant.filterRangeDateOptions.last())
    val selectedSpinnerFilterRange : StateFlow<DropDownItemModel> = _selectedSpinnerFilterRange

    private val _selectedSpinnerTypeTransaction = MutableStateFlow(Constant.filterTypeTransactionOptions.first())
    val selectedSpinnerTypeTransaction: Flow<DropDownItemModel> = _selectedSpinnerTypeTransaction


    //combine filters
    private val filters = combine(selectedSpinnerTypeTransaction, selectedSpinnerFilterRange){ type, range ->
        Pair(type.key, range.key)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactionPagingData : LiveData<PagingData<Transaction>> = filters.flatMapLatest { (typeKey, rangeKey) ->
        repository.getTransactionPaging(typeKey, rangeKey).cachedIn(viewModelScope)
    }.asLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val summaryTransactionState : LiveData<Result<SummaryResponse>> = filters.flatMapLatest { (_, rangeKey) ->
        repository.getSummary(rangeKey)
    }.asLiveData()

    fun changeSelectedSpinnerFilterRange(value: DropDownItemModel){
        _selectedSpinnerFilterRange.value = value
    }

    fun changeSelectedSpinnerTypeTransaction(value: DropDownItemModel){
        _selectedSpinnerTypeTransaction.value = value
    }

}