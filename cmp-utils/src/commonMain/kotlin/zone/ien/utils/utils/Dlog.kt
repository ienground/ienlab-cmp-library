package zone.ien.utils.utils

expect object Dlog {
    internal val isDebug: Boolean

    fun init(isDebug: Boolean)

    /**
     * Log Level Error
     */
    fun e(tag: String, message: String)

    /**
     * Log Level Warning
     */
    fun w(tag: String, message: String)

    /**
     * Log Level Information
     */
    fun i(tag: String, message: String)

    /**
     * Log Level Debug
     */
    fun d(tag: String, message: String)

    /**
     * Log Level Verbose
     */
    fun v(tag: String, message: String)
}