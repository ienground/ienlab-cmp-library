package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.network_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.network_dialog_title
import zone.ien.utils.cmp_ui.generated.resources.retry

@Composable
actual fun NetworkAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onDismiss: (() -> Unit)?,
) {
    AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = null,
        title = stringResource(Res.string.network_dialog_title),
        message = stringResource(Res.string.network_dialog_content),
        textDismiss = stringResource(Res.string.close),
        onDismiss = onDismiss,
    )
}