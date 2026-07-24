package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.IenNetworkAlertDialog

@Composable
actual fun NetworkAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onDismiss: (() -> Unit)?,
) {
    IenNetworkAlertDialog(
        modifier = modifier,
        visible = visible,
        onDismiss = onDismiss,
    )
}