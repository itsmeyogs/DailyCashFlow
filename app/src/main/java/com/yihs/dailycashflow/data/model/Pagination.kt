package com.yihs.dailycashflow.data.model

import com.google.gson.annotations.SerializedName

data class Pagination(

    @field:SerializedName("total")
    val total: Int,

    @field:SerializedName("lastPage")
    val lastPage: Int,

    @field:SerializedName("currentPage")
    val currentPage: Int
)
