package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import zone.ien.hig.adaptive.icons.AdaptiveIcons

@Composable
fun adaptiveIconData(
    material: @Composable () -> ImageVector,
    cupertino: @Composable () -> String
): IconData.Paint {
    return IconData.Paint(
        painter = AdaptiveIcons.painter(
            material = material,
            cupertino = cupertino
        )
    )
}