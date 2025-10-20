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


