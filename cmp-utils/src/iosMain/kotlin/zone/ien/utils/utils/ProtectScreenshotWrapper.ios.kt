package zone.ien.utils.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.QuartzCore.CALayer
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIImageView
import platform.UIKit.UIScreen
import platform.UIKit.UITextField
import platform.UIKit.UITextFieldViewMode
import platform.UIKit.UIView
import platform.UIKit.UIWindow

@Composable
actual fun ProtectScreenshotWrapper(
    content: @Composable () -> Unit,
) {
    val state = remember { mutableStateOf<ScreenshotProtectionState?>(null) }

    DisposableEffect(Unit) {
        disableScreenshot(state)

        onDispose {
            enableScreenshot(state)
        }
    }

    content()
}

data class ScreenshotProtectionState(
    val textField: UITextField,
    val windowSuperlayer: CALayer  // disableScreenshot 시점의 superlayer 저장
)

@OptIn(ExperimentalForeignApi::class)
private fun disableScreenshot(state: MutableState<ScreenshotProtectionState?>) {
    try {
        if (state.value != null) return
        val window: UIWindow = UIApplication.sharedApplication.keyWindow ?: return

        // superlayer를 이동 전에 저장
        val superlayer = window.layer.superlayer ?: return

        val textField = UITextField()
        textField.setSecureTextEntry(true)
        textField.setUserInteractionEnabled(false)

        val placeholderView = UIView(frame = textField.frame)
        val imageView = UIImageView()
        imageView.setFrame(UIScreen.mainScreen.bounds)
        imageView.setBackgroundColor(UIColor.blackColor)
        placeholderView.addSubview(imageView)

        window.addSubview(textField)
        textField.setLeftView(placeholderView)
        textField.setLeftViewMode(UITextFieldViewMode.UITextFieldViewModeAlways)

        superlayer.addSublayer(textField.layer)

        val sublayers = textField.layer.sublayers
        if (sublayers != null && sublayers.count() > 0) {
            val lastLayer = sublayers[sublayers.count() - 1] as? CALayer
            lastLayer?.addSublayer(window.layer)
        }

        state.value = ScreenshotProtectionState(
            textField = textField,
            windowSuperlayer = superlayer
        )

    } catch (e: Exception) {
        println("disableScreenshot error: ${e.message}")
    }
}

private fun enableScreenshot(state: MutableState<ScreenshotProtectionState?>) {
    try {
        val (textField, windowSuperlayer) = state.value ?: return

        // window.layer를 저장해둔 원래 superlayer로 복구
        // addSublayer는 자동으로 기존 superlayer에서 제거 후 이동시켜줌
        windowSuperlayer.addSublayer(
            UIApplication.sharedApplication.keyWindow?.layer ?: return
        )

        textField.layer.removeFromSuperlayer()
        textField.removeFromSuperview()
        state.value = null

    } catch (e: Exception) {
        println("enableScreenshot error: ${e.message}")
    }
}