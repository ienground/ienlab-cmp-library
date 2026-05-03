package zone.ien.utils.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual fun moveToBackground() {
    UIApplication.sharedApplication.performSelector(aSelector = NSSelectorFromString("suspend"))
}