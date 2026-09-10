package zone.ien.utils.utils

import platform.Foundation.NSBundle
import platform.UIKit.UIDevice
import kotlin.experimental.ExperimentalNativeApi

private fun bundleValue(key: String): String =
    NSBundle.mainBundle.infoDictionary?.get(key)?.toString().orEmpty()

/** iOS 런타임 정보를 제공합니다. */
actual object PlatformDependent {
    @OptIn(ExperimentalNativeApi::class)
    actual val isDebug: Boolean = Platform.isDebugBinary

    actual val isIos: Boolean = true

    actual val appId: String = NSBundle.mainBundle.bundleIdentifier.orEmpty()

    actual val versionName: String = bundleValue("CFBundleShortVersionString")

    actual val versionCode: Int = bundleValue("CFBundleVersion").toIntOrNull() ?: 0

    actual val deviceName: String = UIDevice.currentDevice.name

    actual val deviceOS: String = UIDevice.currentDevice.systemVersion
}
