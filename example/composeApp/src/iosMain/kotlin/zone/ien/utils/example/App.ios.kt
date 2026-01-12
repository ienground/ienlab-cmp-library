package zone.ien.utils.example

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.viewinterop.UIKitView
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIKeyboardTypeDefault
import platform.UIKit.UIViewController
import platform.UIKit.UIWindowScene

@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onCancel: () -> Unit
) {
    val viewController = LocalUIViewController.current

    if (visible) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = "Hello World",
            message = "Hello World",
            preferredStyle = UIAlertControllerStyleAlert
        )

        val action = UIAlertAction.actionWithTitle(
            title = "action",
            style = UIAlertControllerStyleAlert,
            handler = {
                alert.dismissViewControllerAnimated(flag = true, completion = null)
                onCancel()
            }
        )

//        alert.addTextFieldWithConfigurationHandler { textField ->
//            textField?.placeholder = "placeholder"
//            textField?.keyboardType = UIKeyboardTypeDefault
//            textField?.textColor = UIColor.blackColor
//            // textField.isSecureTextEntry = true  // 비밀번호용
//        }

        alert.addAction(action)
        viewController.presentViewController(alert, animated = true, completion = null)

        alert.view
    }
}