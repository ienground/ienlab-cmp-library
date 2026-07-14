package zone.ien.utils.adaptive.select

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.menu.IenMenu
import zone.ien.utils.ui.view.textfield.IenTextFieldIconButton

/**
 * iOS와 Android 플랫폼 간에 서로 다른 구현을 제공하는 드롭다운 메뉴 박스 컴포저블.
 * 
 * 이 컴포저블은 Android와 iOS의 서로 다른 UI 디자인을 따르도록 구현되어 있습니다.
 * - Android: cmp-ui 드롭다운 메뉴 구현
 * - iOS: 커스텀 선택 UI 구현
 * 
 * @param modifier 드롭다운 메뉴 박스에 적용할 수 있는 Modifier
 * @param title 드롭다운 메뉴의 제목 (Android에서는 사용되지 않음)
 * @param itemsWithLabels 선택할 수 있는 항목들과 관련된 라벨 매핑
 * @param currentItem 현재 선택된 항목 (단일 선택 모드)
 * @param onItemSelected 선택된 항목 변경 시 호출되는 콜백 함수
 * @param trailingIconButton 드롭다운 메뉴에 사용되는 trailing icon 버튼
 * @param dropdownMenuItem 드롭다운 메뉴 아이템 컴포저블 (iOS에서는 사용되지 않음)
 * @param textField 드롭다운 메뉴에 사용되는 텍스트 필드 컴포저블
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
        IenTextFieldIconButton(
            onClick = onClick,
            icon = IconData.Vector(if (expanded) M3SystemIcons.ArrowDropUp else M3SystemIcons.ArrowDropDown)
        )
    },
    dropdownMenuItem: @Composable (
        text: @Composable () -> Unit,
        onClick: () -> Unit,
    ) -> Unit = { text, onClick -> IenMenu.DropdownItem(onClick = onClick, content = text) },
    textField: @Composable (value: String, trailingIcon: @Composable () -> Unit) -> Unit
)

/**
 * iOS와 Android 플랫폼 간에 서로 다른 구현을 제공하는 다중 선택 드롭다운 메뉴 박스 컴포저블.
 * 
 * 이 컴포저블은 Android와 iOS의 서로 다른 UI 디자인을 따르도록 구현되어 있습니다.
 * - Android: cmp-ui 다중 선택 드롭다운 메뉴 구현
 * - iOS: 커스텀 다중 선택 UI 구현
 * 
 * @param modifier 드롭다운 메뉴 박스에 적용할 수 있는 Modifier
 * @param title 드롭다운 메뉴의 제목 (Android에서는 사용되지 않음)
 * @param itemsWithLabels 선택할 수 있는 항목들과 관련된 라벨 매핑
 * @param currentItems 현재 선택된 항목들 (다중 선택 모드)
 * @param onItemsSelected 선택된 항목 변경 시 호출되는 콜백 함수
 * @param trailingIconButton 드롭다운 메뉴에 사용되는 trailing icon 버튼
 * @param dropdownMenuItem 드롭다운 메뉴 아이템 컴포저블 (iOS에서는 사용되지 않음)
 * @param textField 드롭다운 메뉴에 사용되는 텍스트 필드 컴포저블
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
        IenTextFieldIconButton(
            onClick = onClick,
            icon = IconData.Vector(if (expanded) M3SystemIcons.ArrowDropUp else M3SystemIcons.ArrowDropDown)
        )
    },
    dropdownMenuItem: @Composable (
        text: @Composable () -> Unit,
        onClick: () -> Unit,
        checked: Boolean
    ) -> Unit = { text, onClick, checked ->
        IenMenu.DropdownItem(
            onClick = onClick,
            left = if (checked) { { Icon(imageVector = M3SystemIcons.Check, contentDescription = null) } } else null,
            content = text,
        )
    },
    textField: @Composable (value: String, trailingIcon: @Composable () -> Unit) -> Unit
)
