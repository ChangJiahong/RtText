package cn.changjiahong.rttext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import cn.changjiahong.rttext.CssStyle
import cn.changjiahong.rttext.addCssStyle
import cn.changjiahong.rttext.markdownCss
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Node
import com.fleeksoft.ksoup.nodes.TextNode


@Composable
fun RtHtml(
    html: String,
    css: String = markdownCss,
    modifier: Modifier = Modifier,
    nodeHandlers: List<NodeHandler> = defaultNodeHandlers,
    linkAction: LinkAction = {}
) {
    val document =
        remember(html, css) { Ksoup.parse(html).apply { addCssStyle(CssStyle.parseCss(css)) } }
    CompositionLocalProvider(LocalLinkAction provides linkAction) {
        RenderNode(document.body(), modifier = modifier, nodeHandlers = nodeHandlers)
    }
}

@Composable
fun RenderNode(
    node: Node,
    modifier: Modifier = Modifier,
    nodeHandlers: List<NodeHandler>
) {
    CompositionLocalProvider(LocalHandlerContext provides rememberHandlerContext(nodeHandlers)) {
        Box {
            val annotatedString = BuildAnnotatedString(node)
            RenderNode(annotatedString, modifier)
        }
    }
}

@Composable
private fun RenderNode(
    annotatedString: AnnotatedString,
    modifier: Modifier
) {
    val handlerContext = LocalHandlerContext.current
    Text(
        annotatedString,
        modifier = modifier.background(Color.White),
        inlineContent = handlerContext.inlineTextContents
    )
}


@Composable
fun BuildAnnotatedString(node: Node) = buildAnnotatedString {
    appendNode(node)
}


@Composable
fun AnnotatedString.Builder.appendNode(node: Node) {
    val handlerContext = LocalHandlerContext.current
    when {
        node is TextNode -> {
            append(node.text())
        }

        else -> handlerContext.handler(this, node)
    }
}


@Composable
fun AnnotatedString.Builder.appendChild(node: Node) {
    node.childNodes().forEach {
        appendNode(it)
    }
}