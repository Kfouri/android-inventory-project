package com.kfouri.inventoryproject.screens.device.addDeviceScreen.data

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.kfouri.inventoryproject.core.data.model.ErrorResponse
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.data.service.AddDeviceService
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.domain.AddDeviceRepository
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceResponse
import com.kfouri.inventoryproject.core.data.model.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.SocketTimeoutException

class AddDeviceRepositoryImpl(
    private val api: AddDeviceService,
    @ApplicationContext private val context: Context
): AddDeviceRepository {
    override suspend fun addDevice(code: String, sets: String, location: String, implements: String, fileUri: Uri?): Result<Unit> {
        return try {

            val codePart = code.toRequestBody("text/plain".toMediaTypeOrNull())
            val setsPart = sets.toRequestBody("text/plain".toMediaTypeOrNull())
            val locationPart = location.toRequestBody("text/plain".toMediaTypeOrNull())
            val implementsPart = implements.toRequestBody("text/plain".toMediaTypeOrNull())

            var filePart: MultipartBody.Part? = null
            fileUri?.let { uri ->
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val fileBytes = inputStream.readBytes()
                    val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
                    val requestFile = fileBytes.toRequestBody(mimeType.toMediaTypeOrNull())
                    filePart = MultipartBody.Part.createFormData("file", "${code}_device.jpg", requestFile)
                }
            }

            val response = api.addDevice(
                code = codePart,
                sets = setsPart,
                location = locationPart,
                implements = implementsPart,
                image = filePart
            )

            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val errorBody = response.errorBody()?.charStream()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return if (response.code() == 401) {
                    Result.Unauthorized
                } else {
                    Result.Error(response.code().toString(), errorResponse?.message ?: response.message().toString())
                }
            }

        } catch (e: IOException) {
            Result.Error("0", e.message.toString())
        } catch (e: SocketTimeoutException) {
            Result.Error("0", e.message.toString())
        } catch (e: Exception) {
            Result.Error("0", e.message.toString())
        }
    }
}