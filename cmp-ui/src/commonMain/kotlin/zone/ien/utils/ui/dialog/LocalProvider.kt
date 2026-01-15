package zone.ien.utils.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

internal object LocalDialogProviderDefault {
    val Shape @Composable get() = MaterialTheme.shapes.extraLarge
    val BackgroundColor @Composable get() = MaterialTheme.colorScheme.surfaceContainerHigh
    val ContentColor @Composable get() = MaterialTheme.colorScheme.onSurface
}

val LocalDialogShape: ProvidableCompositionLocal<Shape?> = staticCompositionLocalOf { null }
val LocalDialogBorder: ProvidableCompositionLocal<BorderStroke?> = staticCompositionLocalOf { null }
val LocalDialogBackgroundColor: ProvidableCompositionLocal<Color?> = staticCompositionLocalOf { null }
val LocalDialogContentColor: ProvidableCompositionLocal<Color?> = staticCompositionLocalOf { null }