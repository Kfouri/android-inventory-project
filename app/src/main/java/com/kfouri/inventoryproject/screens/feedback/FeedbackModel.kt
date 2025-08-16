package com.kfouri.inventoryproject.screens.feedback

data class FeedbackModel(
    val hasBackButton: Boolean,
    val image: String,
    val title: String,
    val description: String,
    val errorCode: String?,
    val primaryButton: Button,
    val secondaryButton: Button? = null
)

data class Button(
    val title: String,
    val route: String
)