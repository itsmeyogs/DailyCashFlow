package com.yihs.dailycashflow.data.model

import com.google.gson.annotations.SerializedName

data class SummaryResponse(

	@field:SerializedName("data")
	val data: Summary,

	@field:SerializedName("message")
	val message: String
)

data class Summary(

	@field:SerializedName("income")
	val income: Long,

	@field:SerializedName("total")
	val total: Long,

	@field:SerializedName("balance")
	val balance: Long,

	@field:SerializedName("range")
	val range: String,

	@field:SerializedName("expense")
	val expense: Long
)
