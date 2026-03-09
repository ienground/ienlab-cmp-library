package zone.ien.utils.adaptive.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.kyant.backdrop.Backdrop
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.hig.CupertinoDropdownMenu
import zone.ien.hig.CupertinoDropdownMenuDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.MenuAction
import zone.ien.hig.MenuDivider
import zone.ien.hig.MenuSection
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import kotlin.math.exp

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
                modifier = modifier.graphicsLayer { clip = false }
            ) {
                val alpha by animateFloatAsState(
                    targetValue = if (expanded) 0.1f else 1f,
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
    items: List<DropdownMenuSection>,
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
                    items.forEachIndexed { index, section ->
                        if (index != 0) {
                            HorizontalDivider()
                        }
                        section.items.filter { it.visible }.forEach { action ->
                            DropdownMenuItem(
                                text = action.text,
                                onClick = action.onClick,
                                modifier = action.modifier,
                                leadingIcon = action.icon,
                                enabled = action.enabled
                            )
                        }
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
                    items.forEachIndexed { index, section ->
                        if (index != 0) {
                            MenuDivider()
                        }
                        MenuSection(
                            title = section.title,
                        ) {
                            section.items.filter { it.visible }.forEach { action ->
                                MenuAction(
                                    onClick = action.onClick,
                                    modifier = action.modifier,
                                    icon = action.icon,
                                    title = action.text,
                                    enabled = action.enabled
                                )
                            }
                        }
                    }
                }
            )
        }
    )
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
        val enabled: Boolean = true
    )
}

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