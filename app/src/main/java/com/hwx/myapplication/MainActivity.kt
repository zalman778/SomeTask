package com.hwx.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import com.hwx.myapplication.network.NetworkRepositoryImpl
import com.hwx.myapplication.service.ImageService
import com.hwx.myapplication.service.NetworkService
import com.hwx.myapplication.ui.listScreen.ListScreen
import com.hwx.myapplication.ui.listScreen.ListScreenContract
import com.hwx.myapplication.ui.listScreen.ListScreenViewModel
import com.hwx.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val repository = NetworkRepositoryImpl()
    private val service = NetworkService(repository)
    private val imageService = ImageService(repository)
    private val viewModel = ListScreenViewModel(service)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {

                ListScreen(
                    viewModel.state.collectAsState(ListScreenContract.State()),
                    imageService,
                ) {
                    viewModel.onRequestPage()
                }
            }
        }
    }
}

/*
Создать приложение для получения списка продуктов и их отображение — название, изображение.
API https://dummyjson.com/products. Вывод по 20 штук с дозагрузкой при скролле.
Сделать собственный Image Cache.
Асинхронная загрузка изображений. MVI

легкий сетевой слой
 */