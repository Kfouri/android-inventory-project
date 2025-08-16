package com.kfouri.inventoryproject.screens.login.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kfouri.inventoryproject.screens.login.data.model.AuthResult
import com.kfouri.inventoryproject.screens.login.data.model.SignInBody
import com.kfouri.inventoryproject.screens.login.data.model.SignUpBody
import com.kfouri.inventoryproject.screens.login.data.model.SignUpResponse
import com.kfouri.inventoryproject.screens.login.domain.usecase.AuthenticateUseCase
import com.kfouri.inventoryproject.screens.login.domain.usecase.SignInUseCase
import com.kfouri.inventoryproject.screens.login.domain.usecase.SignUpUseCase
import com.kfouri.inventoryproject.screens.login.ui.AuthState
import com.kfouri.inventoryproject.screens.login.ui.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val authenticateUseCase: AuthenticateUseCase
): ViewModel() {

    var state by mutableStateOf(AuthState())
    private val resultChannel = Channel<AuthResult<Unit>>()
    private val resultSignUpChannel = Channel<AuthResult<SignUpResponse>>()
    val signUpResults = resultSignUpChannel.receiveAsFlow()
    val authResults = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.SignInEmailChanged -> {
                state = state.copy(signInEmail = event.email, emailError = null)
            }
            is AuthUiEvent.SignInPasswordChanged -> {
                state = state.copy(signInPassword = event.password, passwordError = null)
            }
            AuthUiEvent.SignIn -> {
                state = state.copy(error = "")
                if (state.signInEmail.isBlank()) {
                    state = state.copy(emailError = "El Email no puede estar vacío")
                    return
                }
                if (state.signInPassword.length < 6) {
                    state = state.copy(passwordError = "La contraseña debe ser mayor a 6 caracteres")
                    return
                }
                signIn(
                    email = state.signInEmail,
                    password = state.signInPassword
                )
            }
            AuthUiEvent.SignUp -> {
                state = state.copy(error = "")
                signUp(
                    email = state.signUpEmail,
                    password = state.signUpPassword,
                    name = state.signUpName
                )
            }
            is AuthUiEvent.SignUpEmailChanged -> {
                state = state.copy(signUpEmail = event.email)
            }
            is AuthUiEvent.SignUpNameChanged -> {
                state = state.copy(signUpName = event.name)
            }
            is AuthUiEvent.SignUpPasswordChanged -> {
                state = state.copy(signUpPassword = event.password)
            }
            is AuthUiEvent.Error -> {
                state = state.copy(error = event.message)
            }

            AuthUiEvent.TogglePasswordVisibility -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }

            AuthUiEvent.ShowSuccessSignUp -> {
                state = state.copy(successSignUp = true)
            }
        }
    }


    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val response = signUpUseCase.invoke(
                SignUpBody(
                    email = email,
                    password = password,
                    name = name
                )
            )
            resultSignUpChannel.send(response)
            state = state.copy(isLoading = false)
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val response = signInUseCase.invoke(
                SignInBody(
                    email = email,
                    password = password,
                )
            )
            resultChannel.send(response)
            state = state.copy(isLoading = false)
        }
    }

    fun authenticate() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val response = authenticateUseCase.invoke()
            state = state.copy(isLoading = false)
            resultChannel.send(response)
        }
    }
}