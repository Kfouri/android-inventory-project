package com.kfouri.inventoryproject.screens.login.data

import android.content.SharedPreferences
import com.kfouri.inventoryproject.screens.login.data.model.AuthResult
import com.kfouri.inventoryproject.screens.login.data.model.SignInBody
import com.kfouri.inventoryproject.screens.login.data.model.SignUpBody
import com.kfouri.inventoryproject.screens.login.data.service.LoginService
import com.kfouri.inventoryproject.screens.login.domain.LoginRepository
import androidx.core.content.edit
import com.google.gson.Gson
import com.kfouri.inventoryproject.core.data.model.ErrorResponse
import com.kfouri.inventoryproject.di.AppModule.AUTH_TOKEN_KEY
import com.kfouri.inventoryproject.screens.login.data.model.SignUpResponse
import java.net.SocketTimeoutException

class LoginRepositoryImpl(
    private val api: LoginService,
    private val prefs: SharedPreferences
) : LoginRepository {

    override suspend fun signUp(body: SignUpBody): AuthResult<SignUpResponse> {
        return try {
            val response = api.signUp(body)
            if (response.isSuccessful) {
                AuthResult.AuthorizedSignUp(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.charStream()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return if (response.code() == 401) {
                    AuthResult.Unauthorized
                } else {
                    AuthResult.UnknownError(response.code().toString(), errorResponse.message)
                }
            }
        } catch (e: SocketTimeoutException) {
            AuthResult.UnknownError("0", e.message.toString())
        } catch (e: Exception) {
            AuthResult.UnknownError("0", e.message.toString())
        }
    }

    override suspend fun signIn(body: SignInBody): AuthResult<Unit> {
        return try {
            val response = api.signIn(body)
            prefs.edit {
                putString(AUTH_TOKEN_KEY, response.body()?.token)
            }
            if (response.isSuccessful) {
                AuthResult.AuthorizedSignIn(Unit)
            } else {
                val errorBody = response.errorBody()?.charStream()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return if (response.code() == 401) {
                    AuthResult.Unauthorized
                } else {
                    AuthResult.UnknownError(response.code().toString(), errorResponse.message)
                }
            }
        } catch (e: SocketTimeoutException) {
            AuthResult.UnknownError("0", e.message.toString())
        } catch (e: Exception) {
            AuthResult.UnknownError("0", e.message.toString())
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString(AUTH_TOKEN_KEY, null) ?: return AuthResult.Unauthorized

            val response = api.authenticate("Bearer $token")
            if (response.isSuccessful) {
                AuthResult.AuthorizedSignIn(Unit)
            } else {
                val errorBody = response.errorBody()?.charStream()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return if (response.code() == 401) {
                    AuthResult.Unauthorized
                } else {
                    AuthResult.UnknownError(response.code().toString(), errorResponse.message)
                }
            }
        } catch (e: SocketTimeoutException) {
            AuthResult.UnknownError("0", e.message.toString())
        } catch (e: Exception) {
            AuthResult.UnknownError("0", e.message.toString())
        }
    }
}