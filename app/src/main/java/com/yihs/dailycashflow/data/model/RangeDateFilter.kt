package com.yihs.dailycashflow.data.model

data class RangeDateFilter(
    val key:String,
    val value: String
){
    override fun toString(): String {
        return value
    }
}