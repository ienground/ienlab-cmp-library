package zone.ien.utils.navigation.tracker

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 화면 추적 정보를 나타내는 데이터 클래스
 * @property currentRoute 현재 라우트
 * @property currentPath 현재 경로
 */
data class ScreenTracker(
    var currentRoute: NavKey? = null,
    var currentPath: String? = null
)

/**
 * ScreenTracker 리스트에서 특정 라우트와 경로를 포함하는지 확인하는 확장 함수
 * @param route 비교할 라우트
 * @param path 비교할 경로
 * @return 포함 여부
 */
fun List<ScreenTracker>.includes(route: NavKey?, path: String?): Boolean {
    return any { it.currentRoute == route && it.currentPath == path }
}

/**
 * 화면 추적 정보를 관리하는 클래스
 * Compose 상태 흐름을 사용하여 화면 추적 정보를 관리
 */
class ScreenTrackerHolder {
    private val _screenTrackers = MutableStateFlow<List<ScreenTracker>>(listOf())
    val screenTrackers: StateFlow<List<ScreenTracker>> = _screenTrackers.asStateFlow()

    /**
     * 화면 추적 정보를 추가하는 함수
     * @param route 라우트
     * @param path 경로
     */
    fun addData(route: NavKey?, path: String?) {
        _screenTrackers.update {
            it.plus(ScreenTracker(route, path))
        }
    }

    /**
     * 화면 추적 정보를 초기화하는 함수
     */
    fun clearData() {
        _screenTrackers.update { listOf() }
    }
}