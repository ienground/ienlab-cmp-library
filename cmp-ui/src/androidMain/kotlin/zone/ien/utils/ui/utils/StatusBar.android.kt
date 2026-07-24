package zone.ien.utils.ui.utils

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

/**
 * Android 플랫폼에서 상태 표시줄 스타일을 설정하는 실제 구현체입니다.
 *
 * Edge-to-Edge 모드를 활성화하고, 현재 테마 모드([isDarkTheme]) 또는 시스템의 원래 테마에 맞춰
 * 상태 표시줄의 스타일을 투명한 배경과 어울리도록 자동 조정합니다.
 *
 * @param isDarkTheme 다크 테마 적용 여부
 * @return 스타일 설정 성공 여부 (항상 true 반환)
 */
@Composable
actual fun setStatusBarStyle(isDarkTheme: Boolean): Boolean {
    val context = LocalContext.current
    val systemDarkTheme = isSystemInDarkTheme()

    DisposableEffect(isDarkTheme, systemDarkTheme) {
        val activity = context as? ComponentActivity
        activity?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT,
                detectDarkMode = { isDarkTheme }
            )
        )

        onDispose {
            activity?.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                    detectDarkMode = { systemDarkTheme }
                )
            )
        }
    }

    return true
}