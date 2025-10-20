package com.yihs.dailycashflow.data.model

import com.google.gson.annotations.SerializedName

data class TransactionResponse(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("data")
	val data: List<Transaction>,

	@field:SerializedName("message")
	val message: String
)

data class Transaction(

	@field:SerializedName("timeStamp")
	val timeStamp: Int,

	@field:SerializedName("amount")
	val amount: Long,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: Category
)

