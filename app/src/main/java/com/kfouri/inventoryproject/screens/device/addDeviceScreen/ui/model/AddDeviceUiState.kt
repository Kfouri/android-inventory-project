package com.kfouri.inventoryproject.screens.device.addDeviceScreen.ui.model

import android.net.Uri
import java.io.File

data class AddDeviceUiState(
    val code: String = "",
    val codeError: String? = "",
    val sets: String = "",
    val setsError: String? = "",
    val location: String = "",
    val locationError: String? = "",
    val implements: String = "",
    val implementsError: String? = "",
    val imageUri: Uri? = null,
    val error: String? = "",
    val isLoading: Boolean = false
)

sealed class AddDeviceEvent {
    data class CodeChanged(val code: String) : AddDeviceEvent()
    data class SetsChanged(val sets: String) : AddDeviceEvent()
    data class LocationChanged(val location: String) : AddDeviceEvent()
    data class ImplementsChanged(val implements: String) : AddDeviceEvent()
    data class ImageChanged(val imageUri: Uri) : AddDeviceEvent()
    data class ImageSelected(val uri: Uri) : AddDeviceEvent()
    data class Error(val message: String) : AddDeviceEvent()

    object Create : AddDeviceEvent()
    object AddImage : AddDeviceEvent()
}