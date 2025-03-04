package io.github.kotlin.fibonacci.cn.changjiahong.rttext

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

typealias LinkAction = (action: String) -> Unit

val LocalLinkAction : ProvidableCompositionLocal<LinkAction> =
    staticCompositionLocalOf { error("LinkAction not initialized") }
