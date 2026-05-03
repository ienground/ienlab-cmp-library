package zone.ien.utils.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplication
import platform.UIKit.UIControl

@OptIn(ExperimentalForeignApi::class)
actual fun moveToBackground() {
    UIControl().sendAction(
        NSSelectorFromString("suspend"),
        to = UIApplication.sharedApplication,
        forEvent = null
    )
}