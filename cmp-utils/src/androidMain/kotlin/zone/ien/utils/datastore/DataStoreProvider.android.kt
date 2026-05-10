package zone.ien.utils.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import zone.ien.utils.utils.applicationContext

/**
 * Android implementation of DefaultDataStoreProvider.
 *
 * This class provides Android-specific implementation for DataStore access.
 * It uses the application context's files directory to store preferences data.
 *
 * @param name The name of the data store file
 */
actual class DefaultDataStoreProvider actual constructor(name: String) {
    private val context = applicationContext
    private val dataStore = createDataStoreWithDefaults(migrations = emptyList()) {
        context.filesDir.resolve(name).absolutePath
    }

    /**
     * Returns the DataStore instance for Android platform.
     *
     * @return The configured DataStore<Preferences> instance
     */
    actual fun getDataStore(): DataStore<Preferences> = dataStore
}