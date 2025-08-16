package com.kfouri.inventoryproject.screens.homeScreen.ui

import com.kfouri.inventoryproject.screens.homeScreen.data.model.HomeResponse

sealed interface HomeStateUI {
    data object Loading: HomeStateUI
    data class Success(val data: HomeResponse): HomeStateUI
    data class Error(val status: String, val message: String): HomeStateUI
    data object Unauthorized: HomeStateUI
}

