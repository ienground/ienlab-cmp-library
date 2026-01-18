package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertActionStyleDestructive
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert

private fun UIAlertActionStyle.toStyle() = when (this) {
    UIAlertActionStyle.Cancel -> UIAlertActionStyleCancel
    UIAlertActionStyle.Default -> UIAlertActionStyleDefault
    UIAlertActionStyle.Destructive -> UIAlertActionStyleDestructive
}

@Composable
private fun BaseAlertDialog(
    visible: Boolean,
    title: String?,
    message: String?,
    buttons: (UIAlertController) -> Unit
) {
    val viewController = LocalUIViewController.current

    if (visible) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = title,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )

        buttons(alert)

        viewController.presentViewController(alert, animated = true, completion = null)
    }
}

@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textDismiss: String,
    styleDismiss: UIAlertActionStyle,
    onDismiss: () -> Unit
) {
    BaseAlertDialog(
        visible = visible,
        title = title,
        message = message
    ) { alertController ->
        val dismissAction = UIAlertAction.actionWithTitle(
            title = textDismiss,
            style = styleDismiss.toStyle(),
            handler = {
                onDismiss()
            }
        )

        alertController.addAction(dismissAction)
    }
}

@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textDismiss: String,
    styleDismiss: UIAlertActionStyle,
    onDismiss: () -> Unit,
    textConfirm: String,
    styleConfirm: UIAlertActionStyle,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean
) {
    BaseAlertDialog(
        visible = visible,
        title = title,
        message = message
    ) { alertController ->
        val dismissAction = UIAlertAction.actionWithTitle(
            title = textDismiss,
            style = styleDismiss.toStyle(),
            handler = {
                onDismiss()
            }
        )
        val confirmAction = UIAlertAction.actionWithTitle(
            title = textConfirm,
            style = styleConfirm.toStyle(),
            handler = {
                onConfirm()
            }
        ).apply {
            setEnabled(enabledConfirm)
        }

        alertController.addAction(dismissAction)
        alertController.addAction(confirmAction)

        alertController.preferredAction = confirmAction
    }
}

@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textNeutral: String,
    styleNeutral: UIAlertActionStyle,
    onNeutral: () -> Unit,
    enabledNeutral: Boolean,
    textNegative: String,
    styleNegative: UIAlertActionStyle,
    onNegative: () -> Unit,
    textPositive: String,
    stylePositive: UIAlertActionStyle,
    onPositive: () -> Unit,
    enabledPositive: Boolean
) {
    BaseAlertDialog(
        visible = visible,
        title = title,
        message = message
    ) { alertController ->
        val neutralAction = UIAlertAction.actionWithTitle(
            title = textNeutral,
            style = styleNeutral.toStyle(),
            handler = {
                onNeutral()
            }
        ).apply {
            setEnabled(enabledNeutral)
        }
        val negativeAction = UIAlertAction.actionWithTitle(
            title = textNegative,
            style = styleNegative.toStyle(),
            handler = {
                onNegative()
            }
        )
        val positiveAction = UIAlertAction.actionWithTitle(
            title = textPositive,
            style = stylePositive.toStyle(),
            handler = {
                onPositive()
            }
        ).apply {
            setEnabled(enabledPositive)
        }

        alertController.addAction(neutralAction)
        alertController.addAction(negativeAction)
        alertController.addAction(positiveAction)

        alertController.preferredAction = positiveAction
    }
}