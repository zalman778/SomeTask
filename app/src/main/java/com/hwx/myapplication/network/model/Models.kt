package com.hwx.myapplication.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys


@JsonIgnoreUnknownKeys
@Serializable
data class ProductResponse(
    val id: Int? = null,
    val title: String? = null,
    val images: List<String>? = null,
)

@JsonIgnoreUnknownKeys
@Serializable
data class MainResponse(
    val products: List<ProductResponse>? = null,
)