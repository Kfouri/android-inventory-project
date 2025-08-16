package com.kfouri.inventoryproject.screens.device.deviceScreen.domain

import androidx.paging.PagingData
import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceResponse
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceScreenResponse
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.model.DeviceModel
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getDevicesPaginated(query: String): Flow<PagingData<DeviceModel>>
    suspend fun getDevicesScreen(): Result<DeviceScreenResponse>
    suspend fun getDevices(): Result<List<DeviceResponse>>
    suspend fun getDeviceById(id: Int): Result<DeviceResponse>

    suspend fun updateDevice(id: Int, device: DeviceResponse): Result<DeviceResponse>
    suspend fun deleteDevice(id: Int): Result<Unit>
}