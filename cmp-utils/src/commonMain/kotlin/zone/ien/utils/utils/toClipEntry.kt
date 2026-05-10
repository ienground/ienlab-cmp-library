package zone.ien.utils.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

/**
 * 문자열을 Compose 플랫폼 ClipEntry로 변환합니다.
 *
 * 이 함수는 문자열을 시스템 클립보드에 복사하는 데 사용할 수 있는 ClipEntry로 변환하는
 * 교차 플랫폼 방식을 제공합니다.
 *
 * @return 이 문자열을 나타내는 ClipEntry
 */
@ExperimentalComposeUiApi
expect fun String.toClipEntry(): ClipEntry