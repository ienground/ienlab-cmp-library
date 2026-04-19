package zone.ien.utils.utils

import android.content.ClipData
import androidx.compose.ui.ExperimentalComposeUiApi
actual fun String.toClipEntry(): ClipEntry {
    return ClipData.newPlainText("text_data", this).toClipEntry()
}
    return ClipData.newPlainText("clip_entry", this).toClipEntry()
}