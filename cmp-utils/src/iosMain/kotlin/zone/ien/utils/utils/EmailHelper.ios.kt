package zone.ien.utils.utils

import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.UIKit.UIApplication

/**
 * iOS implementation of sendEmail function.
 *
 * This function constructs a mailto URL with the specified email details and opens
 * the iOS email application with pre-filled data. If no email app is available,
 * the action will be ignored.
 *
 * @param address Recipient email address
 * @param subject Email subject
 * @param body Email body content
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