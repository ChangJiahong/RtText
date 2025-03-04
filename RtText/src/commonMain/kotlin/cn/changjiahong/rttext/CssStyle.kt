package cn.changjiahong.rttext

import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Node


// 数据结构表示 CSS 规则
data class CssRule(val selector: String, val properties: Map<String, String>)


// 解析 CSS 字符串
object CssParser {

    fun parseCssString(css: String): List<CssRule> {
        // 移除注释
        val cssWithoutComments = css.replace("""/\*.*?\*/""".toRegex(), "")

        // 匹配多个选择器组合以及其属性: 选择器1, 选择器2 { 属性名: 属性值; }
        val ruleRegex = """([^{]+)\s*\{\s*([^}]+)\s*\}""".toRegex()
        val rules = mutableMapOf<String, CssRule>()

        ruleRegex.findAll(cssWithoutComments).forEach { matchResult ->
            val selectorsBlock = matchResult.groupValues[1].trim()  // CSS 选择器块
            val propertiesBlock = matchResult.groupValues[2].trim()  // 属性块

            // 解析多个选择器
            val selectors = selectorsBlock.split(',').map { it.trim() }

            // 匹配单个属性名和值
            val propertyRegex = """\s*([a-zA-Z\-]+)\s*:\s*([^;]+)\s*;""".toRegex()
            val properties = mutableMapOf<String, String>()

            propertyRegex.findAll(propertiesBlock).forEach { propertyMatch ->
                val propertyName = propertyMatch.groupValues[1].trim()
                val propertyValue = propertyMatch.groupValues[2].trim()
                properties[propertyName] = propertyValue
            }

            // 对每个选择器进行处理，合并规则
            selectors.forEach { selector ->
                if (rules.containsKey(selector)) {
                    // 如果已经有该选择器规则，合并属性
                    val existingRule = rules[selector]!!
                    val mergedProperties = existingRule.properties.toMutableMap().apply {
                        properties.forEach { (key, value) ->
                            put(key, value)  // 合并属性，后面的覆盖前面的
                        }
                    }
                    rules[selector] = CssRule(selector, mergedProperties)
                } else {
                    // 否则直接添加新规则
                    rules[selector] = CssRule(selector, properties)
                }
            }
        }

        // 将 Map 转为 List 形式
        return rules.values.toList()
    }


}


class CssStyle(val cssRules: List<CssRule>) {
    companion object {
        fun parseCss(cssStyle: String): CssStyle {
            val rules = CssParser.parseCssString(cssStyle)
            return CssStyle(rules)
        }
    }

}

fun Document.addCssStyle(cssStyle: CssStyle) {
    cssStyle.cssRules.forEach { (selector, properties) ->
        val elements = select(selector)
        for (element in elements) {
            properties.forEach { (key, value) ->
                element.attr("style", "${element.attr("style")} $key: $value;")
            }
        }
    }
}


fun Node.parseStyle(): Pair<SpanStyle?, ParagraphStyle?> {
    if (!hasAttr("style")) {
        return Pair(null,null)
    }
    val styleStr = attr("style")
    val spanStyle = CssEncoder.run { parseCssStyleMapToSpanStyle(parseCssStyle(styleStr)) }
    val paragraphStyle =
        CssEncoder.run { parseCssStyleMapToParagraphStyle(parseCssStyle(styleStr), mapOf()) }
    return Pair(spanStyle, paragraphStyle)
}


val markdownCss = """

    .post-md
    {
        width: 100%;
        font-size: 16.8px;
        letter-spacing: 0;
    }

    .post-md h1,
    .post-md h2,
    .post-md h3,
    .post-md h4,
    .post-md h5,
    .post-md h6
    {
        color: #000;
        margin: 12px 0;
    }
    .post-md h1
    {
        font-size: 2.5rem;
        line-height: 1.2;
        padding: 24px 0;
    }
    .post-md h2
    {
        font-size: 2rem;
        line-height: 1.2;
        padding: 20px 0;
    }
    .post-md h3
    {
        font-size: 1.75rem;
        line-height: 1.2;
        padding: 18px 0;
    }
    .post-md h4
    {
        font-size: 1.5rem;
        line-height: 1.2;
        padding: 16px 0;
    }
    .post-md h5
    {
        font-size: 1.25rem;
        line-height: 1.2;
        padding: 14px 0;
    }
    .post-md h6
    {
        font-size: 1.125rem;
        line-height: 1.2;
        padding: 12px 0;
    }
    .post-md a
    {
        color: #666;
        box-shadow: 0 2px 0 #ccc;
        /* transition: color ease-in-out .65s, box-shadow ease-in-out .65s; */
    }
  
    .post-md strong
    {
        font-weight: bold;
        font-family: LXGW WenKai Bold;
    }
    .post-md em
    {
        font-style: italic;

    }
    .post-md kbd
    {
        padding: 2px 4px;
        border-radius: 2px;
        background: #eee;
        border: 1px solid #ddd;
    }
    .post-md ol
    {
        list-style: decimal;
        padding-left: 24px;
    }
    .post-md ul
    {
        list-style: disc;
        padding-left: 24px;
    }
    .post-md img
    {
        border-radius: 4px;
    }
    .post-md hr
    {
        border: none;
        height: 1px;
        background: #ccc;
        margin: 24px 0;
    }

    .post-md p,
    .post-md blockquote
    {
        width: 100%;
        margin: 12px 0;
    }
    .post-md blockquote
    {
        border-left: 2px solid #ddd;
        padding-left: 12px;
        word-wrap: break-word;
    }

    .post-md .video-container
    {
        background: #000;
        border-radius: 4px;
        overflow: hidden;
    }
    .post-md iframe,
    .post-md .video-container iframe
    {
        width: 100%;
    }

    .post-md table
    {
        width: 100%;
        text-align: left;
        border-spacing: 0;
    }
    .post-md table th
    {
        padding: 12px 0;
        border-bottom: 2px solid #ccc;
    }
    .post-md table tr:nth-child(odd) td
    {
        padding: 12px 0;
        border-bottom: 1px solid #ccc;
        background: #fafafa;
    }
    .post-md table tr:nth-child(even) td
    {
        padding: 12px 0;
        border-bottom: 1px solid #ccc;
        background: #fff;
    }

    .post-md code
    {
        border-radius: 4px;
        background: #e4e4e4;
        padding: 2px 4px;
        color: #2d2d2d;
        font-size: 1rem;
    }

    .post-md .hljs-ln-numbers
    {
        opacity: .5;
        padding-right: 12px;
    }

""".trimIndent()