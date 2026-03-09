package zone.ien.utils.adaptive.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.kyant.backdrop.Backdrop
import zone.ien.hig.utils.rememberDefaultLayerBackdrop
import zone.ien.hig.CupertinoLargeFloatingActionButton
import zone.ien.hig.CupertinoLiquidButtonColors
import zone.ien.hig.CupertinoLiquidButtonDefaults
import zone.ien.hig.CupertinoMediumFloatingActionButton
import zone.ien.hig.CupertinoSmallFloatingActionButton
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveSmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<HigFloatingActionButtonAdaptation, M3FloatingActionButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { SmallFloatingActionButtonAdaptation() },
        adaptationScope = adaptation,
        material = {
            SmallFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                shape = it.shape,
                containerColor = it.containerColor,
                contentColor = it.contentColor,
                elevation = it.elevation,
                interactionSource = it.interactionSource,
                content = content
            )
        },
        cupertino = {
            CupertinoSmallFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                colors = it.colors,
                shape = it.shape,
                interactionSource = it.interactionSource,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                content = content
            )
        }
    )
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AdaptiveMediumFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<HigFloatingActionButtonAdaptation, M3FloatingActionButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { MediumFloatingActionButtonAdaptation() },
        adaptationScope = adaptation,
        material = {
            MediumFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                shape = it.shape,
                containerColor = it.containerColor,
                contentColor = it.contentColor,
                elevation = it.elevation,
                interactionSource = it.interactionSource,
                content = content
            )
        },
        cupertino = {
            CupertinoMediumFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                colors = it.colors,
                shape = it.shape,
                interactionSource = it.interactionSource,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                content = content
            )
        }
    )
}

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveLargeFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<HigFloatingActionButtonAdaptation, M3FloatingActionButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { LargeFloatingActionButtonAdaptation() },
        adaptationScope = adaptation,
        material = {
            LargeFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                shape = it.shape,
                containerColor = it.containerColor,
                contentColor = it.contentColor,
                elevation = it.elevation,
                interactionSource = it.interactionSource,
                content = content
            )
        },
        cupertino = {
            CupertinoLargeFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                colors = it.colors,
                shape = it.shape,
                interactionSource = it.interactionSource,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                content = content
            )
        }
    )
}

class M3FloatingActionButtonAdaptation internal constructor(
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    elevation: FloatingActionButtonElevation,
    interactionSource: MutableInteractionSource? = null
) {
    var shape: Shape by mutableStateOf(shape)
    var containerColor: Color by mutableStateOf(containerColor)
    var contentColor: Color by mutableStateOf(contentColor)
    var elevation: FloatingActionButtonElevation by mutableStateOf(elevation)
    var interactionSource: MutableInteractionSource? by mutableStateOf(interactionSource)
}

class HigFloatingActionButtonAdaptation internal constructor(
    colors: CupertinoLiquidButtonColors,
    shape: Shape,
    interactionSource: MutableInteractionSource,
    backdrop: Backdrop,
    isBackgroundAdaptive: Boolean = true
) {
    var colors: CupertinoLiquidButtonColors by mutableStateOf(colors)
    var shape: Shape by mutableStateOf(shape)
    var interactionSource: MutableInteractionSource by mutableStateOf(interactionSource)
    var backdrop: Backdrop by mutableStateOf(backdrop)
    var isBackgroundAdaptive: Boolean by mutableStateOf(isBackgroundAdaptive)
}

@OptIn(ExperimentalAdaptiveApi::class)
private class SmallFloatingActionButtonAdaptation: Adaptation<HigFloatingActionButtonAdaptation, M3FloatingActionButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigFloatingActionButtonAdaptation {
        val colors = CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        val shape = CircleShape
        val interactionSource = remember { MutableInteractionSource() }
        val backdrop = rememberDefaultLayerBackdrop()
        val isBackgroundAdaptive = true

        return remember(colors, shape, interactionSource, backdrop, isBackgroundAdaptive) {
            HigFloatingActionButtonAdaptation(
                colors = colors,
                shape = shape,
                interactionSource = interactionSource,
                backdrop = backdrop,
                isBackgroundAdaptive = isBackgroundAdaptive
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): M3FloatingActionButtonAdaptation {
        val shape = FloatingActionButtonDefaults.smallShape
        val containerColor = FloatingActionButtonDefaults.containerColor
        val contentColor = contentColorFor(containerColor)
        val elevation = FloatingActionButtonDefaults.elevation()
        val interactionSource: MutableInteractionSource? = null

        return remember(shape, containerColor, contentColor, elevation, interactionSource) {
            M3FloatingActionButtonAdaptation(
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                elevation = elevation,
                interactionSource = interactionSource
            )
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class)
private class MediumFloatingActionButtonAdaptation: Adaptation<HigFloatingActionButtonAdaptation, M3FloatingActionButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigFloatingActionButtonAdaptation {
        val colors = CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        val shape = CircleShape
        val interactionSource = remember { MutableInteractionSource() }
        val backdrop = rememberDefaultLayerBackdrop()
        val isBackgroundAdaptive = true

        return remember(colors, shape, interactionSource, backdrop, isBackgroundAdaptive) {
            HigFloatingActionButtonAdaptation(
                colors = colors,
                shape = shape,
                interactionSource = interactionSource,
                backdrop = backdrop,
                isBackgroundAdaptive = isBackgroundAdaptive
            )
        }
    }

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    override fun rememberMaterialAdaptation(): M3FloatingActionButtonAdaptation {
        val shape = FloatingActionButtonDefaults.mediumShape
        val containerColor = FloatingActionButtonDefaults.containerColor
        val contentColor = contentColorFor(containerColor)
        val elevation = FloatingActionButtonDefaults.elevation()
        val interactionSource: MutableInteractionSource? = null

        return remember(shape, containerColor, contentColor, elevation, interactionSource) {
            M3FloatingActionButtonAdaptation(
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                elevation = elevation,
                interactionSource = interactionSource
            )
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class)
private class LargeFloatingActionButtonAdaptation: Adaptation<HigFloatingActionButtonAdaptation, M3FloatingActionButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigFloatingActionButtonAdaptation {
        val colors = CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        val shape = CircleShape
        val interactionSource = remember { MutableInteractionSource() }
        val backdrop = rememberDefaultLayerBackdrop()
        val isBackgroundAdaptive = true

        return remember(colors, shape, interactionSource, backdrop, isBackgroundAdaptive) {
            HigFloatingActionButtonAdaptation(
                colors = colors,
                shape = shape,
                interactionSource = interactionSource,
                backdrop = backdrop,
                isBackgroundAdaptive = isBackgroundAdaptive
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): M3FloatingActionButtonAdaptation {
        val shape = FloatingActionButtonDefaults.largeShape
        val containerColor = FloatingActionButtonDefaults.containerColor
        val contentColor = contentColorFor(containerColor)
        val elevation = FloatingActionButtonDefaults.elevation()
        val interactionSource: MutableInteractionSource? = null

        return remember(shape, containerColor, contentColor, elevation, interactionSource) {
            M3FloatingActionButtonAdaptation(
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                elevation = elevation,
                interactionSource = interactionSource
            )
        }
    }
}