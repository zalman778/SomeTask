package com.hwx.myapplication.ui.listScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hwx.myapplication.service.NetworkService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ListScreenViewModel(
    private val networkService: NetworkService,
) : ViewModel() {

    val state = MutableStateFlow(ListScreenContract.State())

    init {
        requestData()
    }

    fun onRequestPage() {
        Log.i("ATAG", "[onRequestPage] invoked")

        requestData()
    }

    private fun requestData() {

        if (state.value.isPageLoading) return
        Log.i("ATAG", "[requestData] invoked")

        GlobalScope.launch {
            state.value = state.value.copy(isPageLoading = true)

            val nextPage = state.value.currentPage + 1
            val response = networkService.fetchData(
                offset = nextPage * PAGE_SIZE,
                pageSize = PAGE_SIZE,
            )

            Log.i("ATAG", "[requestData] response = $response")


            state.value = state.value.copy(isPageLoading = false)

            if (response.isFailure) {
                //show error
            } else {
                val data = response.getOrNull() ?: return@launch
                val newList = state.value.items.toMutableList().apply {
                    addAll(data)
                }
                state.value = state.value.copy(
                    currentPage = nextPage,
                    items = newList,
                )
            }
        }
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}