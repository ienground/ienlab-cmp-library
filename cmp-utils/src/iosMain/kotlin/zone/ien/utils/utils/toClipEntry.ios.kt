package zone.ien.utils.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

@ExperimentalComposeUiApi
actual fun String.toClipEntry(): ClipEntry {
    return ClipEntry.withPlainText(this)
}