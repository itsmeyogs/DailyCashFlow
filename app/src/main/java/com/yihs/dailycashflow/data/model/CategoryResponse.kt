package com.yihs.dailycashflow.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("data")
	val data: List<Category>,

	@field:SerializedName("message")
	val message: String
)