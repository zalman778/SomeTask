package com.hwx.myapplication.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun ByteArray.compressImageToSizeInKb(maxSizeKb: Int = 100): ByteArray {
    val maxSizeInBytes = maxSizeKb * 1024
    var quality = 100
    var compressedBytes = this
    var bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)

    var width = bitmap.width
    var height = bitmap.height

    do {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        val outputStream = ByteArrayOutputStream()
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream)
        compressedBytes = outputStream.toByteArray()

        quality -= 25
        width = (width * 0.5).toInt()
        height = (height * 0.5).toInt()

        if (width <= 0 || height <= 0) break

        bitmap = scaledBitmap // Update bitmap for next iteration
    } while (compressedBytes.size > maxSizeInBytes && quality > 0)

    return compressedBytes
}