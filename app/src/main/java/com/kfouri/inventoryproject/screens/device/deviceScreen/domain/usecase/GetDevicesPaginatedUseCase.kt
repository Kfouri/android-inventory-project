package com.kfouri.inventoryproject.screens.device.deviceScreen.domain.usecase

import androidx.paging.PagingData
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.DeviceRepository
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.model.DeviceModel
import kotlinx.coroutines.flow.Flow

class GetDevicesPaginatedUseCase(private val deviceRepository: DeviceRepository) {
    operator fun invoke(query: String): Flow<PagingData<DeviceModel>> {
        return deviceRepository.getDevicesPaginated(query)
    }
}