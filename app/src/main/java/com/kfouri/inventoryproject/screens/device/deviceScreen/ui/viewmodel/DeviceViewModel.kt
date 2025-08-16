package com.kfouri.inventoryproject.screens.device.deviceScreen.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.usecase.GetDeviceScreenUseCase
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.usecase.GetDevicesPaginatedUseCase
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.DeviceUiState
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.ErrorModel
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.ScreenStatus
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.model.DeviceModel

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val getDevicesPaginatedUseCase: GetDevicesPaginatedUseCase,
    private val getDeviceScreenUseCase: GetDeviceScreenUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(DeviceUiState())
    val uiState: StateFlow<DeviceUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchedDevices = MutableStateFlow<PagingData<DeviceModel>>(PagingData.empty())
    val searchedDevices = _searchedDevices.asStateFlow()

    init {
        getDevices()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchDevices(query: String) {
        viewModelScope.launch {
            getDevicesPaginatedUseCase.invoke(query).cachedIn(viewModelScope).collect {
                _searchedDevices.value = it
            }
        }
    }

    private fun getDevices() {
        _uiState.value = _uiState.value.copy(screenStatus = ScreenStatus.LOADING)
        viewModelScope.launch {
            val response = getDeviceScreenUseCase.invoke()
            when (response) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        screenData = response.data,
                        screenStatus = ScreenStatus.SUCCESS
                    )
                    searchDevices("")
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        screenStatus = ScreenStatus.ERROR,
                        error = ErrorModel(response.code, response.message)
                    )
                }
                Result.Unauthorized -> {
                    _uiState.value = _uiState.value.copy(
                        screenStatus = ScreenStatus.UNAUTHORIZED
                    )
                }
            }
        }
    }
}