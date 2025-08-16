package com.kfouri.inventoryproject.screens.login.data.model

import com.kfouri.inventoryproject.screens.feedback.Button

data class SignUpResponse(
    val hasBackButton: Boolean,
    val image: String,
    val title: String,
    val description: String,
    val button: Button
)