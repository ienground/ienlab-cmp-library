package zone.ien.utils.utils

import android.util.Log

/**
 * Android용 로그 기능 구현.
 * 
 * 이 클래스는 Android 플랫폼에서 로그를 출력하는 기능을 제공합니다.
 * 디버그 모드 여부에 따라 로그를 출력하거나 무시합니다.
 * 
 * @property isDebug 디버그 모드 여부. true이면 로그 출력, false이면 무시.
 */
actual object Dlog {
    private var _isDebug: Boolean = false // 내부 상태 저장 변수
    actual val isDebug: Boolean
        get() = _isDebug

    /**
     * 로그 기능의 디버그 모드를 설정합니다.
     * 
     * @param isDebug 디버그 모드 여부. true이면 로그 출력, false이면 무시.
     */
    actual fun init(isDebug: Boolean) {
        this._isDebug = isDebug
    }

    /**
     * 오류 레벨 로그를 출력합니다.
     * 
     * @param tag 로그 태그
     * @param message 로그 메시지
     */
    actual fun e(tag: String, message: String) {
        if (isDebug) Log.e(tag, buildLogMsg(message))
    }

    /**
     * 정보 레벨 로그를 출력합니다.
     * 
     * @param tag 로그 태그
     * @param message 로그 메시지
     */
    actual fun i(tag: String, message: String) {
        if (isDebug) Log.i(tag, buildLogMsg(message))
    }

    /**
     * 경고 레벨 로그를 출력합니다.
     * 
     * @param tag 로그 태그
     * @param message 로그 메시지
     */
    actual fun w(tag: String, message: String) {
        if (isDebug) Log.w(tag, buildLogMsg(message))
    }

    /**
     * 디버그 레벨 로그를 출력합니다.
     * 
     * @param tag 로그 태그
     * @param message 로그 메시지
     */
    actual fun d(tag: String, message: String) {
        if (isDebug) Log.d(tag, buildLogMsg(message))
    }

    /**
     * Verbal 레벨 로그를 출력합니다.
     *
     * @param tag 로그 태그
     * @param message 로그 메시지
     */
    actual fun v(tag: String, message: String) {
        if (isDebug) Log.v(tag, buildLogMsg(message))
    }

    /**
     * 로그 메시지에 시간과 스택 정보를 추가합니다.
     * 
     * @param message 원본 로그 메시지
     * @return 포맷팅된 로그 메시지
     */
    private fun buildLogMsg(message: String): String {
        val stackTrace = Thread.currentThread().stackTrace
        val caller = stackTrace[4].let { "${it.fileName}:${it.lineNumber}" }
        return "[$caller] $message"
    }
}