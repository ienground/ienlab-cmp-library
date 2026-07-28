package zone.ien.utils.ui.interactive

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.utils.animateContentSizeWithoutClipping

/** Chip의 활성화 및 진행 상태를 정의합니다. */
@Immutable
data class IenChipState(
    val enabled: Boolean = true,
    val loading: Boolean = false,
) {
    /** 사용자 입력을 받을 수 있는 상태인지 나타냅니다. */
    val isInteractive: Boolean get() = enabled && !loading
}

/** Chip의 기본, 선택, 비활성 상태에 적용할 색상과 배경 브러시를 정의합니다. */
@Immutable
data class IenChipColors(
    val container: Color,
    val content: Color,
    val border: Color,
    val containerBrush: Brush?,
    val selectedContainer: Color,
    val selectedContent: Color,
    val selectedBorder: Color,
    val selectedContainerBrush: Brush?,
    val disabledContainer: Color,
    val disabledContent: Color,
    val disabledBorder: Color,
    val disabledSelectedContainer: Color,
    val disabledSelectedContent: Color,
    val disabledSelectedBorder: Color,
)

/** IEN Chip의 기본 색상과 크기 값을 제공합니다. */
object IenChipDefault {
    /** Chip의 상태별 색상과 그라데이션을 생성합니다. */
    @Composable
    fun colors(
        tone: IenSemanticTone = IenSemanticTone.Brand,
        container: Color = toneWeakColor(tone),
        content: Color = toneColor(tone),
        border: Color = Color.Transparent,
        containerBrush: Brush? = toneWeakGradientBrush(tone),
        selectedContainer: Color = toneColor(tone),
        selectedContent: Color = toneOnColor(tone),
        selectedBorder: Color = selectedContainer,
        selectedContainerBrush: Brush? = toneGradientBrush(tone),
        disabledContainer: Color = IenTheme.colors.surfaceVariant,
        disabledContent: Color = IenTheme.colors.textDisabled,
        disabledBorder: Color = Color.Transparent,
        disabledSelectedContainer: Color = IenTheme.colors.surfaceVariant,
        disabledSelectedContent: Color = IenTheme.colors.textDisabled,
        disabledSelectedBorder: Color = Color.Transparent,
    ): IenChipColors = IenChipColors(
        container = container,
        content = content,
        border = border,
        containerBrush = containerBrush,
        selectedContainer = selectedContainer,
        selectedContent = selectedContent,
        selectedBorder = selectedBorder,
        selectedContainerBrush = selectedContainerBrush,
        disabledContainer = disabledContainer,
        disabledContent = disabledContent,
        disabledBorder = disabledBorder,
        disabledSelectedContainer = disabledSelectedContainer,
        disabledSelectedContent = disabledSelectedContent,
        disabledSelectedBorder = disabledSelectedBorder,
    )

    /** Chip의 기본 내부 여백입니다. */
    val ContentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
}

/** 작업을 실행하는 Assist Chip입니다. */
@Composable
fun IenAssistChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: IenChipState = IenChipState(),
    shape: Shape = ContinuousCapsule(),
    colors: IenChipColors = IenChipDefault.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) = IenActionChip(
    onClick = onClick,
    modifier = modifier,
    state = state,
    shape = shape,
    colors = colors,
    elevated = false,
    interactionSource = interactionSource,
    leading = leadingIcon,
    trailing = trailingIcon,
    label = label,
)

/** 그림자 깊이를 갖는 Assist Chip입니다. */
@Composable
fun IenElevatedAssistChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: IenChipState = IenChipState(),
    shape: Shape = ContinuousCapsule(),
    colors: IenChipColors = IenChipDefault.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) = IenActionChip(
    onClick = onClick,
    modifier = modifier,
    state = state,
    shape = shape,
    colors = colors,
    elevated = true,
    interactionSource = interactionSource,
    leading = leadingIcon,
    trailing = trailingIcon,
    label = label,
)

/** 선택 상태를 통해 필터 조건을 표현하는 Filter Chip입니다. */
@Composable
fun IenFilterChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    state: IenChipState = IenChipState(),
    shape: Shape = ContinuousCapsule(),
    colors: IenChipColors = IenChipDefault.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) = IenSelectableChip(
    selected = selected,
    onSelectedChange = onSelectedChange,
    modifier = modifier,
    state = state,
    shape = shape,
    colors = colors,
    elevated = false,
    interactionSource = interactionSource,
    leading = leadingIcon,
    trailing = trailingIcon,
    label = label,
)

/** 그림자 깊이를 갖는 Filter Chip입니다. */
@Composable
fun IenElevatedFilterChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    state: IenChipState = IenChipState(),
    shape: Shape = ContinuousCapsule(),
    colors: IenChipColors = IenChipDefault.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) = IenSelectableChip(
    selected = selected,
    onSelectedChange = onSelectedChange,
    modifier = modifier,
    state = state,
    shape = shape,
    colors = colors,
    elevated = true,
    interactionSource = interactionSource,
    leading = leadingIcon,
    trailing = trailingIcon,
    label = label,
)

/** 사용자 입력이나 선택된 항목을 표현하는 Input Chip입니다. */
@Composable
fun IenInputChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    state: IenChipState = IenChipState(),
    shape: Shape = ContinuousCapsule(),
    colors: IenChipColors = IenChipDefault.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    avatar: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) = IenSelectableChip(
    selected = selected,
    onSelectedChange = onSelectedChange,
    modifier = modifier,
    state = state,
    shape = shape,
    colors = colors,
    elevated = false,
    interactionSource = interactionSource,
    leading = avatar ?: leadingIcon,
    trailing = trailingIcon,
    label = label,
)

/** 사용자 맥락에 맞는 추천 작업을 제공하는 Suggestion Chip입니다. */
@Composable
fun IenSuggestionChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: IenChipState = IenChipState(),
    shape: Shape = ContinuousCapsule(),
    colors: IenChipColors = IenChipDefault.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) = IenActionChip(
    onClick = onClick,
    modifier = modifier,
    state = state,
    shape = shape,
    colors = colors,
    elevated = false,
    interactionSource = interactionSource,
    leading = icon,
    label = label,
)

/** 그림자 깊이를 갖는 Suggestion Chip입니다. */
@Composable
fun IenElevatedSuggestionChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: IenChipState = IenChipState(),
    shape: Shape = ContinuousCapsule(),
    colors: IenChipColors = IenChipDefault.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) = IenActionChip(
    onClick = onClick,
    modifier = modifier,
    state = state,
    shape = shape,
    colors = colors,
    elevated = true,
    interactionSource = interactionSource,
    leading = icon,
    label = label,
)

@Composable
private fun IenActionChip(
    onClick: () -> Unit,
    modifier: Modifier,
    state: IenChipState,
    shape: Shape,
    colors: IenChipColors,
    elevated: Boolean,
    interactionSource: MutableInteractionSource,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) = IenChipContainer(
    onClick = onClick,
    modifier = modifier,
    selected = null,
    state = state,
    shape = shape,
    colors = colors,
    elevated = elevated,
    interactionSource = interactionSource,
    leading = leading,
    trailing = trailing,
    label = label,
)

@Composable
private fun IenSelectableChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier,
    state: IenChipState,
    shape: Shape,
    colors: IenChipColors,
    elevated: Boolean,
    interactionSource: MutableInteractionSource,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
) = IenChipContainer(
    onClick = { onSelectedChange(!selected) },
    modifier = modifier,
    selected = selected,
    state = state,
    shape = shape,
    colors = colors,
    elevated = elevated,
    interactionSource = interactionSource,
    leading = leading,
    trailing = trailing,
    label = label,
)

@Composable
private fun IenChipContainer(
    onClick: () -> Unit,
    modifier: Modifier,
    selected: Boolean?,
    state: IenChipState,
    shape: Shape,
    colors: IenChipColors,
    elevated: Boolean,
    interactionSource: MutableInteractionSource,
    leading: (@Composable () -> Unit)?,
    trailing: (@Composable () -> Unit)?,
    label: @Composable () -> Unit,
) {
    val visualColors = colors.resolve(selected = selected == true, enabled = state.enabled)
    val isPressed by interactionSource.collectIsPressedAsState()
    val fastAnimationMillis = IenTheme.motion.fastMillis
    val instantAnimationMillis = IenTheme.motion.instantMillis
    val scale by animateFloatAsState(
        targetValue = if (isPressed && state.isInteractive) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "IenChipScale",
    )
    val colorAnimationSpec = tween<Color>(durationMillis = fastAnimationMillis)
    val animatedContainer by animateColorAsState(
        targetValue = visualColors.container,
        animationSpec = colorAnimationSpec,
        label = "IenChipContainerColor",
    )
    val animatedContent by animateColorAsState(
        targetValue = visualColors.content,
        animationSpec = colorAnimationSpec,
        label = "IenChipContentColor",
    )
    val animatedBorder by animateColorAsState(
        targetValue = visualColors.border,
        animationSpec = colorAnimationSpec,
        label = "IenChipBorderColor",
    )

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = IenTheme.state.minimumTouchTarget,
                minHeight = IenTheme.state.minimumTouchTarget,
            )
            .semantics {
                selected?.let { this.selected = it }
                if (!state.isInteractive) disabled()
                role = if (selected == null) Role.Button else Role.Checkbox
            }
            .clickable(
                enabled = state.isInteractive,
                role = if (selected == null) Role.Button else Role.Checkbox,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        val visualModifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (elevated) {
                    Modifier.shadow(IenTheme.elevation.raised, shape, clip = false)
                } else {
                    Modifier
                },
            )
            .animateContentSizeWithoutClipping(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            )
            .clip(shape)
            .border(IenTheme.stroke.thin, animatedBorder, shape)
            .heightIn(min = 32.dp)

        Box(modifier = visualModifier, contentAlignment = Alignment.Center) {
            Crossfade(
                targetState = visualColors.containerBrush,
                modifier = Modifier.matchParentSize(),
                animationSpec = tween(durationMillis = fastAnimationMillis),
                label = "IenChipBackground",
            ) { brush ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            brush?.let { Modifier.background(it) }
                                ?: Modifier.background(animatedContainer),
                        ),
                )
            }

            CompositionLocalProvider(androidx.compose.material3.LocalContentColor provides animatedContent) {
                IenProvideTextStyle(IenTheme.typography.label2, animatedContent) {
                    AnimatedContent(
                        targetState = state.loading,
                        modifier = Modifier.padding(IenChipDefault.ContentPadding),
                        transitionSpec = {
                            fadeIn(tween(fastAnimationMillis)) togetherWith
                                fadeOut(tween(instantAnimationMillis))
                        },
                        contentAlignment = Alignment.Center,
                        label = "IenChipLoading",
                    ) { loading ->
                        if (loading) {
                            IenLoaderPrimitive(
                                modifier = Modifier.size(IenTheme.icon.sm),
                                color = animatedContent,
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IenAnimatedChipSlot(
                                    content = leading,
                                    contentPadding = PaddingValues(end = IenTheme.spacing.xxs),
                                    label = "IenChipLeading",
                                )
                                label()
                                IenAnimatedChipSlot(
                                    content = trailing,
                                    contentPadding = PaddingValues(start = IenTheme.spacing.xxs),
                                    label = "IenChipTrailing",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IenAnimatedChipSlot(
    content: (@Composable () -> Unit)?,
    contentPadding: PaddingValues,
    label: String,
) {
    val fastAnimationMillis = IenTheme.motion.fastMillis
    val instantAnimationMillis = IenTheme.motion.instantMillis
    AnimatedContent(
        targetState = content,
        contentKey = { it != null },
        transitionSpec = {
            (fadeIn(tween(fastAnimationMillis)) +
                scaleIn(tween(fastAnimationMillis), initialScale = 0.82f)) togetherWith
                (fadeOut(tween(instantAnimationMillis)) +
                    scaleOut(tween(instantAnimationMillis), targetScale = 0.82f))
        },
        contentAlignment = Alignment.Center,
        label = label,
    ) { slot ->
        if (slot != null) {
            Box(modifier = Modifier.padding(contentPadding)) {
                slot()
            }
        }
    }
}

@Immutable
internal data class IenChipVisualColors(
    val container: Color,
    val content: Color,
    val border: Color,
    val containerBrush: Brush?,
)

internal fun IenChipColors.resolve(selected: Boolean, enabled: Boolean): IenChipVisualColors {
    if (!enabled) {
        return if (selected) {
            IenChipVisualColors(
                container = disabledSelectedContainer,
                content = disabledSelectedContent,
                border = disabledSelectedBorder,
                containerBrush = null,
            )
        } else {
            IenChipVisualColors(
                container = disabledContainer,
                content = disabledContent,
                border = disabledBorder,
                containerBrush = null,
            )
        }
    }

    return if (selected) {
        IenChipVisualColors(
            container = selectedContainer,
            content = selectedContent,
            border = selectedBorder,
            containerBrush = selectedContainerBrush,
        )
    } else {
        IenChipVisualColors(
            container = container,
            content = content,
            border = border,
            containerBrush = containerBrush,
        )
    }
}
