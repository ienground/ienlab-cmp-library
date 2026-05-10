package zone.ien.utils.utils

import android.content.ClipData
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.toClipEntry

/**
 * Android용 문자열을 ClipEntry로 변환.
 * 
 * 이 함수는 문자열을 Android의 ClipData로 변환하여 ClipEntry로 반환합니다.
 * 플랫폼의 기존 toClipEntry() 확장 함수를 사용하여 변환합니다.
 * 
 * @return Android 플랫폼용 ClipEntry
 */
@ExperimentalComposeUiApi
actual fun String.toClipEntry(): ClipEntry {
    return ClipData.newPlainText("text_data", this).toClipEntry()
}