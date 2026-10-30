package zone.ien.utils.utils

import android.app.Dialog
import android.view.WindowManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SecureWindowManagerTest {
    @Test
    fun overlappingWindowsRetainIndependentProtection() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.runOnMainSync {
            val first = requireNotNull(Dialog(instrumentation.targetContext).window)
            val second = requireNotNull(Dialog(instrumentation.targetContext).window)
            try {
                SecureWindowManager.acquire(first)
                SecureWindowManager.acquire(first)
                SecureWindowManager.acquire(second)
                assertTrue(first.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0)
                assertTrue(second.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0)

                SecureWindowManager.release(first)
                assertTrue(first.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0)
                SecureWindowManager.release(second)
                assertFalse(second.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0)
                assertTrue(first.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0)
                SecureWindowManager.release(first)
                assertFalse(first.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0)
            } finally {
                SecureWindowManager.release(first)
                SecureWindowManager.release(first)
                SecureWindowManager.release(second)
            }
        }
    }

    @Test
    fun releasingProtectionPreservesExistingSecureFlag() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.runOnMainSync {
            val window = requireNotNull(Dialog(instrumentation.targetContext).window)
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            try {
                SecureWindowManager.acquire(window)
                SecureWindowManager.release(window)
                assertTrue(window.attributes.flags and WindowManager.LayoutParams.FLAG_SECURE != 0)
            } finally {
                SecureWindowManager.release(window)
                window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }
}
