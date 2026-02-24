package zone.ien.utils.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import zone.ien.utils.utils.documentDirectory

actual class DefaultDataStoreProvider actual constructor(name: String) {
    private val dataStore = createDataStoreWithDefaults(migrations = listOf()) {
        "${documentDirectory()}/$name"
    }

    actual fun getDataStore(): DataStore<Preferences> = dataStore
}