package zone.ien.utils.adaptive.dialog

import androidx.compose.material3.DisplayMode
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.foundation.IenSemanticTone
import kotlinx.cinterop.BetaInteropApi
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
import zone.ien.hig.toUIColor
import zone.ien.utils.ui.interactive.toneColor

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
private fun BaseDateTimePickerDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    title: String,
    createDatePicker: () -> UIDatePicker,
    onDismiss: () -> Unit,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    onConfirm: (NSDate) -> Unit
) {
    val presenter = LocalUIViewController.current
    val currentOnDismiss by rememberUpdatedState(onDismiss)
    val currentOnConfirm by rememberUpdatedState(onConfirm)
    val nativeTintColor = toneColor(tone).toUIColor()
    val nativeAppearance = remember(presenter) { DateTimePickerNativeAppearance() }
    if (!visible) return

    val datePickerRef = remember(presenter) { createDatePicker() }
    val delegate = remember(presenter) { object: NSObject(), UISheetPresentationControllerDelegateProtocol {
        override fun presentationControllerDidDismiss(presentationController: UIPresentationController) {
            currentOnDismiss()
        }
    } }
    val buttonTarget = remember(presenter) { object: NSObject() {
        @ObjCAction
        fun onBackClick() = currentOnDismiss()

        @ObjCAction
        fun onFinishClick() {
            currentOnConfirm(datePickerRef.date)
        }
    } }

    DisposableEffect(presenter) {
        val viewController = UIViewController().apply {
            view.tintColor = nativeTintColor
        }
        val navBar = UINavigationBar().apply {
            tintColor = nativeTintColor
        }
        val navItem = UINavigationItem(title = title)
        nativeAppearance.viewController = viewController
        nativeAppearance.navBar = navBar
        nativeAppearance.navItem = navItem

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

        viewController.view.addSubview(datePickerRef)
        datePickerRef.translatesAutoresizingMaskIntoConstraints = false

        NSLayoutConstraint.activateConstraints(listOf(
            datePickerRef.topAnchor.constraintEqualToAnchor(navBar.bottomAnchor),
            datePickerRef.leadingAnchor.constraintEqualToAnchor(viewController.view.leadingAnchor, constant = 16.0),
            datePickerRef.trailingAnchor.constraintEqualToAnchor(viewController.view.trailingAnchor, constant = -16.0),
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

        presenter.presentViewController(viewController, true, null)
        onDispose {
            nativeAppearance.viewController = null
            nativeAppearance.navBar = null
            nativeAppearance.navItem = null
            sheet?.delegate = null
            if (presenter.presentedViewController == viewController) {
                presenter.dismissViewControllerAnimated(true, null)
            }
        }
    }

    SideEffect {
        nativeAppearance.viewController?.view?.tintColor = nativeTintColor
        nativeAppearance.navBar?.tintColor = nativeTintColor
        nativeAppearance.navItem?.title = title
    }
}

private class DateTimePickerNativeAppearance {
    var viewController: UIViewController? = null
    var navBar: UINavigationBar? = null
    var navItem: UINavigationItem? = null
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
    tone: IenSemanticTone,
    onConfirm: (Long) -> Unit
) {
    BaseDateTimePickerDialog(
        tone = tone,
        modifier = modifier,
        visible = visible,
        title = title,
        createDatePicker = {
            UIDatePicker().apply {
                this.datePickerMode = UIDatePickerMode.UIDatePickerModeDate
                this.preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
                initialSelectedDateMillis?.let { this.setDate(NSDate.dateWithTimeIntervalSince1970(it / 1000.0)) }
                this.minimumDate = createNSDate(yearRange.first, 1, 1)
                this.maximumDate = createNSDate(yearRange.last, 12, 31)
            }
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
    tone: IenSemanticTone,
    onConfirm: (Int, Int) -> Unit
) {
    BaseDateTimePickerDialog(
        tone = tone,
        modifier = modifier,
        visible = visible,
        title = title,
        createDatePicker = {
            UIDatePicker().apply {
                this.datePickerMode = UIDatePickerMode.UIDatePickerModeTime
                this.preferredDatePickerStyle = UIDatePickerStyle.UIDatePickerStyleWheels
                this.setDate(createTimeNSDate(initialHour, initialMinute))
            }
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
