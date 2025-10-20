package com.yihs.dailycashflow.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("data")
	val data: List<Category>,

	@field:SerializedName("message")
	val message: String
)

data class Category(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String
)
