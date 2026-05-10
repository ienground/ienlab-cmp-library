package zone.ien.utils.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

/**
 * iOS implementation of toClipEntry function.
 *
 * This function converts a String into a ClipEntry for iOS by using
 * the ClipEntry.withPlainText factory method.
 *
 * @return A ClipEntry representing this string for iOS platform
 */
@ExperimentalComposeUiApi
actual fun String.toClipEntry(): ClipEntry {
    return ClipEntry.withPlainText(this)
}