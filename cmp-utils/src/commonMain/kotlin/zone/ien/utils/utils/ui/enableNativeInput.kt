package zone.ien.utils.utils.ui

import androidx.compose.foundation.text.KeyboardOptions

/**
 * 플랫폼별 네이티브 입력 기능을 활성화합니다.
 *
 * iOS 플랫폼에서는 네이티브 텍스트 입력 핸들링을 활성화하며, 그 외 플랫폼에서는 현재 설정을 그대로 반환합니다.
 *
 * @return 네이티브 입력이 활성화된 [KeyboardOptions]
 */
expect fun KeyboardOptions.enableNativeInput(): KeyboardOptions