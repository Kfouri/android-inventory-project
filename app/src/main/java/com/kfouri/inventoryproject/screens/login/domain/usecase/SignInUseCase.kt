package com.kfouri.inventoryproject.screens.login.domain.usecase

import com.kfouri.inventoryproject.screens.login.data.model.AuthResult
import com.kfouri.inventoryproject.screens.login.data.model.SignInBody
import com.kfouri.inventoryproject.screens.login.domain.LoginRepository

class SignInUseCase(private val loginRepository: LoginRepository) {
    suspend operator fun invoke(body: SignInBody): AuthResult<Unit> {
        return loginRepository.signIn(body)
    }

}