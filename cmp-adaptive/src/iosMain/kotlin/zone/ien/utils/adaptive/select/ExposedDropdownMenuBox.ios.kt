package zone.ien.utils.adaptive.select

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCSignatureOverride
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import platform.Foundation.NSSelectorFromString
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIApplication
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIBarButtonItemStyle
import platform.UIKit.UIImage
import platform.UIKit.UINavigationBar
import platform.UIKit.UINavigationItem
import platform.UIKit.UIPickerView
import platform.UIKit.UIPickerViewDataSourceProtocol
import platform.UIKit.UIPickerViewDelegateProtocol
import platform.UIKit.UIPresentationController
import platform.UIKit.UISheetPresentationControllerDelegateProtocol
import platform.UIKit.UISheetPresentationControllerDetent
import platform.UIKit.UIViewController
import platform.UIKit.sheetPresentationController
import platform.darwin.NSInteger
import platform.darwin.NSObject
import zone.ien.utils.cmp_adaptive.generated.resources.Res
import zone.ien.utils.cmp_adaptive.generated.resources.select_dialog_title
import kotlin.math.exp

/**
 * @param dropdownMenuItem is not used at iOS.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun <T> ExposedDropdownMenuBox(
    modifier: Modifier,
    itemsWithLabels: Map<T, String>,
    currentItem: T?,
    onItemSelected: (T) -> Unit,
    trailingIconButton: @Composable (onClick: () -> Unit, expanded: Boolean) -> Unit,
    dropdownMenuItem: @Composable (text: @Composable () -> Unit, onClick: () -> Unit) -> Unit,
    textField: @Composable (value: String, trailingIcon: @Composable () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var pickerRef by remember { mutableStateOf<UIPickerView?>(null) }
    var tempCurrentItem by remember { mutableStateOf(currentItem) }
    val buttonTarget = remember { object: NSObject() {
        @ObjCAction
        fun onBackClick() = run { expanded = false }

        @ObjCAction
        fun onFinishClick() {
            tempCurrentItem?.let(onItemSelected)
            expanded = false
        }
    } }
    val sheetDelegate = remember { object: NSObject(), UISheetPresentationControllerDelegateProtocol {
        override fun presentationControllerDidDismiss(presentationController: UIPresentationController) {
            expanded = false
        }
    } }
    val pickerDelegate = remember { PickerDataSourceDelegate(itemsWithLabels = itemsWithLabels, onSelectionChange = { tempCurrentItem = it }) }

    LaunchedEffect(expanded) {
        if (expanded) {
            val viewController = UIViewController()
            val navBar = UINavigationBar()
            val navItem = UINavigationItem(title = getString(Res.string.select_dialog_title))

            navItem.leftBarButtonItem = UIBarButtonItem(
                image = UIImage.systemImageNamed("xmark"),
                style = UIBarButtonItemStyle.UIBarButtonItemStylePlain,
                target = buttonTarget,
                action = NSSelectorFromString("onBackClick")
            )

            navItem.rightBarButtonItem = UIBarButtonItem(
                image = UIImage.systemImageNamed("checkmark"),
                style = UIBarButtonItemStyle.UIBarButtonItemStyleDone,
                target = buttonTarget,
                action = NSSelectorFromString("onFinishClick")
            )

            navBar.items = listOf(navItem)
            navBar.translatesAutoresizingMaskIntoConstraints = false

            viewController.view.addSubview(navBar)

            NSLayoutConstraint.activateConstraints(listOf(
                navBar.topAnchor.constraintEqualToAnchor(viewController.view.safeAreaLayoutGuide.topAnchor, 16.0),
                navBar.leadingAnchor.constraintEqualToAnchor(viewController.view.leadingAnchor),
                navBar.trailingAnchor.constraintEqualToAnchor(viewController.view.trailingAnchor),
            ))

            val pickerView = UIPickerView().apply {
                this.dataSource = pickerDelegate
                this.delegate = pickerDelegate
                currentItem?.let { this.selectRow(itemsWithLabels.keys.indexOf(it).toLong(), animated = false, inComponent = 0) }
            }
            pickerRef = pickerView

            viewController.view.addSubview(pickerView)
            pickerView.translatesAutoresizingMaskIntoConstraints = false

            NSLayoutConstraint.activateConstraints(listOf(
                pickerView.topAnchor.constraintEqualToAnchor(navBar.bottomAnchor),
                pickerView.leadingAnchor.constraintEqualToAnchor(viewController.view.leadingAnchor, constant = 16.0),
                pickerView.trailingAnchor.constraintEqualToAnchor(viewController.view.trailingAnchor, constant = -16.0),
            ))

            val sheet = viewController.sheetPresentationController
            sheet?.detents = listOf(
                UISheetPresentationControllerDetent.customDetentWithIdentifier(
                    identifier = "contentSize",
                    resolver = { _ -> 260.0 }
                )
            )

            sheet?.prefersGrabberVisible = true
            sheet?.prefersScrollingExpandsWhenScrolledToEdge = true
            sheet?.delegate = sheetDelegate

            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(viewController, true, null)
        } else {
            UIApplication.sharedApplication.keyWindow?.rootViewController?.dismissViewControllerAnimated(true, null)
            pickerRef = null
        }
    }

    textField(itemsWithLabels[currentItem].orEmpty()) {
        trailingIconButton({ expanded = !expanded }, expanded)
    }
}

class PickerDataSourceDelegate<T>(
    private val itemsWithLabels: Map<T, String>,
    private val onSelectionChange: (T) -> Unit
): NSObject(), UIPickerViewDataSourceProtocol, UIPickerViewDelegateProtocol {
    override fun numberOfComponentsInPickerView(pickerView: UIPickerView): NSInteger = 1

    @ObjCSignatureOverride
    override fun pickerView(pickerView: UIPickerView, numberOfRowsInComponent: NSInteger): NSInteger = itemsWithLabels.size.toLong()

    @ObjCSignatureOverride
    @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun pickerView(
        pickerView: UIPickerView,
        titleForRow: NSInteger,
        forComponent: NSInteger
    ): String? {
        return itemsWithLabels.entries.toList()[titleForRow.toInt()].value
    }

    @ObjCSignatureOverride
    @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun pickerView(
        pickerView: UIPickerView,
        didSelectRow: NSInteger,
        inComponent: NSInteger
    ) {
        val key = itemsWithLabels.entries.toList()[didSelectRow.toInt()].key
        onSelectionChange(key)
    }
}