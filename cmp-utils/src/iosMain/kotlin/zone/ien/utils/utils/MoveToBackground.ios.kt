package zone.ien.utils.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplication
import platform.UIKit.UIControl

/**
 * iOS에서의 moveToBackground 함수 구현.
 *
 * 이 함수는 iOS 네이티브 컨트롤을 사용하여 iOS 애플리케이션을 백그라운드로 이동합니다.
 * UIApplication에 "suspend" 작업을 전송합니다.
 */
@OptIn(ExperimentalForeignApi::class)
actual fun moveToBackground() {
    UIControl().sendAction(
        NSSelectorFromString("suspend"),
        to = UIApplication.sharedApplication,
        forEvent = null
    )
}