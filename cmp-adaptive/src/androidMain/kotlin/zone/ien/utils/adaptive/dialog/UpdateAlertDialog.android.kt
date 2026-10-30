package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.IenUpdateAlertDialog

@Composable
actual fun UpdateAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    appName: String,
    onDismiss: () -> Unit,
    title: String,
    message: String,
) {
    IenUpdateAlertDialog(
        modifier = modifier,
        visible = visible,
        appName = appName,
        onDismiss = onDismiss,
        title = title,
        message = message,
    )
}
