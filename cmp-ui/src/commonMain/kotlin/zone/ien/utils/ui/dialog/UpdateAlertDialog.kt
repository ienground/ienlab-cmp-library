package zone.ien.utils.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.version_update_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.version_update_dialog_title
import zone.ien.utils.ui.icon.MaterialIcons

@Composable
fun M3UpdateAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = { Icon(imageVector = MaterialIcons.Update, contentDescription = null) },
        title = stringResource(Res.string.version_update_dialog_title),
        message = stringResource(Res.string.version_update_dialog_content),
        onDismiss = onDismiss
    )
}