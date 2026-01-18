package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.move_to_app_store
import zone.ien.utils.cmp_ui.generated.resources.version_update_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.version_update_dialog_title
import zone.ien.utils.ui.icon.MaterialIcons

@Composable
actual fun UpdateAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    appName: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = null,
        title = stringResource(Res.string.version_update_dialog_title),
        message = stringResource(Res.string.version_update_dialog_content, appName),
        onDismiss = onDismiss,
        textDismiss = stringResource(Res.string.move_to_app_store)
    )
}