package zone.ien.utils.ui.icon

import androidx.compose.runtime.Composable
import zone.ien.utils.icon.IconStyle
import zone.ien.utils.icon.LocalIconStyle
import zone.ien.utils.ui.icon.filled.Delete
import zone.ien.utils.ui.icon.filled.Save
import zone.ien.utils.ui.icon.rounded.Delete
import zone.ien.utils.ui.icon.rounded.Save
import zone.ien.utils.ui.icon.sharp.Delete
import zone.ien.utils.ui.icon.sharp.Save

object MaterialIcons {
    object Filled

    object Rounded

    object Sharp

    val Delete @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.Delete
        IconStyle.Rounded -> Rounded.Delete
        IconStyle.Sharp -> Sharp.Delete
    }

    val Save @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.Save
        IconStyle.Rounded -> Rounded.Save
        IconStyle.Sharp -> Sharp.Save
    }
}