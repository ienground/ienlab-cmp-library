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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import com.kyant.backdrop.Backdrop
import zone.ien.hig.CupertinoText
import zone.ien.hig.CupertinoLiquidIconButton
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.AdaptiveButton as HigAdaptiveButton
import zone.ien.hig.adaptive.AdaptiveFilledIconButton as HigAdaptiveFilledIconButton
import zone.ien.hig.adaptive.AdaptiveIconButton as HigAdaptiveIconButton
import zone.ien.hig.adaptive.AdaptiveTextButton as HigAdaptiveTextButton
import zone.ien.hig.adaptive.AdaptiveTonalButton as HigAdaptiveTonalButton
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.icon.ComplexIcon
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.LocalBackButtonIcon
import zone.ien.utils.icon.LocalButtonProviderDefault
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenExtendedFab
import zone.ien.utils.ui.interactive.IenIconButton
import zone.ien.utils.ui.interactive.IenIconToggleButton
import zone.ien.utils.ui.interactive.IenIconPlacement
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.interactive.IenTextButtonSize
import zone.ien.utils.ui.interactive.IenTextButtonVariant
import zone.ien.utils.ui.interactive.IenToggleButton
import zone.ien.utils.ui.interactive.IenToggleButtonColors
import zone.ien.utils.ui.interactive.IenToggleButtonShapes
import zone.ien.utils.ui.interactive.IenToggleButtonVariants
import zone.ien.utils.ui.screen.IenBackButton

/**
 * Material 분기에서 [IenButton]을 사용하는 적응형 버튼 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(enabled = enabled, loading = loading),
    icon: (@Composable () -> Unit)? = null,
    iconPlacement: IenIconPlacement = IenIconPlacement.Start,
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    backgroundBrush: Brush? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    display: IenButtonDisplay = IenButtonDisplay.Inline,
) {
    AdaptiveWidget(
        material = {
            IenButton(
                text = text,
                onClick = onClick,
                modifier = modifier,
                size = size,
                variant = variant,
                tone = tone,
                state = state.copy(enabled = state.enabled && enabled, loading = state.loading || loading),
                icon = icon,
                iconPlacement = iconPlacement,
                shape = shape,
                contentPadding = contentPadding,
                backgroundBrush = backgroundBrush,
                interactionSource = interactionSource,
                display = display,
            )
        },
        cupertino = {
            HigAdaptiveButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled && state.enabled,
                interactionSource = interactionSource,
            ) {
                AdaptiveButtonTextContent(
                    text = text,
                    icon = icon,
                    iconPlacement = iconPlacement,
                )
            }
        },
    )
}

/**
 * Material 분기에서 [IenTextButton]을 사용하는 적응형 텍스트 버튼 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenTextButtonSize = IenTextButtonSize.Medium,
    variant: IenTextButtonVariant = IenTextButtonVariant.Clear,
    disabled: Boolean = false,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(enabled = !disabled),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    AdaptiveWidget(
        material = {
            IenTextButton(
                text = text,
                onClick = onClick,
                modifier = modifier,
                size = size,
                variant = variant,
                disabled = disabled,
                tone = tone,
                state = state,
                interactionSource = interactionSource,
            )
        },
        cupertino = {
            HigAdaptiveTextButton(
                onClick = onClick,
                modifier = modifier,
                enabled = state.enabled && !disabled,
                interactionSource = interactionSource,
            ) {
                CupertinoText(text)
            }
        },
    )
}

/**
 * Material 분기에서 [IenIconButton]을 사용하는 적응형 아이콘 버튼 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Ghost,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(enabled = enabled, loading = loading),
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        material = {
            IenIconButton(
                onClick = onClick,
                modifier = modifier,
                size = size,
                variant = variant,
                tone = tone,
                state = state.copy(enabled = state.enabled && enabled, loading = state.loading || loading),
                shape = shape,
                interactionSource = interactionSource,
                content = content,
            )
        },
        cupertino = {
            HigAdaptiveIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled && state.enabled,
                interactionSource = interactionSource,
                content = content,
            )
        },
    )
}

/**
 * Material 분기에서 채움 스타일 [IenIconButton]을 사용하는 적응형 아이콘 버튼 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveFilledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    size: IenButtonSize = IenButtonSize.Large,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(enabled = enabled, loading = loading),
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        material = {
            IenIconButton(
                onClick = onClick,
                modifier = modifier,
                size = size,
                variant = IenButtonVariant.Fill,
                tone = tone,
                state = state.copy(enabled = state.enabled && enabled, loading = state.loading || loading),
                shape = shape,
                interactionSource = interactionSource,
                content = content,
            )
        },
        cupertino = {
            HigAdaptiveFilledIconButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled && state.enabled,
                interactionSource = interactionSource,
                content = content,
            )
        },
    )
}

/**
 * Material 분기에서 [IenToggleButton]을 사용하는 적응형 토글 버튼 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    size: IenButtonSize = IenButtonSize.Large,
    state: IenButtonState = IenButtonState(enabled = enabled, loading = loading),
    icon: (@Composable () -> Unit)? = null,
    iconPlacement: IenIconPlacement = IenIconPlacement.Start,
    shapes: IenToggleButtonShapes? = null,
    variants: IenToggleButtonVariants = IenToggleButtonVariants(),
    colors: IenToggleButtonColors? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    display: IenButtonDisplay = IenButtonDisplay.Inline,
) {
    AdaptiveWidget(
        material = {
            IenToggleButton(
                checked = checked,
                onCheckedChange = onCheckedChange,
                text = text,
                modifier = modifier,
                size = size,
                state = state.copy(enabled = state.enabled && enabled, loading = state.loading || loading),
                icon = icon,
                iconPlacement = iconPlacement,
                shapes = shapes,
                variants = variants,
                colors = colors,
                contentPadding = contentPadding,
                interactionSource = interactionSource,
                display = display,
            )
        },
        cupertino = {
            val onClick = { onCheckedChange(!checked) }
            val isEnabled = enabled && state.enabled && !state.loading && !loading
            if (checked) {
                HigAdaptiveTonalButton(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = isEnabled,
                    interactionSource = interactionSource,
                ) {
                    AdaptiveButtonTextContent(
                        text = text,
                        icon = icon,
                        iconPlacement = iconPlacement,
                    )
                }
            } else {
                HigAdaptiveButton(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = isEnabled,
                    interactionSource = interactionSource,
                ) {
                    AdaptiveButtonTextContent(
                        text = text,
                        icon = icon,
                        iconPlacement = iconPlacement,
                    )
                }
            }
        },
    )
}

/**
 * Material 분기에서 [IenIconToggleButton]을 사용하는 적응형 아이콘 토글 버튼 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    size: IenButtonSize = IenButtonSize.Large,
    state: IenButtonState = IenButtonState(enabled = enabled, loading = loading),
    shapes: IenToggleButtonShapes? = null,
    variants: IenToggleButtonVariants = IenToggleButtonVariants(),
    colors: IenToggleButtonColors? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    AdaptiveWidget(
        material = {
            IenIconToggleButton(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                size = size,
                state = state.copy(enabled = state.enabled && enabled, loading = state.loading || loading),
                shapes = shapes,
                variants = variants,
                colors = colors,
                interactionSource = interactionSource,
                content = content,
            )
        },
        cupertino = {
            val onClick = { onCheckedChange(!checked) }
            val isEnabled = enabled && state.enabled && !state.loading && !loading
            if (checked) {
                HigAdaptiveFilledIconButton(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = isEnabled,
                    interactionSource = interactionSource,
                    content = content,
                )
            } else {
                HigAdaptiveIconButton(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = isEnabled,
                    interactionSource = interactionSource,
                    content = content,
                )
            }
        },
    )
}

/**
 * Material 분기에서 [IenExtendedFab]을 사용하는 적응형 확장 플로팅 액션 버튼 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveExtendedFloatingActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(enabled = enabled, loading = loading),
    icon: (@Composable () -> Unit)? = null,
    iconPlacement: IenIconPlacement = IenIconPlacement.Start,
    shape: Shape = ContinuousCapsule(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    AdaptiveWidget(
        material = {
            IenExtendedFab(
                text = text,
                onClick = onClick,
                modifier = modifier,
                variant = variant,
                tone = tone,
                state = state.copy(enabled = state.enabled && enabled, loading = state.loading || loading),
                icon = icon,
                iconPlacement = iconPlacement,
                shape = shape,
                contentPadding = contentPadding,
                interactionSource = interactionSource,
            )
        },
        cupertino = {
            HigAdaptiveTonalButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled && state.enabled,
                interactionSource = interactionSource,
            ) {
                AdaptiveButtonTextContent(
                    text = text,
                    icon = icon,
                    iconPlacement = iconPlacement,
                )
            }
        },
    )
}

@Composable
private fun AdaptiveButtonTextContent(
    text: String,
    icon: (@Composable () -> Unit)?,
    iconPlacement: IenIconPlacement,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
        if (iconPlacement == IenIconPlacement.Start) icon?.invoke()
        CupertinoText(text)
        if (iconPlacement == IenIconPlacement.End) icon?.invoke()
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
