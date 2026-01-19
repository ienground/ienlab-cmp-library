package zone.ien.utils.adaptive.dialog

import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSSelectorFromString
import platform.Foundation.NSTimeInterval
import platform.Foundation.NSTimeIntervalVar
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.NSDirectionalEdgeInsetsMake
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIApplication
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIBarButtonItemStyle
import platform.UIKit.UIColor
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle
import platform.UIKit.UIImage
import platform.UIKit.UILabel
import platform.UIKit.UINavigationBar
import platform.UIKit.UINavigationItem
import platform.UIKit.UIPresentationController
import platform.UIKit.UISheetPresentationController
import platform.UIKit.UISheetPresentationControllerDelegateProtocol
import platform.UIKit.UISheetPresentationControllerDetent
import platform.UIKit.UISheetPresentationControllerDetentIdentifier
import platform.UIKit.UIViewController
import platform.UIKit.sheetPresentationController
import platform.darwin.NSObject

/*
@Composable
actual fun DatePickerDialog(
    modifier: Modifier,
    visible: Boolean,
    initialSelectedDateMillis: Long?,
    initialDisplayedMonthMillis: Long?,
    yearRange: IntRange,
    initialDisplayMode: DisplayMode,
    selectableDates: SelectableDates,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val viewController = LocalUIViewController.current

    var alertRef by remember { mutableStateOf<UIAlertController?>(null) }

    LaunchedEffect(visible) {
        if (visible) {
            val alert = UIAlertController.alertControllerWithTitle(
                title = title,
                message = "\n\n\n\n\n\n\n\n\n\n",
                preferredStyle = UIAlertControllerStyleActionSheet
            )

            val datePicker = UIDatePicker().apply {
                this.datePickerMode = UIDatePickerMode.UIDatePickerModeDate
                this.preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleCompact
            }

            alert.view.addSubview(datePicker)
//            NSLayoutConstraint.activateConstraints(listOf(
//                datePicker.centerXAnchor().constraintEqualToAnchor(alert.view.centerXAnchor),
//                datePicker.centerYAnchor().constraintEqualToAnchor(alert.view.centerYAnchor)
//            ))

            val dismissAction = UIAlertAction.actionWithTitle(
                title = "dismiss",
                style = UIAlertActionStyleCancel,
//                style = styleDismiss.toStyle(),
                handler = {
                    onDismiss()
                }
            )
            val confirmAction = UIAlertAction.actionWithTitle(
                title = "confirm",
                style = UIAlertActionStyleDefault,
//                title = textConfirm,
//                style = styleConfirm.toStyle(),
                handler = {
                    onConfirm(0L)
                }
            ).apply {
//                setEnabled(enabledConfirm)
            }

            alert.addAction(dismissAction)
            alert.addAction(confirmAction)

            alertRef = alert
            viewController.presentViewController(alert, animated = true, completion = null)
        } else {
            alertRef?.dismissViewControllerAnimated(true, null)
            alertRef = null
        }
    }
}

 */

@OptIn(ExperimentalForeignApi::class)
@Composable
private fun BaseDateTimePickerDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    title: String,
    datePicker: UIDatePicker,
    onDismiss: () -> Unit,
    onConfirm: (NSDate) -> Unit
) {
    val delegate = remember { object: NSObject(), UISheetPresentationControllerDelegateProtocol {
        override fun presentationControllerDidDismiss(presentationController: UIPresentationController) {
            onDismiss()
        }
    } }
    var datePickerRef by remember { mutableStateOf<UIDatePicker?>(null) }
    val buttonTarget = remember { object: NSObject() {
        @ObjCAction
        fun onBackClick() = onDismiss()

        @ObjCAction
        fun onFinishClick() {
            datePickerRef?.date?.let { onConfirm(it) }
        }
    } }

    LaunchedEffect(visible) {
        if (visible) {
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

            datePickerRef = datePicker

            viewController.view.addSubview(datePicker)
            datePicker.translatesAutoresizingMaskIntoConstraints = false

            NSLayoutConstraint.activateConstraints(listOf(
                datePicker.topAnchor.constraintEqualToAnchor(navBar.bottomAnchor),
                datePicker.leadingAnchor.constraintEqualToAnchor(viewController.view.leadingAnchor, constant = 16.0),
                datePicker.trailingAnchor.constraintEqualToAnchor(viewController.view.trailingAnchor, constant = -16.0),
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
            sheet?.delegate = delegate

            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(viewController, true, null)
        } else {
            UIApplication.sharedApplication.keyWindow?.rootViewController?.dismissViewControllerAnimated(true, null)
            datePickerRef = null
        }
    }
}

@Composable
actual fun DatePickerDialog(
    modifier: Modifier,
    visible: Boolean,
    initialSelectedDateMillis: Long?,
    initialDisplayedMonthMillis: Long?,
    yearRange: IntRange,
    initialDisplayMode: DisplayMode,
    selectableDates: SelectableDates,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    BaseDateTimePickerDialog(
        modifier = modifier,
        visible = visible,
        title = title,
        datePicker = UIDatePicker().apply {
            this.datePickerMode = UIDatePickerMode.UIDatePickerModeDate
            this.preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
            initialSelectedDateMillis?.let { this.setDate(NSDate.dateWithTimeIntervalSince1970(it / 1000.0)) }
            this.minimumDate = createNSDate(yearRange.first, 1, 1)
            this.maximumDate = createNSDate(yearRange.last, 12, 31)
        },
        onDismiss = onDismiss,
        onConfirm = { onConfirm(it.timeIntervalSince1970.toLong() * 1000) }
    )
}

@Composable
actual fun TimePickerDialog(
    modifier: Modifier,
    visible: Boolean,
    initialHour: Int,
    initialMinute: Int,
    is24Hour: Boolean,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    BaseDateTimePickerDialog(
        modifier = modifier,
        visible = visible,
        title = title,
        datePicker = UIDatePicker().apply {
            this.datePickerMode = UIDatePickerMode.UIDatePickerModeTime
            this.preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
            this.setDate(createTimeNSDate(initialHour, initialMinute))
        },
        onDismiss = onDismiss,
        onConfirm = { it.getHourMinute().let { onConfirm(it.first, it.second) } }
    )
}

private fun createNSDate(year: Int, month: Int, day: Int): NSDate? {
    val components = NSDateComponents().apply {
        this.year = year.toLong()
        this.month = month.toLong()
        this.day = day.toLong()
    }

    return NSCalendar.currentCalendar.dateFromComponents(components)
}

private fun createTimeNSDate(hour: Int, minute: Int): NSDate {
    val components = NSDateComponents().apply {
        this.hour = hour.toLong()
        this.minute = minute.toLong()
        this.second = 0L
    }
    return NSCalendar.currentCalendar.dateFromComponents(components)!!
}

private fun NSDate.getHourMinute(): Pair<Int, Int> {
    val calendar = NSCalendar.currentCalendar
    val components = calendar.components(NSCalendarUnitHour or NSCalendarUnitMinute, this)
    return Pair(components.hour.toInt(), components.minute.toInt())
}