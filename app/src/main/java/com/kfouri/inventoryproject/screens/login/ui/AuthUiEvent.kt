package com.kfouri.inventoryproject.screens.login.ui

sealed class AuthUiEvent {
    data class SignUpEmailChanged(val email: String) : AuthUiEvent()
    data class SignUpPasswordChanged(val password: String) : AuthUiEvent()
    data class SignUpNameChanged(val name: String) : AuthUiEvent()
    object SignUp : AuthUiEvent()

    data class SignInEmailChanged(val email: String) : AuthUiEvent()
    data class SignInPasswordChanged(val password: String) : AuthUiEvent()
    object SignIn : AuthUiEvent()

    data class Error(val message: String) : AuthUiEvent()
    data object TogglePasswordVisibility : AuthUiEvent()

    object ShowSuccessSignUp: AuthUiEvent()
}