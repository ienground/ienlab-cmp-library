package zone.ien.utils.ui.icon

import androidx.compose.runtime.Composable
import zone.ien.utils.icon.IconStyle
import zone.ien.utils.icon.LocalIconStyle
import zone.ien.utils.ui.icon.filled.ArrowDropDown
import zone.ien.utils.ui.icon.filled.ArrowDropUp
import zone.ien.utils.ui.icon.filled.CloudOff
import zone.ien.utils.ui.icon.filled.Delete
import zone.ien.utils.ui.icon.filled.Keyboard
import zone.ien.utils.ui.icon.filled.Save
import zone.ien.utils.ui.icon.filled.Schedule
import zone.ien.utils.ui.icon.rounded.CloudOff
import zone.ien.utils.ui.icon.rounded.Delete
import zone.ien.utils.ui.icon.rounded.Save
import zone.ien.utils.ui.icon.sharp.CloudOff
import zone.ien.utils.ui.icon.filled.Update
import zone.ien.utils.ui.icon.rounded.ArrowDropDown
import zone.ien.utils.ui.icon.rounded.ArrowDropUp
import zone.ien.utils.ui.icon.rounded.Delete
import zone.ien.utils.ui.icon.rounded.Keyboard
import zone.ien.utils.ui.icon.rounded.Save
import zone.ien.utils.ui.icon.rounded.Schedule
import zone.ien.utils.ui.icon.rounded.Update
import zone.ien.utils.ui.icon.sharp.ArrowDropDown
import zone.ien.utils.ui.icon.sharp.ArrowDropUp
import zone.ien.utils.ui.icon.sharp.Delete
import zone.ien.utils.ui.icon.sharp.Keyboard
import zone.ien.utils.ui.icon.sharp.Save
import zone.ien.utils.ui.icon.sharp.Schedule
import zone.ien.utils.ui.icon.sharp.Update

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

    val CloudOff @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.CloudOff
        IconStyle.Rounded -> Rounded.CloudOff
        IconStyle.Sharp -> Sharp.CloudOff
    }

    val Update @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.Update
        IconStyle.Rounded -> Rounded.Update
        IconStyle.Sharp -> Sharp.Update
    }

    val Keyboard @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.Keyboard
        IconStyle.Rounded -> Rounded.Keyboard
        IconStyle.Sharp -> Sharp.Keyboard
    }

    val Schedule @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.Schedule
        IconStyle.Rounded -> Rounded.Schedule
        IconStyle.Sharp -> Sharp.Schedule
    }

    val ArrowDropUp @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.ArrowDropUp
        IconStyle.Rounded -> Rounded.ArrowDropUp
        IconStyle.Sharp -> Sharp.ArrowDropUp
    }

    val ArrowDropDown @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.ArrowDropDown
        IconStyle.Rounded -> Rounded.ArrowDropDown
        IconStyle.Sharp -> Sharp.ArrowDropDown
    }
}