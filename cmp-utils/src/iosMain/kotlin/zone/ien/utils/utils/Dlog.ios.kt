package zone.ien.utils.utils

import platform.Foundation.NSLog

actual object Dlog {
    private var _isDebug: Boolean = false // 내부 상태 저장 변수
    actual val isDebug: Boolean
        get() = _isDebug

    actual fun init(isDebug: Boolean) {
        this._isDebug = isDebug
    }

    /**
     * Log Level Error
     */
    actual fun e(tag: String, message: String) {
        NSLog("[${tag}]\t\uD83D\uDD34\t${message}")
    }

    /**
     * Log Level Warning
     */
    actual fun w(tag: String, message: String) {
        NSLog("[${tag}]\t\uD83D\uDFE1\t${message}")
    }

    /**
     * Log Level Information
     */
    actual fun i(tag: String, message: String) {
        NSLog("[${tag}]\t\uD83D\uDFE2\t${message}")
    }

    /**
     * Log Level Debug
     */
    actual fun d(tag: String, message: String) {
        NSLog("[${tag}]\t\uD83D\uDD35\t${message}")
    }

    /**
     * Log Level Verbose
     */
    actual fun v(tag: String, message: String) {
        NSLog("[${tag}]\t⚪\uFE0F\t${message}")
    }
}