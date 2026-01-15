package zone.ien.utils.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCSignatureOverride
import platform.CoreGraphics.CGFloat
import platform.CoreGraphics.CGRectInfinite
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSAttributedString
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.NSSelectorFromString
import platform.UIKit.NSDirectionalEdgeInsets
import platform.UIKit.NSDirectionalEdgeInsetsMake
import platform.UIKit.UIActionSheet
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertActionStyleDestructive
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleActionSheet
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIAxisVertical
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIBarButtonItemStyle
import platform.UIKit.UIBarButtonSystemItem
import platform.UIKit.UIButton
import platform.UIKit.UIButtonConfiguration
import platform.UIKit.UIButtonConfigurationSize
import platform.UIKit.UIButtonTypePlain
import platform.UIKit.UIButtonTypeSystem
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIImage
import platform.UIKit.UIImageRenderingMode
import platform.UIKit.UIImageSymbolConfiguration
import platform.UIKit.UIImageSymbolScaleLarge
import platform.UIKit.UIImageSymbolWeightBold
import platform.UIKit.UIKeyboardTypeDefault
import platform.UIKit.UIKeyboardTypeNumberPad
import platform.UIKit.UILayoutConstraintAxisVertical
import platform.UIKit.UINavigationBar
import platform.UIKit.UINavigationItem
import platform.UIKit.UIPickerView
import platform.UIKit.UIPickerViewDataSourceProtocol
import platform.UIKit.UIPickerViewDelegateProtocol
import platform.UIKit.UIScreen
import platform.UIKit.UIStackView
import platform.UIKit.UISwitch
import platform.UIKit.UITextFieldTextDidChangeNotification
import platform.UIKit.UIView
import platform.UIKit.interactionState
import platform.darwin.NSInteger
import platform.darwin.NSObject
import platform.posix.INFINITY
import zone.ien.utils.utils.Dlog

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

        val alert2 = UIAlertController.alertControllerWithTitle(
            title = null,
            message = null,
            preferredStyle = UIAlertControllerStyleActionSheet
        )

        val action = UIAlertAction.actionWithTitle(
            title = "action",
            style = UIAlertActionStyleDefault,
            handler = {
                alert2.dismissViewControllerAnimated(flag = true, completion = null)
                onCancel()
            }
        )

        val action2 = UIAlertAction.actionWithTitle(
            title ="action2",
            style = UIAlertActionStyleCancel,
            handler = {
                alert2.dismissViewControllerAnimated(flag = true, completion = null)
                onCancel()
            }
        )

        val action3 = UIAlertAction.actionWithTitle(
            title ="action3",
            style = UIAlertActionStyleDestructive,
            handler = {
                alert.dismissViewControllerAnimated(flag = true, completion = null)
                onCancel()
            }
        )

        alert.addTextFieldWithConfigurationHandler { textField ->
            textField?.placeholder = "placeholder"
            textField?.keyboardType = UIKeyboardTypeDefault
//            textField?.textColor = UIColor.blackColor
            textField?.interactionState

            NSNotificationCenter.defaultCenter.addObserverForName(UITextFieldTextDidChangeNotification, `object` = textField, queue = NSOperationQueue.mainQueue) { notification ->
                Dlog.d(TAG, "${textField?.text}")
            }

//            textField?.text = "hi"
            // textField.isSecureTextEntry = true  // 비밀번호용
        }

        alert.addTextFieldWithConfigurationHandler { textField ->
            textField?.placeholder = "password"
            textField?.keyboardType = UIKeyboardTypeNumberPad
            textField?.secureTextEntry = true
        }

        alert2.addAction(action)
        alert2.addAction(action2)
        alert.addAction(action3)
        viewController.presentViewController(alert2, animated = true, completion = null)

        alert2.view
    }
}



@OptIn(ExperimentalComposeUiApi::class, ExperimentalForeignApi::class)
@Composable
actual fun DeviceBox(modifier: Modifier) {
    val data = remember { mapOf(
        "key1" to "value1",
        "key2" to "value2",
        "key3" to "value3",
        "key4" to "value4",
        "key5" to "value5",
    ) }
    val delegate = remember { PickerDataSourceDelegate(items = data, onSelectionChange = { Dlog.d(TAG, "onSelectionChange: $it") }) }
    val buttonTarget = remember {
        object : NSObject() {
            @ObjCAction  // 핵심: 이 애노테이션으로 ObjC selector 생성
            fun backTapped() {
                Dlog.d(TAG, "backTapped")
            }
        }
    }
    Column(
        modifier = modifier
    ) {
        UIKitView(
            factory = {
                val containerView = UIView()

                val navBar = UINavigationBar()
//                navBar.setFrame(CGRectMake(0.0, 20.0, Double.POSITIVE_INFINITY, 64.0 + 20.0))
//                navBar.directionalLayoutMargins = NSDirectionalEdgeInsetsMake(12.0, 16.0, 12.0, 16.0)

//                navBar.prefersLargeTitles = true

                val navItem = UINavigationItem(title = "Title")
                navItem.leftBarButtonItem = UIBarButtonItem(
                    image = UIImage.systemImageNamed("chevron.left"),
                    style = UIBarButtonItemStyle.UIBarButtonItemStylePlain,
                    target = this,
                    action = null
                )

//                val buttonTarget = object : NSObject() {
//                    @ObjCAction  // 핵심: 이 애노테이션으로 ObjC selector 생성
//                    fun backTapped() {
//                        Dlog.d(TAG, "backTapped")
//                    }
//                }

                val selector = NSSelectorFromString("backTapped")

                navItem.rightBarButtonItems = listOf(
                    UIBarButtonItem(
                        image = UIImage.systemImageNamed("square.and.arrow.up"),
                        style = UIBarButtonItemStyle.UIBarButtonItemStyleDone,
                        target = buttonTarget,
                        action = selector
                    ),
                    UIBarButtonItem(
                        barButtonSystemItem = UIBarButtonSystemItem.UIBarButtonSystemItemCamera,
                        menu = null
                    ),
                    UIBarButtonItem().apply {
                        title = "hi"
                    }
//                    UIBarButtonItem()
                )

                navBar.items = listOf(navItem)
                navBar.setDirectionalLayoutMargins(NSDirectionalEdgeInsetsMake(64.0, 0.0, 0.0, 0.0))

                containerView.addSubview(navBar)
//                containerView.setFrame(navBar.frame)

                containerView
            },
            properties = UIKitInteropProperties(placedAsOverlay = true),
            modifier = Modifier
                .fillMaxWidth()
        )
//        UIKitView(
//            factory = {
//                val view = UIPickerView().apply {
////                val delegate = remember { PickerDataSourceDelegate(items = data, onSelectionChange = { Dlog.d(TAG, "onSelectionChange: $it") }) }
//                    this.dataSource = delegate
//                    this.delegate = delegate
//                }
//
//
//
////            val view = UIDatePicker().apply {
////                this.datePickerMode = UIDatePickerMode.UIDatePickerModeTime
////                this.preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
////            }
//
//                view
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
    }
}

/*
@OptIn(ExperimentalComposeUiApi::class, ExperimentalForeignApi::class)
@Composable
actual fun DeviceBox(modifier: Modifier) {
    val data = remember { mapOf(
        "key1" to "value1",
        "key2" to "value2",
        "key3" to "value3",
        "key4" to "value4",
        "key5" to "value5",
    ) }
    val delegate = remember { PickerDataSourceDelegate(items = data, onSelectionChange = { Dlog.d(TAG, "onSelectionChange: $it") }) }
    Column(
        modifier = modifier
    ) {
        UIKitView(
            factory = {
                val stackView = UIStackView()
                stackView.axis = UILayoutConstraintAxisVertical
                stackView.spacing = 20.0
                val imageConfig = UIImageSymbolConfiguration
                    .configurationWithPointSize(pointSize = 16.0, weight = UIImageSymbolWeightBold, scale = UIImageSymbolScaleLarge)
                val padding = 24.0

                listOf(
                    UIButtonConfiguration.glassButtonConfiguration(), UIButtonConfiguration.clearGlassButtonConfiguration(), UIButtonConfiguration.prominentGlassButtonConfiguration(), UIButtonConfiguration.prominentClearGlassButtonConfiguration()
                ).forEach { config ->
                    val button = UIButton.buttonWithConfiguration(config, null)
                    button.setImage(UIImage.systemImageNamed("square.and.pencil", imageConfig)?.imageWithRenderingMode(UIImageRenderingMode.UIImageRenderingModeAutomatic), forState = UIControlStateNormal)
                    button.configuration?.contentInsets = NSDirectionalEdgeInsetsMake(padding, padding, padding, padding)

                    stackView.addArrangedSubview(button)
                }
                val switch = UISwitch()
                stackView.addArrangedSubview(switch)
                val button = UIButton.buttonWithConfiguration(configuration = UIButtonConfiguration.prominentGlassButtonConfiguration().apply { this.buttonSize = UIButtonConfigurationSize.UIButtonConfigurationSizeSmall }, null)
                button.setTitle("Hello World!", UIControlStateNormal)
                button.sizeToFit()
                stackView.addArrangedSubview(button)

                stackView
            },
            properties = UIKitInteropProperties(placedAsOverlay = true),
            modifier = Modifier.fillMaxWidth()
        )
//        UIKitView(
//            factory = {
//                val view = UIPickerView().apply {
////                val delegate = remember { PickerDataSourceDelegate(items = data, onSelectionChange = { Dlog.d(TAG, "onSelectionChange: $it") }) }
//                    this.dataSource = delegate
//                    this.delegate = delegate
//                }
//
//
//
////            val view = UIDatePicker().apply {
////                this.datePickerMode = UIDatePickerMode.UIDatePickerModeTime
////                this.preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
////            }
//
//                view
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
    }
}

 */

class PickerDataSourceDelegate(
    private val items: Map<String, String>,
    private val onSelectionChange: (Int) -> Unit
) : NSObject(), UIPickerViewDataSourceProtocol, UIPickerViewDelegateProtocol {

    override fun numberOfComponentsInPickerView(pickerView: UIPickerView): Long = 1

    @ObjCSignatureOverride
    override fun pickerView(pickerView: UIPickerView, numberOfRowsInComponent: NSInteger): NSInteger = items.size.toLong()

    @ObjCSignatureOverride
    @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun pickerView(
        pickerView: UIPickerView,
        titleForRow: NSInteger,
        forComponent: NSInteger
    ): String? {
//        Dlog.d(TAG, "titleForRow: $titleForRow, ${items.entries}")
        return items.entries.toList()[titleForRow.toInt()].value + "!"
//        return super.pickerView(pickerView = pickerView, titleForRow = titleForRow, forComponent = forComponent)
    }

    @ObjCSignatureOverride
    @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun pickerView(
        pickerView: UIPickerView,
        didSelectRow: NSInteger,
        inComponent: NSInteger
    ) {
        onSelectionChange(didSelectRow.toInt())
//        super.pickerView(pickerView = pickerView, didSelectRow = didSelectRow, inComponent = inComponent)
    }

//    @ObjCSignatureOverride
//    @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
//    override fun pickerView(
//        pickerView: UIPickerView,
//        attributedTitleForRow: NSInteger,
//        forComponent: NSInteger
//    ): NSAttributedString? {
//        return null
////        return super.pickerView(pickerView = pickerView, attributedTitleForRow = attributedTitleForRow, forComponent = forComponent)
//    }

}