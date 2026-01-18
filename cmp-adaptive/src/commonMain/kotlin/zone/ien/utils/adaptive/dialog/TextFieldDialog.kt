package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.ok
import zone.ien.utils.ui.utils.TextFieldDialogData

@Composable
expect fun TextFieldDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textFields: Map<String, TextFieldDialogData> = mapOf(),
    textDismiss: String = stringResource(Res.string.cancel),
    styleDismiss: UIAlertActionStyle = UIAlertActionStyle.Cancel,
    onDismiss: () -> Unit,
    textConfirm: String = stringResource(Res.string.ok),
    styleConfirm: UIAlertActionStyle = UIAlertActionStyle.Default,
    onConfirm: (Map<String, String>) -> Unit,
)