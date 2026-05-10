package zone.ien.utils.utils

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

/**
 * Android용 이미지 비트맵을 바이트 배열로 인코딩.
 * 
 * 이 함수는 Android 비트맵으로 변환하고 JPEG 형식으로 압축하여 바이트 배열로 반환합니다.
 * 
 * @param quality 이미지 품질 (0-100)
 * @return 인코딩된 이미지 바이트 배열
 */
actual fun ImageBitmap.encodeToByteArray(quality: Int): ByteArray {
    val androidBitmap: Bitmap = this.asAndroidBitmap()
    val stream = ByteArrayOutputStream()

    androidBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)

    return stream.toByteArray()
}