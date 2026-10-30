@file:OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class,
)

package zone.ien.utils.ui.list

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
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.animateToWithDecay
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.snapTo
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zone.ien.utils.icon.IconData
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
import kotlin.math.abs
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
    modifier: Modifier = Modifier,
    state: IenSwipeBoxState = rememberIenSwipeBoxState(),
    itemWidth: Dp = IenSwipeBoxDefaults.actionItemWidth,
    height: Dp = Dp.Unspecified,
    startToEndFullSwipeEnabled: Boolean = IenSwipeBoxDefaults.allowFullSwipe,
    endToStartFullSwipeEnabled: Boolean = IenSwipeBoxDefaults.allowFullSwipe,
    expansionHapticEnabled: Boolean = IenSwipeBoxDefaults.expansionHapticEnabled,
    actionItemBuilder: IenSwipeBoxActionsBuilder.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val reverseDirection = LocalLayoutDirection.current == LayoutDirection.Rtl
    IenSwipeBoxContent(
        modifier = modifier,
        state = state.anchoredDraggableState,
        dragModifier = Modifier.draggable(
            state = state.dragState,
            orientation = Orientation.Horizontal,
            reverseDirection = reverseDirection,
            startDragImmediately = state.isAnimationRunning,
            onDragStopped = { velocity -> state.settle(velocity) },
        ),
        restoreAnimationSpec = state.animationSpec,
        itemWidth = itemWidth,
        height = height,
        startToEndFullSwipeEnabled = startToEndFullSwipeEnabled,
        endToStartFullSwipeEnabled = endToStartFullSwipeEnabled,
        expansionHapticEnabled = expansionHapticEnabled,
        actionItemBuilder = actionItemBuilder,
        content = content,
    )
}

/** 직접 생성한 Compose 앵커 상태를 사용하는 기존 호출을 지원합니다. */
@Composable
fun IenSwipeBox(
    modifier: Modifier = Modifier,
    state: AnchoredDraggableState<IenSwipeBoxStates>,
    itemWidth: Dp = IenSwipeBoxDefaults.actionItemWidth,
    height: Dp = Dp.Unspecified,
    startToEndFullSwipeEnabled: Boolean = IenSwipeBoxDefaults.allowFullSwipe,
    endToStartFullSwipeEnabled: Boolean = IenSwipeBoxDefaults.allowFullSwipe,
    expansionHapticEnabled: Boolean = IenSwipeBoxDefaults.expansionHapticEnabled,
    actionItemBuilder: IenSwipeBoxActionsBuilder.() -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    IenSwipeBoxContent(
        modifier = modifier,
        state = state,
        dragModifier = Modifier.anchoredDraggable(state, Orientation.Horizontal),
        restoreAnimationSpec = null,
        itemWidth = itemWidth,
        height = height,
        startToEndFullSwipeEnabled = startToEndFullSwipeEnabled,
        endToStartFullSwipeEnabled = endToStartFullSwipeEnabled,
        expansionHapticEnabled = expansionHapticEnabled,
        actionItemBuilder = actionItemBuilder,
        content = content,
    )
}

@Composable
private fun IenSwipeBoxContent(
    modifier: Modifier,
    state: AnchoredDraggableState<IenSwipeBoxStates>,
    dragModifier: Modifier,
    restoreAnimationSpec: AnimationSpec<Float>?,
    itemWidth: Dp,
    height: Dp,
    startToEndFullSwipeEnabled: Boolean,
    endToStartFullSwipeEnabled: Boolean,
    expansionHapticEnabled: Boolean,
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
    var anchorsInitialized by remember(state) { mutableStateOf(false) }
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
        restoreAnimationSpec = restoreAnimationSpec,
        startFullExpansionOnClick = startFullSwipeAction,
        endFullExpansionOnClick = endFullSwipeAction,
    )

    CompositionLocalProvider(
        LocalIenSwipeBoxState provides state,
        LocalIenSwipeBoxRestoreAnimationSpec provides restoreAnimationSpec,
    ) {
        Box(
            modifier = modifier.onGloballyPositioned { coordinates ->
                parentWidth = coordinates.size.width
            },
        ) {
            val offset by remember(state) {
                derivedStateOf {
                    if (anchorsInitialized) state.offset else 0f
                }
            }
            val revealedWidth = with(density) { kotlin.math.abs(offset).toDp() }
            val isSwiping = offset != 0f
            val swipeBackgroundColor = IenTheme.colors.surfaceVariant
            val foregroundColorAlpha by animateFloatAsState(
                targetValue = if (isSwiping) swipeBackgroundColor.alpha else 0f,
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
                                    LocalIenSwipeBoxItemWidth provides itemWidth,
                                    LocalIenSwipeBoxItemLabelVisible provides (
                                        index == 0 && (
                                            revealedWidth > normalActionRowWidth + IenTheme.spacing.xl ||
                                                state.targetValue == IenSwipeBoxStates.StartFullyExpanded
                                            )
                                        ),
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
                                    LocalIenSwipeBoxItemWidth provides itemWidth,
                                    LocalIenSwipeBoxItemLabelVisible provides (
                                        index == actionItems.endActions.lastIndex && (
                                            revealedWidth > normalActionRowWidth + IenTheme.spacing.xl ||
                                                state.targetValue == IenSwipeBoxStates.EndFullyExpanded
                                            )
                                        ),
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
                            dragModifier
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
                        .then(
                            if (isSwiping || foregroundColorAlpha > 0f) {
                                Modifier.background(
                                    swipeBackgroundColor.copy(alpha = foregroundColorAlpha),
                                )
                            } else {
                                Modifier
                            },
                        ),
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
    icon: IconData? = null,
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
            { Text(text = labelText, maxLines = 1) }
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
    icon: IconData? = null,
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
    icon: IconData?,
    labelContent: (@Composable () -> Unit)?,
    weight: Float,
    showLabelOnExpansion: Boolean,
) {
    val swipeBoxState = LocalIenSwipeBoxState.current
    val restoreAnimationSpec = LocalIenSwipeBoxRestoreAnimationSpec.current
    val actionPosition = LocalIenSwipeActionPosition.current
    val isFullSwipeActionItem = LocalIenSwipeBoxItemFullSwipe.current
    val itemWidth = LocalIenSwipeBoxItemWidth.current
    val revealScale = LocalIenSwipeBoxItemRevealScale.current
    val isLabelVisible = LocalIenSwipeBoxItemLabelVisible.current
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
                        animateSwipeBoxToRest(swipeBoxState, restoreAnimationSpec)
                    }
                }
            },
            modifier = Modifier
                .then(
                    if (isFullSwipeActionItem) {
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
                                    icon = it,
                                    contentDescription = onClickLabel,
                                    size = IenTheme.icon.sm,
                                )
                            }
                            labelContent?.let { content ->
                                AnimatedVisibility(
                                    visible = shouldRenderSwipeBoxLabel(
                                        isExpanding = isLabelVisible,
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
    expansionThreshold: Dp = 0.dp,
): Boolean =
    revealedWidth > itemWidth * actionCount + actionRowOuterPadding + expansionThreshold

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

/**
 * 앵커 상태와 사용자 지정 플링·복원 애니메이션을 함께 소유합니다.
 *
 * Compose 앵커 API에 직접 접근할 때는 [anchoredDraggableState]를 사용합니다.
 */
@Stable
class IenSwipeBoxState internal constructor(
    initialValue: IenSwipeBoxStates,
    private val positionalThreshold: (Float) -> Float,
    private val velocityThreshold: Float,
    internal val animationSpec: SpringSpec<Float>,
    private val decayAnimationSpec: DecayAnimationSpec<Float>,
) {
    /** Compose의 앵커 갱신·관찰 API와 연동할 때 사용하는 상태입니다. */
    val anchoredDraggableState = AnchoredDraggableState(initialValue)

    /** 현재 통과한 앵커입니다. */
    val currentValue: IenSwipeBoxStates get() = anchoredDraggableState.currentValue

    /** 마지막으로 정착한 앵커입니다. */
    val settledValue: IenSwipeBoxStates get() = anchoredDraggableState.settledValue

    /** 현재 드래그 또는 애니메이션의 목표 앵커입니다. */
    val targetValue: IenSwipeBoxStates get() = anchoredDraggableState.targetValue

    /** 초기 배치 전에는 NaN인 수평 이동량입니다. */
    val offset: Float get() = anchoredDraggableState.offset

    /** 프로그램 애니메이션이나 플링 정착이 진행 중인지 나타냅니다. */
    val isAnimationRunning: Boolean get() = anchoredDraggableState.isAnimationRunning

    internal val dragState = object : DraggableState {
        override suspend fun drag(dragPriority: MutatePriority, block: suspend DragScope.() -> Unit) {
            anchoredDraggableState.anchoredDrag(dragPriority) { anchors ->
                val anchoredScope = this
                block(object : DragScope {
                    override fun dragBy(pixels: Float) {
                        anchoredScope.dragTo(
                            (anchoredDraggableState.requireOffset() + pixels)
                                .coerceIn(anchors.minPosition(), anchors.maxPosition()),
                        )
                    }
                })
            }
        }

        override fun dispatchRawDelta(delta: Float) {
            anchoredDraggableState.dispatchRawDelta(delta)
        }
    }

    /** 생성 시 지정한 스프링으로 목표 앵커까지 이동합니다. */
    suspend fun animateTo(targetValue: IenSwipeBoxStates) {
        anchoredDraggableState.animateTo(targetValue, animationSpec)
    }

    /** 애니메이션 없이 목표 앵커로 이동합니다. */
    suspend fun snapTo(targetValue: IenSwipeBoxStates) {
        anchoredDraggableState.snapTo(targetValue)
    }

    internal suspend fun settle(velocity: Float) {
        val target = swipeBoxTargetValue(
            anchors = anchoredDraggableState.anchors,
            offset = anchoredDraggableState.requireOffset(),
            velocity = velocity,
            velocityThreshold = velocityThreshold,
            positionalThreshold = positionalThreshold,
        ) ?: return
        anchoredDraggableState.animateToWithDecay(target, velocity, animationSpec, decayAnimationSpec)
    }
}

internal fun swipeBoxTargetValue(
    anchors: DraggableAnchors<IenSwipeBoxStates>,
    offset: Float,
    velocity: Float,
    velocityThreshold: Float,
    positionalThreshold: (Float) -> Float,
): IenSwipeBoxStates? {
    if (velocity == 0f) return anchors.closestAnchor(offset)
    val forwards = velocity > 0f
    if (abs(velocity) >= abs(velocityThreshold)) {
        return anchors.closestAnchor(offset, forwards)
    }
    val left = anchors.closestAnchor(offset, false) ?: return null
    val right = anchors.closestAnchor(offset, true) ?: return null
    val leftPosition = anchors.positionOf(left)
    val rightPosition = anchors.positionOf(right)
    val distanceFromStart = abs(offset - if (forwards) leftPosition else rightPosition)
    val crossedThreshold = distanceFromStart >= abs(positionalThreshold(abs(rightPosition - leftPosition)))
    return if (forwards == crossedThreshold) right else left
}

/**
 * `IenSwipeBox`의 상태와 플링 설정을 생성하고 스크롤·다른 행의 열림에 따라 닫습니다.
 *
 * 설정은 [key]가 바뀔 때 다시 생성됩니다. 반환 타입은 전용 [IenSwipeBoxState]이며,
 * 기존의 명시적인 `AnchoredDraggableState` 참조는 [IenSwipeBoxState.anchoredDraggableState]로 접근합니다.
 */
@Composable
fun rememberIenSwipeBoxState(
    key: Any? = null,
    initialValue: IenSwipeBoxStates = IenSwipeBoxStates.Resting,
    positionalThreshold: (distance: Float) -> Float = { distance -> distance },
    velocityThreshold: Float = IenSwipeBoxDefaults.velocityThreshold,
    animationSpec: SpringSpec<Float> = IenSwipeBoxDefaults.animationSpec,
    scrollableState: ScrollableState? = null,
    openSwipeBoxState: MutableState<IenSwipeBoxState?> = remember { mutableStateOf(null) },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): IenSwipeBoxState {
    val density = LocalDensity.current
    val state = remember(key) {
        IenSwipeBoxState(initialValue, positionalThreshold, velocityThreshold, animationSpec, splineBasedDecay(density))
    }
    ScrollEffect(scrollableState = scrollableState, swipeBoxState = state)
    ObserverGlobalSwipeBoxListenerEffect(state = state, openSwipeBoxState = openSwipeBoxState, coroutineScope = coroutineScope)
    return state
}

private enum class IenSwipeActionPosition {
    Start,
    End,
}

private val LocalIenSwipeBoxItemFullSwipe = compositionLocalOf { false }
private val LocalIenSwipeBoxItemWidth = compositionLocalOf { 0.dp }
private val LocalIenSwipeBoxItemRevealScale = compositionLocalOf { 1f }
private val LocalIenSwipeBoxItemLabelVisible = compositionLocalOf { false }
private val LocalIenSwipeActionPosition = compositionLocalOf { IenSwipeActionPosition.Start }
private val LocalIenSwipeBoxRestoreAnimationSpec = compositionLocalOf<AnimationSpec<Float>?> { null }
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
    LaunchedEffect(
        swipeBoxState,
        parentWidth,
        totalStartActionItemWidth,
        totalEndActionItemWidth,
        fullExpansionStart,
        fullExpansionEnd,
    ) {
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
    swipeBoxState: IenSwipeBoxState,
) {
    LaunchedEffect(scrollableState, swipeBoxState) {
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
    restoreAnimationSpec: AnimationSpec<Float>?,
    startFullExpansionOnClick: (() -> Unit)? = null,
    endFullExpansionOnClick: (() -> Unit)? = null,
) {
    LaunchedEffect(swipeBoxState, swipeBoxState.settledValue) {
        if (fullExpansionStart && isStartActionItemSupplied &&
            swipeBoxState.settledValue == IenSwipeBoxStates.StartFullyExpanded
        ) {
            dismissAndAnimate(swipeBoxState, restoreAnimationSpec)
            startFullExpansionOnClick?.invoke()
        }
        if (fullExpansionEnd && isEndActionItemSupplied &&
            swipeBoxState.settledValue == IenSwipeBoxStates.EndFullyExpanded
        ) {
            dismissAndAnimate(swipeBoxState, restoreAnimationSpec)
            endFullExpansionOnClick?.invoke()
        }
    }
}

@Composable
private fun ObserverGlobalSwipeBoxListenerEffect(
    state: IenSwipeBoxState,
    openSwipeBoxState: MutableState<IenSwipeBoxState?>,
    coroutineScope: CoroutineScope,
) {
    DisposableEffect(state, openSwipeBoxState) {
        onDispose {
            if (openSwipeBoxState.value == state) openSwipeBoxState.value = null
        }
    }
    LaunchedEffect(state, openSwipeBoxState, coroutineScope, state.currentValue) {
        if (state.currentValue != IenSwipeBoxStates.Resting) {
            val currentlyOpenState = openSwipeBoxState.value
            if (currentlyOpenState != null && currentlyOpenState != state) {
                coroutineScope.launch {
                    dismissAndAnimate(
                        currentlyOpenState.anchoredDraggableState,
                        currentlyOpenState.animationSpec,
                    )
                }
            }
            openSwipeBoxState.value = state
        } else if (openSwipeBoxState.value == state) {
            openSwipeBoxState.value = null
        }
    }
}

private suspend fun dismissAndAnimate(
    swipeBoxState: AnchoredDraggableState<IenSwipeBoxStates>,
    animationSpec: AnimationSpec<Float>?,
) {
    delay(10.milliseconds)
    animateSwipeBoxToRest(swipeBoxState, animationSpec)
}

private suspend fun animateSwipeBoxToRest(
    state: AnchoredDraggableState<IenSwipeBoxStates>,
    animationSpec: AnimationSpec<Float>?,
) {
    if (animationSpec == null) state.animateTo(IenSwipeBoxStates.Resting)
    else state.animateTo(IenSwipeBoxStates.Resting, animationSpec)
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
