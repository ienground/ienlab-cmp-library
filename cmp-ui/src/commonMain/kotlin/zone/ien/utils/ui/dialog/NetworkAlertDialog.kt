package zone.ien.utils.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.network_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.network_dialog_title
import zone.ien.utils.cmp_ui.generated.resources.retry
import zone.ien.utils.ui.icon.MaterialIcons

@Composable
fun M3NetworkAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = { Icon(imageVector = MaterialIcons.CloudOff, contentDescription = null) },
        title = stringResource(Res.string.network_dialog_title),
        message = stringResource(Res.string.network_dialog_content),
        textDismiss = stringResource(Res.string.close),
        onDismiss = onDismiss,
        textConfirm = stringResource(Res.string.retry),
        onConfirm = onConfirm
    )
}