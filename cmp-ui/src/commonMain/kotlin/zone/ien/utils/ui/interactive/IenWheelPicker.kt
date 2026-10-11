package zone.ien.utils.ui.interactive

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.utils.shakeOnDisabledClick
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * 중앙 항목에 스냅되는 세로 휠 선택기. 선택 상태는 호출자가 소유하며 항목 변경 시 햅틱과 소리 피드백을 제공합니다.
 * JWheelPicker의 중앙 여백 및 원통형 회전 효과를 공통 Compose로 이식했습니다.
 * 원본: https://github.com/oOJohn6Oo/JWheelPicker (WTFPL)
 *
 * @param items 비어 있지 않은 선택 항목 목록.
 * @param selectedIndex 현재 선택된 항목의 인덱스.
 * @param onSelectedIndexChange 스크롤이 멈추거나 접근성 작업 또는 항목 탭으로 선택될 때 호출됩니다.
 * @param modifier 루트 레이아웃에 적용할 Modifier.
 * @param enabled 스크롤 및 항목 선택 가능 여부.
 * @param visibleItemCount 표시할 행 수. 3 이상의 홀수여야 합니다.
 * @param enableHapticFeedback 스크롤 중 중앙 항목 변경 시 햅틱 사용 여부.
 * @param itemLabel 각 항목에 표시할 텍스트.
 */
@Composable
fun <T> IenWheelPicker(
    items: List<T>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    visibleItemCount: Int = 5,
    enableHapticFeedback: Boolean = true,
    itemLabel: (T) -> String = { it.toString() },
) {
    require(items.isNotEmpty()) { "선택 항목은 비어 있을 수 없습니다." }
    require(selectedIndex in items.indices) { "선택 인덱스가 항목 범위를 벗어났습니다." }
    require(visibleItemCount >= 3 && visibleItemCount % 2 == 1) { "표시할 행 수는 3 이상의 홀수여야 합니다." }
    val style = IenTheme.typography.title3
    val rowHeight = with(LocalDensity.current) {
        val textHeight = when {
            style.lineHeight != TextUnit.Unspecified -> style.lineHeight.toDp()
            style.fontSize != TextUnit.Unspecified -> style.fontSize.toDp()
            else -> 0.dp
        }
        maxOf(IenTheme.state.minimumTouchTarget, textHeight + IenTheme.spacing.xs)
    }
    val state = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    val scope = rememberCoroutineScope()
    val soundFeedback = rememberIenWheelPickerSoundFeedback()
    val currentSelectedIndex by rememberUpdatedState(selectedIndex)
    val currentOnChange by rememberUpdatedState(onSelectedIndexChange)
    val currentEnabled by rememberUpdatedState(enabled)
    val currentHapticEnabled by rememberUpdatedState(enableHapticFeedback)
    var synchronizing by remember { mutableStateOf(false) }
    val centerIndex by remember(state) { derivedStateOf { state.wheelCenterIndex() } }

    LaunchedEffect(selectedIndex, items) {
        if (state.wheelCenterIndex() != selectedIndex || !state.isScrollInProgress) {
            synchronizing = true
            try {
                state.scrollToItem(selectedIndex)
            } finally {
                synchronizing = false
            }
        }
    }
    LaunchedEffect(state, items.size, soundFeedback) {
        var wasScrolling = false
        var previousIndex = selectedIndex
        snapshotFlow { state.isScrollInProgress to state.wheelCenterIndex() }.collect { (scrolling, index) ->
            if (scrolling && !synchronizing && currentEnabled && index != previousIndex && index in items.indices) {
                soundFeedback.playTick(currentHapticEnabled)
            }
            if (wasScrolling && !scrolling && !synchronizing && currentEnabled &&
                index in items.indices && index != currentSelectedIndex
            ) {
                currentOnChange(index)
            }
            previousIndex = index
            wasScrolling = scrolling
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .shakeOnDisabledClick(enabled)
            .height(rowHeight * visibleItemCount)
            .clipToBounds()
            .alpha(if (enabled) 1f else IenTheme.state.disabledAlpha)
            .semantics {
                stateDescription = itemLabel(items[selectedIndex])
                progressBarRangeInfo = ProgressBarRangeInfo(
                    selectedIndex.toFloat(), 0f..items.lastIndex.toFloat(), (items.size - 2).coerceAtLeast(0),
                )
                if (!enabled) disabled()
                else setProgress { value ->
                    val index = value.roundToInt().coerceIn(items.indices)
                    if (index != selectedIndex) {
                        soundFeedback.playTick(currentHapticEnabled)
                    }
                    onSelectedIndexChange(index)
                    true
                }
            },
    ) {
        Box(
            Modifier.align(Alignment.Center).fillMaxWidth().height(rowHeight)
                .background(IenTheme.colors.brand.copy(alpha = IenTheme.state.selectedAlpha), RoundedCornerShape(IenTheme.radius.sm)),
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth().height(maxHeight).nestedScroll(WheelParentScrollConsumer),
            state = state,
            contentPadding = PaddingValues(vertical = ((maxHeight - rowHeight) / 2).coerceAtLeast(0.dp)),
            flingBehavior = rememberSnapFlingBehavior(state),
            userScrollEnabled = enabled,
            overscrollEffect = null,
        ) {
            items(items.size) { index ->
                Box(
                    modifier = Modifier.fillMaxWidth().height(rowHeight)
                        .graphicsLayer {
                            val layout = state.layoutInfo
                            val item = layout.visibleItemsInfo.firstOrNull { it.index == index } ?: return@graphicsLayer
                            val center = (layout.viewportStartOffset + layout.viewportEndOffset) / 2f
                            val position = item.offset + item.size / 2f
                            val radius = layout.viewportSize.height / 2f
                            if (radius <= 0f) return@graphicsLayer
                            val offset = ((position - center) / radius).coerceIn(-1f, 1f)
                            rotationX = -90f * offset
                            scaleX = 1f - offset * offset * 0.37f
                            alpha = 1f - abs(offset)
                            translationY = (sin(offset * PI / 2) * (2 * radius / PI) * 1.24 - (position - center)).toFloat()
                        }
                        .semantics { selected = index == centerIndex }
                        .clickable(
                            enabled = enabled,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            if (index == currentSelectedIndex) {
                                currentOnChange(index)
                            } else {
                                scope.launch { state.animateScrollToItem(index) }
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = itemLabel(items[index]),
                        style = style,
                        color = if (index == centerIndex) IenTheme.colors.brand else IenTheme.colors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

private fun LazyListState.wheelCenterIndex(): Int {
    val layout = layoutInfo
    val center = (layout.viewportStartOffset + layout.viewportEndOffset) / 2f
    return layout.visibleItemsInfo.minByOrNull { abs(it.offset + it.size / 2f - center) }?.index ?: -1
}

private val WheelParentScrollConsumer = object : NestedScrollConnection {
    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource) = Offset(0f, available.y)
    override suspend fun onPostFling(consumed: Velocity, available: Velocity) = Velocity(0f, available.y)
}
