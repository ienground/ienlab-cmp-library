package zone.ien.utils.utils

import platform.Foundation.NSLog

/**
 * iOS용 로그 기능 구현.
 * 
 * 이 클래스는 iOS 플랫폼에서 로그를 출력하는 기능을 제공합니다.
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
        NSLog("[${tag}]\t\uD83D\uDD34\t${message}")
    }

    /**
     * 경고 레벨 로그를 출력합니다.
     * 
     * @param tag 로그 태그
     * @param message 로그 메시지
     */
    actual fun w(tag: String, message: String) {
        NSLog("[${tag}]\t\uD83D\uDFE1\t${message}")
    }

    /**
     * 정보 레벨 로그를 출력합니다.
     * 
     * @param tag 로그 태그
     * @param message 로그 메시지
     */
    actual fun i(tag: String, message: String) {
        NSLog("[${tag}]\t\uD83D\uDFE2\t${message}")
    }

    /**
     * 디버그 레벨 로그를 출력합니다.
     * 
     * @param tag 로그 태그
     * @param message 로그 메시지
     */
    actual fun d(tag: String, message: String) {
        NSLog("[${tag}]\t\uD83D\uDD35\t${message}")
    }

    /**
     * 상세 레벨 로그를 출력합니다.
     * 
     * @param tag 로그 태그
     * @param message 로그 메시지
     */
    actual fun v(tag: String, message: String) {
        NSLog("[${tag}]\t⚪\uFE0F\t${message}")
    }
}