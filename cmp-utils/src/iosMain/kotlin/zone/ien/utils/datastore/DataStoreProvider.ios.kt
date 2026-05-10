package zone.ien.utils.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import zone.ien.utils.utils.documentDirectory

/**
 * DefaultDataStoreProvider의 iOS 구현.
 * 
 * 이 클래스는 iOS 플랫폼에서 DataStore에 접근하는 플랫폼별 구현을 제공합니다.
 * 문서 디렉토리를 사용하여 환경설정 데이터를 저장합니다.
 * 
 * @param name 데이터 저장소 파일 이름
 */
actual class DefaultDataStoreProvider actual constructor(name: String) {
    private val dataStore = createDataStoreWithDefaults(migrations = listOf()) {
        "${documentDirectory()}/$name"
    }

    /**
     * iOS 플랫폼용 DataStore 인스턴스를 반환합니다.
     * 
     * @return 구성된 DataStore<Preferences> 인스턴스
     */
    actual fun getDataStore(): DataStore<Preferences> = dataStore
}