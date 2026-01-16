package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.M3ProgressDialog

@Composable
actual fun ProgressDialog(
    modifier: Modifier,
    visible: Boolean,
    isLoadingIndicator: Boolean,
    isWavyIndicator: Boolean
) {
    M3ProgressDialog(
        modifier = modifier,
        visible = visible,
        isLoadingIndicator = isLoadingIndicator,
        isWavyIndicator = isWavyIndicator
    )
}

@Composable
actual fun ProgressDialog(
    modifier: Modifier,
    visible: Boolean,
    progress: () -> Float,
    isWavyIndicator: Boolean
) {
    M3ProgressDialog(
        modifier = modifier,
        visible = visible,
        progress = progress,
        isWavyIndicator = isWavyIndicator
    )
}