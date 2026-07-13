package zone.ien.utils.ui.menu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.more_options
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.interactive.IenBadge
import zone.ien.utils.ui.interactive.IenBadgeSize
import zone.ien.utils.ui.interactive.IenBadgeVariant
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.view.IenTooltipBox
import zone.ien.utils.utils.ui.animateContentSizeWithoutClipping

/**
 * IenActionsMenu는 액션 메뉴를 표시하기 위한 컴포저블입니다.
 *
 * @param items 메뉴 항목 리스트
 * @param isOpen 드롭다운이 열려 있는지 여부
 * @param closeDropdown 드롭다운을 닫는 콜백 함수
 * @param onToggleOverflow 드롭다운 토글 콜백 함수
 * @param maxVisibleItems 최대 표시 가능한 항목 수
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenActionsMenu(
    items: List<ActionMenuItem>,
    isOpen: Boolean,
    closeDropdown: () -> Unit,
    onToggleOverflow: () -> Unit,
    maxVisibleItems: Int,
) {
    val menuItems = remember(items, maxVisibleItems) { splitMenuItems(items, maxVisibleItems) }
    val actionFadeAnimation = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )
    val actionFadeOut = tween<Float>(durationMillis = 120)
    val visibleOverflowItems = menuItems.overflowItems.filter { it.visible }

    Row(modifier = Modifier.animateContentSizeWithoutClipping()) {
        menuItems.alwaysShownItems.forEach { item ->
            val alpha by animateFloatAsState(
                targetValue = if (item.enabled) 1f else 0.25f,
                animationSpec = actionFadeAnimation,
                label = "action_alpha"
            )

            AnimatedVisibility(
                visible = item.visible,
                enter = fadeIn(actionFadeAnimation) +
                    slideInHorizontally(spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)) { it / 2 } +
                    expandHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow), expandFrom = androidx.compose.ui.Alignment.End),
                exit = fadeOut(actionFadeOut) +
                    slideOutHorizontally(spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)) { it / 2 } +
                    shrinkHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow), shrinkTowards = androidx.compose.ui.Alignment.End),
            ) {
                item.icon?.let { icon ->
                    IenTooltipBox(
                        label = item.title
                    ) {
                        BadgedBox(
                            badge = {
                                if (item.badge != 0) {
                                    IenBadge(
                                        modifier = Modifier
                                            .offset(x = (-8).dp, y = 8.dp),
                                        text = if (item.badge > 0) item.badge.toString() else "",
                                        size = IenBadgeSize.Small,
                                        variant = IenBadgeVariant.Fill,
                                        tone = IenSemanticTone.Danger,
                                    )
                                }
                            }
                        ) {
                            IenIconButton(
                                onClick = item.onClick,
                                modifier = Modifier.size(
                                    width = LocalMenuIconButtonSize.current.first,
                                    height = LocalMenuIconButtonSize.current.second,
                                ),
                                size = IenButtonSize.Medium,
                                variant = IenButtonVariant.Ghost,
                                tone = IenSemanticTone.Neutral,
                                state = IenButtonState(enabled = item.enabled),
                            ) {
                                AnimatedContent(
                                    targetState = item.icon,
                                    label = "menu_icon"
                                ) { targetIcon ->
                                    targetIcon?.let {
                                        ComplexIcon(
                                            icon = it,
                                            contentDescription = item.title,
                                            modifier = Modifier
                                                .alpha(alpha)
                                                .size(LocalMenuIconButtonSize.current.first - 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                } ?: run {
                    IenTextButton(
                        text = item.title,
                        onClick = item.onClick,
                        tone = IenSemanticTone.Neutral,
                        state = IenButtonState(enabled = item.enabled),
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = visibleOverflowItems.isNotEmpty(),
        enter = fadeIn(actionFadeAnimation) +
            slideInHorizontally(spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)) { it / 2 } +
            expandHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow), expandFrom = androidx.compose.ui.Alignment.End),
        exit = fadeOut(actionFadeOut) +
            slideOutHorizontally(spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow)) { it / 2 } +
            shrinkHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMediumLow), shrinkTowards = androidx.compose.ui.Alignment.End),
    ) {
        IenTooltipBox(
            label = stringResource(Res.string.more_options),
        ) {
            Box {
                IenIconButton(
                    onClick = onToggleOverflow,
                    size = IenButtonSize.Medium,
                    variant = IenButtonVariant.Ghost,
                    tone = IenSemanticTone.Neutral,
                ) {
                    Icon(
                        imageVector = M3SystemIcons.MoreVert,
                        contentDescription = stringResource(Res.string.more_options),
                    )
                }
                IenDropdownMenu(
                    expanded = isOpen,
                    onDismissRequest = onToggleOverflow
                ) {
                    visibleOverflowItems.forEach { item ->
                        IenDropdownMenuItem(
                            text = { Text(text = item.title) },
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
                                        IenBadge(
                                            text = if (item.badge > 0) item.badge.toString() else "",
                                            size = IenBadgeSize.Small,
                                            variant = IenBadgeVariant.Fill,
                                            tone = IenSemanticTone.Danger,
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
