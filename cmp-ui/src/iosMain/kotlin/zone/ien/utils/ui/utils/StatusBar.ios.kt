package zone.ien.utils.ui.utils

import androidx.compose.runtime.Composable

/**
 * iOS 플랫폼에서 상태 표시줄 스타일을 설정하는 실제 구현체입니다.
 *
 * iOS의 경우 현재 구현에서는 항상 `true`를 반환하며 별도의 처리를 하지 않습니다.
 *
 * @param isDarkTheme 다크 테마 적용 여부
 * @return 스타일 설정 성공 여부 (항상 true 반환)
 */
@Composable
actual fun setStatusBarStyle(isDarkTheme: Boolean) = true