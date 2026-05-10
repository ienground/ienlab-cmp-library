package zone.ien.utils.utils

import android.content.Context
import androidx.startup.Initializer

/**
 * 전역 애플리케이션 컨텍스트 참조.
 * 이 참조는 [ApplicationContextInitializer]에 의해 시작 시 초기화됩니다.
 * 
 * @property applicationContext 전역 애플리케이션 컨텍스트.
 */
lateinit var applicationContext: Context
    private set

/**
 * 전역 애플리케이션 컨텍스트를 설정하는 초기화기.
 * 이 클래스는 Android의 App Startup 라이브러리에서 앱 시작 시 전역 애플리케이션 컨텍스트를 초기화하는 데 사용됩니다.
 * 
 * 애플리케이션 내 어디서든 애플리케이션 컨텍스트에 접근할 수 있도록 단일 인스턴스로 설계되었습니다.
 */
internal class ApplicationContextInitializer : Initializer<Context> {
    override fun create(context: Context): Context = context.also {
        applicationContext = it.applicationContext
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}