package zone.ien.utils.ui.shimmer

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

val LocalM3ShimmerShape: ProvidableCompositionLocal<Shape> = staticCompositionLocalOf { RoundedCornerShape(4.dp) }