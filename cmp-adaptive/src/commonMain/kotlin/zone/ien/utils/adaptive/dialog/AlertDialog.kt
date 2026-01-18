package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

enum class UIAlertActionStyle {
    Default, Cancel, Destructive
}

@Composable
expect fun AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.close),
    styleDismiss: UIAlertActionStyle = UIAlertActionStyle.Cancel,
    onDismiss: () -> Unit,
)

@Composable
expect fun AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.cancel),
    styleDismiss: UIAlertActionStyle = UIAlertActionStyle.Cancel,
    onDismiss: () -> Unit,
    textConfirm: String = stringResource(Res.string.ok),
    styleConfirm: UIAlertActionStyle = UIAlertActionStyle.Default,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean = true
)

@Composable
expect fun AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textNeutral: String = stringResource(Res.string.close),
    styleNeutral: UIAlertActionStyle = UIAlertActionStyle.Default,
    onNeutral: () -> Unit,
    enabledNeutral: Boolean = true,
    textNegative: String = stringResource(Res.string.cancel),
    styleNegative: UIAlertActionStyle = UIAlertActionStyle.Cancel,
    onNegative: () -> Unit,
    textPositive: String = stringResource(Res.string.ok),
    stylePositive: UIAlertActionStyle = UIAlertActionStyle.Default,
    onPositive: () -> Unit,
    enabledPositive: Boolean = true
)