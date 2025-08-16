package com.kfouri.inventoryproject.screens.login.ui

data class AuthState(
    val isLoading: Boolean = false,
    val signUpEmail: String = "",
    val signUpPassword: String = "",
    val signUpName: String = "",
    val signInEmail: String = "",
    val signInPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val nameError: String? = null,
    val error: String? = null,
    val successSignUp: Boolean = false
)