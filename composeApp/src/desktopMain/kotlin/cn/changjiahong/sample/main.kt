package cn.changjiahong.bamb

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cn.changjiahong.sample.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Sample",
    ) {
        App()
    }
}