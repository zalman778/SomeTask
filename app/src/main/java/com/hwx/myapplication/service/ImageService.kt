package com.hwx.myapplication.service

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.hwx.myapplication.network.INetworkRepository
import com.hwx.myapplication.util.compressImageToSizeInKb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
class ImageService(
    private val networkRepository: INetworkRepository,
) {

    sealed interface ImageState {
        data object Initial : ImageState
        data object Error : ImageState
        data object Loading : ImageState
        data class Ready(val bitmap: ImageBitmap) : ImageState
    }

    val scope = CoroutineScope(Dispatchers.IO)

    private val imageCache = MutableStateFlow<Map<String, ImageState>>(emptyMap())
    private val stateMutex = Mutex()

    fun getImageState(
        url: String,
    ): Flow<ImageState> {
        requestData(url)
        return imageCache.map { it[url] ?: ImageState.Initial }
    }

    private fun requestData(url: String) {
        val currentState = imageCache.value[url]
        if (currentState == ImageState.Loading || currentState is ImageState.Ready) return

        scope.launch {
            stateMutex.withLock {
                imageCache.value = imageCache.value.toMutableMap().apply {
                    this[url] = ImageState.Loading
                }
            }

            val result = networkRepository.fetchImage(url)
            Log.i("ATAG", "[requestData] result=$result")

            if (result.isFailure) {
                stateMutex.withLock {
                    imageCache.value = imageCache.value.toMutableMap().apply {
                        this[url] = ImageState.Error
                    }
                }
            } else {
                val imageBytes = result.getOrNull()!!
                val compressedBytes = imageBytes.compressImageToSizeInKb(maxSizeKb = 100)
                val imageBitmap = BitmapFactory.decodeByteArray(
                    compressedBytes, 0, compressedBytes.size
                ).asImageBitmap()
                stateMutex.withLock {
                    imageCache.value = imageCache.value.toMutableMap().apply {
                        this[url] = ImageState.Ready(imageBitmap)
                    }
                }
            }
        }
    }
}