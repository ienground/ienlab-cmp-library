package zone.ien.utils.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplication
import platform.UIKit.UIControl

/**
 * iOS용 백그라운드 이동 기능 구현.
 * 
 * 이 함수는 iOS 네이티브 컨트롤을 사용하여 iOS 애플리케이션을 백그라운드로 이동합니다.
 * UIApplication에 "suspend" 작업을 전송합니다.
 * 
 * @see moveToBackground
 */
@OptIn(ExperimentalForeignApi::class)
actual fun moveToBackground() {
    UIControl().sendAction(
        NSSelectorFromString("suspend"),
        to = UIApplication.sharedApplication,
        forEvent = null
    )
}