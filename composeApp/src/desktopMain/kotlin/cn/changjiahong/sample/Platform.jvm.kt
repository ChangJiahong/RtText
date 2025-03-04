package cn.changjiahong.sample

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override fun moveTaskToBack(boolean: Boolean) {

    }
}

actual fun getPlatform(): Platform = JVMPlatform()