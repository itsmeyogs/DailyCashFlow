package com.yihs.dailycashflow.data.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: UserResponse
)