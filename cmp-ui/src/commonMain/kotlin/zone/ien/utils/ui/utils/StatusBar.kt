package zone.ien.utils.ui.utils

import androidx.compose.runtime.Composable

/**
 * setStatusBarStyle은 상태 표시줄의 스타일을 설정하기 위한 함수입니다.
 * 
 * 이 함수는 Android 플랫폼에서 상태 표시줄의 텍스트 색상 및 배경 색상을 설정합니다.
 * dark theme에 따라 상태 표시줄의 스타일을 자동으로 조정합니다.
 * 
 * @param isDarkTheme 다크 테마 여부
 * @return 스타일 설정 성공 여부
 */
@Composable
expect fun setStatusBarStyle(
    isDarkTheme: Boolean
): Boolean
