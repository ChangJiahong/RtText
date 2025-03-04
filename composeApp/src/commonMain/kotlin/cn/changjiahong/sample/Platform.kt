package cn.changjiahong.sample

interface Platform {
    val name: String

    fun moveTaskToBack(boolean: Boolean)
}

expect fun getPlatform(): Platform