package zone.ien.utils.icon.material

import androidx.compose.runtime.Composable
import zone.ien.utils.icon.IconStyle
import zone.ien.utils.icon.LocalIconStyle
import zone.ien.utils.icon.material.filled.ArrowBack
import zone.ien.utils.icon.material.filled.ArrowBackIosNew
import zone.ien.utils.icon.material.filled.ArrowDropDown
import zone.ien.utils.icon.material.filled.ArrowDropUp
import zone.ien.utils.icon.material.filled.Cancel
import zone.ien.utils.icon.material.filled.Check
import zone.ien.utils.icon.material.filled.Close
import zone.ien.utils.icon.material.filled.CloudOff
import zone.ien.utils.icon.material.filled.Delete
import zone.ien.utils.icon.material.filled.Edit
import zone.ien.utils.icon.material.filled.Keyboard
import zone.ien.utils.icon.material.filled.MoreVert
import zone.ien.utils.icon.material.filled.Save
import zone.ien.utils.icon.material.filled.Schedule
import zone.ien.utils.icon.material.filled.Update
import zone.ien.utils.icon.material.rounded.ArrowBack
import zone.ien.utils.icon.material.rounded.ArrowBackIosNew
import zone.ien.utils.icon.material.rounded.ArrowDropDown
import zone.ien.utils.icon.material.rounded.ArrowDropUp
import zone.ien.utils.icon.material.rounded.Cancel
import zone.ien.utils.icon.material.rounded.Check
import zone.ien.utils.icon.material.rounded.Close
import zone.ien.utils.icon.material.rounded.CloudOff
import zone.ien.utils.icon.material.rounded.Delete
import zone.ien.utils.icon.material.rounded.Edit
import zone.ien.utils.icon.material.rounded.Keyboard
import zone.ien.utils.icon.material.rounded.MoreVert
import zone.ien.utils.icon.material.rounded.Save
import zone.ien.utils.icon.material.rounded.Schedule
import zone.ien.utils.icon.material.rounded.Update
import zone.ien.utils.icon.material.sharp.ArrowBack
import zone.ien.utils.icon.material.sharp.ArrowBackIosNew
import zone.ien.utils.icon.material.sharp.ArrowDropDown
import zone.ien.utils.icon.material.sharp.ArrowDropUp
import zone.ien.utils.icon.material.sharp.Cancel
import zone.ien.utils.icon.material.sharp.Check
import zone.ien.utils.icon.material.sharp.Close
import zone.ien.utils.icon.material.sharp.CloudOff
import zone.ien.utils.icon.material.sharp.Delete
import zone.ien.utils.icon.material.sharp.Edit
import zone.ien.utils.icon.material.sharp.Keyboard
import zone.ien.utils.icon.material.sharp.MoreVert
import zone.ien.utils.icon.material.sharp.Save
import zone.ien.utils.icon.material.sharp.Schedule
import zone.ien.utils.icon.material.sharp.Update

object M3SystemIcons {
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

    val Check @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.Check
        IconStyle.Rounded -> Rounded.Check
        IconStyle.Sharp -> Sharp.Check
    }

    val ArrowBack @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.ArrowBack
        IconStyle.Rounded -> Rounded.ArrowBack
        IconStyle.Sharp -> Sharp.ArrowBack
    }

    val ArrowBackIos @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.ArrowBackIosNew
        IconStyle.Rounded -> Rounded.ArrowBackIosNew
        IconStyle.Sharp -> Sharp.ArrowBackIosNew
    }

    val MoreVert @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.MoreVert
        IconStyle.Rounded -> Rounded.MoreVert
        IconStyle.Sharp -> Sharp.MoreVert
    }

    val Close @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.Close
        IconStyle.Rounded -> Rounded.Close
        IconStyle.Sharp -> Sharp.Close
    }

    val Edit @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.Edit
        IconStyle.Rounded -> Rounded.Edit
        IconStyle.Sharp -> Sharp.Edit
    }

    val Cancel @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Filled -> Filled.Cancel
        IconStyle.Rounded -> Rounded.Cancel
        IconStyle.Sharp -> Sharp.Cancel
    }
}
