package com.kfouri.inventoryproject.screens.homeScreen.data.service

import com.kfouri.inventoryproject.screens.homeScreen.data.model.HomeResponse
import retrofit2.Response
import retrofit2.http.GET

interface HomeService {
    @GET("home")
    suspend fun getHome(): Response<HomeResponse>
}