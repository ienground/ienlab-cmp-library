package zone.ien.utils.datastore

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

/**
 * 환경설정을 위한 교차 플랫폼 DataStore 구현을 제공합니다.
 *
 * 이 클래스는 Android와 iOS의 DataStore에 접근하는 플랫폼 독립적 방식을 구현합니다.
 * 플랫폼별 초기화 및 파일 관리를 처리합니다.
 *
 * @param name 데이터 저장소 파일 이름 (기본값: "datastore/settings.preferences_pb")
 */
expect class DefaultDataStoreProvider(name: String = DEFAULT_DATASTORE_NAME) {
    /**
     * 이 제공자에 대한 DataStore 인스턴스를 반환합니다.
     *
     * @return 구성된 DataStore<Preferences> 인스턴스
     */
    fun getDataStore(): DataStore<Preferences>
}

/**
 * 데이터 저장소 파일의 기본 이름입니다.
 */
internal const val DEFAULT_DATASTORE_NAME = "datastore/settings.preferences_pb"

/**
 * 기본 구성을 사용하여 DataStore를 생성합니다.
 *
 * 이 헬퍼 함수는 제공된 마이그레이션과 경로 생성 함수를 사용하여 PreferenceDataStore를 생성합니다.
 * 수정 감지 핸들러는 없으며, 기본적으로 구성을 적용합니다.
 *
 * @param migrations 적용할 데이터 마이그레이션 목록
 * @param producePath 데이터 저장소 파일의 경로를 생성하는 함수
 * @return 구성된 PreferenceDataStore 인스턴스
 */
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