package com.kfouri.inventoryproject.screens.device.deviceScreen.domain.usecase

import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceScreenResponse
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.DeviceRepository

class GetDeviceScreenUseCase(private val deviceRepository: DeviceRepository) {
    suspend operator fun invoke(): Result<DeviceScreenResponse> {
        return deviceRepository.getDevicesScreen()
    }
}