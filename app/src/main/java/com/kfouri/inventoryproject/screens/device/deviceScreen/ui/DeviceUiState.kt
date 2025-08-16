package com.kfouri.inventoryproject.screens.device.deviceScreen.ui

import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceResponse
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceScreenResponse

enum class ScreenStatus {
    LOADING,
    ERROR,
    SUCCESS,
    UNAUTHORIZED
}

data class DeviceUiState(
    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val screenData: DeviceScreenResponse? = null,
    val devices: List<DeviceResponse> = emptyList(),
    val error: ErrorModel? = null
)

data class ErrorModel(
    val code: String,
    val message: String
)
