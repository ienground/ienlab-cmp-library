package zone.ien.utils.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import zone.ien.utils.utils.applicationContext

actual class DefaultDataStoreProvider actual constructor(name: String) {
    private val context = applicationContext
    private val dataStore = createDataStoreWithDefaults(migrations = emptyList()) {
        context.filesDir.resolve(name).absolutePath
    }

    actual fun getDataStore(): DataStore<Preferences> = dataStore
}