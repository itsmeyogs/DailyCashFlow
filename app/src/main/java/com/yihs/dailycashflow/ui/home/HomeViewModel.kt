package com.yihs.dailycashflow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yihs.dailycashflow.data.model.RangeDateFilter
import com.yihs.dailycashflow.data.model.Summary
import com.yihs.dailycashflow.data.model.Transaction
import com.yihs.dailycashflow.data.model.TransactionCategory
import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.utils.Constant

class HomeViewModel(private val repository: Repository) : ViewModel() {

    //list filter range date
    val filterRangeDateOptions = listOf(
        RangeDateFilter("all", "Semua"),
        RangeDateFilter("daily", "Hari ini"),
        RangeDateFilter("weekly", "Minggu ini"),
        RangeDateFilter("monthly", "Bulan Ini")
    )
    private val _selectedFilterRangeDate = MutableLiveData(filterRangeDateOptions[1])
    val selectedFilterRangeDate: LiveData<RangeDateFilter> = _selectedFilterRangeDate

    fun changeSelectedFilterRangeDate(value :RangeDateFilter){
        _selectedFilterRangeDate.value = value
    }


    //pie chart
    val exampleDataPieChart = Summary(income = 200000f, expense = 60000f)

    private val _transactionHistory = MutableLiveData<List<Transaction>>()
    val transactionHistory : LiveData<List<Transaction>> = _transactionHistory


    init {
        val data = arrayListOf<Transaction>()
        for(i in 1..10){
            var item : Transaction
            if(i % 5 == 0){
                item = Transaction(
                    id = i,
                    amount = 100000,
                    description = "Gajian",
                    date = "1760865109",
                    category = TransactionCategory(
                        id = 1,
                        name = "Income",
                        type = Constant.CATEGORY_TYPE_INCOME
                    )
                )
            }else{
             item = Transaction(
                    id = i,
                    amount = 100000,
                    description = "Makan Padang",
                    date = "1760865109",
                    category = TransactionCategory(
                        id = 2,
                        name = "Makan",
                        type = Constant.CATEGORY_TYPE_EXPENSE
                    )
                )
            }

            data.add(item)
        }

        _transactionHistory.value = data
    }




}