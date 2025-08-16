package com.kfouri.inventoryproject.screens.device.addDeviceScreen.domain

import android.net.Uri
import com.kfouri.inventoryproject.core.data.model.Result

interface AddDeviceRepository {

    suspend fun addDevice(code: String, sets: String, location: String, implements: String, fileUri: Uri?): Result<Unit>

}