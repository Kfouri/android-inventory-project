package com.kfouri.inventoryproject.screens.device.addDeviceScreen.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.domain.usecase.AddDeviceUseCase
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.ui.model.AddDeviceEvent
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.ui.model.AddDeviceUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddDeviceViewModel @Inject constructor(
    private val addDeviceUseCase: AddDeviceUseCase
) : ViewModel() {

    var state by mutableStateOf(AddDeviceUiState())
    private val _resultChannel = Channel<Result<Unit>>()
    val resultChannel = _resultChannel.receiveAsFlow()

    fun onEvent(event: AddDeviceEvent) {
        when (event) {
            is AddDeviceEvent.CodeChanged -> {
                state = state.copy(code = event.code, codeError = null)
            }

            is AddDeviceEvent.ImageChanged -> {
                state = state.copy(imageUri = event.imageUri)
            }

            is AddDeviceEvent.ImplementsChanged -> {
                state = state.copy(implements = event.implements, implementsError = null)
            }

            is AddDeviceEvent.LocationChanged -> {
                state = state.copy(location = event.location, locationError = null)
            }

            is AddDeviceEvent.SetsChanged -> {
                state = state.copy(sets = event.sets, setsError = null)
            }

            AddDeviceEvent.Create -> {
                state = state.copy(error = "")
                if (state.code.isBlank()) {
                    state = state.copy(codeError = "El Código no puede estar vacío")
                    return
                }
                if (state.sets.isBlank()) {
                    state = state.copy(setsError = "Los Conjuntos no pueden estar vacíos")
                    return
                }
                if (state.location.isBlank()) {
                    state = state.copy(locationError = "La Ubicación no puede estar vacía")
                    return
                }
                if (state.implements.isBlank()) {
                    state = state.copy(implementsError = "Los Implementos no pueden estar vacíos")
                    return
                }
                createDevice(
                    code = state.code,
                    imageUri = state.imageUri,
                    implements = state.implements,
                    location = state.location,
                    sets = state.sets
                )
            }

            is AddDeviceEvent.Error -> {
                state = state.copy(error = event.message)
            }

            is AddDeviceEvent.ImageSelected -> {
                state = state.copy(imageUri = event.uri)
            }

            AddDeviceEvent.AddImage -> {

            }
        }
    }

    fun createDevice(
        code: String,
        imageUri: Uri?,
        implements: String,
        location: String,
        sets: String
    ) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val response = addDeviceUseCase.invoke(
                code = code,
                sets = sets,
                location = location,
                implements = implements,
                imageUri = imageUri
            )
            _resultChannel.send(response)
            state = state.copy(isLoading = false)
        }
    }
}