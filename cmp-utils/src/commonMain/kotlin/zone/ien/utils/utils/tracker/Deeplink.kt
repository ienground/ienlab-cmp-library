package zone.ien.utils.utils.tracker

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface DeeplinkData {
    var type: String
}

class DeeplinkHolder<T: DeeplinkData> {
    private val _deepLinkData = MutableStateFlow<T?>(null)
    val deepLinkData: StateFlow<T?> = _deepLinkData.asStateFlow()

    fun update(data: T) {
        _deepLinkData.update { data }
    }
}