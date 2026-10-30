package zone.ien.utils.utils

import android.view.Window
import android.view.WindowManager
import java.util.WeakHashMap

object SecureWindowManager {
    private data class Protection(var count: Int, val wasSecure: Boolean)

    private val protections = WeakHashMap<Window, Protection>()

    @Synchronized
    fun acquire(window: Window) {
        val protection = protections[window]
        if (protection != null) {
            protection.count++
            return
        }
        val wasSecure = window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0
        protections[window] = Protection(count = 1, wasSecure = wasSecure)
        if (!wasSecure) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    @Synchronized
    fun release(window: Window) {
        val protection = protections[window] ?: return
        if (--protection.count > 0) return
        protections.remove(window)
        if (!protection.wasSecure) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
