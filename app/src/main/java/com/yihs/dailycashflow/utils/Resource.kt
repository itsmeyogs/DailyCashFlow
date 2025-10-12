package com.yihs.dailycashflow.utils

sealed class Resource<out T> {
    object Idle : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T, val code: Int?) : Resource<T>()
    data class Error(val message: String, val code: Int?) : Resource<Nothing>()
}