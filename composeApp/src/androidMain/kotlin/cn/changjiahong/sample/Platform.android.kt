package cn.changjiahong.sample

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override fun moveTaskToBack(boolean: Boolean) {
        MainActivity.mainActivity.moveTaskToBack(boolean)
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()