package zone.ien.utils.utils

import android.app.Activity

/**
 * Koin 초기화 시
 * androidContext(this@MyApplication)
 * modules(androidAppModule) 사용
 *
 * AndroidAppModule을 androidApp의 di에 추가 후, single<[CurrentActivityProvider]>(createdAtStart = true) { [DefaultCurrentActivityProvider] (get()) } 추가
 */
interface CurrentActivityProvider {
    fun currentActivity(): Activity?
}