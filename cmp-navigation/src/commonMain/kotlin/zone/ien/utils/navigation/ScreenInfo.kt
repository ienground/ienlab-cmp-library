package zone.ien.utils.navigation

import androidx.navigationevent.NavigationEventInfo

/**
 * 화면 정보를 나타내는 데이터 클래스
 * @property route 화면 라우트
 * @property title 화면 제목 (선택)
 */
data class ScreenInfo(
    val route: String,
    val title: String? = null,
): NavigationEventInfo()
