package com.yihs.dailycashflow.data

sealed class ResultForm<out T> {
    object Idle : ResultForm<Nothing>()
    object Loading : ResultForm<Nothing>()
    data class Success<T>(val data: T, val code: Int?) : ResultForm<T>()
    data class Error(val message: String, val code: Int?) : ResultForm<Nothing>()
}