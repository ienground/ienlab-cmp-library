package zone.ien.utils.utils

import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.UIKit.UIApplication

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