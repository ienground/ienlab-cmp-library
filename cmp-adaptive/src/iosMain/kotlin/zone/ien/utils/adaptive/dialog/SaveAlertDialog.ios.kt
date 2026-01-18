package zone.ien.utils.adaptive.dialog

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
import zone.ien.utils.ui.dialog.M3AlertDialog
import zone.ien.utils.ui.icon.MaterialIcons

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
    AlertDialog(
        visible = visible,
        icon = null,
        title = stringResource(Res.string.save_dialog_title),
        message = stringResource(Res.string.save_dialog_content),
        textNeutral = stringResource(Res.string.not_save),
        styleNeutral = UIAlertActionStyle.Destructive,
        onNeutral = onUnsave,
        enabledNeutral = enabledUnsave,
        textNegative = stringResource(Res.string.cancel),
        styleNegative = UIAlertActionStyle.Cancel,
        onNegative = onCancel,
        textPositive = stringResource(Res.string.save),
        stylePositive = UIAlertActionStyle.Default,
        onPositive = onSave,
        enabledPositive = enabledSave
    )
}