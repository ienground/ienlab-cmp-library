package zone.ien.utils.utils.tracker

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * 키보드 패딩 트래커 데이터 클래스.
 * 
 * 이 클래스는 키보드 표시/숨김 상태에 따라 패딩을 조정하는 데 사용되는 정보를 저장합니다.
 * 
 * @property enabled 키보드 패딩 조정 기능의 활성화 여부를 나타냅니다. 
 *                   true인 경우 패딩이 적용되고, false인 경우 적용되지 않습니다.
 */
data class ImePaddingTracker(
    var enabled: Boolean = true
)

/**
 * 키보드 패딩 트래커 데이터를 저장하고 관리하는 호ル더 클래스.
 * 
 * 이 클래스는 키보드 패딩 정보를 Flow를 통해 관리하며, 상태 업데이트를 지원합니다.
 * 
 * @see ImePaddingTracker
 */
class ImePaddingTrackerHolder {
    private val _imePaddingTracker = MutableStateFlow<ImePaddingTracker?>(null)
    val imePaddingTracker = _imePaddingTracker.asStateFlow()

    /**
     * 키보드 패딩 설정을 업데이트합니다.
     * 
     * @param enabled 키보드 패딩 조정 기능의 활성화 여부
     */
    fun update(enabled: Boolean) {
        _imePaddingTracker.update { ImePaddingTracker(enabled) }
    }
}
