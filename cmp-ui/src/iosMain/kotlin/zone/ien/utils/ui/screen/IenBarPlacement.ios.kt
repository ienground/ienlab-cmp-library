package zone.ien.utils.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIView
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import zone.ien.utils.ui.uikit.IENStatusRegion
import zone.ien.utils.ui.uikit.IENCameraRegion
import zone.ien.utils.ui.uikit.IENObserveHinge
import zone.ien.utils.ui.uikit.IENRemoveHingeObserver

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun rememberIenBarPlacement(): State<IenBarPlacement?> {
    val controller = LocalUIViewController.current
    val placement = remember(controller) { mutableStateOf<IenBarPlacement?>(null) }
    DisposableEffect(controller) {
        val parent = controller.view
        var hasHinge = false
        val observer = object : UIView(parent.bounds) {
            override fun layoutSubviews() {
                super.layoutSubviews()
                updatePlacement()
            }

            override fun safeAreaInsetsDidChange() {
                super.safeAreaInsetsDidChange()
                updatePlacement()
            }

            override fun didMoveToWindow() {
                super.didMoveToWindow()
                updatePlacement()
            }

            private fun updatePlacement() {
                placement.value = IENStatusRegion(parent).useContents {
                    val isSide = (hasHinge && parent.safeAreaInsets.useContents { right > 0.0 }) ||
                        (size.width > 0.0 && size.height > size.width)
                    if (!isSide) return@useContents if (hasHinge) IenBarPlacement() else null
                    val viewWidth = parent.bounds.useContents { size.width }
                    val viewHeight = parent.bounds.useContents { size.height }
                    val statusTop = origin.y
                    val statusBottom = origin.y + size.height
                    val sideWidth = maxOf(size.width, parent.safeAreaInsets.useContents { right }, 72.0)
                    val camera = IENCameraRegion(parent)
                    IenBarPlacement(
                        sideWidth = sideWidth.dp,
                        controlsTop = (if (statusTop < viewHeight / 2) statusBottom + 8.0
                            else parent.safeAreaInsets.useContents { top } + 8.0).dp,
                        controlsBottom = (if (statusTop >= viewHeight / 2) viewHeight - statusTop + 8.0
                            else 24.0).dp,
                        centerFromRight = camera.useContents {
                            if (size.width > 0.0) (viewWidth - origin.x - size.width / 2).dp
                            else (sideWidth / 2).dp
                        },
                        titleTopInset = camera.useContents {
                            if (size.height > 0.0 && origin.y < viewHeight / 2)
                                (origin.y + size.height / 2 - 32.0).coerceAtLeast(0.0).dp
                            else 0.dp
                        },
                    )
                }
            }
        }.apply {
            userInteractionEnabled = false
            autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
        }
        parent.addSubview(observer)
        val hingeObserver = IENObserveHinge(parent) {
            hasHinge = it
            observer.setNeedsLayout()
        }
        onDispose {
            IENRemoveHingeObserver(parent, hingeObserver)
            observer.removeFromSuperview()
        }
    }
    return placement
}
