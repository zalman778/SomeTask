package com.hwx.myapplication.network

import com.hwx.myapplication.network.model.MainResponse

interface INetworkRepository {

    suspend fun fetchData(
        offset: Int,
        pageSize: Int,
    ): Result<MainResponse>

    suspend fun fetchImage(
        url: String,
    ): Result<ByteArray>
}