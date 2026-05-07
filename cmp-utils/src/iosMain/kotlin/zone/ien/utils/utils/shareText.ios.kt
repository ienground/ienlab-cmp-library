package zone.ien.utils.utils

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.popoverPresentationController

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