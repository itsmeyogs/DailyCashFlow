package com.yihs.dailycashflow.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("tokenType")
	val tokenType: String,

	@field:SerializedName("token")
	val token: String,

	@field:SerializedName("user")
	val user: UserResponse,
)

data class UserResponse(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("email")
	val email: String
)


