package cn.changjiahong.rttext

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.sp
import com.fleeksoft.ksoup.nodes.Node
import kotlinx.serialization.json.Json

typealias AnnotatedStringBuilder = AnnotatedString.Builder

typealias NodeProcessor = @Composable AnnotatedStringBuilder.(node: Node) -> Unit

typealias InlineContent = Pair<String, InlineTextContent>

@Composable
inline fun AnnotatedStringBuilder.withStyle(
    style: SpanStyle,
    block: @Composable AnnotatedStringBuilder.() -> Unit
) {
    val index = pushStyle(style)
    block(this)
    pop(index)
}

@Composable
inline fun AnnotatedStringBuilder.withStyle(
    style: ParagraphStyle,
    crossinline block: @Composable AnnotatedStringBuilder.() -> Unit
) {
    val index = pushStyle(style)
    block(this)
    pop(index)
}


interface AnnotatedStringBuilderHandler {
    @Composable
    fun handler(builder: AnnotatedStringBuilder, node: Node)
}

open class NodeHandler(val name: String, val nodeProcessor: NodeProcessor) :
    AnnotatedStringBuilderHandler {

    @Composable
    override fun handler(builder: AnnotatedStringBuilder, node: Node) {
        builder.nodeProcessor(node)
    }

}

open class InlineNodeProcessor(
    name: String,
    val composeFun: @Composable (contentText: String) -> Unit
) : NodeHandler(name, {}) {


    @Composable
    override fun handler(builder: AnnotatedStringBuilder, node: Node) {

        val nodeHash = (node.hashCode()).toString()
        val param = Param(nodeHash,node.outerHtml())

        val alternateText = Json.encodeToString(param)

        builder.apply {
            appendInlineContent(
                nodeHash,
                alternateText
            )
        }

        onMeasure(
            nodeHash,
            alternateText
        )
    }

    @Composable
    private fun onMeasure(
        id: String,
        alternateText: String
    ) {
        val handlerContext = LocalHandlerContext.current
        if (handlerContext.hasInlineContent(id)) {
            return
        }
        var show by remember { mutableStateOf(true) }
        if (show) {
            handlerContext.preRegisterInlineContent()
            val density = LocalDensity.current.density
            val fontScale = LocalDensity.current.fontScale
            Box(
                modifier = Modifier
                    .fillMaxWidth()  // 设置固定大小，也可以根据需求动态计算
                    .verticalScroll(rememberScrollState()) // 可滚动内容
                    .onSizeChanged { size ->
                        // 获取测量后的尺寸
                        show = false
                        handlerContext.registerInlineContent(
                            id to InlineTextContent(
                                Placeholder(
                                    (size.width / density * fontScale).sp,
                                    (size.height / density * fontScale).sp,
                                    PlaceholderVerticalAlign.Center
                                ),
                                composeFun
                            )
                        )
                    }
            ) {
                composeFun(alternateText)
            }
        }
    }
}