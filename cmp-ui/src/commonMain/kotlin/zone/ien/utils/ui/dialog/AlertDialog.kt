package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ienlab_cmp_library.cmp_ui.generated.resources.Res
import ienlab_cmp_library.cmp_ui.generated.resources.cancel
import ienlab_cmp_library.cmp_ui.generated.resources.close
import ienlab_cmp_library.cmp_ui.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun M3BaseAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    onDismiss: () -> Unit,
    buttons: @Composable RowScope.() -> Unit
) {
    if (visible) {
        BaseDialog(
            modifier = modifier,
            icon = icon,
            title = title,
            content = message?.let { {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            } },
            onCancel = onDismiss,
            buttons = { Row(modifier = it) { buttons() } }
        )
    }
}

@Composable
fun M3AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.close),
    onDismiss: () -> Unit,
) {
    M3BaseAlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onDismiss,
        buttons = {
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onDismiss,
            ) { Text(text = textDismiss) }
        }
    )
}

@Composable
fun M3AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.cancel),
    onDismiss: () -> Unit,
    textConfirm: String = stringResource(Res.string.ok),
    onConfirm: () -> Unit,
    enabledConfirm: Boolean = true
) {
    M3BaseAlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onDismiss,
        buttons = {
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onDismiss,
            ) { Text(text = textDismiss) }

            TextButton(
                onClick = onConfirm,
                enabled = enabledConfirm
            ) { Text(text = textConfirm) }
        }
    )
}

@Composable
fun M3AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textNeutral: String = stringResource(Res.string.close),
    onNeutral: () -> Unit,
    enabledNeutral: Boolean = true,
    textNegative: String = stringResource(Res.string.cancel),
    onNegative: () -> Unit,
    textPositive: String = stringResource(Res.string.ok),
    onPositive: () -> Unit,
    enabledPositive: Boolean = true
) {
    M3BaseAlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onNegative,
        buttons = {
            TextButton(
                onClick = onNeutral,
                enabled = enabledNeutral
            ) { Text(text = textNeutral) }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onNegative,
            ) { Text(text = textNegative) }

            TextButton(
                onClick = onPositive,
                enabled = enabledPositive
            ) { Text(text = textPositive) }
        }
    )
}