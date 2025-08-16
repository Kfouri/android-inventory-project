package com.kfouri.inventoryproject.screens.device.deviceScreen.domain.usecase

import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceResponse
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.DeviceRepository

class GetDeviceByIdUseCase(private val deviceRepository: DeviceRepository) {
    suspend operator fun invoke(id: Int): Result<DeviceResponse> {
        return deviceRepository.getDeviceById(id)
    }
}