package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.M3SaveAlertDialog

@Composable
actual fun SaveAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onCancel: () -> Unit,
    onUnsave: () -> Unit,
    enabledUnsave: Boolean,
    onSave: () -> Unit,
    enabledSave: Boolean
) {
    M3SaveAlertDialog(
        modifier = modifier,
        visible = visible,
        onCancel = onCancel,
        onUnsave = onUnsave,
        enabledUnsave = enabledUnsave,
        onSave = onSave,
        enabledSave = enabledSave
    )
}