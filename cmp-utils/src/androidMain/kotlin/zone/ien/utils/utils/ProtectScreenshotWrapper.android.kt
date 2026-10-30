package zone.ien.utils.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
actual fun ProtectScreenshotWrapper(
    content: @Composable () -> Unit,
) {
    val window = LocalActivity.current?.window

    DisposableEffect(window) {
        window?.let { SecureWindowManager.acquire(it) }
        onDispose {
            window?.let { SecureWindowManager.release(it) }
        }
    }

    content()
}
