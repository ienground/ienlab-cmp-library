package zone.ien.utils.utils

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.toClipEntry

actual fun String.toClipEntry(): ClipEntry {
    return ClipData.newPlainText("clip_entry", this).toClipEntry()
}