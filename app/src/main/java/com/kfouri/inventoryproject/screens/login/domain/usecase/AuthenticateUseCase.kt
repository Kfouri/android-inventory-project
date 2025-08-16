package com.kfouri.inventoryproject.screens.login.domain.usecase

import com.kfouri.inventoryproject.screens.login.data.model.AuthResult
import com.kfouri.inventoryproject.screens.login.domain.LoginRepository

class AuthenticateUseCase(private val loginRepository: LoginRepository) {
    suspend operator fun invoke(): AuthResult<Unit> {
        return loginRepository.authenticate()
    }
}