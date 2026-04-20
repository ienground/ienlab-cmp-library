package zone.ien.utils.adaptive.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.kyant.backdrop.Backdrop
import zone.ien.hig.CupertinoDropdownMenu
import zone.ien.hig.CupertinoDropdownMenuDefaults
import zone.ien.hig.CupertinoDropdownMenuNative
import zone.ien.hig.CupertinoMenuItemData
import zone.ien.hig.CupertinoMenuScope
import zone.ien.hig.CupertinoMenuSectionData
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.MenuAction
import zone.ien.hig.MenuDivider
import zone.ien.hig.MenuSection
import zone.ien.hig.ProvideTextStyle
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.IconData

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveDropdownBox(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    trigger: @Composable () -> Unit,
    dropdown: @Composable () -> Unit
) {
    AdaptiveWidget(
        material = {
            Box(
                modifier = modifier
            ) {
                trigger()
                dropdown()
            }
        },
        cupertino = {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = modifier.graphicsLayer { clip = false }
            ) {
                val alpha by animateFloatAsState(
                    targetValue = if (expanded) 0.001f else 1f,
                    animationSpec = spring(1.2f)
                )

                Box(
                    modifier = Modifier.graphicsLayer {
                        this.scaleX = alpha
                        this.scaleY = alpha
                        this.alpha = alpha
                        compositingStrategy = CompositingStrategy.ModulateAlpha
                    }
                ) {
                    trigger()
                }
                dropdown()
            }
        }
    )
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    scrollState: ScrollState = rememberScrollState(),
    properties: PopupProperties = PopupProperties(clippingEnabled = false),
    adaptation: AdaptationScope<HigDropdownMenuAdaptation, M3DropdownMenuAdaptation>.() -> Unit = {},
    items: List<DropdownMenuSection.Action>,
    sections: List<DropdownMenuSection>
) {
    AdaptiveWidget(
        adaptation = remember { DropdownMenuAdaptation() },
        adaptationScope = adaptation,
        material = {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                modifier = modifier,
                offset = offset,
                scrollState = scrollState,
                properties = properties,
                shape = it.shape,
                containerColor = it.containerColor,
                tonalElevation = it.tonalElevation,
                shadowElevation = it.shadowElevation,
                border = it.border,
                content = {
                    items.filter { it.visible }.forEach { it.M3Menu() }
                    sections.forEachIndexed { index, section ->
                        if (index != 0 || items.isNotEmpty()) {
                            HorizontalDivider()
                        }
                        section.title?.let { DropdownMenuSectionTitle(it) }
                        section.items.filter { it.visible }.forEach { it.M3Menu() }
                    }
                }
            )
        },
        cupertino = {
            CupertinoDropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                modifier = modifier,
                offset = offset,
                paddingValues = it.paddingValues,
                containerColor = it.containerColor,
                width = it.width,
                scrollState = scrollState,
                properties = properties,
                backdrop = it.backdrop,
                content = {
                    items.filter { it.visible }.forEach { ActionToUIMenu(it) }
                    sections.forEachIndexed { index, section ->
                        if (index != 0 || items.isNotEmpty()) {
                            MenuDivider()
                        }
                        MenuSection(
                            title = section.title,
                        ) {
                            section.items.filter { it.visible }.forEach { ActionToUIMenu(it) }
                        }
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveDropdownMenuNative(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    scrollState: ScrollState = rememberScrollState(),
    properties: PopupProperties = PopupProperties(clippingEnabled = false),
    adaptation: AdaptationScope<HigDropdownMenuAdaptation, M3DropdownMenuAdaptation>.() -> Unit = {},
    items: List<DropdownMenuSectionNative.Action> = listOf(),
    sections: List<DropdownMenuSectionNative> = listOf()
) {
    AdaptiveWidget(
        adaptation = remember { DropdownMenuAdaptation() },
        adaptationScope = adaptation,
        material = {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                modifier = modifier,
                offset = offset,
                scrollState = scrollState,
                properties = properties,
                shape = it.shape,
                containerColor = it.containerColor,
                tonalElevation = it.tonalElevation,
                shadowElevation = it.shadowElevation,
                border = it.border,
                content = {
                    items.filter { it.visible }.forEach { it.M3Menu() }
                    sections.forEachIndexed { index, section ->
                        if (index != 0 || items.isNotEmpty()) {
                            HorizontalDivider()
                        }
                        section.title?.let { DropdownMenuSectionTitle { Text(text = it) } }
                        section.items.filter { it.visible }.forEach { it.M3Menu() }
                    }
                }
            )
        },
        cupertino = {
            CupertinoDropdownMenuNative(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                modifier = modifier,
                offset = offset,
                paddingValues = it.paddingValues,
                containerColor = it.containerColor,
                width = it.width,
                scrollState = scrollState,
                properties = properties,
                backdrop = it.backdrop,
                items = items.filter { it.visible }.map { it.toUIMenu() },
                sections = sections.map { it.toUISection() }
            )
        }
    )
}

@Composable
private fun DropdownMenuSectionTitle(
    title: @Composable () -> Unit
) {
    ProvideTextStyle(
        value = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 12.dp)
        ) {
            title()
        }
    }
}

data class DropdownMenuSection(
    val title: (@Composable () -> Unit)? = null,
    val items: List<Action>
) {
    data class Action(
        val text: @Composable () -> Unit,
        val onClick: () -> Unit,
        val modifier: Modifier = Modifier,
        val icon: @Composable () -> Unit = {},
        val visible: Boolean = true,
        val enabled: Boolean = true,
        val isDestructive: Boolean = false
    )
}

data class DropdownMenuSectionNative(
    val title: String? = null,
    val items: List<Action>
) {
    data class Action(
        val text: String,
        val onClick: () -> Unit,
        val modifier: Modifier = Modifier,
        val icon: IconData? = null,
        val visible: Boolean = true,
        val enabled: Boolean = true,
        val isDestructive: Boolean = false
    )
}

@Composable
private fun DropdownMenuSection.Action.M3Menu() = DropdownMenuItem(
    text = text,
    onClick = onClick,
    modifier = modifier,
    leadingIcon = icon,
    enabled = enabled
)

@Composable
private fun CupertinoMenuScope.ActionToUIMenu(action: DropdownMenuSection.Action) {
    MenuAction(
        onClick = action.onClick,
        modifier = action.modifier,
        leadingIcon = action.icon,
        title = action.text,
        enabled = action.enabled
    )
}

@Composable
private fun DropdownMenuSectionNative.Action.M3Menu() = DropdownMenuItem(
    text = { Text(text = text) },
    onClick = onClick,
    modifier = modifier,
    leadingIcon = icon?.let { { ComplexIcon(icon = it) } },
    enabled = enabled,
    colors = if (isDestructive) MenuDefaults.itemColors(textColor = MaterialTheme.colorScheme.error, leadingIconColor = MaterialTheme.colorScheme.error) else MenuDefaults.itemColors()
)

@Composable
private fun DropdownMenuSectionNative.toUISection() = CupertinoMenuSectionData(
    title = title.orEmpty(),
    items = items.filter { it.visible }.map { it.toUIMenu() }
)

@Composable
private fun DropdownMenuSectionNative.Action.toUIMenu() = CupertinoMenuItemData(
    title = text,
    onClick = onClick,
    enabled = enabled,
    icon = when (icon) {
        is IconData.Vector -> rememberVectorPainter(icon.imageVector)
        is IconData.Paint -> icon.painter
        null -> null
    },
    isDestructive = isDestructive
)

class M3DropdownMenuAdaptation internal constructor(
    shape: Shape,
    containerColor: Color,
    tonalElevation: Dp,
    shadowElevation: Dp,
    border: BorderStroke?
) {
    var shape by mutableStateOf(shape)
    var containerColor by mutableStateOf(containerColor)
    var tonalElevation by mutableStateOf(tonalElevation)
    var shadowElevation by mutableStateOf(shadowElevation)
    var border by mutableStateOf(border)
}

class HigDropdownMenuAdaptation internal constructor(
    paddingValues: PaddingValues,
    containerColor: Color,
    width: Dp,
    backdrop: Backdrop
) {
    var paddingValues by mutableStateOf(paddingValues)
    var containerColor by mutableStateOf(containerColor)
    var width by mutableStateOf(width)
    var backdrop by mutableStateOf(backdrop)
}

@OptIn(ExperimentalAdaptiveApi::class)
private class DropdownMenuAdaptation: Adaptation<HigDropdownMenuAdaptation, M3DropdownMenuAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigDropdownMenuAdaptation {
        val paddingValues = CupertinoDropdownMenuDefaults.PaddingValues
        val containerColor = CupertinoDropdownMenuDefaults.ContainerColor
        val width = CupertinoDropdownMenuDefaults.DefaultWidth
        val backdrop = rememberDefaultBackdrop()

        return remember(paddingValues, containerColor, width, backdrop) {
            HigDropdownMenuAdaptation(
                paddingValues = paddingValues,
                containerColor = containerColor,
                width = width,
                backdrop = backdrop
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): M3DropdownMenuAdaptation {
        val shape = MenuDefaults.shape
        val containerColor = MenuDefaults.containerColor
        val tonalElevation = MenuDefaults.TonalElevation
        val shadowElevation = MenuDefaults.ShadowElevation
        val border: BorderStroke? = null

        return remember(shape, containerColor, tonalElevation, shadowElevation, border) {
            M3DropdownMenuAdaptation(
                shape = shape,
                containerColor = containerColor,
                tonalElevation = tonalElevation,
                shadowElevation = shadowElevation,
                border = border
            )
        }
    }
}