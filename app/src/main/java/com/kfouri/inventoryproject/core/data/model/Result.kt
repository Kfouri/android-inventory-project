package com.kfouri.inventoryproject.core.data.model

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val code: String, val message: String) : Result<Nothing>()
    object Unauthorized : Result<Nothing>()
}