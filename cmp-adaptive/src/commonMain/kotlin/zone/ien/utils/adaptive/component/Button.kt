package zone.ien.utils.adaptive.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import zone.ien.hig.CupertinoButtonSize
import zone.ien.hig.CupertinoLiquidButton
import zone.ien.hig.CupertinoLiquidButtonColors
import zone.ien.hig.CupertinoLiquidButtonDefaults
import zone.ien.hig.CupertinoLiquidIconButton
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.LocalBackButtonIcon
import zone.ien.utils.icon.LocalButtonProviderDefault
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonColors
import zone.ien.utils.ui.interactive.IenButtonDefault
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenExtendedFab
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.interactive.IenIconToggleButton
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.interactive.IenTextButtonSize
import zone.ien.utils.ui.interactive.IenTextButtonVariant
import zone.ien.utils.ui.interactive.IenToggleButton
import zone.ien.utils.ui.interactive.IenToggleButtonColors
import zone.ien.utils.ui.interactive.IenToggleButtonDefault
import zone.ien.utils.ui.interactive.IenToggleButtonShapes
import zone.ien.utils.ui.interactive.IenToggleButtonVariants
import zone.ien.utils.ui.screen.IenBackButton

/**
 * 현재 플랫폼에 맞는 버튼 구현을 선택하는 적응형 기본 버튼 컴포저블입니다.
 *
 * Material 분기에서는 [IenButton]을, Cupertino 분기에서는 compose-hig의 버튼을 사용합니다.
 *
 * @param onClick 버튼 클릭 시 실행할 콜백 함수입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param size Material 분기 버튼 크기 규격입니다.
 * @param variant Material 분기 버튼 스타일 변형입니다.
 * @param tone Material 분기 색상에 반영할 의미적 톤입니다.
 * @param state 버튼의 활성화 및 로딩 상태입니다.
 * @param shape Material 분기 버튼 형태입니다.
 * @param contentPadding Material 분기 버튼 내부 여백입니다.
 * @param colors Material 분기 버튼 색상과 배경 브러시입니다.
 * @param interactionSource 버튼 상호작용 상태를 전달하는 [MutableInteractionSource]입니다.
 * @param display Material 분기 버튼의 가로 배치 방식입니다.
 * @param adaptation 플랫폼별 버튼 값을 덮어쓰기 위한 설정 블록입니다.
 * @param content 버튼 내부에 표시할 컴포저블 콘텐츠입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    colors: IenButtonColors = IenButtonDefault.colors(variant = variant, tone = tone),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    display: IenButtonDisplay = IenButtonDisplay.Inline,
    adaptation: AdaptationScope<HigButtonAdaptation, IenButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember(size, variant, tone, state, shape, contentPadding, colors, display) {
            ButtonAdaptation(
                type = AdaptiveButtonType.Filled,
                size = size,
                variant = variant,
                tone = tone,
                state = state,
                shape = shape,
                contentPadding = contentPadding,
                colors = colors,
                display = display,
            )
        },
        adaptationScope = adaptation,
        material = {
            IenButton(
                onClick = onClick,
                modifier = modifier,
                size = it.size,
                variant = it.variant,
                tone = it.tone,
                state = it.state,
                shape = it.shape,
                contentPadding = it.contentPadding,
                colors = it.colors,
                interactionSource = interactionSource,
                display = it.display,
                content = content,
            )
        },
        cupertino = {
            CupertinoLiquidButton(
                onClick = onClick,
                modifier = modifier,
                enabled = it.state.enabled && !it.state.loading,
                interactionSource = interactionSource,
                size = it.size,
                contentPadding = it.contentPadding ?: it.size.contentPadding,
                shape = it.shape ?: it.size.shape(CupertinoTheme.shapes),
                colors = it.colors,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                isInteractive = it.isInteractive,
            ) {
                content()
            }
        },
    )
}

/**
 * 현재 플랫폼에 맞는 텍스트 버튼 구현을 선택하는 적응형 텍스트 버튼 컴포저블입니다.
 *
 * @param onClick 버튼 클릭 시 실행할 콜백 함수입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param size Material 분기 텍스트 버튼 크기 규격입니다.
 * @param variant Material 분기 텍스트 버튼 표시 방식입니다.
 * @param tone Material 분기 텍스트 색상에 반영할 의미적 톤입니다.
 * @param state 버튼의 활성화 및 로딩 상태입니다.
 * @param colors Material 분기 버튼 색상입니다.
 * @param interactionSource 버튼 상호작용 상태를 전달하는 [MutableInteractionSource]입니다.
 * @param adaptation 플랫폼별 버튼 값을 덮어쓰기 위한 설정 블록입니다.
 * @param content 버튼 내부에 표시할 컴포저블 콘텐츠입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenTextButtonSize = IenTextButtonSize.Medium,
    variant: IenTextButtonVariant = IenTextButtonVariant.Clear,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    colors: IenButtonColors = IenButtonDefault.textColors(tone = tone),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    adaptation: AdaptationScope<HigButtonAdaptation, IenTextButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember(size, variant, tone, state, colors) {
            TextButtonAdaptation(
                size = size,
                variant = variant,
                tone = tone,
                state = state,
                colors = colors,
            )
        },
        adaptationScope = adaptation,
        material = {
            IenTextButton(
                onClick = onClick,
                modifier = modifier,
                size = it.size,
                variant = it.variant,
                tone = it.tone,
                state = it.state,
                colors = it.colors,
                interactionSource = interactionSource,
                content = content,
            )
        },
        cupertino = {
            CupertinoLiquidButton(
                onClick = onClick,
                modifier = modifier,
                enabled = it.state.enabled && !it.state.loading,
                interactionSource = interactionSource,
                size = it.size,
                contentPadding = it.contentPadding ?: it.size.contentPadding,
                shape = it.shape ?: it.size.shape(CupertinoTheme.shapes),
                colors = it.colors,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                isInteractive = it.isInteractive,
            ) {
                content()
            }
        },
    )
}

/**
 * 현재 플랫폼에 맞는 톤 버튼 구현을 선택하는 적응형 버튼 컴포저블입니다.
 *
 * Material 분기에서는 [IenButtonVariant.Weak] 기반 [IenButton]을, Cupertino 분기에서는 glass prominent 버튼을 사용합니다.
 *
 * @param onClick 버튼 클릭 시 실행할 콜백 함수입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param size Material 분기 버튼 크기 규격입니다.
 * @param tone Material 분기 색상에 반영할 의미적 톤입니다.
 * @param state 버튼의 활성화 및 로딩 상태입니다.
 * @param shape Material 분기 버튼 형태입니다.
 * @param contentPadding Material 분기 버튼 내부 여백입니다.
 * @param colors Material 분기 버튼 색상과 배경 브러시입니다.
 * @param interactionSource 버튼 상호작용 상태를 전달하는 [MutableInteractionSource]입니다.
 * @param display Material 분기 버튼의 가로 배치 방식입니다.
 * @param adaptation 플랫폼별 버튼 값을 덮어쓰기 위한 설정 블록입니다.
 * @param content 버튼 내부에 표시할 컴포저블 콘텐츠입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    colors: IenButtonColors = IenButtonDefault.colors(variant = IenButtonVariant.Weak, tone = tone),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    display: IenButtonDisplay = IenButtonDisplay.Inline,
    adaptation: AdaptationScope<HigButtonAdaptation, IenButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember(size, tone, state, shape, contentPadding, colors, display) {
            ButtonAdaptation(
                type = AdaptiveButtonType.Tonal,
                size = size,
                variant = IenButtonVariant.Weak,
                tone = tone,
                state = state,
                shape = shape,
                contentPadding = contentPadding,
                colors = colors,
                display = display,
            )
        },
        adaptationScope = adaptation,
        material = {
            IenButton(
                onClick = onClick,
                modifier = modifier,
                size = it.size,
                variant = it.variant,
                tone = it.tone,
                state = it.state,
                shape = it.shape,
                contentPadding = it.contentPadding,
                colors = it.colors,
                interactionSource = interactionSource,
                display = it.display,
                content = content,
            )
        },
        cupertino = {
            CupertinoLiquidButton(
                onClick = onClick,
                modifier = modifier,
                enabled = it.state.enabled && !it.state.loading,
                interactionSource = interactionSource,
                size = it.size,
                contentPadding = it.contentPadding ?: it.size.contentPadding,
                shape = it.shape ?: it.size.shape(CupertinoTheme.shapes),
                colors = it.colors,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                isInteractive = it.isInteractive,
            ) {
                content()
            }
        },
    )
}

/**
 * Material 분기에서 [IenIconButton]을 사용하는 적응형 아이콘 버튼 컴포저블.
 *
 * @param onClick 버튼 클릭 시 실행할 콜백 함수입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param size Material 분기 아이콘 버튼 크기 규격입니다.
 * @param variant Material 분기 버튼 스타일 변형입니다.
 * @param tone Material 분기 색상에 반영할 의미적 톤입니다.
 * @param state 버튼의 활성화 및 로딩 상태입니다.
 * @param shape Material 분기 버튼 형태입니다.
 * @param colors Material 분기 버튼 색상과 배경 브러시입니다.
 * @param interactionSource 버튼 상호작용 상태를 전달하는 [MutableInteractionSource]입니다.
 * @param adaptation 플랫폼별 아이콘 버튼 값을 덮어쓰기 위한 설정 블록입니다.
 * @param content 버튼 내부에 표시할 아이콘 컴포저블 콘텐츠입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Ghost,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    colors: IenButtonColors = IenButtonDefault.colors(variant = variant, tone = tone),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    adaptation: AdaptationScope<HigIconButtonAdaptation, IenIconButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember(size, variant, tone, state, shape, colors) {
            IconButtonAdaptation(
                isFilled = false,
                size = size,
                variant = variant,
                tone = tone,
                state = state,
                shape = shape,
                colors = colors,
            )
        },
        adaptationScope = adaptation,
        material = {
            IenIconButton(
                onClick = onClick,
                modifier = modifier,
                size = it.size,
                variant = it.variant,
                tone = it.tone,
                state = it.state,
                shape = it.shape,
                colors = it.colors,
                interactionSource = interactionSource,
                content = content,
            )
        },
        cupertino = {
            CupertinoLiquidIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = it.state.enabled && !it.state.loading,
                interactionSource = interactionSource,
                colors = it.colors,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                isInteractive = it.isInteractive,
                content = content,
            )
        },
    )
}

/**
 * Material 분기에서 채움 스타일 [IenIconButton]을 사용하는 적응형 아이콘 버튼 컴포저블.
 *
 * @param onClick 버튼 클릭 시 실행할 콜백 함수입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param size Material 분기 아이콘 버튼 크기 규격입니다.
 * @param tone Material 분기 색상에 반영할 의미적 톤입니다.
 * @param state 버튼의 활성화 및 로딩 상태입니다.
 * @param shape Material 분기 버튼 형태입니다.
 * @param colors Material 분기 버튼 색상과 배경 브러시입니다.
 * @param interactionSource 버튼 상호작용 상태를 전달하는 [MutableInteractionSource]입니다.
 * @param adaptation 플랫폼별 아이콘 버튼 값을 덮어쓰기 위한 설정 블록입니다.
 * @param content 버튼 내부에 표시할 아이콘 컴포저블 콘텐츠입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveFilledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    colors: IenButtonColors = IenButtonDefault.colors(variant = IenButtonVariant.Fill, tone = tone),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    adaptation: AdaptationScope<HigIconButtonAdaptation, IenIconButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember(size, tone, state, shape, colors) {
            IconButtonAdaptation(
                isFilled = true,
                size = size,
                variant = IenButtonVariant.Fill,
                tone = tone,
                state = state,
                shape = shape,
                colors = colors,
            )
        },
        adaptationScope = adaptation,
        material = {
            IenIconButton(
                onClick = onClick,
                modifier = modifier,
                size = it.size,
                variant = it.variant,
                tone = it.tone,
                state = it.state,
                shape = it.shape,
                colors = it.colors,
                interactionSource = interactionSource,
                content = content,
            )
        },
        cupertino = {
            CupertinoLiquidIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = it.state.enabled && !it.state.loading,
                interactionSource = interactionSource,
                colors = it.colors,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                isInteractive = it.isInteractive,
                content = content,
            )
        },
    )
}

/**
 * 현재 플랫폼에 맞는 토글 버튼 구현을 선택하는 적응형 토글 버튼 컴포저블입니다.
 *
 * Material 분기에서는 [IenToggleButton]을 사용하고, Cupertino 분기에서는 선택 상태에 따라 tonal/fill 버튼을 전환합니다.
 *
 * @param checked 현재 선택 상태입니다.
 * @param onCheckedChange 선택 상태 변경 콜백입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param size Material 분기 버튼 크기 규격입니다.
 * @param state 버튼의 활성화 및 로딩 상태입니다.
 * @param shapes Material 분기 선택/비선택 상태별 버튼 형태 구성입니다.
 * @param variants Material 분기 선택/비선택 상태별 스타일 변형 구성입니다.
 * @param colors Material 분기 선택/비선택 상태별 색상 구성입니다.
 * @param contentPadding Material 분기 버튼 내부 여백입니다.
 * @param interactionSource 버튼 상호작용 상태를 전달하는 [MutableInteractionSource]입니다.
 * @param display Material 분기 버튼의 가로 배치 방식입니다.
 * @param adaptation 플랫폼별 토글 버튼 값을 덮어쓰기 위한 설정 블록입니다.
 * @param content 버튼 내부에 표시할 컴포저블 콘텐츠입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    state: IenButtonState = IenButtonState(),
    shapes: IenToggleButtonShapes = IenToggleButtonDefault.shapes(),
    variants: IenToggleButtonVariants = IenToggleButtonDefault.variants(),
    colors: IenToggleButtonColors = IenToggleButtonDefault.colors(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    display: IenButtonDisplay = IenButtonDisplay.Inline,
    adaptation: AdaptationScope<HigToggleButtonAdaptation, IenToggleButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember(size, state, shapes, variants, colors, contentPadding, display) {
            ToggleButtonAdaptation(
                size = size,
                state = state,
                shapes = shapes,
                variants = variants,
                colors = colors,
                contentPadding = contentPadding,
                display = display,
            )
        },
        adaptationScope = adaptation,
        material = {
            IenToggleButton(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                size = it.size,
                state = it.state,
                shapes = it.shapes,
                variants = it.variants,
                colors = it.colors,
                contentPadding = it.contentPadding,
                interactionSource = interactionSource,
                display = it.display,
                content = content,
            )
        },
        cupertino = {
            val onClick = { onCheckedChange(!checked) }
            val hig = if (checked) it.checked else it.unchecked
            CupertinoLiquidButton(
                onClick = onClick,
                modifier = modifier,
                enabled = it.state.enabled && !it.state.loading,
                interactionSource = interactionSource,
                size = hig.size,
                contentPadding = hig.contentPadding ?: hig.size.contentPadding,
                shape = hig.shape ?: hig.size.shape(CupertinoTheme.shapes),
                colors = hig.colors,
                backdrop = hig.backdrop,
                isBackgroundAdaptive = hig.isBackgroundAdaptive,
                isInteractive = hig.isInteractive,
            ) {
                content()
            }
        },
    )
}

/**
 * Material 분기에서 [IenIconToggleButton]을 사용하는 적응형 아이콘 토글 버튼 컴포저블.
 *
 * @param checked 현재 선택 상태입니다.
 * @param onCheckedChange 선택 상태 변경 콜백입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param size Material 분기 버튼 크기 규격입니다.
 * @param state 버튼의 활성화 및 로딩 상태입니다.
 * @param shapes Material 분기 선택/비선택 상태별 버튼 형태 구성입니다.
 * @param variants Material 분기 선택/비선택 상태별 스타일 변형 구성입니다.
 * @param colors Material 분기 선택/비선택 상태별 색상 구성입니다.
 * @param interactionSource 버튼 상호작용 상태를 전달하는 [MutableInteractionSource]입니다.
 * @param adaptation 플랫폼별 아이콘 토글 버튼 값을 덮어쓰기 위한 설정 블록입니다.
 * @param content 버튼 내부에 표시할 아이콘 컴포저블 콘텐츠입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    state: IenButtonState = IenButtonState(),
    shapes: IenToggleButtonShapes = IenToggleButtonDefault.shapes(),
    variants: IenToggleButtonVariants = IenToggleButtonDefault.variants(),
    colors: IenToggleButtonColors = IenToggleButtonDefault.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    adaptation: AdaptationScope<HigIconToggleButtonAdaptation, IenIconToggleButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember(size, state, shapes, variants, colors) {
            IconToggleButtonAdaptation(
                size = size,
                state = state,
                shapes = shapes,
                variants = variants,
                colors = colors,
            )
        },
        adaptationScope = adaptation,
        material = {
            IenIconToggleButton(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                size = it.size,
                state = it.state,
                shapes = it.shapes,
                variants = it.variants,
                colors = it.colors,
                interactionSource = interactionSource,
                content = content,
            )
        },
        cupertino = {
            val onClick = { onCheckedChange(!checked) }
            val hig = if (checked) it.checked else it.unchecked
            CupertinoLiquidIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = it.state.enabled && !it.state.loading,
                interactionSource = interactionSource,
                colors = hig.colors,
                backdrop = hig.backdrop,
                isBackgroundAdaptive = hig.isBackgroundAdaptive,
                isInteractive = hig.isInteractive,
                content = content,
            )
        },
    )
}

/**
 * 현재 플랫폼에 맞는 확장형 플로팅 액션 버튼 구현을 선택하는 적응형 컴포저블입니다.
 *
 * Material 분기에서는 [IenExtendedFab]을, Cupertino 분기에서는 tonal 버튼을 사용합니다.
 *
 * @param onClick 버튼 클릭 시 실행할 콜백 함수입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param variant Material 분기 버튼 스타일 변형입니다.
 * @param tone Material 분기 색상에 반영할 의미적 톤입니다.
 * @param state 버튼의 활성화 및 로딩 상태입니다.
 * @param shape Material 분기 버튼 형태입니다.
 * @param contentPadding Material 분기 버튼 내부 여백입니다.
 * @param colors Material 분기 버튼 색상과 배경 브러시입니다.
 * @param interactionSource 버튼 상호작용 상태를 전달하는 [MutableInteractionSource]입니다.
 * @param adaptation 플랫폼별 확장형 FAB 값을 덮어쓰기 위한 설정 블록입니다.
 * @param content 버튼 내부에 표시할 컴포저블 콘텐츠입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveExtendedFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = ContinuousCapsule(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    colors: IenButtonColors = IenButtonDefault.colors(variant = variant, tone = tone),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    adaptation: AdaptationScope<HigButtonAdaptation, IenExtendedFloatingActionButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember(variant, tone, state, shape, contentPadding, colors) {
            ExtendedFloatingActionButtonAdaptation(
                variant = variant,
                tone = tone,
                state = state,
                shape = shape,
                contentPadding = contentPadding,
                colors = colors,
            )
        },
        adaptationScope = adaptation,
        material = {
            IenExtendedFab(
                onClick = onClick,
                modifier = modifier,
                variant = it.variant,
                tone = it.tone,
                state = it.state,
                shape = it.shape,
                contentPadding = it.contentPadding,
                colors = it.colors,
                interactionSource = interactionSource,
                content = content,
            )
        },
        cupertino = {
            CupertinoLiquidButton(
                onClick = onClick,
                modifier = modifier,
                enabled = it.state.enabled && !it.state.loading,
                interactionSource = interactionSource,
                size = it.size,
                contentPadding = it.contentPadding ?: it.size.contentPadding,
                shape = it.shape ?: it.size.shape(CupertinoTheme.shapes),
                colors = it.colors,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                isInteractive = it.isInteractive,
            ) {
                content()
            }
        },
    )
}

/**
 * IEN 기본 버튼 Material 분기의 적응형 설정입니다.
 */
@Stable
class IenButtonAdaptation internal constructor(
    size: IenButtonSize,
    variant: IenButtonVariant,
    tone: IenSemanticTone,
    state: IenButtonState,
    shape: Shape,
    contentPadding: PaddingValues,
    colors: IenButtonColors,
    display: IenButtonDisplay,
) {
    var size: IenButtonSize by mutableStateOf(size)
    var variant: IenButtonVariant by mutableStateOf(variant)
    var tone: IenSemanticTone by mutableStateOf(tone)
    var state: IenButtonState by mutableStateOf(state)
    var shape: Shape by mutableStateOf(shape)
    var contentPadding: PaddingValues by mutableStateOf(contentPadding)
    var colors: IenButtonColors by mutableStateOf(colors)
    var display: IenButtonDisplay by mutableStateOf(display)
}

/**
 * IEN 텍스트 버튼 Material 분기의 적응형 설정입니다.
 */
@Stable
class IenTextButtonAdaptation internal constructor(
    size: IenTextButtonSize,
    variant: IenTextButtonVariant,
    tone: IenSemanticTone,
    state: IenButtonState,
    colors: IenButtonColors,
) {
    var size: IenTextButtonSize by mutableStateOf(size)
    var variant: IenTextButtonVariant by mutableStateOf(variant)
    var tone: IenSemanticTone by mutableStateOf(tone)
    var state: IenButtonState by mutableStateOf(state)
    var colors: IenButtonColors by mutableStateOf(colors)
}

/**
 * IEN 아이콘 버튼 Material 분기의 적응형 설정입니다.
 */
@Stable
class IenIconButtonAdaptation internal constructor(
    size: IenButtonSize,
    variant: IenButtonVariant,
    tone: IenSemanticTone,
    state: IenButtonState,
    shape: Shape,
    colors: IenButtonColors,
) {
    var size: IenButtonSize by mutableStateOf(size)
    var variant: IenButtonVariant by mutableStateOf(variant)
    var tone: IenSemanticTone by mutableStateOf(tone)
    var state: IenButtonState by mutableStateOf(state)
    var shape: Shape by mutableStateOf(shape)
    var colors: IenButtonColors by mutableStateOf(colors)
}

/**
 * IEN 토글 버튼 Material 분기의 적응형 설정입니다.
 */
@Stable
class IenToggleButtonAdaptation internal constructor(
    size: IenButtonSize,
    state: IenButtonState,
    shapes: IenToggleButtonShapes,
    variants: IenToggleButtonVariants,
    colors: IenToggleButtonColors,
    contentPadding: PaddingValues,
    display: IenButtonDisplay,
) {
    var size: IenButtonSize by mutableStateOf(size)
    var state: IenButtonState by mutableStateOf(state)
    var shapes: IenToggleButtonShapes by mutableStateOf(shapes)
    var variants: IenToggleButtonVariants by mutableStateOf(variants)
    var colors: IenToggleButtonColors by mutableStateOf(colors)
    var contentPadding: PaddingValues by mutableStateOf(contentPadding)
    var display: IenButtonDisplay by mutableStateOf(display)
}

/**
 * IEN 아이콘 토글 버튼 Material 분기의 적응형 설정입니다.
 */
@Stable
class IenIconToggleButtonAdaptation internal constructor(
    size: IenButtonSize,
    state: IenButtonState,
    shapes: IenToggleButtonShapes,
    variants: IenToggleButtonVariants,
    colors: IenToggleButtonColors,
) {
    var size: IenButtonSize by mutableStateOf(size)
    var state: IenButtonState by mutableStateOf(state)
    var shapes: IenToggleButtonShapes by mutableStateOf(shapes)
    var variants: IenToggleButtonVariants by mutableStateOf(variants)
    var colors: IenToggleButtonColors by mutableStateOf(colors)
}

/**
 * IEN 확장형 FAB Material 분기의 적응형 설정입니다.
 */
@Stable
class IenExtendedFloatingActionButtonAdaptation internal constructor(
    variant: IenButtonVariant,
    tone: IenSemanticTone,
    state: IenButtonState,
    shape: Shape,
    contentPadding: PaddingValues,
    colors: IenButtonColors,
) {
    var variant: IenButtonVariant by mutableStateOf(variant)
    var tone: IenSemanticTone by mutableStateOf(tone)
    var state: IenButtonState by mutableStateOf(state)
    var shape: Shape by mutableStateOf(shape)
    var contentPadding: PaddingValues by mutableStateOf(contentPadding)
    var colors: IenButtonColors by mutableStateOf(colors)
}

/**
 * HIG 액체 버튼 Cupertino 분기의 적응형 설정입니다.
 */
@Stable
class HigButtonAdaptation internal constructor(
    state: IenButtonState,
    colors: CupertinoLiquidButtonColors,
    backdrop: Backdrop,
    isBackgroundAdaptive: Boolean,
) {
    var state: IenButtonState by mutableStateOf(state)
    var colors: CupertinoLiquidButtonColors by mutableStateOf(colors)
    var backdrop: Backdrop by mutableStateOf(backdrop)
    var isBackgroundAdaptive: Boolean by mutableStateOf(isBackgroundAdaptive)
    var isInteractive: Boolean by mutableStateOf(true)
    var size: CupertinoButtonSize by mutableStateOf(CupertinoButtonSize.Regular)
    var shape: Shape? by mutableStateOf(null)
    var contentPadding: PaddingValues? by mutableStateOf(null)
}

/**
 * HIG 액체 아이콘 버튼 Cupertino 분기의 적응형 설정입니다.
 */
@Stable
class HigIconButtonAdaptation internal constructor(
    state: IenButtonState,
    colors: CupertinoLiquidButtonColors,
    backdrop: Backdrop,
    isBackgroundAdaptive: Boolean,
) {
    var state: IenButtonState by mutableStateOf(state)
    var colors: CupertinoLiquidButtonColors by mutableStateOf(colors)
    var backdrop: Backdrop by mutableStateOf(backdrop)
    var isBackgroundAdaptive: Boolean by mutableStateOf(isBackgroundAdaptive)
    var isInteractive: Boolean by mutableStateOf(true)
}

/**
 * HIG 선택/비선택 버튼 Cupertino 분기의 적응형 설정입니다.
 */
@Stable
class HigToggleButtonAdaptation internal constructor(
    state: IenButtonState,
    checked: HigButtonAdaptation,
    unchecked: HigButtonAdaptation,
) {
    var state: IenButtonState by mutableStateOf(state)
    var checked: HigButtonAdaptation by mutableStateOf(checked)
    var unchecked: HigButtonAdaptation by mutableStateOf(unchecked)
}

/**
 * HIG 선택/비선택 아이콘 버튼 Cupertino 분기의 적응형 설정입니다.
 */
@Stable
class HigIconToggleButtonAdaptation internal constructor(
    state: IenButtonState,
    checked: HigIconButtonAdaptation,
    unchecked: HigIconButtonAdaptation,
) {
    var state: IenButtonState by mutableStateOf(state)
    var checked: HigIconButtonAdaptation by mutableStateOf(checked)
    var unchecked: HigIconButtonAdaptation by mutableStateOf(unchecked)
}

private enum class AdaptiveButtonType {
    Filled,
    Text,
    Tonal,
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
private class ButtonAdaptation(
    private val type: AdaptiveButtonType,
    private val size: IenButtonSize,
    private val variant: IenButtonVariant,
    private val tone: IenSemanticTone,
    private val state: IenButtonState,
    private val shape: Shape,
    private val contentPadding: PaddingValues,
    private val colors: IenButtonColors,
    private val display: IenButtonDisplay,
) : Adaptation<HigButtonAdaptation, IenButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigButtonAdaptation {
        val colors = when (type) {
            AdaptiveButtonType.Filled,
            AdaptiveButtonType.Text -> CupertinoLiquidButtonDefaults.glassButtonColors()
            AdaptiveButtonType.Tonal -> CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        }
        val backdrop = rememberLayerBackdrop()

        return remember(state, colors, backdrop) {
            HigButtonAdaptation(
                state = state,
                colors = colors,
                backdrop = backdrop,
                isBackgroundAdaptive = true,
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenButtonAdaptation {
        return remember(size, variant, tone, state, shape, contentPadding, colors, display) {
            IenButtonAdaptation(
                size = size,
                variant = variant,
                tone = tone,
                state = state,
                shape = shape,
                contentPadding = contentPadding,
                colors = colors,
                display = display,
            )
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
private class TextButtonAdaptation(
    private val size: IenTextButtonSize,
    private val variant: IenTextButtonVariant,
    private val tone: IenSemanticTone,
    private val state: IenButtonState,
    private val colors: IenButtonColors,
) : Adaptation<HigButtonAdaptation, IenTextButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigButtonAdaptation {
        val colors = CupertinoLiquidButtonDefaults.glassButtonColors()
        val backdrop = rememberLayerBackdrop()

        return remember(state, colors, backdrop) {
            HigButtonAdaptation(
                state = state,
                colors = colors,
                backdrop = backdrop,
                isBackgroundAdaptive = true,
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenTextButtonAdaptation {
        return remember(size, variant, tone, state, colors) {
            IenTextButtonAdaptation(
                size = size,
                variant = variant,
                tone = tone,
                state = state,
                colors = colors,
            )
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
private class IconButtonAdaptation(
    private val isFilled: Boolean,
    private val size: IenButtonSize,
    private val variant: IenButtonVariant,
    private val tone: IenSemanticTone,
    private val state: IenButtonState,
    private val shape: Shape,
    private val colors: IenButtonColors,
) : Adaptation<HigIconButtonAdaptation, IenIconButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigIconButtonAdaptation {
        val colors = if (isFilled) {
            CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        } else {
            CupertinoLiquidButtonDefaults.glassButtonColors()
        }
        val backdrop = rememberLayerBackdrop()

        return remember(state, colors, backdrop) {
            HigIconButtonAdaptation(
                state = state,
                colors = colors,
                backdrop = backdrop,
                isBackgroundAdaptive = true,
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenIconButtonAdaptation {
        return remember(size, variant, tone, state, shape, colors) {
            IenIconButtonAdaptation(
                size = size,
                variant = variant,
                tone = tone,
                state = state,
                shape = shape,
                colors = colors,
            )
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
private class ToggleButtonAdaptation(
    private val size: IenButtonSize,
    private val state: IenButtonState,
    private val shapes: IenToggleButtonShapes,
    private val variants: IenToggleButtonVariants,
    private val colors: IenToggleButtonColors,
    private val contentPadding: PaddingValues,
    private val display: IenButtonDisplay,
) : Adaptation<HigToggleButtonAdaptation, IenToggleButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigToggleButtonAdaptation {
        val checkedColors = CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        val uncheckedColors = CupertinoLiquidButtonDefaults.glassButtonColors()
        val checkedBackdrop = rememberLayerBackdrop()
        val uncheckedBackdrop = rememberLayerBackdrop()

        return remember(state, checkedColors, uncheckedColors, checkedBackdrop, uncheckedBackdrop) {
            HigToggleButtonAdaptation(
                state = state,
                checked = HigButtonAdaptation(
                    state = state,
                    colors = checkedColors,
                    backdrop = checkedBackdrop,
                    isBackgroundAdaptive = true,
                ),
                unchecked = HigButtonAdaptation(
                    state = state,
                    colors = uncheckedColors,
                    backdrop = uncheckedBackdrop,
                    isBackgroundAdaptive = true,
                ),
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenToggleButtonAdaptation {
        return remember(size, state, shapes, variants, colors, contentPadding, display) {
            IenToggleButtonAdaptation(
                size = size,
                state = state,
                shapes = shapes,
                variants = variants,
                colors = colors,
                contentPadding = contentPadding,
                display = display,
            )
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
private class IconToggleButtonAdaptation(
    private val size: IenButtonSize,
    private val state: IenButtonState,
    private val shapes: IenToggleButtonShapes,
    private val variants: IenToggleButtonVariants,
    private val colors: IenToggleButtonColors,
) : Adaptation<HigIconToggleButtonAdaptation, IenIconToggleButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigIconToggleButtonAdaptation {
        val checkedColors = CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        val uncheckedColors = CupertinoLiquidButtonDefaults.glassButtonColors()
        val checkedBackdrop = rememberLayerBackdrop()
        val uncheckedBackdrop = rememberLayerBackdrop()

        return remember(state, checkedColors, uncheckedColors, checkedBackdrop, uncheckedBackdrop) {
            HigIconToggleButtonAdaptation(
                state = state,
                checked = HigIconButtonAdaptation(
                    state = state,
                    colors = checkedColors,
                    backdrop = checkedBackdrop,
                    isBackgroundAdaptive = true,
                ),
                unchecked = HigIconButtonAdaptation(
                    state = state,
                    colors = uncheckedColors,
                    backdrop = uncheckedBackdrop,
                    isBackgroundAdaptive = true,
                ),
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenIconToggleButtonAdaptation {
        return remember(size, state, shapes, variants, colors) {
            IenIconToggleButtonAdaptation(
                size = size,
                state = state,
                shapes = shapes,
                variants = variants,
                colors = colors,
            )
        }
    }
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
private class ExtendedFloatingActionButtonAdaptation(
    private val variant: IenButtonVariant,
    private val tone: IenSemanticTone,
    private val state: IenButtonState,
    private val shape: Shape,
    private val contentPadding: PaddingValues,
    private val colors: IenButtonColors,
) : Adaptation<HigButtonAdaptation, IenExtendedFloatingActionButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigButtonAdaptation {
        val colors = CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        val backdrop = rememberLayerBackdrop()

        return remember(state, colors, backdrop) {
            HigButtonAdaptation(
                state = state,
                colors = colors,
                backdrop = backdrop,
                isBackgroundAdaptive = true,
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenExtendedFloatingActionButtonAdaptation {
        return remember(variant, tone, state, shape, contentPadding, colors) {
            IenExtendedFloatingActionButtonAdaptation(
                variant = variant,
                tone = tone,
                state = state,
                shape = shape,
                contentPadding = contentPadding,
                colors = colors,
            )
        }
    }
}

/**
 * 적응형 뒤로가기 버튼 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param icon 뒤로가기 아이콘 데이터
 * @param enabled 버튼 활성화 여부
 * @param visible 버튼 가시성 여부
 * @param backdrop Backdrop 컴포넌트 (iOS에서 사용함)
 * @param isBackgroundAdaptive 배경 적응 여부 (iOS에서 사용함)
 * @param onClick 버튼 클릭 시 실행할 함수
 * @return 뒤로가기 버튼 컴포저블
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveBackButton(
    modifier: Modifier = Modifier,
    icon: IconData = LocalBackButtonIcon.current ?: LocalButtonProviderDefault.BackIcon,
    enabled: Boolean = true,
    visible: Boolean = true,
    backdrop: Backdrop,
    isBackgroundAdaptive: Boolean = true,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(1.2f)
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(spring(1.2f)),
        exit = slideOutHorizontally(spring(1.2f))
    ) {
        AdaptiveWidget(
            material = {
                IenBackButton(
                    modifier = modifier,
                    icon = icon,
                    enabled = enabled,
                    onClick = onClick,
                )
            },
            cupertino = {
                CupertinoLiquidIconButton(
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .graphicsLayer {
                            this.alpha = alpha
                            this.compositingStrategy = CompositingStrategy.ModulateAlpha
                        }
                    ,
                    enabled = enabled,
                    backdrop = backdrop,
                    isBackgroundAdaptive = isBackgroundAdaptive,
                    onClick = onClick
                ) {
                    ComplexIcon(
                        icon = icon,
                        contentDescription = null
                    )
                }
            }
        )
    }
}
