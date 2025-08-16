package com.kfouri.inventoryproject.screens.login.domain

import com.kfouri.inventoryproject.screens.login.data.model.AuthResult
import com.kfouri.inventoryproject.screens.login.data.model.SignUpResponse
import com.kfouri.inventoryproject.screens.login.data.model.SignInBody
import com.kfouri.inventoryproject.screens.login.data.model.SignUpBody

interface LoginRepository {
    suspend fun signUp(body: SignUpBody): AuthResult<SignUpResponse>
    suspend fun signIn(body: SignInBody): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}