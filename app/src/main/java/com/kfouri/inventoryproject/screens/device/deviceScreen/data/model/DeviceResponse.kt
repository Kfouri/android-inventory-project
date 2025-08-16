package com.kfouri.inventoryproject.screens.device.deviceScreen.data.model

import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.model.DeviceModel

data class DeviceResponse(
    val id: Int,
    val code: String?,
    val sets: String?,
    val location: String?,
    val implements: String?,
    val imageUrl: String?
) {
    fun toModel(): DeviceModel {
        return DeviceModel(
            id = id,
            code = code,
            sets = sets,
            location = location,
            implements = implements,
            imageUrl = imageUrl
        )
    }
}