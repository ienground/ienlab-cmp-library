package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.M3NetworkAlertDialog

@Composable
actual fun NetworkAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onDismiss: (() -> Unit)?,
) {
    M3NetworkAlertDialog(
        modifier = modifier,
        visible = visible,
        onDismiss = onDismiss,
    )
}