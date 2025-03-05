package cn.changjiahong.rttext

import androidx.compose.runtime.Composable

@Composable
fun withIf(
    condition: Boolean,
    modifier: @Composable (@Composable () -> Unit) -> Unit = { it() },
    content: @Composable () -> Unit
) {
    if (condition) {
        modifier(content)
    } else {
        content()
    }
}

fun String.encodeLn():String{
    return replace("\n","\\n")
}

fun String.decodeLn():String{
    return replace("\\n","\n")
}