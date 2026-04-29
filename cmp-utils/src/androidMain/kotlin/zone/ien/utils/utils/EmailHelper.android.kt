package zone.ien.utils.utils

import android.content.Context
import android.content.Intent

actual fun sendEmail(address: String, subject: String, body: String) {
val intent = Intent(Intent.ACTION_SENDTO).apply {
    data = android.net.Uri.parse("mailto:$address")
    putExtra(Intent.EXTRA_SUBJECT, subject)
    putExtra(Intent.EXTRA_TEXT, body)
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
}
context.startActivity(intent)
}