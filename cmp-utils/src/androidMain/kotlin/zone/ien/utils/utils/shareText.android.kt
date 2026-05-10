package zone.ien.utils.utils

import android.content.Intent

/**
 * Android implementation of shareText function.
 *
 * This function creates an intent to share text using Android's native sharing system.
 * It uses Intent.ACTION_SEND with text/plain type and displays the share chooser dialog.
 *
 * @param text Text to be shared via the system's sharing mechanism
 */
actual fun shareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    applicationContext.startActivity(
        Intent.createChooser(intent, null).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    )
}