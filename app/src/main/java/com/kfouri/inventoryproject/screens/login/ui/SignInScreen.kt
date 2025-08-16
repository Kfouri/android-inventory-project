package com.kfouri.inventoryproject.screens.login.ui

import android.util.Log
import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kfouri.inventoryproject.R
import com.kfouri.inventoryproject.screens.component.TopBar
import com.kfouri.inventoryproject.screens.login.data.model.AuthResult
import com.kfouri.inventoryproject.screens.login.ui.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {

        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.AuthorizedSignIn -> {
                    onNavigateToHome.invoke()
                }
                is AuthResult.Unauthorized -> {
                    Log.d("Kafu", "Error: Unauthorized")
                }
                is AuthResult.UnknownError -> {
                    viewModel.onEvent(AuthUiEvent.Error(result.message))
                    Log.d("Kafu", "LoginScreen UnknownError: ${result.message}")
                }
                else -> {
                    viewModel.onEvent(AuthUiEvent.Error("Error desconocido"))
                    Log.d("Kafu", "LoginScreen UnknownError")
                }
            }
        }

    }

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(state.signInEmail).matches()
    val isPasswordNotEmpty = state.signInPassword.isNotEmpty()
    val isLoginButtonEnabled = isEmailValid && isPasswordNotEmpty && !state.isLoading

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = null,
                hasBackButton = false,
                onBack = {}
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = padding.calculateTopPadding() + 32.dp,
                    bottom = padding.calculateBottomPadding()
                ),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                )

                Text(
                    text = "Gestioná tu inventario",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.signInEmail,
                        onValueChange = {
                            viewModel.onEvent(AuthUiEvent.SignInEmailChanged(it))
                        },
                        label = { Text("Email") },
                        placeholder = { Text("Ingresá tu Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = state.emailError != null,
                    )
                    Spacer(modifier = Modifier.height(4.dp)) // Space for error message
                    AnimatedVisibility(visible = state.emailError != null) {
                        Text(
                            text = state.emailError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.signInPassword,
                        onValueChange = { viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it)) },
                        label = { Text("Contraseña") },
                        placeholder = { Text("Ingresá tu contraseña") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { viewModel.onEvent(AuthUiEvent.TogglePasswordVisibility) }) {
                                Icon(
                                    modifier = Modifier.padding(end = 4.dp),
                                    painter = painterResource(if (state.isPasswordVisible) R.drawable.ic_eye_open else R.drawable.ic_eye_closed),
                                    contentDescription = "Password Visibility Toggle",
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = state.passwordError != null,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AnimatedVisibility(visible = state.passwordError != null) {
                        Text(
                            text = state.passwordError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }

                AnimatedVisibility(visible = state.error != null) {
                    Text(
                        text = state.error.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Button(
                    onClick = { viewModel.onEvent(AuthUiEvent.SignIn) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    enabled = isLoginButtonEnabled,
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text("Login")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Center,
                ) {
                    /*
                    TextButton(onClick = { /* Handle forgot password */ }) {
                        Text("Olvidaste tu contraseña?", fontSize = 14.sp)
                    }
                     */
                    TextButton(onClick = {
                        onNavigateToSignUp()
                    }) {
                        Text("Crear Usuario", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}