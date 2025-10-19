package com.yihs.dailycashflow.data.model


data class Transaction(
    val id: Int,
    val amount: Long,
    val description: String,
    val date: String,
    val category: TransactionCategory
)

data class TransactionCategory(
    val id: Int,
    val name: String,
    val type: String
)
