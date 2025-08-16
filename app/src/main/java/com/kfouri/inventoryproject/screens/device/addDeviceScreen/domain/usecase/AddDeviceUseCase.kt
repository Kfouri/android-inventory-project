package com.kfouri.inventoryproject.screens.device.addDeviceScreen.domain.usecase

import android.net.Uri
import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.domain.AddDeviceRepository

class AddDeviceUseCase(private val repository: AddDeviceRepository) {
    suspend operator fun invoke(
        code: String,
        sets: String,
        location: String,
        implements: String,
        imageUri: Uri?
    ): Result<Unit> {
        return repository.addDevice(
            code = code,
            sets = sets,
            implements = implements,
            location = location,
            fileUri = imageUri
        )
    }
}