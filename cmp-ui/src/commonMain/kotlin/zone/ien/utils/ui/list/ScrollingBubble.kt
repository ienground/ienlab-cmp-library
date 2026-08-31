package zone.ien.utils.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import zone.ien.utils.ui.foundation.IenTheme

private val ScrollingBubbleSize = 112.dp
private val ScrollingBubbleMinThumbHeight = 48.dp
private val ScrollingBubbleScrollbarWidth = 4.dp
private val ScrollingBubbleGap = 8.dp
private val ScrollingBubbleShape = RoundedCornerShape(
    topStart = 56.dp,
    topEnd = 0.dp,
    bottomEnd = 0.dp,
    bottomStart = 56.dp,
)

private data class ScrollingBubbleData(
    val index: Int,
    val firstVisibleItemScrollOffset: Int,
    val firstVisibleItemSize: Int,
    val visibleItemCount: Int,
    val totalItemCount: Int,
)

/**
 * 리스트를 스크롤하는 동안 스크롤바 썸과 함께 현재 항목을 버블로 보여주는 컨테이너입니다.
 *
 * [content]에는 [state]를 사용하는 Lazy 리스트를 전달하고, [bubbleContent]에서는
 * 현재 첫 번째 표시 항목의 인덱스를 이용해 버블 내용을 구성할 수 있습니다. 버블은
 * 스크롤 영역의 오른쪽에 배치되며 현재 스크롤 위치에 따라 세로로 이동합니다.
 *
 * @param state 콘텐츠에 연결된 [LazyListState]
 * @param modifier 컨테이너에 적용할 [Modifier]
 * @param bubbleContent 스크롤 중인 항목 인덱스로 버블 내부를 구성하는 슬롯
 * @param content 스크롤 가능한 리스트 콘텐츠
 */
@Composable
fun ScrollingBubble(
    state: LazyListState,
    modifier: Modifier = Modifier,
    bubbleContent: @Composable (index: Int) -> Unit,
    content: @Composable () -> Unit,
) {
    val bubbleData by remember(state) {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val firstVisibleItem = layoutInfo.visibleItemsInfo.firstOrNull()
            val index = scrollingBubbleIndex(
                isScrollInProgress = state.isScrollInProgress,
                firstVisibleItemIndex = state.firstVisibleItemIndex,
                itemCount = layoutInfo.totalItemsCount,
            )

            if (index == null || firstVisibleItem == null) {
                null
            } else {
                ScrollingBubbleData(
                    index = index,
                    firstVisibleItemScrollOffset = state.firstVisibleItemScrollOffset,
                    firstVisibleItemSize = firstVisibleItem.size,
                    visibleItemCount = layoutInfo.visibleItemsInfo.size,
                    totalItemCount = layoutInfo.totalItemsCount,
                )
            }
        }
    }

    BoxWithConstraints(modifier = modifier) {
        content()

        val density = androidx.compose.ui.platform.LocalDensity.current
        val trackHeightPx = with(density) { maxHeight.toPx() }
        val bubbleHeightPx = with(density) { ScrollingBubbleSize.toPx() }
        val minimumThumbHeightPx = with(density) { ScrollingBubbleMinThumbHeight.toPx() }
        val horizontalOffsetPx = with(density) {
            -(ScrollingBubbleScrollbarWidth + ScrollingBubbleGap).roundToPx()
        }
        val data = bubbleData
        val position = data?.let { data ->
            scrollingBubblePosition(
                trackHeight = trackHeightPx,
                bubbleHeight = bubbleHeightPx,
                firstVisibleItemIndex = data.index,
                firstVisibleItemScrollOffset = data.firstVisibleItemScrollOffset,
                firstVisibleItemSize = data.firstVisibleItemSize,
                visibleItemCount = data.visibleItemCount,
                totalItemCount = data.totalItemCount,
                minimumThumbHeight = minimumThumbHeightPx,
            )
        }

        if (position != null) {
            val thumbHeight = with(density) { position.thumbHeight.toDp() }
            val scrollbarColor = IenTheme.colors.border.copy(alpha = 0.72f)
            val bubbleIndex = data.index

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxHeight()
                    .width(ScrollingBubbleScrollbarWidth)
                    .background(scrollbarColor),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset { IntOffset(0, position.thumbTop.roundToInt()) }
                    .width(ScrollingBubbleScrollbarWidth)
                    .height(thumbHeight)
                    .background(IenTheme.colors.brand),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset { IntOffset(horizontalOffsetPx, position.bubbleTop.roundToInt()) }
                    .size(ScrollingBubbleSize)
                    .clip(ScrollingBubbleShape)
                    .background(IenTheme.colors.brand),
                contentAlignment = Alignment.Center,
            ) {
                CompositionLocalProvider(LocalContentColor provides IenTheme.colors.onBrand) {
                    bubbleContent(bubbleIndex)
                }
            }
        }
    }
}

internal fun scrollingBubbleIndex(
    isScrollInProgress: Boolean,
    firstVisibleItemIndex: Int,
    itemCount: Int,
): Int? {
    return firstVisibleItemIndex.takeIf {
        isScrollInProgress && itemCount > 0 && it in 0 until itemCount
    }
}

internal data class ScrollingBubblePosition(
    val thumbTop: Float,
    val thumbHeight: Float,
    val bubbleTop: Float,
)

internal fun scrollingBubblePosition(
    trackHeight: Float,
    bubbleHeight: Float,
    firstVisibleItemIndex: Int,
    firstVisibleItemScrollOffset: Int,
    firstVisibleItemSize: Int,
    visibleItemCount: Int,
    totalItemCount: Int,
    minimumThumbHeight: Float,
): ScrollingBubblePosition? {
    if (
        !trackHeight.isFinite() ||
        trackHeight <= 0f ||
        bubbleHeight <= 0f ||
        firstVisibleItemIndex < 0 ||
        firstVisibleItemScrollOffset < 0 ||
        firstVisibleItemSize <= 0 ||
        visibleItemCount <= 0 ||
        totalItemCount <= visibleItemCount
    ) {
        return null
    }

    val maxScrollOffset = ((totalItemCount - visibleItemCount) * firstVisibleItemSize).toFloat()
    if (maxScrollOffset <= 0f) return null

    val scrollOffset = (
        firstVisibleItemIndex * firstVisibleItemSize + firstVisibleItemScrollOffset
        ).toFloat()
    val scrollFraction = (scrollOffset / maxScrollOffset).coerceIn(0f, 1f)
    val thumbHeight = (trackHeight * visibleItemCount / totalItemCount)
        .coerceAtLeast(minimumThumbHeight)
        .coerceAtMost(trackHeight)
    val thumbTop = (trackHeight - thumbHeight) * scrollFraction
    val bubbleTop = (
        thumbTop + (thumbHeight - bubbleHeight) / 2f
        ).coerceIn(0f, (trackHeight - bubbleHeight).coerceAtLeast(0f))

    return ScrollingBubblePosition(
        thumbTop = thumbTop,
        thumbHeight = thumbHeight,
        bubbleTop = bubbleTop,
    )
}
