package zone.ien.utils.utils

import android.content.ClipData
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.toClipEntry

/**
 * Android implementation of toClipEntry function.
 *
 * This function converts a String into a ClipEntry for Android by creating a
 * new plain text ClipData and converting it using the platform's existing
 * toClipEntry() extension function.
 *
 * @return A ClipEntry representing this string for Android platform
 */
@ExperimentalComposeUiApi
actual fun String.toClipEntry(): ClipEntry {
    return ClipData.newPlainText("text_data", this).toClipEntry()
}