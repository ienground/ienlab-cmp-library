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
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.menu.M3DropdownMenuItem
import zone.ien.utils.ui.view.textfield.M3TextFieldIconButton

/**
 * M3ExposedDropdownMenuBox은 노출된 드롭다운 메뉴 박스를 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param itemsWithLabels 아이템과 라벨의 매핑
 * @param currentItem 현재 선택된 아이템
 * @param onItemSelected 아이템 선택 시 호출되는 콜백 함수
 * @param trailingIconButton trailing 아이콘
 * @param dropdownMenuItem 드롭다운 메뉴 항목
 * @param textField 텍스트 필드
 */
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
            icon = IconData.Vector(if (expanded) M3SystemIcons.ArrowDropUp else M3SystemIcons.ArrowDropDown)
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

/**
 * M3ExposedDropdownMenuBox은 다중 선택 가능한 노출된 드롭다운 메뉴 박스를 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param itemsWithLabels 아이템과 라벨의 매핑
 * @param currentItems 현재 선택된 아이템 목록
 * @param onItemsSelected 아이템 선택 시 호출되는 콜백 함수
 * @param trailingIconButton trailing 아이콘
 * @param dropdownMenuItem 드롭다운 메뉴 항목
 * @param textField 텍스트 필드
 */
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
            icon = IconData.Vector(if (expanded) M3SystemIcons.ArrowDropUp else M3SystemIcons.ArrowDropDown)
        )
    },
    dropdownMenuItem: @Composable (
        text: @Composable () -> Unit,
        onClick: () -> Unit,
        checked: Boolean
    ) -> Unit = { text, onClick, checked ->
        M3DropdownMenuItem(
            text = text,
            onClick = onClick,
            leadingIcon = if (checked) { { Icon(imageVector = M3SystemIcons.Check, contentDescription = null) } } else null
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