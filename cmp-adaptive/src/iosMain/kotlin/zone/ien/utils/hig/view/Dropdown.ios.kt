package zone.ien.utils.hig.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupProperties
import com.kyant.backdrop.Backdrop
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Data
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import zone.ien.utils.adaptive.view.DropdownMenuSection
import zone.ien.utils.icon.IconData

@Composable
fun ImageVector.toImageBitmap(): ImageBitmap {
    val painter = rememberVectorPainter(this)
    val density = LocalDensity.current
    val width = with(density) { defaultWidth.roundToPx() }
    val height = with(density) { defaultHeight.roundToPx() }

    return remember(this) {
        val bitmap = ImageBitmap(width, height)
        val canvas = Canvas(bitmap)
        CanvasDrawScope().draw(density, LayoutDirection.Ltr, canvas,
            Size(width.toFloat(), height.toFloat())) {
            with(painter) { draw(Size(width.toFloat(), height.toFloat())) }
        }
        bitmap
    }
}

fun Painter.toImageBitmap(
    size: Size = intrinsicSize,
    density: Density = Density(1f),
    layoutDirection: LayoutDirection = LayoutDirection.Ltr
): ImageBitmap {
    val width = size.width.toInt().takeIf { it > 0 } ?: 64
    val height = size.height.toInt().takeIf { it > 0 } ?: 64
    val bitmap = ImageBitmap(width, height)
    val canvas = Canvas(bitmap)
    CanvasDrawScope().draw(
        density = density,
        layoutDirection = layoutDirection,
        canvas = canvas,
        size = Size(width.toFloat(), height.toFloat())
    ) {
        draw(Size(width.toFloat(), height.toFloat()))
    }
    return bitmap
}

@OptIn(ExperimentalForeignApi::class)
@Composable
fun IconData.toUIImage(): UIImage {
    val bitmap = when (this) {
        is IconData.Vector -> {
            this.imageVector.toImageBitmap()
        }
        is IconData.Paint -> {
            this.painter.toImageBitmap()
        }
    }
    val skiaImage = Image.makeFromBitmap(bitmap.asSkiaBitmap())
    val pngData: Data = skiaImage.encodeToData(EncodedImageFormat.PNG) ?: throw Exception("Failed to encode PNG")
    val pngBytes: ByteArray = pngData.bytes
    val nsData = pngBytes.usePinned { NSData.dataWithBytes(it.addressOf(0), pngBytes.size.toULong()) }

    return UIImage(data = nsData)
}

@Composable
actual fun HigDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier,
    offset: DpOffset,
    paddingValues: PaddingValues,
    containerColor: Color,
    width: Dp,
    scrollState: ScrollState,
    properties: PopupProperties,
    backdrop: Backdrop,
    items: List<DropdownMenuSection>
) {
    val density = LocalDensity.current
    val viewController = LocalUIViewController.current

    UIImage.image
}