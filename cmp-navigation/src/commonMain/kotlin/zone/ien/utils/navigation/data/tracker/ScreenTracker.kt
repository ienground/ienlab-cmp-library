package zone.ien.utils.navigation.data.tracker

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ScreenTracker(
    var currentRoute: NavKey? = null,
    var currentPath: String? = null
)

fun List<ScreenTracker>.includes(route: NavKey?, path: String?): Boolean {
    return any { it.currentRoute == route && it.currentPath == path }
}

class ScreenTrackerHolder {
    private val _screenTrackers = MutableStateFlow<List<ScreenTracker>>(listOf())
    val screenTrackers: StateFlow<List<ScreenTracker>> = _screenTrackers.asStateFlow()

    fun addData(route: NavKey?, path: String?) {
        _screenTrackers.update {
            it.plus(ScreenTracker(route, path))
        }
    }

    fun clearData() {
        _screenTrackers.update { listOf() }
    }
}