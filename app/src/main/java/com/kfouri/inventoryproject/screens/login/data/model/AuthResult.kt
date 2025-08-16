package com.kfouri.inventoryproject.screens.login.data.model

sealed class AuthResult<out T> {
    data class AuthorizedSignUp<out T>(val data: T) : AuthResult<T>()
    data class AuthorizedSignIn<T>(val data: T) : AuthResult<T>()
    data class UnknownError(val code: String, val message: String) : AuthResult<Nothing>()
    object Unauthorized : AuthResult<Nothing>()
}