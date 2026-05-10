package zone.ien.utils.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import zone.ien.utils.utils.documentDirectory

/**
 * iOS implementation of DefaultDataStoreProvider.
 *
 * This class provides iOS-specific implementation for DataStore access.
 * It uses the document directory to store preferences data.
 *
 * @param name The name of the data store file
 */
actual class DefaultDataStoreProvider actual constructor(name: String) {
    private val dataStore = createDataStoreWithDefaults(migrations = listOf()) {
        "${documentDirectory()}/$name"
    }

    /**
     * Returns the DataStore instance for iOS platform.
     *
     * @return The configured DataStore<Preferences> instance
     */
    actual fun getDataStore(): DataStore<Preferences> = dataStore
}