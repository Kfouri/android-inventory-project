package com.kfouri.inventoryproject.screens.homeScreen.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.homeScreen.domain.usecase.GetHomeUseCase
import com.kfouri.inventoryproject.screens.homeScreen.ui.HomeStateUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: GetHomeUseCase
): ViewModel() {

    private val _state = MutableStateFlow<HomeStateUI>(value = HomeStateUI.Loading)
    var state = _state.asStateFlow()

    init {
        getHomeData()
    }

    private fun getHomeData() {
        viewModelScope.launch {
            _state.emit(HomeStateUI.Loading)
            val response = getHomeUseCase.invoke()
            when (response) {
                is Result.Success -> {
                    _state.emit(HomeStateUI.Success(response.data))
                }
                is Result.Error -> {
                    _state.emit(HomeStateUI.Error(response.code, response.message))
                }
                Result.Unauthorized -> {
                    _state.emit(HomeStateUI.Unauthorized)
                }
            }
        }
    }
}