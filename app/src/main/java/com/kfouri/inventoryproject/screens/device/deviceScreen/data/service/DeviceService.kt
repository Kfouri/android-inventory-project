package com.kfouri.inventoryproject.screens.device.deviceScreen.data.service

import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DevicePaginatedResponse
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceResponse
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceScreenResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT

interface DeviceService {

    @GET("device/paginated")
    suspend fun getDevicesPaginated(@Query("page") page: Int, @Query("query") query: String): DevicePaginatedResponse

    @GET("device")
    suspend fun getDevices(): Response<List<DeviceResponse>>

    @GET("device/screen")
    suspend fun getDeviceScreen(): Response<DeviceScreenResponse>

    @GET("device/{id}")
    suspend fun getDeviceById(@Query("id") id: Int): Response<DeviceResponse>

    @PUT("device/update")
    suspend fun updateDevice(@Body device: DeviceResponse): Response<DeviceResponse>

    @PUT("device/delete")
    suspend fun deleteDevice(@Body device: DeviceResponse): Response<Unit>

}