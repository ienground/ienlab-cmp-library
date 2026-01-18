package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIAlertAction
import platform.UIKit.UIKeyboardTypeASCIICapable
import platform.UIKit.UIKeyboardTypeDecimalPad
import platform.UIKit.UIKeyboardTypeDefault
import platform.UIKit.UIKeyboardTypeEmailAddress
import platform.UIKit.UIKeyboardTypeNumberPad
import platform.UIKit.UIKeyboardTypePhonePad
import platform.UIKit.UIKeyboardTypeURL
import platform.UIKit.UILabel
import platform.UIKit.UIReturnKeyType
import platform.UIKit.UITextFieldTextDidChangeNotification
import platform.UIKit.UITextFieldViewMode
import zone.ien.utils.ui.utils.TextFieldDialogData

internal fun KeyboardType.toHig() = when (this) {
    KeyboardType.Text -> UIKeyboardTypeDefault
    KeyboardType.Ascii -> UIKeyboardTypeASCIICapable
    KeyboardType.Number -> UIKeyboardTypeNumberPad
    KeyboardType.Phone -> UIKeyboardTypePhonePad
    KeyboardType.Uri -> UIKeyboardTypeURL
    KeyboardType.Email -> UIKeyboardTypeEmailAddress
    KeyboardType.Password -> UIKeyboardTypeDefault
    KeyboardType.NumberPassword -> UIKeyboardTypeNumberPad
    KeyboardType.Decimal -> UIKeyboardTypeDecimalPad
    else -> UIKeyboardTypeDefault
}

internal fun KeyboardType.isSecureText() = when (this) {
    KeyboardType.Password, KeyboardType.NumberPassword -> true
    else -> false
}

internal fun ImeAction.toHig() = when (this) {
    ImeAction.Go -> UIReturnKeyType.UIReturnKeyGo
    ImeAction.Search -> UIReturnKeyType.UIReturnKeySearch
    ImeAction.Send -> UIReturnKeyType.UIReturnKeySend
    ImeAction.Previous -> UIReturnKeyType.UIReturnKeyDefault
    ImeAction.Next -> UIReturnKeyType.UIReturnKeyNext
    ImeAction.Done -> UIReturnKeyType.UIReturnKeyDone
    else -> UIReturnKeyType.UIReturnKeyDefault
}


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun TextFieldDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textFields: Map<String, TextFieldDialogData>,
    textDismiss: String,
    styleDismiss: UIAlertActionStyle,
    onDismiss: () -> Unit,
    textConfirm: String,
    styleConfirm: UIAlertActionStyle,
    onConfirm: (Map<String, String>) -> Unit
) {
    val textStates = remember(textFields.keys) {
        mutableStateMapOf<String, String>().apply {
            textFields.forEach { (key, data) ->
                put(key, data.initialValue)
            }
        }
    }

    val enabledConfirm by derivedStateOf {
        textFields.all { (key, field) ->
            val text = textStates[key] ?: ""
            field.valid(text)
        }
    }


    var confirmActionRef by remember { mutableStateOf<UIAlertAction?>(null) }

    LaunchedEffect(enabledConfirm) {
        confirmActionRef?.enabled = enabledConfirm
    }

    HigBaseAlertDialog(
        visible = visible,
        title = title,
        message = message
    ) { alertController ->
        textFields.forEach { (key, field) ->
            alertController.addTextFieldWithConfigurationHandler { textField ->
                textField?.text = textStates[key]
                textField?.secureTextEntry = field.keyboardType.isSecureText()
                textField?.placeholder = field.placeholder
                textField?.keyboardType = field.keyboardType.toHig()
                textField?.returnKeyType = field.imeAction.toHig()

                // Prefix
                field.prefix?.let {
                    val prefixLabel = UILabel()
                    prefixLabel.setText(it)
                    prefixLabel.sizeToFit()
                    prefixLabel.setFrame(CGRectMake(0.0, 0.0, prefixLabel.frame.useContents { size.width }, prefixLabel.frame.useContents { size.height }))

                    textField?.leftView = prefixLabel
                    textField?.leftViewMode = UITextFieldViewMode.UITextFieldViewModeAlways
                }

                // Suffix
                field.suffix?.let {
                    val suffixLabel = UILabel()
                    suffixLabel.setText(it)
                    suffixLabel.sizeToFit()
                    suffixLabel.setFrame(CGRectMake(0.0, 0.0, suffixLabel.frame.useContents { size.width }, suffixLabel.frame.useContents { size.height }))

                    textField?.rightView = suffixLabel
                    textField?.rightViewMode = UITextFieldViewMode.UITextFieldViewModeAlways
                }

                NSNotificationCenter.defaultCenter.addObserverForName(
                    UITextFieldTextDidChangeNotification, `object` = textField, queue = NSOperationQueue.mainQueue()) { notification ->
                    textStates[key] = textField?.text.orEmpty()
                }
            }
        }

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
                onConfirm(textStates.mapValues { it.value.trim() })
            }
        ).apply {
            setEnabled(enabledConfirm)
        }
        confirmActionRef = confirmAction

        alertController.addAction(dismissAction)
        alertController.addAction(confirmAction)

        alertController.preferredAction = confirmAction
    }
}