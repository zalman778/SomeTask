package com.hwx.myapplication.service

import com.hwx.myapplication.domain.DomainListItem
import com.hwx.myapplication.network.INetworkRepository

class NetworkService(
    private val networkRepository: INetworkRepository,
) {

    suspend fun fetchData(
        offset: Int,
        pageSize: Int,
    ): Result<List<DomainListItem>> {
        return networkRepository.fetchData(offset, pageSize)
            .map {
                it.products.orEmpty().mapNotNull { listItem ->
                    val id = listItem.id ?: return@mapNotNull null
                    val title = listItem.title ?: return@mapNotNull null
                    val imageUrl = listItem.images?.firstOrNull() ?: return@mapNotNull null
                    return@mapNotNull DomainListItem(id, title, imageUrl)
                }
            }
    }
}