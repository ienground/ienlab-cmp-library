package zone.ien.utils.utils

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.net.toUri
import zone.ien.utils.applicationContext

/**
 * Android용 URL 열기 기능 구현.
 * 
 * 이 함수는 지정된 URL을 Android 브라우저 또는 앱으로 열기 위한 인텐트를 생성합니다.
 * 
 * @param url 열 대상 URL
 */
actual fun openUrl(url: String) {
    val context = applicationContext
    val uri = url.toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)

    context.startActivity(intent)
}

/**
 * Android용 앱스토어 URL 열기 기능 구현.
 * 
 * 이 함수는 Google Play 스토어에서 지정된 Android 앱을 열기 위한 URL을 생성합니다.
 * 
 * @param android Android 앱 ID
 * @param ios iOS 앱 ID (사용되지 않음)
 */
actual fun openAppStoreUrl(android: String, ios: String) {
    val context = applicationContext
    val storeUrl = "https://play.google.com/store/apps/details?id=$android"
    val uri = storeUrl.toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
    intent.setPackage("com.android.vending")

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        context.startActivity(Intent(Intent.ACTION_VIEW, uri).apply { flags = FLAG_ACTIVITY_NEW_TASK })
    }
}
