package zone.ien.utils.ui.utils

import androidx.compose.runtime.Composable

/**
 * 컴포지션에 포함된 동안 Android와 iOS의 화면 자동 꺼짐을 방지합니다.
 * 마지막 호출이 제거되면 기존 설정을 복원하며, 다른 플랫폼에서는 아무 작업도 하지 않습니다.
 *
 * @param enabled 화면 자동 꺼짐 방지 여부
 */
@Composable
expect fun KeepScreenOn(enabled: Boolean = true)
