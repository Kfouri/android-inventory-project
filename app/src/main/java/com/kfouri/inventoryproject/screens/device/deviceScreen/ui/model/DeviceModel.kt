package com.kfouri.inventoryproject.screens.device.deviceScreen.ui.model

import com.google.gson.annotations.SerializedName

data class DeviceModel(
    val id: Int,
    val code: String?,
    val sets: String?,
    val location: String?,
    val implements: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)