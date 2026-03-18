package zone.ien.utils.adaptive.shimmer

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kyant.shapes.RoundedRectangle

val LocalHigShimmerShape: ProvidableCompositionLocal<Shape> = staticCompositionLocalOf { RoundedRectangle(4.dp) }