package com.yihs.dailycashflow.data.model

data class DropDownItemModel(
    val key:String,
    val value: String
){
    override fun toString(): String {
        return value
    }
}