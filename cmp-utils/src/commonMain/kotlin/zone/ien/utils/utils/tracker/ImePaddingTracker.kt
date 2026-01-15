package zone.ien.utils.utils.tracker

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ImePaddingTracker(
    var enabled: Boolean = true
)

class ImePaddingTrackerHolder {
    private val _imePaddingTracker = MutableStateFlow<ImePaddingTracker?>(null)
    val imePaddingTracker = _imePaddingTracker.asStateFlow()

    fun update(enabled: Boolean) {
        _imePaddingTracker.update { ImePaddingTracker(enabled) }
    }
}
