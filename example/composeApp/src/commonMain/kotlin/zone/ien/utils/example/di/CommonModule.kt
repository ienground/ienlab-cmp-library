package zone.ien.utils.example.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.qualifier.named
import org.koin.dsl.module
import zone.ien.utils.datastore.DefaultDataStoreProvider
import zone.ien.utils.example.di.KoinKey.DEFAULT_DATASTORE

val commonModule = module {
    single<DataStore<Preferences>>(named(DEFAULT_DATASTORE)) { DefaultDataStoreProvider().getDataStore() }
}