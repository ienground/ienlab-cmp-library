package zone.ien.utils.ui.window

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntSize

internal fun shouldUseIenOverlayWindowBounds(containerSize: IntSize): Boolean =
    containerSize.width > 0 && containerSize.height > 0

@Composable
internal fun Modifier.ienOverlayWindowSize(): Modifier {
    val containerSize = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current

    return if (shouldUseIenOverlayWindowBounds(containerSize)) {
        requiredSize(
            width = with(density) { containerSize.width.toDp() },
            height = with(density) { containerSize.height.toDp() },
        )
    } else {
        fillMaxSize()
    }
}
