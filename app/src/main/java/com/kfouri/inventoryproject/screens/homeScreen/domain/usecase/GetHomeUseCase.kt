package com.kfouri.inventoryproject.screens.homeScreen.domain.usecase

import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.homeScreen.data.model.HomeResponse
import com.kfouri.inventoryproject.screens.homeScreen.domain.HomeRepository

class GetHomeUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(): Result<HomeResponse> {
        return homeRepository.getHomeData()
    }
}