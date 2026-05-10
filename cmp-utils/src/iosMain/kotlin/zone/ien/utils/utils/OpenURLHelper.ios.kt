package zone.ien.utils.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

/**
 * iOS용 URL 열기 기능 구현.
 * 
 * 이 함수는 지정된 URL을 iOS 애플리케이션에서 열기 위한 기능을 제공합니다.
 * UIApplication을 사용하여 URL을 열 수 있는지 확인하고 열기를 시도합니다.
 * 
 * @param url 열 대상 URL
 */
actual fun openUrl(url: String) {
    val appUrl = NSURL(string = url)
    if (UIApplication.sharedApplication.canOpenURL(appUrl)) {
        UIApplication.sharedApplication.openURL(url = appUrl, options = mapOf<Any?, Any>(), null)
    }
}

/**
 * iOS용 앱스토어 URL 열기 기능 구현.
 * 
 * 이 함수는 iOS 앱스토어에서 지정된 iOS 앱을 열기 위한 URL을 생성합니다.
 * 
 * @param android Android 앱 ID (사용되지 않음)
 * @param ios iOS 앱 ID
 */
actual fun openAppStoreUrl(android: String, ios: String) {
    openUrl("itms-apps://itunes.apple.com/app/id$ios")
}
