package zone.ien.utils.adaptive.select

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSIndexPath
import platform.Foundation.NSSelectorFromString
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIApplication
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIBarButtonItemStyle
import platform.UIKit.UIBarMetrics
import platform.UIKit.UIBarMetricsDefault
import platform.UIKit.UIBarStyleBlack
import platform.UIKit.UIBarStyleBlackTranslucent
import platform.UIKit.UIColor
import platform.UIKit.UIEdgeInsetsMake
import platform.UIKit.UIImage
import platform.UIKit.UIModalPresentationPageSheet
import platform.UIKit.UIModalPresentationStyle
import platform.UIKit.UINavigationBar
import platform.UIKit.UINavigationBarAppearance
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationItem
import platform.UIKit.UIPickerView
import platform.UIKit.UIPickerViewDataSourceProtocol
import platform.UIKit.UIPickerViewDelegateProtocol
import platform.UIKit.UIPresentationController
import platform.UIKit.UIScreen
import platform.UIKit.UIScrollViewContentInsetAdjustmentBehavior
import platform.UIKit.UISheetPresentationControllerDelegateProtocol
import platform.UIKit.UISheetPresentationControllerDetent
import platform.UIKit.UITableView
import platform.UIKit.UITableViewAutomaticDimension
import platform.UIKit.UITableViewCell
import platform.UIKit.UITableViewCellAccessoryType
import platform.UIKit.UITableViewCellSelectionStyle
import platform.UIKit.UITableViewCellSeparatorStyle
import platform.UIKit.UITableViewDataSourceProtocol
import platform.UIKit.UITableViewDelegateProtocol
import platform.UIKit.UITableViewRowAnimationAutomatic
import platform.UIKit.UITableViewRowAnimationFade
import platform.UIKit.UITableViewScrollPosition
import platform.UIKit.UITableViewStyle
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.indexPathForRow
import platform.UIKit.labelColor
import platform.UIKit.navigationController
import platform.UIKit.navigationItem
import platform.UIKit.row
import platform.UIKit.secondaryLabelColor
import platform.UIKit.sheetPresentationController
import platform.UIKit.systemBackgroundColor
import platform.UIKit.systemBlueColor
import platform.UIKit.systemGroupedBackgroundColor
import platform.darwin.NSInteger
import platform.darwin.NSObject

/**
 * @param dropdownMenuItem is not used at iOS.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun <T> ExposedDropdownMenuBox(
    modifier: Modifier,
    title: String,
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
    val buttonTarget = remember(expanded) { object: NSObject() {
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
            val navItem = UINavigationItem(title = title)

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

/**
 * @param dropdownMenuItem is not used at iOS.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun <T> ExposedDropdownMenuBox(
    modifier: Modifier,
    title: String,
    itemsWithLabels: Map<T, String>,
    currentItems: List<T>,
    onItemsSelected: (List<T>) -> Unit,
    trailingIconButton: @Composable (onClick: () -> Unit, expanded: Boolean) -> Unit,
    dropdownMenuItem: @Composable (text: @Composable () -> Unit, onClick: () -> Unit, checked: Boolean) -> Unit,
    textField: @Composable (value: String, trailingIcon: @Composable () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var pickerRef by remember { mutableStateOf<UIPickerView?>(null) }
    var tempCurrentItems by remember(expanded) { mutableStateOf(currentItems) }
    val buttonTarget = remember(expanded, tempCurrentItems) { object: NSObject() {
        @ObjCAction
        fun onBackClick() = run { expanded = false }

        @ObjCAction
        fun onFinishClick() {
            onItemsSelected(tempCurrentItems)
            expanded = false
        }
    } }
    val sheetDelegate = remember { object: NSObject(), UISheetPresentationControllerDelegateProtocol {
        override fun presentationControllerDidDismiss(presentationController: UIPresentationController) {
            expanded = false
        }
    } }

    LaunchedEffect(expanded) {
        if (expanded) {
            val pickerViewController = ListPickerViewController(itemsWithLabels, tempCurrentItems) {
                tempCurrentItems = it
            }

            pickerViewController.navigationItem.title = title
            pickerViewController.navigationItem.leftBarButtonItem = UIBarButtonItem(
                image = UIImage.systemImageNamed("xmark"),
                style = UIBarButtonItemStyle.UIBarButtonItemStylePlain,
                target = buttonTarget,
                action = NSSelectorFromString("onBackClick")
            )
            pickerViewController.navigationItem.rightBarButtonItem = UIBarButtonItem(
                image = UIImage.systemImageNamed("checkmark"),
                style = UIBarButtonItemStyle.UIBarButtonItemStyleDone,
                target = buttonTarget,
                action = NSSelectorFromString("onFinishClick")
            )

            // 🔥 navController 생성 (container 역할만)
            val navController = UINavigationController(rootViewController = pickerViewController)
            val sheet = navController.sheetPresentationController
            sheet?.detents = listOf(UISheetPresentationControllerDetent.largeDetent())
            sheet?.delegate = sheetDelegate

            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(navController, true, null)

        } else {
            // dismiss 로직
            UIApplication.sharedApplication.keyWindow?.rootViewController?.dismissViewControllerAnimated(true, null)
            pickerRef = null
        }
    }

    textField(currentItems.map { itemsWithLabels[it] }.joinToString(", ")) {
        trailingIconButton({ expanded = !expanded }, expanded)
    }
}

class ListPickerViewController<T>(
    private val itemsWithLabels: Map<T, String>,
    private val currentItems: List<T>,
    private val onSelectionChange: (List<T>) -> Unit
): UIViewController(null, bundle = null), UITableViewDelegateProtocol, UITableViewDataSourceProtocol {
    @OptIn(ExperimentalForeignApi::class)
    private val tableView = UITableView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0), style = UITableViewStyle.UITableViewStyleInsetGrouped)
    private val tempCurrentItems = currentItems.toMutableList()

    override fun debugDescription(): String {
        return "ListPickerViewController(items=${itemsWithLabels.size}, selected=${currentItems.size})"
    }


    @OptIn(BetaInteropApi::class)
    override fun loadView() {
        super.loadView()
        tableView.backgroundView = null

        tableView.delegate = this
        tableView.dataSource = this
        tableView.registerClass(cellClass = UITableViewCell.`class`(), forCellReuseIdentifier = "cell")

        // 셀 배경도 투명
        tableView.allowsMultipleSelection = true
        tableView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(tableView)

        NSLayoutConstraint.activateConstraints(listOf(
            tableView.topAnchor.constraintEqualToAnchor(view.topAnchor),
            tableView.leadingAnchor.constraintEqualToAnchor(view.leadingAnchor),
            tableView.trailingAnchor.constraintEqualToAnchor(view.trailingAnchor),
            tableView.bottomAnchor.constraintEqualToAnchor(view.bottomAnchor)
        ))

    }

    @ObjCSignatureOverride
    @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun tableView(tableView: UITableView, numberOfRowsInSection: NSInteger): NSInteger {
        return itemsWithLabels.size.toLong()
    }

    @ObjCSignatureOverride
    @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun tableView(
        tableView: UITableView,
        cellForRowAtIndexPath: NSIndexPath
    ): UITableViewCell {
        val cell = tableView.dequeueReusableCellWithIdentifier("cell")!!
        val entry = itemsWithLabels.entries.toList()[cellForRowAtIndexPath.row.toInt()]
        cell.textLabel?.text = entry.value

        cell.selectionStyle = UITableViewCellSelectionStyle.UITableViewCellSelectionStyleGray
        cell.accessoryType = if (entry.key in tempCurrentItems) {
            UITableViewCellAccessoryType.UITableViewCellAccessoryCheckmark
        } else {
            UITableViewCellAccessoryType.UITableViewCellAccessoryNone
        }

        return cell
    }

    @ObjCSignatureOverride
    @Suppress("CONFLICTING_OVERLOADS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun tableView(
        tableView: UITableView,
        didSelectRowAtIndexPath: NSIndexPath
    ) {
        val row = didSelectRowAtIndexPath.row
        val currentItem = itemsWithLabels.keys.toList()[row.toInt()]
        updateSelection(currentItem)

        tableView.reloadRowsAtIndexPaths(listOf(didSelectRowAtIndexPath), withRowAnimation = UITableViewRowAnimationFade)
    }

    private fun updateSelection(key: T) {
        if (key !in tempCurrentItems) {
            tempCurrentItems.add(key)
            onSelectionChange(tempCurrentItems)
        } else {
            tempCurrentItems.remove(key)
            onSelectionChange(tempCurrentItems)
        }
    }
}