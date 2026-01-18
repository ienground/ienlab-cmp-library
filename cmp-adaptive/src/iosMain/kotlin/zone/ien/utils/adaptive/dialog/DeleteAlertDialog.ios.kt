package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.delete_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.delete_dialog_title
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun DeleteAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean
) {
    AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = null,
        title = stringResource(Res.string.delete_dialog_title),
        message = stringResource(Res.string.delete_dialog_content),
        onDismiss = onDismiss,
        styleConfirm = UIAlertActionStyle.Destructive,
        onConfirm = onConfirm,
        enabledConfirm = enabledConfirm
    )
}