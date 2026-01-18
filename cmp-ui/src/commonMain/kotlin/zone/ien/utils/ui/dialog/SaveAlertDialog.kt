package zone.ien.utils.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.not_save
import zone.ien.utils.cmp_ui.generated.resources.save
import zone.ien.utils.cmp_ui.generated.resources.save_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.save_dialog_title
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.ui.icon.MaterialIcons

@Composable
fun M3SaveAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onCancel: () -> Unit,
    onUnsave: () -> Unit,
    enabledUnsave: Boolean = true,
    onSave: () -> Unit,
    enabledSave: Boolean = true,
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = { Icon(imageVector = MaterialIcons.Save, contentDescription = null) },
        title = stringResource(Res.string.save_dialog_title),
        message = stringResource(Res.string.save_dialog_content),
        textNeutral = stringResource(Res.string.not_save),
        onNeutral = onUnsave,
        enabledNeutral = enabledUnsave,
        textNegative = stringResource(Res.string.cancel),
        onNegative = onCancel,
        textPositive = stringResource(Res.string.save),
        onPositive = onSave,
        enabledPositive = enabledSave
    )
}