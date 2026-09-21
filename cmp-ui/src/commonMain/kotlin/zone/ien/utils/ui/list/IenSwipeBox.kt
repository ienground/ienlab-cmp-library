@file:OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class,
)

package zone.ien.utils.ui.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButtonColors
import zone.ien.utils.ui.interactive.IenButtonContainer
import zone.ien.utils.ui.interactive.IenButtonDefault
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.utils.animateContentSizeWithoutClipping
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

/** `IenSwipeBox`에 사용하는 기본 크기와 스프링 설정입니다. */
object IenSwipeBoxDefaults {
    /** 전체 스와이프 동작을 기본으로 허용할지 여부입니다. */
    const val allowFullSwipe: Boolean = true

    /** 액션 아이템이 확장되는 순간 햅틱 피드백을 기본으로 사용할지 여부입니다. */
    const val expansionHapticEnabled: Boolean = true

    /** 속도 기준값입니다. */
    const val velocityThreshold: Float = Float.POSITIVE_INFINITY

    /** 액션 아이템 좌우 여백입니다. */
    val actionItemHorizontalPadding: Dp = 4.dp

    /** 액션 아이템 상하 여백입니다. */
    val actionItemVerticalPadding: Dp = 10.dp

    /** 액션 아이템의 기본 크기입니다. */
    val actionItemSize: Dp = 52.dp

    /** 액션 아이템 하나가 차지하는 기본 너비입니다. */
    val actionItemWidth: Dp =
        actionItemHorizontalPadding + actionItemSize + actionItemHorizontalPadding

    /** 액션 아이템 영역의 기본 높이입니다. */
    val actionItemHeight: Dp =
        actionItemVerticalPadding + actionItemSize + actionItemVerticalPadding

    /** 액션 아이템에 사용하는 기본 스프링 애니메이션입니다. */
    val animationSpec: SpringSpec<Float> = SpringSpec(
        stiffness = Spring.StiffnessMedium,
        dampingRatio = Spring.DampingRatioNoBouncy,
    )
}

/** `IenSwipeBox`가 사용하는 스와이프 상태입니다. */
enum class IenSwipeBoxStates {
    Resting,
    EndVisible,
    StartVisible,
    EndFullyExpanded,
    StartFullyExpanded,
}

/** `IenSwipeBox`의 시작/끝 액션을 선언하는 DSL입니다. */
class IenSwipeBoxActionsBuilder {
    /** 왼쪽에서 노출되는 액션 목록입니다. */
    val startActions: List<SwipeAction>
        field = mutableListOf<SwipeAction>()

    /** 오른쪽에서 노출되는 액션 목록입니다. */
    val endActions: List<SwipeAction>
        field = mutableListOf<SwipeAction>()

    /** 시작 방향 액션을 추가합니다. */
    fun start(
        key: Any? = null,
        onClick: (() -> Unit)? = null,
        content: @Composable RowScope.() -> Unit,
    ) {
        startActions.add(SwipeAction(key, onClick, content))
    }

    /** 끝 방향 액션을 추가합니다. */
    fun end(
        key: Any? = null,
        onClick: (() -> Unit)? = null,
        content: @Composable RowScope.() -> Unit,
    ) {
        endActions.add(SwipeAction(key, onClick, content))
    }

    /** 하나의 스와이프 액션 정의입니다. */
    class SwipeAction(
        val key: Any? = null,
        val onClick: (() -> Unit)? = null,
        val content: @Composable RowScope.() -> Unit,
    )
}

/**
 * `compose-hig`의 SwipeBox 동작과 시각 구조를 공통 UI에 직접 구현한 스와이프 컨테이너입니다.
 *
 * 시작 액션은 왼쪽에서, 끝 액션은 오른쪽에서 노출됩니다. 전체 스와이프 실행, 햅틱 피드백,
 * 액션 확장 애니메이션은 이 컴포넌트가 직접 처리합니다.
 * [expansionHapticEnabled]로 액션 아이템 확장 시 햅틱 피드백을 제어할 수 있습니다.
 * `height`가 [Dp.Unspecified]이면 콘텐츠 높이를 사용하며, 액션이 있는 경우 액션 아이템의
 * 최소 높이를 보장합니다.
 */
@Composable
fun IenSwipeBox(
    state: AnchoredDraggableState<IenSwipeBoxStates> = rememberIenSwipeBoxState(),
    modifier: Modifier = Modifier,
    itemWidth: Dp = IenSwipeBoxDefaults.actionItemWidth,
    height: Dp = Dp.Unspecified,
    startToEndFullSwipeEnabled: Boolean = IenSwipeBoxDefaults.allowFullSwipe,
    endToStartFullSwipeEnabled: Boolean = IenSwipeBoxDefaults.allowFullSwipe,
    expansionHapticEnabled: Boolean = IenSwipeBoxDefaults.expansionHapticEnabled,
    actionItemBuilder: IenSwipeBoxActionsBuilder.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val density = LocalDensity.current
    var parentWidth by remember { mutableStateOf(0) }
    val actionItems = IenSwipeBoxActionsBuilder().apply(actionItemBuilder)
    val startActionsSize = actionItems.startActions.size
    val endActionsSize = actionItems.endActions.size
    val isStartActionItemSupplied = startActionsSize != 0
    val isEndActionItemSupplied = endActionsSize != 0
    val hasActions = isStartActionItemSupplied || isEndActionItemSupplied
    val startFullSwipeAction = actionItems.startActions.firstOrNull()?.onClick
    val endFullSwipeAction = actionItems.endActions.lastOrNull()?.onClick
    var contentHeightPx by remember { mutableIntStateOf(0) }
    val contentHeight = with(density) { contentHeightPx.toDp() }
    val actionHeight = resolveSwipeBoxHeight(
        height = height,
        contentHeight = contentHeight,
        hasActions = hasActions,
    )

    val hapticFeedback = rememberIenSwipeBoxHapticFeedback()
    var anchorsInitialized by remember { mutableStateOf(false) }
    val actionRowOuterPadding = IenSwipeBoxDefaults.actionItemHorizontalPadding * 2
    val startExpansionThresholdPx = with(density) {
        (itemWidth * startActionsSize + actionRowOuterPadding).toPx()
    }
    val endExpansionThresholdPx = with(density) {
        (itemWidth * endActionsSize + actionRowOuterPadding).toPx()
    }

    AnchorsEffect(
        parentWidth = parentWidth,
        fullExpansionStart = startToEndFullSwipeEnabled,
        isStartActionItemSupplied = isStartActionItemSupplied,
        fullExpansionEnd = endToStartFullSwipeEnabled,
        isEndActionItemSupplied = isEndActionItemSupplied,
        swipeBoxState = state,
        density = density,
        amountOfStartActionItems = startActionsSize,
        amountOfEndActionItems = endActionsSize,
        actionItemWidth = itemWidth,
        actionRowOuterPadding = actionRowOuterPadding,
    ) { anchorsInitialized = it }

    HapticFeedbackEffect(
        expansionHapticEnabled = expansionHapticEnabled,
        fullExpansionStart = startToEndFullSwipeEnabled,
        fullExpansionEnd = endToStartFullSwipeEnabled,
        isStartActionItemSupplied = isStartActionItemSupplied,
        isEndActionItemSupplied = isEndActionItemSupplied,
        startExpansionThresholdPx = startExpansionThresholdPx,
        endExpansionThresholdPx = endExpansionThresholdPx,
        swipeBoxState = state,
        hapticFeedback = hapticFeedback,
    )

    DismissFullyExpandedEffect(
        swipeBoxState = state,
        isStartActionItemSupplied = isStartActionItemSupplied,
        fullExpansionStart = startToEndFullSwipeEnabled,
        isEndActionItemSupplied = isEndActionItemSupplied,
        fullExpansionEnd = endToStartFullSwipeEnabled,
        startFullExpansionOnClick = startFullSwipeAction,
        endFullExpansionOnClick = endFullSwipeAction,
    )

    CompositionLocalProvider(LocalIenSwipeBoxState provides state) {
        Box(
            modifier = modifier.onGloballyPositioned { coordinates ->
                parentWidth = coordinates.size.width
            },
        ) {
            val offset by remember {
                derivedStateOf {
                    if (anchorsInitialized) state.offset else 0f
                }
            }
            val revealedWidth = with(density) { kotlin.math.abs(offset).toDp() }
            val isSwiping = offset != 0f
            val foregroundColor by animateColorAsState(
                targetValue = if (isSwiping) {
                    IenTheme.colors.surfaceVariant
                } else {
                    IenTheme.colors.surface
                },
                animationSpec = ienSwipeAnimationSpec(),
            )
            val foregroundCornerRadius by animateDpAsState(
                targetValue = if (isSwiping) IenTheme.radius.lg else IenTheme.radius.none,
                animationSpec = ienSwipeAnimationSpec(),
            )

            if (offset > 0 && isStartActionItemSupplied) {
                CompositionLocalProvider(
                    LocalIenSwipeActionPosition provides IenSwipeActionPosition.Start,
                ) {
                    Box(
                        modifier = Modifier
                            .height(actionHeight)
                            .width(revealedWidth)
                            .clipToBounds()
                            .align(Alignment.CenterStart),
                    ) {
                        val revealedActionContentWidth =
                            (revealedWidth - actionRowOuterPadding).coerceAtLeast(0.dp)
                        val normalActionRowWidth =
                            itemWidth * startActionsSize + actionRowOuterPadding
                        val isActionRowExpanding = isSwipeBoxActionExpanding(
                            revealedWidth = revealedWidth,
                            itemWidth = itemWidth,
                            actionCount = startActionsSize,
                            actionRowOuterPadding = actionRowOuterPadding,
                        )
                        val actionRowWidth =
                            if (isActionRowExpanding) {
                                revealedWidth
                            } else {
                                normalActionRowWidth
                            }
                        IenSwipeActionRow(width = actionRowWidth, alignToEnd = false) {
                            actionItems.startActions.forEachIndexed { index, swipeAction ->
                                val revealScale =
                                    ((revealedActionContentWidth - itemWidth * index) / itemWidth)
                                        .coerceIn(0f, 1f)
                                CompositionLocalProvider(
                                    LocalIenSwipeBoxItemFullSwipe provides (index == 0),
                                    LocalIenSwipeBoxItemExpanding provides (
                                        index == 0 && (
                                            isActionRowExpanding ||
                                                state.targetValue == IenSwipeBoxStates.StartFullyExpanded
                                            )
                                        ),
                                    LocalIenSwipeBoxItemWidth provides itemWidth,
                                    LocalIenSwipeBoxItemRevealScale provides revealScale,
                                ) {
                                    key(swipeAction.key) {
                                        swipeAction.content(this)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (offset < 0 && isEndActionItemSupplied) {
                CompositionLocalProvider(
                    LocalIenSwipeActionPosition provides IenSwipeActionPosition.End,
                ) {
                    Box(
                        modifier = Modifier
                            .height(actionHeight)
                            .width(revealedWidth)
                            .clipToBounds()
                            .align(Alignment.CenterEnd),
                    ) {
                        val revealedActionContentWidth =
                            (revealedWidth - actionRowOuterPadding).coerceAtLeast(0.dp)
                        val normalActionRowWidth =
                            itemWidth * endActionsSize + actionRowOuterPadding
                        val isActionRowExpanding = isSwipeBoxActionExpanding(
                            revealedWidth = revealedWidth,
                            itemWidth = itemWidth,
                            actionCount = endActionsSize,
                            actionRowOuterPadding = actionRowOuterPadding,
                        )
                        val actionRowWidth =
                            if (isActionRowExpanding) {
                                revealedWidth
                            } else {
                                normalActionRowWidth
                            }
                        IenSwipeActionRow(width = actionRowWidth, alignToEnd = true) {
                            actionItems.endActions.forEachIndexed { index, swipeAction ->
                                val revealOrder = actionItems.endActions.lastIndex - index
                                val revealScale =
                                    ((revealedActionContentWidth - itemWidth * revealOrder) / itemWidth)
                                        .coerceIn(0f, 1f)
                                CompositionLocalProvider(
                                    LocalIenSwipeBoxItemFullSwipe provides
                                        (index == actionItems.endActions.lastIndex),
                                    LocalIenSwipeBoxItemExpanding provides (
                                        index == actionItems.endActions.lastIndex && (
                                            isActionRowExpanding ||
                                                state.targetValue == IenSwipeBoxStates.EndFullyExpanded
                                            )
                                        ),
                                    LocalIenSwipeBoxItemWidth provides itemWidth,
                                    LocalIenSwipeBoxItemRevealScale provides revealScale,
                                ) {
                                    key(swipeAction.key) {
                                        swipeAction.content(this)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .offset { IntOffset(offset.roundToInt(), 0) }
                    .then(
                        if (anchorsInitialized) {
                            Modifier.anchoredDraggable(
                                state = state,
                                orientation = Orientation.Horizontal,
                            )
                        } else {
                            Modifier
                        },
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (!height.value.isNaN()) {
                                Modifier.height(height)
                            } else if (hasActions) {
                                Modifier.heightIn(min = IenSwipeBoxDefaults.actionItemHeight)
                            } else {
                                Modifier
                            },
                        )
                        .onGloballyPositioned { coordinates ->
                            contentHeightPx = coordinates.size.height
                        }
                        .clip(ContinuousRoundedRectangle(foregroundCornerRadius))
                        .background(foregroundColor),
                ) {
                    content()
                }
            }
        }
    }
}

/**
 * `IenSwipeBox` 안에 표시하는 액션 아이템입니다.
 *
 * 액션 버튼은 `IenButton`과 동일하게 [variant], [tone], [state], [colors]로 시각 스타일과
 * 상호작용 상태를 구성합니다. [colors]를 지정하지 않으면 기본 그라데이션이 적용됩니다.
 * [showLabelOnExpansion]이 `true`인 경우에만 확장 상태에서 [label]을 표시합니다.
 */
@Composable
fun RowScope.IenSwipeBoxItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = ContinuousCapsule(),
    colors: IenButtonColors = IenButtonDefault.colors(variant = variant, tone = tone),
    restoreOnClick: Boolean = true,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: ImageVector? = null,
    label: String? = null,
    weight: Float = 1f,
    showLabelOnExpansion: Boolean = true,
) {
    IenSwipeBoxItemImpl(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        tone = tone,
        state = state,
        shape = shape,
        colors = colors,
        restoreOnClick = restoreOnClick,
        onClickLabel = onClickLabel,
        interactionSource = interactionSource,
        icon = icon,
        labelContent = label?.let { labelText ->
            { IenText(text = labelText, maxLines = 1) }
        },
        weight = weight,
        showLabelOnExpansion = showLabelOnExpansion,
    )
}

/**
 * `IenSwipeBox` 안에 표시하는 액션 아이템입니다.
 *
 * 문자열 대신 호출자가 구성한 Composable 라벨을 확장 상태에서 표시할 수 있습니다.
 */
@Composable
fun RowScope.IenSwipeBoxItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = ContinuousCapsule(),
    colors: IenButtonColors = IenButtonDefault.colors(variant = variant, tone = tone),
    restoreOnClick: Boolean = true,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: ImageVector? = null,
    label: @Composable () -> Unit,
    weight: Float = 1f,
    showLabelOnExpansion: Boolean = true,
) {
    IenSwipeBoxItemImpl(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        tone = tone,
        state = state,
        shape = shape,
        colors = colors,
        restoreOnClick = restoreOnClick,
        onClickLabel = onClickLabel,
        interactionSource = interactionSource,
        icon = icon,
        labelContent = label,
        weight = weight,
        showLabelOnExpansion = showLabelOnExpansion,
    )
}

@Composable
private fun RowScope.IenSwipeBoxItemImpl(
    onClick: () -> Unit,
    modifier: Modifier,
    variant: IenButtonVariant,
    tone: IenSemanticTone,
    state: IenButtonState,
    shape: Shape,
    colors: IenButtonColors,
    restoreOnClick: Boolean,
    onClickLabel: String?,
    interactionSource: MutableInteractionSource,
    icon: ImageVector?,
    labelContent: (@Composable () -> Unit)?,
    weight: Float,
    showLabelOnExpansion: Boolean,
) {
    val swipeBoxState = LocalIenSwipeBoxState.current
    val actionPosition = LocalIenSwipeActionPosition.current
    val isFullSwipeActionItem = LocalIenSwipeBoxItemFullSwipe.current
    val itemWidth = LocalIenSwipeBoxItemWidth.current
    val revealScale = LocalIenSwipeBoxItemRevealScale.current
    val isExpanding = LocalIenSwipeBoxItemExpanding.current
    val collapsedItemSize =
        (itemWidth - IenSwipeBoxDefaults.actionItemHorizontalPadding * 2)
            .coerceAtLeast(0.dp)
    val isFullSwipeSettled =
        swipeBoxState.settledValue == IenSwipeBoxStates.EndFullyExpanded ||
            swipeBoxState.settledValue == IenSwipeBoxStates.StartFullyExpanded
    val isFullSwipeTarget =
        swipeBoxState.targetValue == IenSwipeBoxStates.EndFullyExpanded ||
            swipeBoxState.targetValue == IenSwipeBoxStates.StartFullyExpanded
    val shouldRenderItem = !isFullSwipeSettled || isFullSwipeActionItem
    val zIndex = if (isFullSwipeActionItem) 1f else 0f

    val coroutineScope = rememberCoroutineScope()
    val currentOnClick by rememberUpdatedState(onClick)
    val animatedItemWidth by animateDpAsState(
        targetValue = if (shouldRenderItem) itemWidth * weight else 0.dp,
        animationSpec = ienSwipeAnimationSpec(),
    )
    val animatedRevealScale by animateFloatAsState(
        targetValue = revealScale,
        animationSpec = spring(dampingRatio = 0.68f, stiffness = 500f),
    )
    val animatedItemAlpha by animateFloatAsState(
        targetValue = if (isFullSwipeTarget && !isFullSwipeActionItem) 0.35f else revealScale,
        animationSpec = ienSwipeAnimationSpec(),
    )
    val animHorizontalBias by animateFloatAsState(
        targetValue = when {
            isFullSwipeActionItem &&
                swipeBoxState.targetValue == IenSwipeBoxStates.EndFullyExpanded &&
                actionPosition == IenSwipeActionPosition.End -> -1f
            isFullSwipeActionItem &&
                swipeBoxState.targetValue == IenSwipeBoxStates.StartFullyExpanded &&
                actionPosition == IenSwipeActionPosition.Start -> 1f
            else -> 0f
        },
        animationSpec = ienSwipeAnimationSpec(),
    )

    Box(
        modifier = modifier
            .then(
                if (isFullSwipeActionItem) {
                    Modifier.weight(weight)
                } else {
                    Modifier.width(animatedItemWidth)
                },
            )
            .zIndex(zIndex)
            .fillMaxHeight()
            .padding(
                horizontal = IenSwipeBoxDefaults.actionItemHorizontalPadding,
                vertical = IenSwipeBoxDefaults.actionItemVerticalPadding,
            ),
        contentAlignment = Alignment.Center,
    ) {
        IenButtonContainer(
            onClick = {
                currentOnClick()
                if (restoreOnClick) {
                    coroutineScope.launch {
                        swipeBoxState.animateTo(IenSwipeBoxStates.Resting)
                    }
                }
            },
            modifier = Modifier
                .animateContentSizeWithoutClipping(
                    animationSpec = tween(
                        durationMillis = IenTheme.motion.fastMillis,
                        easing = IenTheme.motion.standardEasing,
                    ),
                )
                .then(
                    if (isExpanding) {
                        Modifier
                            .fillMaxWidth()
                            .requiredHeight(collapsedItemSize)
                    } else {
                        Modifier.requiredSize(collapsedItemSize)
                    },
                )
                .graphicsLayer {
                    val buttonScale = maxOf(revealScale, animatedRevealScale).coerceIn(0f, 1f)
                    scaleX = buttonScale
                    scaleY = buttonScale
                    alpha = animatedItemAlpha
                    transformOrigin = TransformOrigin(
                        pivotFractionX = if (actionPosition == IenSwipeActionPosition.End) 1f else 0f,
                        pivotFractionY = 0.5f,
                    )
                },
            variant = variant,
            tone = tone,
            state = state,
            shape = shape,
            contentPadding = PaddingValues(horizontal = IenTheme.spacing.sm),
            colors = colors,
            interactionSource = interactionSource,
            onClickLabel = onClickLabel,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = BiasAlignment(
                    verticalBias = 0f,
                    horizontalBias = animHorizontalBias,
                ),
            ) {
                if (state.loading) {
                    IenLoaderPrimitive(color = LocalContentColor.current)
                } else {
                    IenProvideTextStyle(IenTheme.typography.label2) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                        ) {
                            icon?.let {
                                IenIcon(
                                    imageVector = it,
                                    contentDescription = onClickLabel,
                                    size = IenTheme.icon.sm,
                                )
                            }
                            labelContent?.let { content ->
                                AnimatedVisibility(
                                    visible = shouldRenderSwipeBoxLabel(
                                        isExpanding = isExpanding,
                                        showLabelOnExpansion = showLabelOnExpansion,
                                    ),
                                    enter = fadeIn(
                                        animationSpec = tween(
                                            durationMillis = IenTheme.motion.fastMillis,
                                            easing = IenTheme.motion.standardEasing,
                                        ),
                                    ) + expandHorizontally(
                                        animationSpec = tween(
                                            durationMillis = IenTheme.motion.fastMillis,
                                            easing = IenTheme.motion.standardEasing,
                                        ),
                                        expandFrom = Alignment.Start,
                                    ),
                                    exit = fadeOut(
                                        animationSpec = tween(
                                            durationMillis = IenTheme.motion.fastMillis,
                                            easing = IenTheme.motion.standardEasing,
                                        ),
                                    ) + shrinkHorizontally(
                                        animationSpec = tween(
                                            durationMillis = IenTheme.motion.fastMillis,
                                            easing = IenTheme.motion.standardEasing,
                                        ),
                                        shrinkTowards = Alignment.Start,
                                    ),
                                ) {
                                    content()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> ienSwipeAnimationSpec(): AnimationSpec<T> = tween(
    durationMillis = IenTheme.motion.fastMillis,
    easing = IenTheme.motion.standardEasing,
)

internal fun resolveSwipeBoxHeight(
    height: Dp,
    contentHeight: Dp,
    hasActions: Boolean,
): Dp = when {
    !height.value.isNaN() -> height
    hasActions -> maxOf(contentHeight, IenSwipeBoxDefaults.actionItemHeight)
    else -> contentHeight
}

internal fun isSwipeBoxActionExpanding(
    revealedWidth: Dp,
    itemWidth: Dp,
    actionCount: Int,
    actionRowOuterPadding: Dp,
): Boolean = revealedWidth > itemWidth * actionCount + actionRowOuterPadding

internal fun isSwipeBoxExpansionThresholdReached(
    offset: Float,
    targetValue: IenSwipeBoxStates,
    isStartActionItemSupplied: Boolean,
    isEndActionItemSupplied: Boolean,
    startExpansionThresholdPx: Float,
    endExpansionThresholdPx: Float,
    fullExpansionStart: Boolean,
    fullExpansionEnd: Boolean,
): Boolean =
    (isStartActionItemSupplied && offset > startExpansionThresholdPx) ||
        (isEndActionItemSupplied && offset < -endExpansionThresholdPx) ||
        (fullExpansionStart && targetValue == IenSwipeBoxStates.StartFullyExpanded) ||
        (fullExpansionEnd && targetValue == IenSwipeBoxStates.EndFullyExpanded)

internal fun shouldRenderSwipeBoxLabel(
    isExpanding: Boolean,
    showLabelOnExpansion: Boolean,
): Boolean = isExpanding && showLabelOnExpansion

/** `IenSwipeBox` 상태를 생성하고 스크롤 중 열린 액션을 닫습니다. */
@Composable
fun rememberIenSwipeBoxState(
    key: Any? = null,
    initialValue: IenSwipeBoxStates = IenSwipeBoxStates.Resting,
    positionalThreshold: (distance: Float) -> Float = { distance -> distance },
    velocityThreshold: Float = IenSwipeBoxDefaults.velocityThreshold,
    animationSpec: SpringSpec<Float> = IenSwipeBoxDefaults.animationSpec,
    scrollableState: ScrollableState? = null,
    openSwipeBoxState: MutableState<AnchoredDraggableState<IenSwipeBoxStates>?> = mutableStateOf(null),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AnchoredDraggableState<IenSwipeBoxStates> {
    val density = LocalDensity.current
    val anchoredDraggableState = remember(key) {
        AnchoredDraggableState(
            initialValue = initialValue,
            snapAnimationSpec = animationSpec,
            decayAnimationSpec = splineBasedDecay(density),
            positionalThreshold = positionalThreshold,
            velocityThreshold = { velocityThreshold },
        )
    }

    ScrollEffect(scrollableState = scrollableState, swipeBoxState = anchoredDraggableState)
    ObserverGlobalSwipeBoxListenerEffect(
        state = anchoredDraggableState,
        openSwipeBoxState = openSwipeBoxState,
        coroutineScope = coroutineScope,
    )
    return anchoredDraggableState
}

private enum class IenSwipeActionPosition {
    Start,
    End,
}

private val LocalIenSwipeBoxItemFullSwipe = compositionLocalOf { false }
private val LocalIenSwipeBoxItemWidth = compositionLocalOf { 0.dp }
private val LocalIenSwipeBoxItemRevealScale = compositionLocalOf { 1f }
private val LocalIenSwipeBoxItemExpanding = compositionLocalOf { false }
private val LocalIenSwipeActionPosition = compositionLocalOf { IenSwipeActionPosition.Start }
private val LocalIenSwipeBoxState =
    compositionLocalOf<AnchoredDraggableState<IenSwipeBoxStates>> {
        error("No IenSwipeBoxState provided")
    }

@Composable
private fun AnchorsEffect(
    parentWidth: Int,
    fullExpansionStart: Boolean,
    isStartActionItemSupplied: Boolean,
    fullExpansionEnd: Boolean,
    isEndActionItemSupplied: Boolean,
    swipeBoxState: AnchoredDraggableState<IenSwipeBoxStates>,
    density: Density,
    amountOfStartActionItems: Int,
    amountOfEndActionItems: Int,
    actionItemWidth: Dp,
    actionRowOuterPadding: Dp,
    onAnchorsInitialized: (Boolean) -> Unit,
) {
    val totalStartActionItemWidth = actionItemWidth * amountOfStartActionItems + actionRowOuterPadding
    val totalEndActionItemWidth = actionItemWidth * amountOfEndActionItems + actionRowOuterPadding
    val startSwipeOffset = with(density) { totalStartActionItemWidth.toPx() }
    val endSwipeOffset = with(density) { totalEndActionItemWidth.toPx() }
    LaunchedEffect(parentWidth, totalStartActionItemWidth, totalEndActionItemWidth) {
        if (parentWidth > 0) {
            val fullSwipeStartOffset = parentWidth * if (amountOfStartActionItems >= 2) 0.85f else 0.5f
            val fullSwipeEndOffset = parentWidth * if (amountOfEndActionItems >= 2) 0.85f else 0.5f
            val anchors = DraggableAnchors {
                IenSwipeBoxStates.Resting at 0f
                if (isStartActionItemSupplied) IenSwipeBoxStates.StartVisible at startSwipeOffset
                if (isEndActionItemSupplied) IenSwipeBoxStates.EndVisible at -endSwipeOffset
                if (fullExpansionStart && isStartActionItemSupplied) {
                    IenSwipeBoxStates.StartFullyExpanded at fullSwipeStartOffset
                }
                if (fullExpansionEnd && isEndActionItemSupplied) {
                    IenSwipeBoxStates.EndFullyExpanded at -fullSwipeEndOffset
                }
            }
            swipeBoxState.updateAnchors(anchors)
            onAnchorsInitialized(true)
        }
    }
}

@Composable
private fun HapticFeedbackEffect(
    expansionHapticEnabled: Boolean,
    fullExpansionStart: Boolean,
    fullExpansionEnd: Boolean,
    isStartActionItemSupplied: Boolean,
    isEndActionItemSupplied: Boolean,
    startExpansionThresholdPx: Float,
    endExpansionThresholdPx: Float,
    swipeBoxState: AnchoredDraggableState<IenSwipeBoxStates>,
    hapticFeedback: IenSwipeBoxHapticFeedback,
) {
    LaunchedEffect(
        swipeBoxState,
        expansionHapticEnabled,
        fullExpansionStart,
        fullExpansionEnd,
        isStartActionItemSupplied,
        isEndActionItemSupplied,
        startExpansionThresholdPx,
        endExpansionThresholdPx,
    ) {
        var hasTriggeredHapticFeedback = false
        snapshotFlow { swipeBoxState.offset to swipeBoxState.targetValue }
            .collect { (offset, targetValue) ->
                val isExpansionThresholdReached = isSwipeBoxExpansionThresholdReached(
                    offset = offset,
                    targetValue = targetValue,
                    isStartActionItemSupplied = isStartActionItemSupplied,
                    isEndActionItemSupplied = isEndActionItemSupplied,
                    startExpansionThresholdPx = startExpansionThresholdPx,
                    endExpansionThresholdPx = endExpansionThresholdPx,
                    fullExpansionStart = fullExpansionStart,
                    fullExpansionEnd = fullExpansionEnd,
                )
                when {
                    expansionHapticEnabled &&
                        isExpansionThresholdReached &&
                        !hasTriggeredHapticFeedback -> {
                        hapticFeedback.performImpactLight()
                        hasTriggeredHapticFeedback = true
                    }
                    !isExpansionThresholdReached -> {
                        hasTriggeredHapticFeedback = false
                    }
                }
            }
    }
}

@Composable
private fun ScrollEffect(
    scrollableState: ScrollableState?,
    swipeBoxState: AnchoredDraggableState<IenSwipeBoxStates>,
) {
    LaunchedEffect(scrollableState) {
        snapshotFlow { scrollableState?.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling == true && swipeBoxState.currentValue != IenSwipeBoxStates.Resting) {
                    swipeBoxState.animateTo(IenSwipeBoxStates.Resting)
                }
            }
    }
}

@Composable
private fun DismissFullyExpandedEffect(
    swipeBoxState: AnchoredDraggableState<IenSwipeBoxStates>,
    isStartActionItemSupplied: Boolean,
    fullExpansionStart: Boolean,
    isEndActionItemSupplied: Boolean,
    fullExpansionEnd: Boolean,
    startFullExpansionOnClick: (() -> Unit)? = null,
    endFullExpansionOnClick: (() -> Unit)? = null,
) {
    LaunchedEffect(swipeBoxState.settledValue) {
        if (fullExpansionStart && isStartActionItemSupplied &&
            swipeBoxState.settledValue == IenSwipeBoxStates.StartFullyExpanded
        ) {
            dismissAndAnimate(swipeBoxState)
            startFullExpansionOnClick?.invoke()
        }
        if (fullExpansionEnd && isEndActionItemSupplied &&
            swipeBoxState.settledValue == IenSwipeBoxStates.EndFullyExpanded
        ) {
            dismissAndAnimate(swipeBoxState)
            endFullExpansionOnClick?.invoke()
        }
    }
}

@Composable
private fun ObserverGlobalSwipeBoxListenerEffect(
    state: AnchoredDraggableState<IenSwipeBoxStates>,
    openSwipeBoxState: MutableState<AnchoredDraggableState<IenSwipeBoxStates>?> = mutableStateOf(null),
    coroutineScope: CoroutineScope,
) {
    LaunchedEffect(state.currentValue) {
        if (state.currentValue != IenSwipeBoxStates.Resting) {
            val currentlyOpenState = openSwipeBoxState.value
            if (currentlyOpenState != null && currentlyOpenState != state) {
                coroutineScope.launch { dismissAndAnimate(currentlyOpenState) }
            }
            openSwipeBoxState.value = state
        } else if (openSwipeBoxState.value == state) {
            openSwipeBoxState.value = null
        }
    }
}

private suspend fun dismissAndAnimate(
    swipeBoxState: AnchoredDraggableState<IenSwipeBoxStates>,
) {
    delay(10.milliseconds)
    swipeBoxState.animateTo(IenSwipeBoxStates.Resting)
}

@Composable
private fun IenSwipeActionRow(
    width: Dp,
    alignToEnd: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    Layout(
        modifier = Modifier.fillMaxSize(),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = IenSwipeBoxDefaults.actionItemHorizontalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (alignToEnd) Arrangement.End else Arrangement.Start,
                content = content,
            )
        },
    ) { measurables, constraints ->
        val rowWidth = width.roundToPx()
        val rowHeight = constraints.maxHeight
        val placeable = measurables.single().measure(Constraints.fixed(rowWidth, rowHeight))
        layout(constraints.maxWidth, rowHeight) {
            val x = if (alignToEnd) constraints.maxWidth - rowWidth else 0
            placeable.placeRelative(x, 0)
        }
    }
}
