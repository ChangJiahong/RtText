package cn.changjiahong.sample

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override fun moveTaskToBack(boolean: Boolean) {

    }
}

actual fun getPlatform(): Platform = IOSPlatform()