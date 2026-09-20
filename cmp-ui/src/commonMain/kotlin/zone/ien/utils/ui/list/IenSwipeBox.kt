@file:OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class,
    zone.ien.hig.InternalCupertinoApi::class,
)

package zone.ien.utils.ui.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zone.ien.hig.CupertinoHapticFeedback
import zone.ien.hig.CupertinoIcon
import zone.ien.hig.CupertinoText
import zone.ien.hig.LocalContainerColor
import zone.ien.hig.LocalContentColor
import zone.ien.hig.ProvideTextStyle
import zone.ien.hig.cupertinoTween
import zone.ien.hig.section.CupertinoSectionDefaults
import zone.ien.hig.theme.CupertinoColors
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.hig.theme.White
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

/** `IenSwipeBox`에 사용하는 기본 크기와 스프링 설정입니다. */
object IenSwipeBoxDefaults {
    /** 전체 스와이프 동작을 기본으로 허용할지 여부입니다. */
    const val allowFullSwipe: Boolean = true

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
 */
@Composable
fun IenSwipeBox(
    state: AnchoredDraggableState<IenSwipeBoxStates> = rememberIenSwipeBoxState(),
    modifier: Modifier = Modifier,
    itemWidth: Dp = IenSwipeBoxDefaults.actionItemWidth,
    height: Dp = IenSwipeBoxDefaults.actionItemHeight,
    startToEndFullSwipeEnabled: Boolean = IenSwipeBoxDefaults.allowFullSwipe,
    endToStartFullSwipeEnabled: Boolean = IenSwipeBoxDefaults.allowFullSwipe,
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
    val startFullSwipeAction = actionItems.startActions.firstOrNull()?.onClick
    val endFullSwipeAction = actionItems.endActions.lastOrNull()?.onClick

    val hapticFeedback = LocalHapticFeedback.current
    var hasTriggeredHapticFeedback by remember { mutableStateOf(false) }
    var anchorsInitialized by remember { mutableStateOf(false) }
    val isFullyExpandedStart = remember { mutableStateOf(false) }
    val isFullyExpandedEnd = remember { mutableStateOf(false) }

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
        actionRowOuterPadding = IenSwipeBoxDefaults.actionItemHorizontalPadding * 2,
    ) { anchorsInitialized = it }

    HapticFeedbackEffect(
        fullExpansionStart = startToEndFullSwipeEnabled,
        fullExpansionEnd = endToStartFullSwipeEnabled,
        isFullyExpandedStart = isFullyExpandedStart,
        isFullyExpandedEnd = isFullyExpandedEnd,
        swipeBoxState = state,
        hapticFeedback = hapticFeedback,
        hasTriggeredHapticFeedback = hasTriggeredHapticFeedback,
    ) { hasTriggeredHapticFeedback = it }

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
            val containerColor = LocalContainerColor.current.takeOrElse {
                CupertinoTheme.colorScheme.systemBackground
            }
            val offset by remember {
                derivedStateOf {
                    if (anchorsInitialized) state.offset else 0f
                }
            }
            val revealedWidth = with(density) { kotlin.math.abs(offset).toDp() }
            val isSwiping = offset != 0f
            val foregroundColor by animateColorAsState(
                targetValue = if (isSwiping) {
                    CupertinoTheme.colorScheme.secondarySystemFill
                } else {
                    containerColor
                },
                animationSpec = cupertinoTween(),
            )
            val foregroundCornerRadius by animateDpAsState(
                targetValue = if (isSwiping) 18.dp else 0.dp,
                animationSpec = cupertinoTween(),
            )

            if (offset > 0 && isStartActionItemSupplied) {
                CompositionLocalProvider(
                    LocalIenSwipeActionPosition provides IenSwipeActionPosition.Start,
                ) {
                    Box(
                        modifier = Modifier
                            .height(height)
                            .width(revealedWidth)
                            .clipToBounds()
                            .align(Alignment.CenterStart),
                    ) {
                        val actionRowOuterPadding =
                            IenSwipeBoxDefaults.actionItemHorizontalPadding * 2
                        val revealedActionContentWidth =
                            (revealedWidth - actionRowOuterPadding).coerceAtLeast(0.dp)
                        val normalActionRowWidth =
                            itemWidth * startActionsSize + actionRowOuterPadding
                        val actionRowWidth =
                            if (revealedWidth > normalActionRowWidth) {
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
                                            revealedWidth > normalActionRowWidth ||
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
                            .height(height)
                            .width(revealedWidth)
                            .clipToBounds()
                            .align(Alignment.CenterEnd),
                    ) {
                        val actionRowOuterPadding =
                            IenSwipeBoxDefaults.actionItemHorizontalPadding * 2
                        val revealedActionContentWidth =
                            (revealedWidth - actionRowOuterPadding).coerceAtLeast(0.dp)
                        val normalActionRowWidth =
                            itemWidth * endActionsSize + actionRowOuterPadding
                        val actionRowWidth =
                            if (revealedWidth > normalActionRowWidth) {
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
                                            revealedWidth > normalActionRowWidth ||
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

            if (anchorsInitialized) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(state.requireOffset().roundToInt(), 0) }
                        .anchoredDraggable(
                            state = state,
                            orientation = Orientation.Horizontal,
                        ),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height)
                            .clip(ContinuousRoundedRectangle(foregroundCornerRadius))
                            .background(foregroundColor)
                            .padding(
                                start = CupertinoSectionDefaults.PaddingValues
                                    .calculateStartPadding(LocalLayoutDirection.current),
                                end = CupertinoSectionDefaults.PaddingValues
                                    .calculateStartPadding(LocalLayoutDirection.current),
                            ),
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

/** `IenSwipeBox` 안에 표시하는 액션 아이템입니다. */
@Composable
fun RowScope.IenSwipeBoxItem(
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    restoreOnClick: Boolean = true,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    icon: ImageVector? = null,
    label: String? = null,
    weight: Float = 1f,
    shape: Shape = ContinuousCapsule(),
) {
    val state = LocalIenSwipeBoxState.current
    val actionPosition = LocalIenSwipeActionPosition.current
    val isFullSwipeActionItem = LocalIenSwipeBoxItemFullSwipe.current
    val itemWidth = LocalIenSwipeBoxItemWidth.current
    val revealScale = LocalIenSwipeBoxItemRevealScale.current
    val isExpanding = LocalIenSwipeBoxItemExpanding.current
    val collapsedItemSize =
        (itemWidth - IenSwipeBoxDefaults.actionItemHorizontalPadding * 2)
            .coerceAtLeast(0.dp)
    val isFullSwipeSettled =
        state.settledValue == IenSwipeBoxStates.EndFullyExpanded ||
            state.settledValue == IenSwipeBoxStates.StartFullyExpanded
    val isFullSwipeTarget =
        state.targetValue == IenSwipeBoxStates.EndFullyExpanded ||
            state.targetValue == IenSwipeBoxStates.StartFullyExpanded
    val shouldRenderItem = !isFullSwipeSettled || isFullSwipeActionItem
    val zIndex = if (isFullSwipeActionItem) 1f else 0f

    val coroutineScope = rememberCoroutineScope()
    val currentOnClick by rememberUpdatedState(onClick)
    val animatedItemWidth by animateDpAsState(
        targetValue = if (shouldRenderItem) itemWidth * weight else 0.dp,
        animationSpec = cupertinoTween(),
    )
    val animatedRevealScale by animateFloatAsState(
        targetValue = revealScale,
        animationSpec = spring(dampingRatio = 0.68f, stiffness = 500f),
    )
    val animatedItemAlpha by animateFloatAsState(
        targetValue = if (isFullSwipeTarget && !isFullSwipeActionItem) 0.35f else revealScale,
        animationSpec = cupertinoTween(),
    )
    val animHorizontalBias by animateFloatAsState(
        targetValue = when {
            isFullSwipeActionItem &&
                state.targetValue == IenSwipeBoxStates.EndFullyExpanded &&
                actionPosition == IenSwipeActionPosition.End -> -1f
            isFullSwipeActionItem &&
                state.targetValue == IenSwipeBoxStates.StartFullyExpanded &&
                actionPosition == IenSwipeActionPosition.Start -> 1f
            else -> 0f
        },
        animationSpec = cupertinoTween(),
    )

    CompositionLocalProvider(LocalContentColor provides CupertinoColors.White) {
        ProvideTextStyle(CupertinoTheme.typography.footnote) {
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
                Box(
                    modifier = Modifier
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
                        }
                        .clip(shape)
                        .background(color)
                        .clickable(
                            enabled = enabled,
                            indication = LocalIndication.current,
                            interactionSource = interactionSource,
                            onClick = {
                                currentOnClick()
                                if (restoreOnClick) {
                                    coroutineScope.launch {
                                        state.animateTo(IenSwipeBoxStates.Resting)
                                    }
                                }
                            },
                            onClickLabel = onClickLabel,
                            role = Role.Button,
                        )
                        .padding(horizontal = 10.dp),
                    contentAlignment = BiasAlignment(
                        verticalBias = 0f,
                        horizontalBias = animHorizontalBias,
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        icon?.let {
                            CupertinoIcon(
                                imageVector = it,
                                contentDescription = onClickLabel,
                                tint = CupertinoColors.White,
                                modifier = Modifier.requiredSize(16.dp),
                            )
                        }
                        label?.let {
                            CupertinoText(it, fontSize = 12.sp, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

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
    fullExpansionStart: Boolean,
    fullExpansionEnd: Boolean,
    isFullyExpandedStart: MutableState<Boolean>,
    isFullyExpandedEnd: MutableState<Boolean>,
    swipeBoxState: AnchoredDraggableState<IenSwipeBoxStates>,
    hapticFeedback: HapticFeedback,
    hasTriggeredHapticFeedback: Boolean,
    onHapticFeedbackTriggered: (Boolean) -> Unit,
) {
    LaunchedEffect(swipeBoxState.currentValue, swipeBoxState.targetValue) {
        val isStartFullSwipeTarget =
            fullExpansionStart && swipeBoxState.targetValue == IenSwipeBoxStates.StartFullyExpanded
        val isEndFullSwipeTarget =
            fullExpansionEnd && swipeBoxState.targetValue == IenSwipeBoxStates.EndFullyExpanded
        when {
            isStartFullSwipeTarget && !hasTriggeredHapticFeedback -> {
                hapticFeedback.performHapticFeedback(CupertinoHapticFeedback.ImpactLight)
                onHapticFeedbackTriggered(true)
                isFullyExpandedStart.value = true
                isFullyExpandedEnd.value = false
            }
            isEndFullSwipeTarget && !hasTriggeredHapticFeedback -> {
                hapticFeedback.performHapticFeedback(CupertinoHapticFeedback.ImpactLight)
                onHapticFeedbackTriggered(true)
                isFullyExpandedStart.value = false
                isFullyExpandedEnd.value = true
            }
            !isStartFullSwipeTarget && !isEndFullSwipeTarget -> {
                onHapticFeedbackTriggered(false)
                isFullyExpandedStart.value = false
                isFullyExpandedEnd.value = false
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
