package zone.ien.utils.adaptive.select

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.select.M3ExposedDropdownMenuBox

/**
 * @param title is not used at Android.
 */
@Composable
actual fun <T> ExposedDropdownMenuBox(
    modifier: Modifier,
    title: String,
    itemsWithLabels: Map<T, String>,
    currentItem: T?,
    onItemSelected: (T) -> Unit,
    trailingIconButton: @Composable (onClick: () -> Unit, expanded: Boolean) -> Unit,
    dropdownMenuItem: @Composable (text: @Composable () -> Unit, onClick: () -> Unit) -> Unit,
    textField: @Composable (value: String, trailingIcon: @Composable () -> Unit) -> Unit
) {
    M3ExposedDropdownMenuBox(
        modifier = modifier,
        itemsWithLabels = itemsWithLabels,
        currentItem = currentItem,
        onItemSelected = onItemSelected,
        trailingIconButton = trailingIconButton,
        dropdownMenuItem = dropdownMenuItem,
        textField = textField
    )
}

/**
 * @param title is not used at Android.
 */
@Composable
actual fun <T> ExposedDropdownMenuBox(
    modifier: Modifier,
    title: String,
    itemsWithLabels: Map<T, String>,
    currentItems: List<T>,
    onItemsSelected: (List<T>) -> Unit,
    trailingIconButton: @Composable (onClick: () -> Unit, expanded: Boolean) -> Unit,
    dropdownMenuItem: @Composable (text: @Composable () -> Unit, onClick: () -> Unit, checked: Boolean) -> Unit,
    textField: @Composable (value: String, trailingIcon: @Composable () -> Unit) -> Unit
) {
    M3ExposedDropdownMenuBox(
        modifier = modifier,
        itemsWithLabels = itemsWithLabels,
        currentItems = currentItems,
        onItemsSelected = onItemsSelected,
        trailingIconButton = trailingIconButton,
        dropdownMenuItem = dropdownMenuItem,
        textField = textField
    )
}