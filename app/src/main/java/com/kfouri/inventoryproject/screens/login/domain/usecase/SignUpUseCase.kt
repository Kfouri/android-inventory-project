package com.kfouri.inventoryproject.screens.login.domain.usecase

import com.kfouri.inventoryproject.screens.login.data.model.AuthResult
import com.kfouri.inventoryproject.screens.login.data.model.SignUpResponse
import com.kfouri.inventoryproject.screens.login.data.model.SignUpBody
import com.kfouri.inventoryproject.screens.login.domain.LoginRepository

class SignUpUseCase(private val loginRepository: LoginRepository) {
    suspend operator fun invoke(body: SignUpBody): AuthResult<SignUpResponse> {
        return loginRepository.signUp(body)
    }
}