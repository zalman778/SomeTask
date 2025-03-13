package com.hwx.myapplication.ui.listScreen

import com.hwx.myapplication.domain.DomainListItem

interface ListScreenContract {

    data class State(
        val currentPage: Int = -1,
        val items: List<DomainListItem> = emptyList(),
        val isPageLoading: Boolean = false,
    )
}