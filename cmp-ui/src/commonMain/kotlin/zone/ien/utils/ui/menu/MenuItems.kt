package zone.ien.utils.ui.menu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.back
import zone.ien.utils.cmp_ui.generated.resources.more_options
import zone.ien.utils.icon.material.MaterialIcons
import zone.ien.utils.ui.view.MyTooltipBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsMenu(
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
                BadgedBox(badge = {
                    if (item.badge != 0) {
                        Badge(
                            modifier = Modifier
                                .offset(x = (-8).dp, y = 8.dp)
                            ,
                            content = if (item.badge > 0) {{ Text(text = item.badge.toString()) }} else null
                        )
                    }
                }) {
                    if (item.isPrimary) {
                        TextButton(
                            enabled = item.enabled,
                            onClick = item.onClick,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AnimatedContent(
                                    targetState = item.icon,
                                    label = "menu_icon"
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = item.title,
                                        modifier = Modifier.alpha(alpha)
                                    )
                                }
                                Text(
                                    text = item.title
                                )
                            }
                        }
                    } else {
                        MyTooltipBox(
                            label = item.title
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
                                    Icon(
                                        imageVector = icon,
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
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(positioning = TooltipAnchorPosition.Below),
            state = rememberTooltipState(isPersistent = false),
            focusable = false,
            enableUserInput = false,
            tooltip = {
                Text(
                    text = stringResource(Res.string.more_options),
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp)
                )
            },
        ) {
            IconButton(
                onClick = onToggleOverflow,
            ) {
                Icon(
                    imageVector = MaterialIcons.MoreVert,
                    contentDescription = stringResource(Res.string.more_options),
                )
            }
        }
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = onToggleOverflow,
        ) {
            menuItems.overflowItems.forEach { item ->
                if (item.visible) {
                    DropdownMenuItem(
                        text = { Text(text = item.title) },
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