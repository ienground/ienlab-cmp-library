package zone.ien.utils.utils

import android.util.Log

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
        if (isDebug) Log.e(tag, buildLogMsg(message))
    }

    /**
     * Log Level Warning
     */
    actual fun w(tag: String, message: String) {
        if (isDebug) Log.w(tag, buildLogMsg(message))
    }

    /**
     * Log Level Information
     */
    actual fun i(tag: String, message: String) {
        if (isDebug) Log.i(tag, buildLogMsg(message))
    }

    /**
     * Log Level Debug
     */
    actual fun d(tag: String, message: String) {
        if (isDebug) Log.d(tag, buildLogMsg(message))
    }

    /**
     * Log Level Verbose
     */
    actual fun v(tag: String, message: String) {
        if (isDebug) Log.v(tag, buildLogMsg(message))
    }

    private fun buildLogMsg(message: String?): String {
        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder()
        sb.append("[")
        sb.append(ste.fileName.replace(".kt", ""))
        sb.append("::")
        sb.append(ste.methodName)
        sb.append("] ")
        sb.append(message)
        return sb.toString()
    }

}