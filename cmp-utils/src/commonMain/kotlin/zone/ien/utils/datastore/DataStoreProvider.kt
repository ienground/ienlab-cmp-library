package zone.ien.utils.datastore

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

expect class DefaultDataStoreProvider(name: String = DEFAULT_DATASTORE_NAME) {
    fun getDataStore(): DataStore<Preferences>
}

internal const val DEFAULT_DATASTORE_NAME = "datastore/settings.preferences_pb"

internal fun createDataStoreWithDefaults(
    migrations: List<DataMigration<Preferences>> = listOf(),
    producePath: () -> String
) = PreferenceDataStoreFactory.createWithPath(
    corruptionHandler = null,
    migrations = migrations,
    produceFile = {
        producePath().toPath()
    }
)