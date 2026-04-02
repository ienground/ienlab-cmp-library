package zone.ien.utils.adaptive.select

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.icon.material.M3SystemIcon
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.view.textfield.M3TextFieldIconButton

/**
 * @param title is not used at Android.
 * @param dropdownMenuItem is not used at iOS.
 */
@Composable
expect fun <T> ExposedDropdownMenuBox(
    modifier: Modifier = Modifier,
    title: String,
    itemsWithLabels: Map<T, String>,
    currentItem: T?,
    onItemSelected: (T) -> Unit,
    trailingIconButton: @Composable (
        onClick: () -> Unit,
        expanded: Boolean
    ) -> Unit = { onClick, expanded ->
        M3TextFieldIconButton(
            onClick = onClick,
            icon = IconData.Vector(if (expanded) M3SystemIcon.ArrowDropUp else M3SystemIcon.ArrowDropDown)
        )
    },
    dropdownMenuItem: @Composable (
        text: @Composable () -> Unit,
        onClick: () -> Unit,
    ) -> Unit = { text, onClick -> DropdownMenuItem(text = text, onClick = onClick) },
    textField: @Composable (value: String, trailingIcon: @Composable () -> Unit) -> Unit
)

/**
 * @param title is not used at Android.
 * @param dropdownMenuItem is not used at iOS.
 */
@Composable
expect fun <T> ExposedDropdownMenuBox(
    modifier: Modifier = Modifier,
    title: String,
    itemsWithLabels: Map<T, String>,
    currentItems: List<T>,
    onItemsSelected: (List<T>) -> Unit,
    trailingIconButton: @Composable (
        onClick: () -> Unit,
        expanded: Boolean
    ) -> Unit = { onClick, expanded ->
        M3TextFieldIconButton(
            onClick = onClick,
            icon = IconData.Vector(if (expanded) M3SystemIcon.ArrowDropUp else M3SystemIcon.ArrowDropDown)
        )
    },
    dropdownMenuItem: @Composable (
        text: @Composable () -> Unit,
        onClick: () -> Unit,
        checked: Boolean
    ) -> Unit = { text, onClick, checked ->
        DropdownMenuItem(
            text = text,
            onClick = onClick,
            leadingIcon = if (checked) { { Icon(imageVector = M3SystemIcon.Check, contentDescription = null) } } else null
        )
    },
    textField: @Composable (value: String, trailingIcon: @Composable () -> Unit) -> Unit
)