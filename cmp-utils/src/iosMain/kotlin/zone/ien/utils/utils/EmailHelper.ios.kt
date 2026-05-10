package zone.ien.utils.utils

import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.UIKit.UIApplication

/**
 * iOS용 이메일 전송 기능 구현.
 * 
 * 이 함수는 지정된 이메일 세부 정보와 함께 mailto URL을 생성하고,
 * iOS 이메일 애플리케이션을 열어 미리 채워진 데이터를 표시합니다.
 * 이메일 앱이 없는 경우 작업은 무시됩니다.
 * 
 * @param address 수신자 이메일 주소
 * @param subject 이메일 제목
 * @param body 이메일 본문 내용
 */
actual fun sendEmail(address: String, subject: String, body: String) {
    val urlComponents = NSURLComponents()
    urlComponents.scheme = "mailto"
    urlComponents.path = address
    urlComponents.queryItems = listOf(
        NSURLQueryItem(name = "subject", value = subject),
        NSURLQueryItem(name = "body", value = body)
    )

    urlComponents.URL?.let { emailUrl ->
        if (UIApplication.sharedApplication.canOpenURL(emailUrl)) {
            UIApplication.sharedApplication.openURL(url = emailUrl, options = mapOf<Any?, Any>(), completionHandler = null)
        }
    }
}