package zone.ien.utils.utils

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
actual fun ProtectScreenshotWrapper(
    content: @Composable () -> Unit,
) {
    val activity = LocalActivity.current

    DisposableEffect(Unit) {
        activity?.window?.let { SecureWindowManager.acquire(it) }
        onDispose {
            activity?.window?.let { SecureWindowManager.release(it) }
        }
    }

    content()
}