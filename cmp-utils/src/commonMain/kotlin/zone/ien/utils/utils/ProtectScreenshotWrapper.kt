package zone.ien.utils.utils

import androidx.compose.runtime.Composable

@Composable
expect fun ProtectScreenshotWrapper(
    content: @Composable () -> Unit,
)