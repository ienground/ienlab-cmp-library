package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.material.filled.Add
import zone.ien.utils.icon.material.filled.ArrowBack
import zone.ien.utils.icon.material.filled.ArrowBackIosNew
import zone.ien.utils.icon.material.filled.ArrowDropDown
import zone.ien.utils.icon.material.filled.ArrowDropUp
import zone.ien.utils.icon.material.filled.Cancel
import zone.ien.utils.icon.material.filled.Check
import zone.ien.utils.icon.material.filled.ChevronRight
import zone.ien.utils.icon.material.filled.Close
import zone.ien.utils.icon.material.filled.CloudOff
import zone.ien.utils.icon.material.filled.Delete
import zone.ien.utils.icon.material.filled.Edit
import zone.ien.utils.icon.material.filled.Keyboard
import zone.ien.utils.icon.material.filled.KeyboardArrowDown
import zone.ien.utils.icon.material.filled.MoreVert
import zone.ien.utils.icon.material.filled.Save
import zone.ien.utils.icon.material.filled.Schedule
import zone.ien.utils.icon.material.filled.Update
import zone.ien.utils.icon.material.rounded.Add
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
import zone.ien.utils.icon.tabler.TablerIcons
import zone.ien.utils.icon.tabler.fill.CaretDown
import zone.ien.utils.icon.tabler.fill.CaretUp
import zone.ien.utils.icon.tabler.fill.CircleX
import zone.ien.utils.icon.tabler.fill.Clock
import zone.ien.utils.icon.tabler.fill.DeviceFloppy
import zone.ien.utils.icon.tabler.fill.Keyboard
import zone.ien.utils.icon.tabler.fill.Pencil
import zone.ien.utils.icon.tabler.fill.Trash
import zone.ien.utils.icon.tabler.line.ArrowLeft
import zone.ien.utils.icon.tabler.line.ChevronLeft
import zone.ien.utils.icon.tabler.line.ChevronRight
import zone.ien.utils.icon.tabler.line.CircleOpenArrowUp
import zone.ien.utils.icon.tabler.line.DotsVertical
import zone.ien.utils.icon.tabler.line.Check as TablerCheck
import zone.ien.utils.icon.tabler.line.Close as TablerClose
import zone.ien.utils.icon.tabler.line.CloudOff as TablerCloudOff
import zone.ien.utils.icon.material.rounded.ChevronRight
import zone.ien.utils.icon.material.rounded.KeyboardArrowDown
import zone.ien.utils.icon.material.sharp.Add
import zone.ien.utils.icon.material.sharp.ChevronRight
import zone.ien.utils.icon.material.sharp.KeyboardArrowDown
import zone.ien.utils.icon.tabler.line.Add
import zone.ien.utils.icon.tabler.line.ChevronDown

/**
 * System 아이콘을 제공합니다.
 */
object SystemIcons {
    val Delete @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Fill.Trash
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Delete
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Delete
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Delete
    }

    val Save @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Fill.DeviceFloppy
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Save
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Save
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Save
    }

    val Add @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.Add
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Add
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Add
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Add
    }

    val CloudOff @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.TablerCloudOff
        IconStyle.Material.Filled -> M3SystemIcons.Filled.CloudOff
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.CloudOff
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.CloudOff
    }

    val Update @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.CircleOpenArrowUp
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Update
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Update
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Update
    }

    val Keyboard @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Fill.Keyboard
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Keyboard
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Keyboard
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Keyboard
    }

    val Schedule @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Fill.Clock
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Schedule
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Schedule
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Schedule
    }

    val ArrowDropUp @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Fill.CaretUp
        IconStyle.Material.Filled -> M3SystemIcons.Filled.ArrowDropUp
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.ArrowDropUp
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.ArrowDropUp
    }

    val ArrowDropDown @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Fill.CaretDown
        IconStyle.Material.Filled -> M3SystemIcons.Filled.ArrowDropDown
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.ArrowDropDown
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.ArrowDropDown
    }

    val ChevronDown @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.ChevronDown
        IconStyle.Material.Filled -> M3SystemIcons.Filled.KeyboardArrowDown
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.KeyboardArrowDown
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.KeyboardArrowDown
    }

    val Check @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.TablerCheck
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Check
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Check
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Check
    }

    val ArrowBack @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.ArrowLeft
        IconStyle.Material.Filled -> M3SystemIcons.Filled.ArrowBack
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.ArrowBack
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.ArrowBack
    }

    val ArrowBackIos @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.ChevronLeft
        IconStyle.Material.Filled -> M3SystemIcons.Filled.ArrowBackIosNew
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.ArrowBackIosNew
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.ArrowBackIosNew
    }

    val MoreVert @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.DotsVertical
        IconStyle.Material.Filled -> M3SystemIcons.Filled.MoreVert
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.MoreVert
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.MoreVert
    }

    val Close @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.TablerClose
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Close
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Close
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Close
    }

    val Edit @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Fill.Pencil
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Edit
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Edit
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Edit
    }

    val Cancel @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Fill.CircleX
        IconStyle.Material.Filled -> M3SystemIcons.Filled.Cancel
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.Cancel
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.Cancel
    }

    val ChevronRight @Composable get() = when (LocalIconStyle.current) {
        IconStyle.Tabler -> TablerIcons.Line.ChevronRight
        IconStyle.Material.Filled -> M3SystemIcons.Filled.ChevronRight
        IconStyle.Material.Rounded -> M3SystemIcons.Rounded.ChevronRight
        IconStyle.Material.Sharp -> M3SystemIcons.Sharp.ChevronRight
    }
}
