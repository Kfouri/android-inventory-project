package com.kfouri.inventoryproject.screens.device.deviceScreen.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.service.DeviceService
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.model.DeviceModel
import okio.IOException
import javax.inject.Inject

class DevicePagingSource @Inject constructor(
    private val api: DeviceService,
    private val query: String
) : PagingSource<Int, DeviceModel>() {
    override fun getRefreshKey(state: PagingState<Int, DeviceModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DeviceModel> {
        return try {

            val page = params.key ?: 1
            val response = api.getDevicesPaginated(page, query)
            val devices = response.results
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = response.info.nextPage

            val data = devices.map { it.toModel() }
            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

}