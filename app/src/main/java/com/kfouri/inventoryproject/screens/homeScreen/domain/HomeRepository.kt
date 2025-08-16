package com.kfouri.inventoryproject.screens.homeScreen.domain

import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.homeScreen.data.model.HomeResponse

interface HomeRepository {
    suspend fun getHomeData(): Result<HomeResponse>
}