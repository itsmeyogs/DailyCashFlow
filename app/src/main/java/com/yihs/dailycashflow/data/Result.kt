package com.yihs.dailycashflow.data

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T, val code: Int?) : Result<T>()
    data class Error(val message: String, val code: Int?) : Result<Nothing>()
}