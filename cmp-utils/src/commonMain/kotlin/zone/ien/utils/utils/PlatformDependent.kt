package zone.ien.utils.utils

/**
 * 실행 중인 애플리케이션과 기기의 플랫폼 정보를 제공합니다.
 *
 * 앱 모듈의 `BuildKonfig`에 의존하지 않고, 각 플랫폼의 런타임 정보에서 값을 읽습니다.
 * Android에서는 앱 시작 시 초기화되는 라이브러리의 애플리케이션 컨텍스트를 사용합니다.
 */
expect object PlatformDependent {
    /** 애플리케이션이 디버그 가능한 빌드인지 여부입니다. */
    val isDebug: Boolean

    /** 현재 플랫폼이 iOS인지 여부입니다. */
    val isIos: Boolean

    /** 설치된 애플리케이션의 식별자입니다. */
    val appId: String

    /** 설치된 애플리케이션의 표시 버전입니다. */
    val versionName: String

    /** 설치된 애플리케이션의 빌드 번호입니다. 확인할 수 없으면 0입니다. */
    val versionCode: Int

    /** 현재 기기의 이름입니다. */
    val deviceName: String

    /** 현재 운영체제의 버전입니다. */
    val deviceOS: String
}
