package com.kfouri.inventoryproject.screens.device.deviceScreen.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.kfouri.inventoryproject.core.data.model.ErrorResponse
import com.kfouri.inventoryproject.core.data.model.Result
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceResponse
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.model.DeviceScreenResponse
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.service.DeviceService
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.DeviceRepository
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.model.DeviceModel
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.net.SocketTimeoutException

class DeviceRepositoryImpl(
    private val api: DeviceService,
) : DeviceRepository {

    companion object {
        const val MAX_ITEMS = 20
        const val PRE_FETCH = 3
    }

    override fun getDevicesPaginated(query: String): Flow<PagingData<DeviceModel>> {
        return Pager(
            config = PagingConfig(pageSize = MAX_ITEMS, prefetchDistance = PRE_FETCH),
            pagingSourceFactory = {
                DevicePagingSource(api, query)
            }).flow
    }

    override suspend fun getDevicesScreen(): Result<DeviceScreenResponse> {
        return try {
            val response = api.getDeviceScreen()

            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.charStream()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return if (response.code() == 401) {
                    Result.Unauthorized
                } else {
                    Result.Error(response.code().toString(), errorResponse.message)
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

    override suspend fun getDevices(): Result<List<DeviceResponse>> {
        return try {
            val response = api.getDevices()

            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.charStream()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return if (response.code() == 401) {
                    Result.Unauthorized
                } else {
                    Result.Error(response.code().toString(), errorResponse.message)
                }
            }

        } catch (e: SocketTimeoutException) {
            Result.Error("0", e.message.toString())
        } catch (e: Exception) {
            Result.Error("0", e.message.toString())
        }
    }

    override suspend fun getDeviceById(id: Int): Result<DeviceResponse> {
        return try {
            val response = api.getDeviceById(id)

            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.charStream()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                return if (response.code() == 401) {
                    Result.Unauthorized
                } else {
                    Result.Error(response.code().toString(), errorResponse.message)
                }
            }

        } catch (e: SocketTimeoutException) {
            Result.Error("0", e.message.toString())
        } catch (e: Exception) {
            Result.Error("0", e.message.toString())
        }
    }

    override suspend fun updateDevice(
        id: Int,
        device: DeviceResponse
    ): Result<DeviceResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDevice(id: Int): Result<Unit> {
        TODO("Not yet implemented")
    }
}