package zone.ien.utils.adaptive.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIColor
import platform.UIKit.UITabBar
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun IenNativeNavigationBarColors(selectedColor: Color) {
    val viewController = LocalUIViewController.current

    LaunchedEffect(viewController, selectedColor) {
        repeat(15) {
            viewController.view.findTabBar()?.let { tabBar ->
                tabBar.tintColor = selectedColor.toUIColor()
                return@LaunchedEffect
            }
            withFrameMillis { }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIView.findTabBar(): UITabBar? {
    if (this is UITabBar) return this

    for (subview in subviews) {
        (subview as? UIView)?.findTabBar()?.let { return it }
    }

    return null
}

private fun Color.toUIColor(): UIColor {
    return UIColor(
        alpha = alpha.toDouble(),
        red = red.toDouble(),
        green = green.toDouble(),
        blue = blue.toDouble(),
    )
}
