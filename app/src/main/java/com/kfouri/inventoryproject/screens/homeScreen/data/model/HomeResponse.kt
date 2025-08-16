package com.kfouri.inventoryproject.screens.homeScreen.data.model

data class HomeResponse(
    val title: String?,
    val list: List<HomeItem>?
)

data class HomeItem(
    val icon: String,
    val title: String,
    val description: String,
    val route: String
)