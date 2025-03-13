package com.hwx.myapplication.ui.listScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hwx.myapplication.service.ImageService
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun ListScreen(
    state: State<ListScreenContract.State>,
    imageService: ImageService,
    onPageRequest: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val listState = rememberLazyListState()

            val isNeedToLoadPage = remember {
                derivedStateOf {
                    val totalItemsCount = listState.layoutInfo.totalItemsCount
                    val lastVisibleItemIndex =
                        listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    val isListEmpty = state.value.items.isEmpty()
                    val isPageLoading = state.value.isPageLoading

                    val isNeedToLoadPage =
                        lastVisibleItemIndex >= (totalItemsCount - 2) && !isPageLoading && !isListEmpty
                    //Log.i("ATAG", "[compose] isNeedToLoadPage=$isNeedToLoadPage isListEmpty=$isListEmpty totalItemsCount=$totalItemsCount lastVisibleItemIndex=$lastVisibleItemIndex isPageLoading=$isPageLoading")
                    isNeedToLoadPage
                }
            }
            LaunchedEffect(listState) {
                snapshotFlow { isNeedToLoadPage.value }
                    .distinctUntilChanged()
                    .filter { it }
                    .collect {
                        onPageRequest()
                    }
            }

            val items = state.value.items
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
            ) {
                items(
                    items.size,
                    key = { itemIdx -> items[itemIdx].id },
                ) { itemIdx ->
                    val item = items[itemIdx]

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        ImageBlock(imageService, item.imageUrl)

                        Text(
                            text = item.title,
                        )
                    }
                }
            }
        }

        if (state.value.isPageLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(60.dp)
                    .padding(10.dp),
                color = Color.Black,
            )
        }
    }
}

@Composable
private fun ImageBlock(
    imageService: ImageService,
    imageUrl: String,
) {
    val imageState = imageService.getImageState(url = imageUrl)
        .collectAsState(ImageService.ImageState.Initial).value

    when (imageState) {
        is ImageService.ImageState.Ready -> {
            Image(
                bitmap = imageState.bitmap,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
            )
        }

        ImageService.ImageState.Error -> {
            Text(
                text = "Error loading",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        imageService.getImageState(url = imageUrl)
                    }
            )
        }

        ImageService.ImageState.Initial -> {
            Box(
                modifier = Modifier
                    .size(60.dp)
            )
        }

        ImageService.ImageState.Loading -> {

            CircularProgressIndicator(
                modifier = Modifier
                    .size(60.dp)
                    .padding(10.dp),
                color = Color.Black,
            )
        }
    }
}