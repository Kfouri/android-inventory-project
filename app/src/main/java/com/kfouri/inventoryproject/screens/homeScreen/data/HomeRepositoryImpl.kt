package com.kfouri.inventoryproject.screens.homeScreen.data

import com.google.gson.Gson
import com.kfouri.inventoryproject.core.data.model.ErrorResponse
import com.kfouri.inventoryproject.screens.homeScreen.data.model.HomeResponse
import com.kfouri.inventoryproject.screens.homeScreen.data.service.HomeService
import com.kfouri.inventoryproject.screens.homeScreen.domain.HomeRepository
import java.net.SocketTimeoutException
import com.kfouri.inventoryproject.core.data.model.Result

class HomeRepositoryImpl(
    private val api: HomeService,
) : HomeRepository {

    override suspend fun getHomeData(): Result<HomeResponse> {
        return try {
            val response = api.getHome()

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
}