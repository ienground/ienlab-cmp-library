package zone.ien.utils.adaptive.menu

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import org.jetbrains.compose.resources.stringResource
import zone.ien.hig.CupertinoDropdownMenu
import zone.ien.hig.CupertinoIcon
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.utils.adaptive.view.AdaptiveTooltipBox
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.more_options
import zone.ien.utils.icon.hig.Ellipsis
import zone.ien.utils.icon.hig.HigIcons
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.menu.IconData
import zone.ien.utils.ui.menu.LocalMenuIconButtonSize
import zone.ien.utils.ui.view.M3TooltipBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigActionMenu(
    item: ActionMenuItem.IconMenuItem
) {
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
                            when (icon) {
                                is IconData.Vector -> {
                                    CupertinoIcon(
                                        imageVector = icon.imageVector,
                                        contentDescription = item.title,
                                        modifier = Modifier
                                            .alpha(alpha)
                                            .size(LocalMenuIconButtonSize.current.first - 16.dp)
                                    )
                                }
                                is IconData.Paint -> {
                                    CupertinoIcon(
                                        painter = icon.painter,
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCupertinoApi::class)
@Composable
fun HigActionsMenu(
    items: List<ActionMenuItem>,
    isOpen: Boolean,
    closeDropdown: () -> Unit,
    onToggleOverflow: () -> Unit,
    maxVisibleItems: Int,
) {
    val menuItems = remember(items, maxVisibleItems) { splitMenuItems(items, maxVisibleItems) }
    menuItems.alwaysShownItems.forEach { HigActionMenu(it) }

    if (menuItems.overflowItems.isNotEmpty()) {
        M3TooltipBox(
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
                    imageVector = HigIcons.Ellipsis,
                    contentDescription = stringResource(Res.string.more_options),
                )
            }
        }
        CupertinoDropdownMenu(
            expanded = isOpen,
            onDismissRequest = onToggleOverflow,
            backdrop = rememberLayerBackdrop()
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