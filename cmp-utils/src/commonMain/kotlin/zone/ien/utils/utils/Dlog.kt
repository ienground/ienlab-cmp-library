package zone.ien.utils.utils

/**
 * 교차 플랫폼 로깅 유틸리티.
 * 
 * 이 객체는 다양한 플랫폼에서 일관된 로깅 인터페이스를 제공하며,
 * 디버깅 및 애플리케이션 동작 모니터링을 위한 여러 로그 레벨을 제공합니다.
 * 
 * 로깅 시스템은 디버그 플래그로 초기화되어 상세한 로깅 정보를 표시할지 결정합니다.
 * 
 * @see init
 * @see e
 * @see w
 * @see i
 * @see d
 * @see v
 */
expect object Dlog {
    /**
     * 애플리케이션이 디버그 모드인지 나타냅니다.
     * 
     * @property isDebug 애플리케이션이 디버그 모드에서 실행 중이면 true, 그렇지 않으면 false.
     */
    internal val isDebug: Boolean

    /**
     * 로깅 시스템을 초기화합니다.
     * 
     * @param isDebug 디버그 로깅을 활성화하려면 true, 비활성화하려면 false.
     */
    fun init(isDebug: Boolean)

    /**
     * 에러 메시지를 로그합니다.
     * 
     * @param tag 로그 메시지의 태그.
     * @param message 로그할 에러 메시지.
     */
    fun e(tag: String, message: String)

    /**
     * 경고 메시지를 로그합니다.
     * 
     * @param tag 로그 메시지의 태그.
     * @param message 로그할 경고 메시지.
     */
    fun w(tag: String, message: String)

    /**
     * 정보 메시지를 로그합니다.
     * 
     * @param tag 로그 메시지의 태그.
     * @param message 로그할 정보 메시지.
     */
    fun i(tag: String, message: String)

    /**
     * 디버그 메시지를 로그합니다.
     * 
     * @param tag 로그 메시지의 태그.
     * @param message 로그할 디버그 메시지.
     */
    fun d(tag: String, message: String)

    /**
     * 상세 메시지를 로그합니다.
     * 
     * @param tag 로그 메시지의 태그.
     * @param message 로그할 상세 메시지.
     */
    fun v(tag: String, message: String)
}