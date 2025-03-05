package cn.changjiahong.rttext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.changjiahong.rttext.parseStyle
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Node
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.serialization.json.Json

const val H1 = "h1"
const val H2 = "h2"
const val H3 = "h3"
const val H4 = "h4"
const val H5 = "h5"
const val H6 = "h6"
const val P = "p"
const val A = "a"
const val DIV = "div"
const val SPAN = "span"
const val IMG = "img"
const val UL = "ul"
const val OL = "ol"
const val LI = "li"
const val TABLE = "table"
const val TR = "tr"
const val TD = "td"
const val TH = "th"
const val THEAD = "thead"
const val TBODY = "tbody"
const val FORM = "form"
const val INPUT = "input"
const val BUTTON = "button"
const val LABEL = "label"
const val SELECT = "select"
const val OPTION = "option"
const val TEXTAREA = "textarea"
const val BLOCKQUOTE = "blockquote"
const val B = "b"
const val STRONG = "strong"
const val CODE = "code"

fun tags(vararg tags: String): String {
    return tags.joinToString(",")
}

@Composable
fun rememberHandlerContext(nodeHandlers: List<NodeHandler>): HandlerContext {
    return remember(nodeHandlers) {
        handlerContext(nodeHandlers)
    }
}

fun handlerContext(nodeHandlers: List<NodeHandler>): HandlerContext {
    return HandlerContext().apply {
        registerNodeHandlers(nodeHandlers)
    }
}


val defaultNodeHandler = NodeHandler("") { node ->
    appendChild(node)
}

val a = NodeHandler(A) { node ->
    val (spanStyle, paragraphStyle) = node.parseStyle()
    withStyle(SpanStyle().merge(spanStyle)) {
        withIf(node.hasAttr("href"), modifier = {
            val href = node.attr("href")
            val linkAction = LocalLinkAction.current
            withLink(
                LinkAnnotation.Clickable(
                    href,
                    linkInteractionListener = { linkAction(href) })
            ) {
                it()
            }
        }) {
            appendChild(node)
        }

    }
}

val h = NodeHandler(tags(H1, H2, H3, H4, H5, H6)) { node ->
    val (spanStyle, paragraphStyle) = node.parseStyle()
    withStyle(ParagraphStyle().merge(paragraphStyle)) {
        withStyle(SpanStyle().merge(spanStyle)) {
            appendChild(node)
        }
    }
}

val p = NodeHandler(P) { node ->
    withStyle(ParagraphStyle()) {
        withStyle(style = SpanStyle()) {
            appendChild(node)
        }
    }
}

val strong = NodeHandler(tags(B, STRONG)) { node: Node ->
    val (spanStyle, paragraphStyle) = node.parseStyle()
    withStyle(style = SpanStyle().merge(spanStyle)) {
        appendChild(node)
    }
}

val assemble = NodeHandler("em,kbd,li,hr,blockquote") { node ->
    val (spanStyle, paragraphStyle) = node.parseStyle()
    val paragraphIndex = paragraphStyle?.let {
        pushStyle(paragraphStyle)
    }

    val spanStyleIndex = spanStyle?.let {
        pushStyle(spanStyle)
    }

    appendChild(node)

    spanStyleIndex?.let {
        pop(spanStyleIndex)
    }

    paragraphIndex?.let {
        pop(paragraphIndex)
    }
}

val ul = InlineNodeProcessor(UL) {
    val param: Param = Json.decodeFromString(it)
    val node = Ksoup.parse(param.contentText).body().firstChild()!!
    val linkAction = LocalLinkAction.current
    Column {
        node.childNodes().filter { it.nameIs(LI) }.forEach { n ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(12.dp)
                        .background(Color(0xFFCC6CE7), RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.width(5.dp))
                RtHtml(n.outerHtml(), linkAction = linkAction)
            }
        }
    }
}

val ol = InlineNodeProcessor(OL) {
    val param: Param = Json.decodeFromString(it)
    val node = Ksoup.parse(param.contentText).body().firstChild()!!
    val linkAction = LocalLinkAction.current
    Column {
        node.childNodes().filter { it.nameIs(LI) }.forEachIndexed { index, n ->
            Row {
                Text("${index + 1}.")
                Spacer(modifier = Modifier.width(5.dp))
                RtHtml(n.outerHtml(), linkAction = linkAction)
            }
        }
    }
}

val code =
    InlineNodeProcessor(CODE) {
        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().background(Color.Yellow)) {
            Text("$$$$$ $it")
        }
    }

val img = InlineNodeProcessor(IMG) {
    Box(modifier = Modifier.fillMaxWidth()) {

        val param: Param = Json.decodeFromString(it)
        val node = Ksoup.parse(param.contentText).body().childNode(0)
        var aspectRatio by remember { mutableStateOf(1f) }
        val density = LocalDensity.current.density
        val fontScale = LocalDensity.current.fontScale
        val handlerContext = LocalHandlerContext.current

        CoilImage(
            imageModel = {
                node.attr("src")
            }, // loading a network image or local resource using an URL.
            imageOptions = ImageOptions(
                contentScale = ContentScale.Fit
            ),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .onSizeChanged { size ->
                    handlerContext.updateInlineContent(
                        param, (size.width / density * fontScale).sp,
                        (size.height / density * fontScale).sp,
                    )
                },
            requestListener = {
                object : ImageRequest.Listener {
                    override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                        result.image.let {
                            aspectRatio = it.width / (it.height * 1f)
                        }
                    }
                }
            }
        )
    }
}

val table = InlineNodeProcessor(TABLE) {
    val param: Param = Json.decodeFromString(it)
    val tableNode = Ksoup.parse(param.contentText).body().firstChild()!!

    Column(modifier = Modifier) {
        tableNode.childNodes().forEach { n ->
            n.childNodes().filter { it.nameIs(TR) }.forEach { rowNode ->
                if (rowNode.isEffectivelyFirst()) {
                    HorizontalDivider(thickness = 1.dp, color = Color.Black)
                }
                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                    rowNode.childNodes().filter { it.nameIs(TH) || it.nameIs(TD) }
                        .forEach { unitNode ->
                            if (unitNode.isEffectivelyFirst()) {
                                VerticalDivider(thickness = 1.dp, color = Color.Black)
                            }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                RtHtml(
                                    unitNode.outerHtml()
                                )
                            }
                            VerticalDivider(thickness = 1.dp, color = Color.Black)
                        }
                }
                HorizontalDivider(thickness = 1.dp, color = Color.Black)
            }
        }
    }

}

val defaultNodeHandlers = listOf(
    defaultNodeHandler,
    a,
    h,
    p,
    ul,
    ol,
    strong,
    assemble,
    code,
    img,
    table
)