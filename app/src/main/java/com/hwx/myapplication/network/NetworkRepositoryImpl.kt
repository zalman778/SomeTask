package com.hwx.myapplication.network

import android.util.Log
import com.hwx.myapplication.network.model.MainResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://dummyjson.com/products"
private const val TIME_OUT = 6000

class NetworkRepositoryImpl : INetworkRepository {

    val ktorClient = HttpClient(Android) {
        install(JsonFeature)

        KotlinxSerializer(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })

        engine {
            connectTimeout = TIME_OUT
            socketTimeout = TIME_OUT
        }
    }

    override suspend fun fetchData(offset: Int, pageSize: Int): Result<MainResponse> {
        return try {
            val targetUrl = "$BASE_URL?limit=$pageSize&skip=$offset"
            val result = ktorClient.get<MainResponse> {
                url(targetUrl)
            }
            Result.success(result)
        } catch (e: Exception) {
            Log.e("ATAG", "[fetchData]", e)
            Result.failure(e)
        }
    }

    override suspend fun fetchImage(url: String): Result<ByteArray> {
        return try {
            val result = ktorClient.get<ByteArray> {
                url(url)
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}