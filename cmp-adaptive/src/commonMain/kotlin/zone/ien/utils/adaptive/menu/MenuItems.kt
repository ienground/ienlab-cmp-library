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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
import zone.ien.utils.adaptive.view.AdaptiveDropdownMenu
import zone.ien.utils.adaptive.view.AdaptiveTooltipBox
import zone.ien.utils.adaptive.view.DropdownMenuSection
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.more_options
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.hig.Ellipsis
import zone.ien.utils.ui.menu.ActionMenuItem

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCupertinoApi::class,
    ExperimentalAdaptiveApi::class
)
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
                                imageVector = zone.ien.utils.icon.hig.HigIcons.Ellipsis,
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
            },
            items = listOf(
                DropdownMenuSection(
                    title = null,
                    items = menuItems.overflowItems.mapNotNull { item ->
                        if (item.visible) {
                            DropdownMenuSection.Action(
                                text = item.title,
                                onClick = {
                                    closeDropdown()
                                    item.onClick()
                                },
                                icon = if (item is ActionMenuItem.IconMenuItem) item.icon else null,
                                badge = if (item is ActionMenuItem.IconMenuItem) item.badge else 0,
                                visible = item.visible,
                                enabled = item.enabled
                            )
                        } else null
                    }
                )
            )
        )
//        {
//            menuItems.overflowItems.forEach { item ->
//                if (item.visible) {
//                    DropdownMenuItem(
//                        text = { Text(text = item.title) },
//                        leadingIcon = if (item is ActionMenuItem.IconMenuItem) {
//                            item.icon?.let {
//                                {
//                                    ComplexIcon(
//                                        icon = it,
//                                        contentDescription = item.title
//                                    )
//                                }
//                            }
//                        } else null,
//                        trailingIcon = if (item is ActionMenuItem.IconMenuItem) {
//                            {
//                                if (item.badge != 0) {
//                                    Badge(
//                                        content = if (item.badge > 0) {{ Text(text = item.badge.toString()) }} else null,
//                                        containerColor = CupertinoColors.systemRed,
//                                        contentColor = Color.White
//                                    )
//                                }
//                            }
//                        } else null,
//                        onClick = {
//                            closeDropdown()
//                            item.onClick()
//                        }
//                    )
//                }
//            }
//        }
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