package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.IenUpdateAlertDialog

@Composable
actual fun UpdateAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    appName: String,
    onDismiss: () -> Unit
) {
    IenUpdateAlertDialog(
        modifier = modifier,
        visible = visible,
        appName = appName,
        onDismiss = onDismiss
    )
}