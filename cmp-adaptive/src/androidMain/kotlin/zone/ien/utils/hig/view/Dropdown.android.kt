package zone.ien.utils.hig.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.PopupProperties
import com.kyant.backdrop.Backdrop
import zone.ien.hig.CupertinoDropdownMenu
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.MenuAction
import zone.ien.hig.MenuDivider
import zone.ien.hig.MenuSection
import zone.ien.hig.theme.CupertinoColors
import zone.ien.hig.theme.systemRed
import zone.ien.utils.adaptive.view.DropdownMenuSection
import zone.ien.utils.icon.ComplexIcon

@OptIn(ExperimentalCupertinoApi::class)
@Composable
actual fun HigDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier,
    offset: DpOffset,
    paddingValues: PaddingValues,
    containerColor: Color,
    width: Dp,
    scrollState: ScrollState,
    properties: PopupProperties,
    backdrop: Backdrop,
    items: List<DropdownMenuSection>
) {
    CupertinoDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        offset = offset,
        paddingValues = paddingValues,
        containerColor = containerColor,
        width = width,
        scrollState = scrollState,
        properties = properties,
        backdrop = backdrop,
        content = {
            items.forEachIndexed { index, section ->
                if (index != 0) {
                    MenuDivider()
                }
                MenuSection(
                    title = section.title?.let { { Text(text = it) } },
                ) {
                    section.items.filter { it.visible }.forEach { action ->
                        MenuAction(
                            onClick = action.onClick,
                            modifier = action.modifier,
                            leadingIcon = action.icon?.let {
                                {
                                    ComplexIcon(
                                        icon = it,
                                        contentDescription = action.text
                                    )
                                }
                            },
                            trailingIcon = if (action.badge != 0) {
                                {
                                    Badge(
                                        content = if (action.badge > 0) {{ Text(text = action.badge.toString()) }} else null,
                                        containerColor = CupertinoColors.systemRed,
                                        contentColor = Color.White
                                    )
                                }
                            } else null,
                            title = { Text(text = action.text) },
                            enabled = action.enabled
                        )
                    }
                }
            }
        }
    )
}