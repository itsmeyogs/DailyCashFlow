package com.yihs.dailycashflow.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("tokenType")
	val tokenType: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("user")
	val user: UserResponse? = null,
)


