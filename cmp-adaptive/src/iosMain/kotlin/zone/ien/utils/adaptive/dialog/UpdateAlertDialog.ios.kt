package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.move_to_app_store

@Composable
actual fun UpdateAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    appName: String,
    onDismiss: () -> Unit,
    title: String,
    message: String,
) {
    AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = null,
        title = title,
        message = message,
        onDismiss = onDismiss,
        textDismiss = stringResource(Res.string.move_to_app_store)
    )
}
