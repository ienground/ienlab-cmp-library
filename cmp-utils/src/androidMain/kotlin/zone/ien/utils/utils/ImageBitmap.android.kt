package zone.ien.utils.utils

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

actual fun ImageBitmap.encodeToByteArray(quality: Int): ByteArray {
    val androidBitmap: Bitmap = this.asAndroidBitmap()
    val stream = ByteArrayOutputStream()

    androidBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)

    return stream.toByteArray()
}