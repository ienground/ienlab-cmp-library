package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.IenTextFieldDialog
import zone.ien.utils.ui.utils.TextFieldDialogData

@Composable
actual fun TextFieldDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textFields: Map<String, TextFieldDialogData>,
    textDismiss: String,
    styleDismiss: UIAlertActionStyle,
    onDismiss: () -> Unit,
    textConfirm: String,
    styleConfirm: UIAlertActionStyle,
    onConfirm: (Map<String, String>) -> Unit
) {
    IenTextFieldDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        textFields = textFields,
        textDismiss = textDismiss,
        onDismiss = onDismiss,
        textConfirm = textConfirm,
        onConfirm = onConfirm
    )
}