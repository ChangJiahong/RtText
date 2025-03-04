package cn.changjiahong.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kotlin.fibonacci.cn.changjiahong.rttext.RtHtml
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {

    Box {
        demo()
    }

}


@Composable
fun demo() {
    val html = """
        <div class="post-md">
        <!-- 标题 -->
            <h1>一级标题</h1>
            <h2>二级标题</h2>
            <h3>三级标题</h3>
            <h4>四级标题</h4>
            <h5>五级标题</h5>
            <h6>六级标题</h6>

            <!-- 文本格式 -->
            <p>这是一个普通段落。</p>
            <p><b>加粗文本</b></p>
            <p><i>斜体文本</i></p>
            <p><u>下划线文本</u></p>
            <p><s>删除线文本</s></p>
            <p><mark>高亮文本</mark></p>
            <p>上标：x<sup>2</sup>，下标：H<sub>2</sub>O</p>
            <p>引用文本：<q>这是一段引用</q></p>
            <blockquote>这是一个块级引用</blockquote>
            
            <!-- 超链接 -->
            <a href="https://example.com">点击访问 Example</a>
            <p><a href="https://ex.com" style="color:#ffdd00;">点击访问</a></p>

            <!-- 列表 -->
            <ul>
                <li>无序列表项 1</li>
                <li>无序列表项 2</li>
            </ul>
            <ol>
                <li>有序列表项 1</li>
                <li>有序列表项 2</li>
            </ol>
            
            <!-- 表格 -->
            <table border="1">
                <tr>
                    <th>表头 1</th>
                    <th>表头 2</th>
                </tr>
                <tr>
                    <td>数据 1</td>
                    <td>数据 2</td>
                </tr>
            </table>

            <!-- 图片 -->
            <img src="https://via.placeholder.com/150" alt="测试图片">

            <!-- 按钮 -->
            <button>点击按钮</button>
            
            <code>代码代码代码代码代码代代码代码代码代码代码代码代码代码代码代码代码代码代码代码码代码代码代码代码代码代码代码代码</code>

            <!-- 其他 HTML5 标签 -->
            <details>
                <summary>点击展开详情</summary>
                            <code>代码代码代码代码代码代代码代码代码代码代码代码代码代码代码代码代码代码代码代码码代码代码代码代码代码代码代码代码</code>


                <p>这里是详细内容。</p>
            </details>
            
            
        </div>
    """.trimIndent()

    Column {
        RtHtml(html,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            linkAction = {
                println(it)
            })
    }
}