package zone.ien.utils.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

/**
 * iOS용 문자열을 ClipEntry로 변환.
 * 
 * 이 함수는 문자열을 iOS 플랫폼의 ClipEntry로 변환합니다.
 * 클립보드에 텍스트를 복사하기 위한 기능을 제공합니다.
 * 
 * @return iOS 플랫폼용 ClipEntry
 */
@ExperimentalComposeUiApi
actual fun String.toClipEntry(): ClipEntry {
    return ClipEntry.withPlainText(this)
}