package zone.ien.utils.utils

import android.view.Window
import android.view.WindowManager
import java.util.concurrent.atomic.AtomicInteger

object SecureWindowManager {
    private val count = AtomicInteger(0)

    fun acquire(window: Window) {
        if (count.incrementAndGet() == 1) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    fun release(window: Window) {
        val newCount = count.decrementAndGet()
        if (newCount == 0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else if (newCount < 0) {
            count.set(0) // Prevent underflow
        }
    }
}