package zone.ien.utils.pref

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex

/**
 * 미리보기에서 사용할 메모리 기반 설정 DataStore를 기억합니다.
 *
 * 값은 현재 Composition이 유지되는 동안만 보존됩니다.
 */
@Composable
fun rememberPreviewSettingsDataStore(): DataStore<Preferences> = remember { PreviewSettingsDataStore() }

private class PreviewSettingsDataStore : DataStore<Preferences> {
    private val preferences = MutableStateFlow(emptyPreferences())
    private val mutex = Mutex()

    override val data: Flow<Preferences> = preferences

    override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
        mutex.lock()
        try {
            val updatedPreferences = transform(preferences.value)
            preferences.value = updatedPreferences
            return updatedPreferences
        } finally {
            mutex.unlock()
        }
    }
}
