package com.yihs.dailycashflow.utils

import com.yihs.dailycashflow.data.model.DropDownItemModel

class Constant {
    companion object{

        const val DATE_WITH_DAY_NAME = "EEEE, dd MMMM yyyy"
        const val DATE_WITHOUT_DAY_NAME = "dd MMMM yyyy"
        const val DATE_WITHOUT_DAY_NAME_WITH_SHORT_MONTH_NAME = "dd MMM yyyy"

        const val CATEGORY_TYPE_ALL = "ALL"
        const val CATEGORY_TYPE_INCOME = "INCOME"
        const val CATEGORY_TYPE_EXPENSE = "EXPENSE"

        const val RESULT_BODY_NULL = "Response Body null"
        const val RESULT_UNKNOWN_ERROR = "An Unknown error occurred"


        val filterRangeDateOptions = listOf(
            DropDownItemModel("all", "Semua"),
            DropDownItemModel("daily", "Hari ini"),
            DropDownItemModel("weekly", "Minggu ini"),
            DropDownItemModel("monthly", "Bulan Ini")
        )

        val filterTypeTransactionOptions = listOf(
            DropDownItemModel(CATEGORY_TYPE_ALL, "Semua Transaksi"),
            DropDownItemModel(CATEGORY_TYPE_INCOME, "Pemasukan"),
            DropDownItemModel(CATEGORY_TYPE_EXPENSE, "Pengeluaran")
        )

    }
}