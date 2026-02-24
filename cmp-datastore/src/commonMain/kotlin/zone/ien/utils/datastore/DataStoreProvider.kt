package zone.ien.utils.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DefaultDataStoreProvider() {
    fun getDataStore(): DataStore<Preferences>
}

internal fun createDataStoreWithDefaults(
    migrations: List<DataMigration<Preferences>> = emptyList(),
    producePath: () -> String
) = PreferenceDataStoreFactory.createWithPath(
    corruptionHandler = null,
    migrations = migrations,
    produceFile = {
        producePath().toPath()
    }
)