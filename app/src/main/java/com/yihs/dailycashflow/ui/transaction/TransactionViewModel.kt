package com.yihs.dailycashflow.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yihs.dailycashflow.data.model.DropDownItemModel
import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.utils.Constant

class TransactionViewModel(private val repository: Repository) : ViewModel() {
    private val _selectedSpinnerFilterRange = MutableLiveData(Constant.filterRangeDateOptions.last())
    val selectedSpinnerFilterRange : LiveData<DropDownItemModel> = _selectedSpinnerFilterRange

    private val _selectedSpinnerTypeTransaction = MutableLiveData(Constant.filterTypeTransactionOptions.first())
    val selectedSpinnerTypeTransaction: LiveData<DropDownItemModel> = _selectedSpinnerTypeTransaction


    fun changeSelectedSpinnerFilterRange(value: DropDownItemModel){
        _selectedSpinnerTypeTransaction.value = value
    }

    fun changeSelectedSpinnerTypeTransaction(value: DropDownItemModel){
        _selectedSpinnerTypeTransaction.value = value
    }


}