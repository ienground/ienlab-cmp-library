package zone.ien.utils.ui.menu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.more_options
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenBadge
import zone.ien.utils.ui.interactive.IenBadgeSize
import zone.ien.utils.ui.interactive.IenBadgeVariant
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.screen.LocalIenTopBarFloatingSlotHiddenRequester
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
    val actionFadeAnimation = tween<Float>(
        durationMillis = 90,
        easing = IenTheme.motion.standardEasing,
    )
    val actionFadeOut = tween<Float>(
        durationMillis = 70,
        easing = IenTheme.motion.standardEasing,
    )
    val visibleOverflowItems = menuItems.overflowItems.filter { it.visible }
    val hideFloatingSlot = LocalIenTopBarFloatingSlotHiddenRequester.current
    val triggerAlpha by animateFloatAsState(
        targetValue = if (isOpen) 0.001f else 1f,
        animationSpec = tween(durationMillis = 100, easing = IenTheme.motion.standardEasing),
        label = "actions_trigger_alpha",
    )

    LaunchedEffect(isOpen, hideFloatingSlot) {
        hideFloatingSlot?.invoke(isOpen)
    }
    DisposableEffect(hideFloatingSlot) {
        onDispose {
            hideFloatingSlot?.invoke(false)
        }
    }

    @Composable
    fun OverflowDropdownContent() {
        visibleOverflowItems.forEach { item ->
            IenMenu.DropdownItem(
                text = item.title,
                left = if (item is ActionMenuItem.IconMenuItem) {
                    item.icon?.let {
                        {
                            ComplexIcon(
                                icon = it,
                                contentDescription = item.title
                            )
                        }
                    }
                } else null,
                right = if (item is ActionMenuItem.IconMenuItem) {
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
                enabled = item.enabled,
                onClick = {
                    closeDropdown()
                    item.onClick()
                }
            )
        }
    }

    @Composable
    fun ActionsRow() {
        Row(
            modifier = Modifier
                .clipToBounds()
                .animateContentSize(animationSpec = tween(durationMillis = 110, easing = IenTheme.motion.standardEasing))
        ) {
            menuItems.alwaysShownItems.forEach { item ->
                val alpha by animateFloatAsState(
                    targetValue = if (item.enabled) 1f else 0.25f,
                    animationSpec = actionFadeAnimation,
                    label = "action_alpha"
                )

                AnimatedVisibility(
                    visible = item.visible,
                    enter = fadeIn(actionFadeAnimation) +
                        slideInHorizontally(tween(durationMillis = 110, easing = IenTheme.motion.standardEasing)) { it / 2 } +
                        expandHorizontally(animationSpec = tween(durationMillis = 110, easing = IenTheme.motion.standardEasing), expandFrom = Alignment.End),
                    exit = fadeOut(actionFadeOut) +
                        slideOutHorizontally(tween(durationMillis = 90, easing = IenTheme.motion.standardEasing)) { it / 2 } +
                        shrinkHorizontally(animationSpec = tween(durationMillis = 90, easing = IenTheme.motion.standardEasing), shrinkTowards = Alignment.End),
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

            AnimatedVisibility(
                visible = visibleOverflowItems.isNotEmpty(),
                enter = fadeIn(actionFadeAnimation) +
                    slideInHorizontally(tween(durationMillis = 110, easing = IenTheme.motion.standardEasing)) { it / 2 } +
                    expandHorizontally(animationSpec = tween(durationMillis = 110, easing = IenTheme.motion.standardEasing), expandFrom = Alignment.End),
                exit = fadeOut(actionFadeOut) +
                    slideOutHorizontally(tween(durationMillis = 90, easing = IenTheme.motion.standardEasing)) { it / 2 } +
                    shrinkHorizontally(animationSpec = tween(durationMillis = 90, easing = IenTheme.motion.standardEasing), shrinkTowards = Alignment.End),
            ) {
                IenTooltipBox(
                    label = stringResource(Res.string.more_options),
                ) {
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
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .wrapContentSize(align = Alignment.TopEnd)
            .graphicsLayer { clip = false }
            .animateContentSizeWithoutClipping(),
        contentAlignment = Alignment.TopEnd,
    ) {
        Box(
            modifier = Modifier.graphicsLayer {
                scaleX = triggerAlpha
                scaleY = triggerAlpha
                clip = false
            },
        ) {
            ActionsRow()
        }

        if (visibleOverflowItems.isNotEmpty()) {
            IenMenu.PopupDropdown(
                expanded = isOpen,
                onDismissRequest = closeDropdown,
                offset = DpOffset(IenTheme.spacing.xxs, -IenTheme.spacing.xxs),
                placement = IenMenu.Placement.AnchorTopEnd,
                minWidth = 112.dp,
                content = { OverflowDropdownContent() },
            )
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
