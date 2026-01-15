package zone.ien.utils.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ienlab_cmp_library.cmp_ui.generated.resources.Res
import ienlab_cmp_library.cmp_ui.generated.resources.delete_dialog_content
import ienlab_cmp_library.cmp_ui.generated.resources.delete_dialog_title
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.ui.icon.MaterialIcons

@Composable
fun M3DeleteAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean = true
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = { Icon(imageVector = MaterialIcons.Delete, contentDescription = null) },
        title = stringResource(Res.string.delete_dialog_title),
        message = stringResource(Res.string.delete_dialog_content),
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        enabledConfirm = enabledConfirm
    )
}