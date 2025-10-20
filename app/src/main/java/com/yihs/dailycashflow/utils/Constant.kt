package com.yihs.dailycashflow.utils

import com.yihs.dailycashflow.data.model.RangeDateFilter

class Constant {
    companion object{

        const val CATEGORY_TYPE_ALL = "ALL"
        const val CATEGORY_TYPE_INCOME = "INCOME"
        const val CATEGORY_TYPE_EXPENSE = "EXPENSE"

        const val ORDER_BY_NEWEST = "newest"
        const val ORDER_BY_OLDEST = "oldest"

        const val RESULT_BODY_NULL = "Response Body null"
        const val RESULT_UNKNOWN_ERROR = "An Unknown error occurred"


        val filterRangeDateOptions = listOf(
            RangeDateFilter("all", "Semua"),
            RangeDateFilter("daily", "Hari ini"),
            RangeDateFilter("weekly", "Minggu ini"),
            RangeDateFilter("monthly", "Bulan Ini")
        )

    }
}