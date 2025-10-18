package com.yihs.dailycashflow.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieEntry
import com.yihs.dailycashflow.data.model.RangeDateFilter
import com.yihs.dailycashflow.repository.Repository

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

    val pieChartEntries = ArrayList<PieEntry>()

    init {
        pieChartEntries.add(PieEntry(200000f, "Income"))
        pieChartEntries.add(PieEntry(60000f, "Expense"))
    }







}