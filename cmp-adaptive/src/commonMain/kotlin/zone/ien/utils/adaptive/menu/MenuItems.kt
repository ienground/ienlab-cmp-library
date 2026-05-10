package zone.ien.utils.adaptive.menu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import org.jetbrains.compose.resources.stringResource
import zone.ien.hig.CupertinoDropdownMenu
import zone.ien.hig.CupertinoDropdownMenuNative
import zone.ien.hig.CupertinoIcon
import zone.ien.hig.CupertinoMenuItemData
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.MenuAction
import zone.ien.hig.theme.CupertinoColors
import zone.ien.hig.theme.systemRed
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.view.AdaptiveDropdownBox
import zone.ien.utils.adaptive.view.AdaptiveTooltipBox
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.more_options
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.hig.Ellipsis
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.icon.IconData

/**
 * HIG 액션 메뉴 컴포저블
 *
 * @param item ActionMenuItem.IconMenuItem - 메뉴 항목 정보
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigActionMenu(
    item: ActionMenuItem.IconMenuItem
) {
    val alpha by animateFloatAsState(
        targetValue = if (item.enabled) 1f else 0.25f,
        label = "action_alpha"
    )

    if (item.visible) {
        item.icon?.let { icon ->
            BadgedBox(
                badge = {
                    if (item.badge != 0) {
                        Badge(
                            content = if (item.badge > 0) {{ Text(text = item.badge.toString()) }} else null,
                            containerColor = CupertinoColors.systemRed,
                            contentColor = Color.White,
                            modifier = Modifier//.offset(x = (-8).dp, y = 8.dp)
                        )
                    }
                }
            ) {
                AdaptiveTooltipBox(
                    label = item.title
                ) {
                    Box(
                        modifier = Modifier.clickable(
                            enabled = item.enabled,
                            onClick = item.onClick,
                            indication = null,
                            interactionSource = null
                        )
                    ) {
                        AnimatedContent(
                            targetState = item.icon,
                            label = "menu_icon"
                        ) {
                            ComplexIcon(
                                icon = icon,
                                contentDescription = item.title,
                                modifier = Modifier.alpha(alpha)
                            )
                        }
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.clickable(
                    enabled = item.enabled,
                    onClick = item.onClick,
                    indication = null,
                    interactionSource = null
                )
            ) {
                Text(text = item.title)
            }
        }
    }
}

/**
 * HIG 액션 메뉴 컴포저블
 *
 * @param items 메뉴 항목 리스트
 * @param isOpen 메뉴 열림 상태
 * @param closeDropdown 메뉴 닫기 함수
 * @param onToggleOverflow 메뉴 토글 함수
 * @param maxVisibleItems 최대 표시 가능한 메뉴 항목 수
 * @param isNative 네이티브 메뉴 사용 여부. 기본값은 false
 * @param trigger 트리거 컴포저블
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalCupertinoApi::class)
@Composable
fun HigActionsMenu(
    items: List<ActionMenuItem>,
    isOpen: Boolean,
    closeDropdown: () -> Unit,
    onToggleOverflow: () -> Unit,
    maxVisibleItems: Int,
    isNative: Boolean = false,
    trigger: @Composable (@Composable () -> Unit) -> Unit,
) {
    val menuItems = remember(items, maxVisibleItems) { splitMenuItems(items, maxVisibleItems) }

    AdaptiveDropdownBox(
        expanded = isOpen,
        trigger = {
            trigger {
                menuItems.alwaysShownItems.forEach { HigActionMenu(it) }
                if (menuItems.overflowItems.isNotEmpty()) {
                    AdaptiveTooltipBox(
                        label = stringResource(Res.string.more_options),
                    ) {
                        Box(
                            modifier = Modifier.clickable(
                                onClick = onToggleOverflow,
                                indication = null,
                                interactionSource = null
                            )
                        ) {
                            CupertinoIcon(
                                imageVector = zone.ien.utils.icon.hig.HigSystemIcons.Ellipsis,
                                contentDescription = stringResource(Res.string.more_options),
                            )
                        }
                    }
                }
            }
        }
    ) {
        if (isNative) {
            CupertinoDropdownMenuNative(
                expanded = isOpen,
                onDismissRequest = onToggleOverflow,
                backdrop = rememberDefaultBackdrop(),
                items = menuItems.overflowItems.mapNotNull { item ->
                    CupertinoMenuItemData(
                        title = item.title,
                        icon = if (item is ActionMenuItem.IconMenuItem && item.icon != null) {
                            when (item.icon) {
                                is IconData.Vector -> rememberVectorPainter((item.icon as IconData.Vector).imageVector)
                                is IconData.Paint -> (item.icon as IconData.Paint).painter
                                else -> null
                            }
                        } else null,
                        onClick = item.onClick
                    ).takeIf { item.visible }
                }
            )
        } else {
            CupertinoDropdownMenu(
                expanded = isOpen,
                onDismissRequest = onToggleOverflow,
                backdrop = rememberDefaultBackdrop()
            ) {
                menuItems.overflowItems.forEach { item ->
                    if (item.visible) {
                        MenuAction(
                            title = { Text(text = item.title) },
                            leadingIcon = if (item is ActionMenuItem.IconMenuItem) {
                                item.icon?.let {
                                    {
                                        ComplexIcon(
                                            icon = it,
                                            contentDescription = item.title
                                        )
                                    }
                                }
                            } else null,
                            trailingIcon = if (item is ActionMenuItem.IconMenuItem) {
                                {
                                    if (item.badge != 0) {
                                        Badge(
                                            content = if (item.badge > 0) {{ Text(text = item.badge.toString()) }} else null,
                                            containerColor = CupertinoColors.systemRed,
                                            contentColor = Color.White
                                        )
                                    }
                                }
                            } else null,
                            onClick = {
                                closeDropdown()
                                item.onClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

private data class MenuItems(
    val alwaysShownItems: List<ActionMenuItem.IconMenuItem>,
    val overflowItems: List<ActionMenuItem>,
)

/**
 * 메뉴 항목을 분리하여 항상 표시 및 오버플로 항목으로 구분
 *
 * @param items 메뉴 항목 리스트
 * @param maxVisibleItems 최대 표시 가능한 항목 수
 * @return MenuItems - 항상 표시 항목과 오버플로 항목이 포함된 데이터 클래스
 */
private fun splitMenuItems(
    items: List<ActionMenuItem>,
    maxVisibleItems: Int,
): MenuItems {
    val alwaysShownItems: MutableList<ActionMenuItem.IconMenuItem> = items.filterIsInstance<ActionMenuItem.IconMenuItem.AlwaysShown>().toMutableList()
    val ifRoomItems: MutableList<ActionMenuItem.IconMenuItem> = items.filterIsInstance<ActionMenuItem.IconMenuItem.ShownIfRoom>().toMutableList()
    val overflowItems = items.filterIsInstance<ActionMenuItem.NeverShown>()

    val hasOverflow = overflowItems.isNotEmpty() || (alwaysShownItems.size + ifRoomItems.size - 1) > maxVisibleItems
    val usedSlots = alwaysShownItems.size + (if (hasOverflow) 1 else 0)
    val availableSlots = maxVisibleItems - usedSlots
    if (availableSlots > 0 && ifRoomItems.isNotEmpty()) {
        val visible = ifRoomItems.subList(0, availableSlots.coerceAtMost(ifRoomItems.size))
        alwaysShownItems.addAll(visible)
        ifRoomItems.removeAll(visible)
    }

    return MenuItems(
        alwaysShownItems = alwaysShownItems,
        overflowItems = ifRoomItems + overflowItems,
    )
}