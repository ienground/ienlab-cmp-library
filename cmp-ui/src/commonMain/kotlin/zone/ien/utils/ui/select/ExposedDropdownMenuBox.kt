package zone.ien.utils.ui.select

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.material.M3SystemIcon
import zone.ien.utils.ui.view.textfield.M3TextFieldIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> M3ExposedDropdownMenuBox(
    modifier: Modifier = Modifier,
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
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        textField(itemsWithLabels[currentItem].orEmpty()) {
            trailingIconButton({ expanded = !expanded }, expanded)
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            itemsWithLabels.entries.toList().fastForEach { (item, label) ->
                dropdownMenuItem(
                    { Text(text = label) },
                    {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> M3ExposedDropdownMenuBox(
    modifier: Modifier = Modifier,
    itemsWithLabels: Map<T, String>,
    currentItems: List<T>,
    onItemsSelected: (List<T>) -> Unit,
    trailingIconButton: @Composable (
        onClick: () -> Unit,
        expanded: Boolean,
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
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        textField(currentItems.map { itemsWithLabels[it] }.joinToString(", ")) {
            trailingIconButton({ expanded = !expanded }, expanded)
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            itemsWithLabels.entries.toList().fastForEach { (item, label) ->
                AnimatedContent(
                    targetState = item in currentItems,
                    label = "Animate the selected item"
                ) { isSelected ->
                    dropdownMenuItem(
                        { Text(text = label) },
                        {
                            if (item in currentItems) {
                                onItemsSelected(currentItems - item)
                            } else {
                                onItemsSelected(currentItems + item)
                            }
                        },
                        isSelected
                    )
                }
            }
        }
    }
}