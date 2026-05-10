package zone.ien.utils.utils

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.popoverPresentationController

/**
 * iOS용 텍스트 공유 기능 구현.
 * 
 * 이 함수는 시스템 공유 메커니즘을 사용하여 텍스트 콘텐츠를 공유하는 iOS 네이티브 활동 뷰 컨트롤러를 생성하고 표시합니다.
 * 
 * @param text 시스템 공유 메커니즘을 통해 공유할 텍스트
 */
actual fun shareText(text: String) {
    val activityViewController = UIActivityViewController(
        activityItems = listOf(text),
        applicationActivities = null
    )

    val windowScene = UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .firstOrNull { it.activationState == platform.UIKit.UISceneActivationStateForegroundActive }
        ?: UIApplication.sharedApplication.connectedScenes
            .filterIsInstance<UIWindowScene>()
            .firstOrNull()

    val rootViewController = windowScene?.windows
        ?.filterIsInstance<UIWindow>()
        ?.firstOrNull { it.isKeyWindow() }
        ?.rootViewController

    activityViewController.popoverPresentationController?.sourceView = rootViewController?.view

    rootViewController?.presentViewController(
        activityViewController,
        animated = true,
        completion = null
    )
}