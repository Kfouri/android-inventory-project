package com.kfouri.inventoryproject.screens.login.data.service

import com.kfouri.inventoryproject.screens.login.data.model.SignUpResponse
import com.kfouri.inventoryproject.screens.login.data.model.SignInBody
import com.kfouri.inventoryproject.screens.login.data.model.SignInResponse
import com.kfouri.inventoryproject.screens.login.data.model.SignUpBody
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {

    @POST("signup")
    suspend fun signUp(@Body body: SignUpBody): Response<SignUpResponse>

    @POST("signin")
    suspend fun signIn(@Body body: SignInBody): Response<SignInResponse>

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    ): Response<Unit>
}