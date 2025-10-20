package com.yihs.dailycashflow.data.model

import com.google.gson.annotations.SerializedName

data class Category(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("type")
    val type: String
)
