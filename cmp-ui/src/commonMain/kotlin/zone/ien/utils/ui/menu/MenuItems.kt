package zone.ien.utils.ui.menu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import zone.ien.utils.ui.view.M3TooltipBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3ActionsMenu(
    items: List<ActionMenuItem>,
    isOpen: Boolean,
    closeDropdown: () -> Unit,
    onToggleOverflow: () -> Unit,
    maxVisibleItems: Int,
) {
    val menuItems = remember(items, maxVisibleItems) { splitMenuItems(items, maxVisibleItems) }
    menuItems.alwaysShownItems.forEach { item ->
        val alpha by animateFloatAsState(
            targetValue = if (item.enabled) 1f else 0.25f,
            label = "action_alpha"
        )

        AnimatedVisibility(
            visible = item.visible,
            enter = fadeIn(tween(150)) + expandHorizontally(tween(300)),
            exit = fadeOut(tween(150)) + shrinkHorizontally(tween(300))
        ) {
            item.icon?.let { icon ->
                M3TooltipBox(
                    label = item.title
                ) {
                    BadgedBox(
                        badge = {
                            if (item.badge != 0) {
                                Badge(
                                    modifier = Modifier
                                        .offset(x = (-8).dp, y = 8.dp)
                                    ,
                                    content = if (item.badge > 0) {{ Text(text = item.badge.toString()) }} else null
                                )
                            }
                        }
                    ) {
                        IconButton(
                            enabled = item.enabled,
                            onClick = item.onClick,
                            modifier = Modifier.size(LocalMenuIconButtonSize.current.first),
                        ) {
                            AnimatedContent(
                                targetState = item.icon,
                                label = "menu_icon"
                            ) {
                                ComplexIcon(
                                    icon = icon,
                                    contentDescription = item.title,
                                    modifier = Modifier
                                        .alpha(alpha)
                                        .size(LocalMenuIconButtonSize.current.first - 16.dp)
                                )
                            }
                        }
                    }
                }
            } ?: run {
                TextButton(
                    enabled = item.enabled,
                    onClick = item.onClick
                ) {
                    Text(text = item.title)
                }
            }
        }
    }

    if (menuItems.overflowItems.isNotEmpty()) {
        M3TooltipBox(
            label = stringResource(Res.string.more_options),
        ) {
            Box {
                IconButton(
                    onClick = onToggleOverflow,
                ) {
                    Icon(
                        imageVector = M3SystemIcons.MoreVert,
                        contentDescription = stringResource(Res.string.more_options),
                    )
                }
                DropdownMenu(
                    expanded = isOpen,
                    onDismissRequest = onToggleOverflow,
                ) {
                    menuItems.overflowItems.forEach { item ->
                        if (item.visible) {
                            DropdownMenuItem(
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
                                            Badge(
                                                content = if (item.badge > 0) {{ Text(text = item.badge.toString()) }} else null
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