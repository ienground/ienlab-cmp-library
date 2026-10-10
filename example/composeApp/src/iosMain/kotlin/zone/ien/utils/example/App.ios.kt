package zone.ien.utils.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
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
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UIKeyboardTypeDefault
import platform.UIKit.UIKeyboardTypeNumberPad
import platform.UIKit.UILayoutConstraintAxisVertical
import platform.UIKit.UINavigationBar
import platform.UIKit.UINavigationItem
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType
import platform.UIKit.UIPickerView
import platform.UIKit.UIPickerViewDataSourceProtocol
import platform.UIKit.UIPickerViewDelegateProtocol
import platform.UIKit.UIScreen
import platform.UIKit.UISelectionFeedbackGenerator
import platform.UIKit.UIStackView
import platform.UIKit.UISwitch
import platform.UIKit.UITextFieldTextDidChangeNotification
import platform.UIKit.UIView
import platform.UIKit.interactionState
import platform.darwin.NSInteger
import platform.darwin.NSObject
import platform.posix.INFINITY
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.utils.Dlog

actual val currentTheme: Theme = Theme.Cupertino
actual val isIos: Boolean = true

@Composable
actual fun HapticTestScreen() {
    val types = listOf(
        HapticFeedbackType.LongPress,
        HapticFeedbackType.TextHandleMove,
        HapticFeedbackType.SegmentTick,
        HapticFeedbackType.SegmentFrequentTick,
        HapticFeedbackType.Confirm,
        HapticFeedbackType.Reject,
        HapticFeedbackType.GestureThresholdActivate,
        HapticFeedbackType.GestureEnd,
        HapticFeedbackType.ToggleOn,
        HapticFeedbackType.ToggleOff,
        HapticFeedbackType.VirtualKey,
    )

    Column {
        types.forEach { type ->
            IenButton(
                onClick = {
                    performNativeHapticFeedback(type)
                }
            ) {
                Text(type.toString())
            }
        }
    }
}


fun performNativeHapticFeedback(type: HapticFeedbackType) {
    when (type) {
        HapticFeedbackType.LongPress -> {
            UIImpactFeedbackGenerator(
                style = UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy
            ).impactOccurred()
        }

        HapticFeedbackType.TextHandleMove -> {
            UISelectionFeedbackGenerator()
                .selectionChanged()
        }

        HapticFeedbackType.SegmentTick -> {
            UIImpactFeedbackGenerator(
                style = UIImpactFeedbackStyle.UIImpactFeedbackStyleLight
            ).impactOccurred()
        }

        HapticFeedbackType.SegmentFrequentTick -> {
            UIImpactFeedbackGenerator(
                style = UIImpactFeedbackStyle.UIImpactFeedbackStyleSoft
            ).impactOccurred()
        }

        HapticFeedbackType.Confirm -> {
            UINotificationFeedbackGenerator()
                .notificationOccurred(
                    UINotificationFeedbackType.UINotificationFeedbackTypeSuccess
                )
        }

        HapticFeedbackType.Reject -> {
            UINotificationFeedbackGenerator()
                .notificationOccurred(
                    UINotificationFeedbackType.UINotificationFeedbackTypeError
                )
        }

        HapticFeedbackType.GestureThresholdActivate -> {
            UIImpactFeedbackGenerator(
                style = UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium
            ).impactOccurred()
        }

        HapticFeedbackType.GestureEnd -> {
            UIImpactFeedbackGenerator(
                style = UIImpactFeedbackStyle.UIImpactFeedbackStyleLight
            ).impactOccurred()
        }

        HapticFeedbackType.ToggleOn -> {
            UIImpactFeedbackGenerator(
                style = UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium
            ).impactOccurred()
        }

        HapticFeedbackType.ToggleOff -> {
            UIImpactFeedbackGenerator(
                style = UIImpactFeedbackStyle.UIImpactFeedbackStyleSoft
            ).impactOccurred()
        }

        HapticFeedbackType.VirtualKey -> {
            UIImpactFeedbackGenerator(
                style = UIImpactFeedbackStyle.UIImpactFeedbackStyleLight
            ).impactOccurred()
        }

        else -> Unit
    }
}