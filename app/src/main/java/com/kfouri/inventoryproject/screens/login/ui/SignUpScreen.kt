package com.kfouri.inventoryproject.screens.login.ui

import android.util.Log
import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.kfouri.inventoryproject.R
import com.kfouri.inventoryproject.screens.component.TopBar
import com.kfouri.inventoryproject.screens.feedback.FeedbackModel
import com.kfouri.inventoryproject.screens.login.data.model.AuthResult
import com.kfouri.inventoryproject.screens.login.ui.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToFeedback: (FeedbackModel) -> Unit
) {

    val state = viewModel.state
    val context = LocalContext.current

    BackHandler(enabled = true, onBack = { onBack() })

    LaunchedEffect(viewModel, context) {

        viewModel.signUpResults.collect { result ->
            when (result) {
                is AuthResult.AuthorizedSignUp -> {
                    onNavigateToFeedback(
                        FeedbackModel(
                            hasBackButton = result.data.hasBackButton,
                            image = result.data.image,
                            title = result.data.title,
                            description = result.data.description,
                            errorCode = null,
                            primaryButton = result.data.button
                        )
                    )
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

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(state.signUpEmail).matches()
    val isPasswordNotEmpty = state.signUpPassword.isNotEmpty()
    val isNameNotEmpty = state.signUpName.isNotEmpty()
    val isLoginButtonEnabled =
        isEmailValid && isPasswordNotEmpty && isNameNotEmpty && !state.isLoading

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = null,
                hasBackButton = true,
                onBack = {
                    onBack()
                }
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
                    text = "Crea tu cuenta",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.signUpEmail,
                        onValueChange = {
                            viewModel.onEvent(AuthUiEvent.SignUpEmailChanged(it))
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
                        value = state.signUpPassword,
                        onValueChange = { viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(it)) },
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

                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.signUpName,
                        onValueChange = { viewModel.onEvent(AuthUiEvent.SignUpNameChanged(it)) },
                        label = { Text("Nombre") },
                        placeholder = { Text("Ingresá tu nombre") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = state.nameError != null,
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
                    onClick = { viewModel.onEvent(AuthUiEvent.SignUp) },
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
                        Text("Crear usuario")
                    }
                }
            }
        }
    }
}