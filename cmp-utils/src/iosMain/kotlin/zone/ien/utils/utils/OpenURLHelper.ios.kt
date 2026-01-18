package zone.ien.utils.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openUrl(url: String) {
    val appUrl = NSURL(string = url)
    if (UIApplication.sharedApplication.canOpenURL(appUrl)) {
        UIApplication.sharedApplication.openURL(url = appUrl, options = mapOf<Any?, Any>(), null)
    }
}

actual fun openAppStoreUrl(android: String, ios: String) {
    openUrl("itms-apps://itunes.apple.com/app/id$ios")
}
