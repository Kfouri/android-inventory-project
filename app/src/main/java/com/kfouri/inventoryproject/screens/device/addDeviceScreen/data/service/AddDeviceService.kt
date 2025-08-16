package com.kfouri.inventoryproject.screens.device.addDeviceScreen.data.service

import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AddDeviceService {

    @Multipart
    @POST("device/create")
    suspend fun addDevice(
        @Part("code") code: RequestBody,
        @Part("sets") sets: RequestBody,
        @Part("location") location: RequestBody,
        @Part("implements") implements: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Unit>

}