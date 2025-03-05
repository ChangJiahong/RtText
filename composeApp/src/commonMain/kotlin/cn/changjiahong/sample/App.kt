package cn.changjiahong.sample

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.changjiahong.rttext.RtHtml
import cn.changjiahong.rttext.defaultNodeHandlers
import cn.changjiahong.rttext.strong
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
          
            <!-- 超链接 -->
            <p><a href="https://ex.com" style="color:#ffdd00;">点击访问</a></p>

            <!-- 列表 -->
            <ul>
                <li>无序列表项 1</li>
                <li>无序列表项 2</li>
            </ul>
            <p></p>
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
            
            
            <p></p>
            

            <!-- 图片 -->
            <img src="https://www.jetbrains.com/_assets/www/kotlin-multiplatform/parts/sections/head/hero-shape.41226a16aa9674fbb2f397f143af121c.jpg" alt="测试图片">
            
        </div>
    """.trimIndent()

    val license = "勾选即代表你同意本应用<a href=\"https://ex.license.com\"><b>使用协议</b></a>。"
    val regsiter =
        "你还没有账号？点击跳转<a style=\" color:#ffdd0000\" href=\"register\"><b>注册</b></a>。"

    val customCss = """
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
    """.trimIndent()

    RtHtml(
        regsiter,
        nodeHandlers = mutableListOf(strong).apply { addAll(defaultNodeHandlers) },
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        linkAction = {
            println(it)
        }
    )

}