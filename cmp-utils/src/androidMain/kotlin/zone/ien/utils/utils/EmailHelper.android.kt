package zone.ien.utils.utils

import android.content.Context
import android.content.Intent

actual fun sendEmail(address: String, subject: String, body: String) {
    val context = applicationContext
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        type = "message/rfc822"
    }
    context.startActivity(intent)
}