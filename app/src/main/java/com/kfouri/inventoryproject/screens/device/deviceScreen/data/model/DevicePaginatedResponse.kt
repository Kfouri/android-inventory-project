package com.kfouri.inventoryproject.screens.device.deviceScreen.data.model

data class DevicePaginatedResponse(
    val info: InfoPaginatedResponse,
    val results: List<DeviceResponse>
)

data class InfoPaginatedResponse(
    val currentPage: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int,
    val nextPage: Int?
)