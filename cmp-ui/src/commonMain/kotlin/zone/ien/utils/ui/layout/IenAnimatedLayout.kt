package zone.ien.utils.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.foundation.IenTheme

/**
 * 항목이 추가되거나 제거될 때 수직으로 나타나고 사라지는 레이아웃입니다.
 *
 * 항목은 [key]가 반환하는 고유 키로 식별됩니다. 항목을 제거할 때는
 * 사라지는 애니메이션이 끝난 뒤 컴포지션에서 제거되므로, [LazyColumn] 없이도
 * 콘텐츠 높이가 자연스럽게 줄어듭니다. [key]는 목록 내에서 서로 다른 값을
 * 반환해야 합니다.
 *
 * @param items 표시할 항목 목록
 * @param key 각 항목의 고유 키를 반환하는 함수
 * @param modifier 레이아웃에 적용할 [Modifier]
 * @param verticalArrangement 항목을 수직으로 배치하는 방식
 * @param horizontalAlignment 항목의 수평 정렬 방식
 * @param content 각 항목을 표시하는 컴포저블
 */
@Composable
fun <T> IenAnimatedColumn(
    items: List<T>,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable (T) -> Unit,
) {
    IenAnimatedItems(
        items = items,
        itemKey = key,
        modifier = modifier,
        orientation = AnimatedLayoutOrientation.Vertical,
        layout = { animatedModifier, animatedContent ->
            Column(
                modifier = animatedModifier,
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
            ) {
                animatedContent()
            }
        },
        content = content,
    )
}

/**
 * 항목이 추가되거나 제거될 때 수평으로 나타나고 사라지는 레이아웃입니다.
 *
 * 항목은 [key]가 반환하는 고유 키로 식별됩니다. 항목을 제거할 때는
 * 사라지는 애니메이션이 끝난 뒤 컴포지션에서 제거되므로, [LazyRow] 없이도
 * 콘텐츠 너비가 자연스럽게 줄어듭니다. [key]는 목록 내에서 서로 다른 값을
 * 반환해야 합니다.
 *
 * @param items 표시할 항목 목록
 * @param key 각 항목의 고유 키를 반환하는 함수
 * @param modifier 레이아웃에 적용할 [Modifier]
 * @param horizontalArrangement 항목을 수평으로 배치하는 방식
 * @param verticalAlignment 항목의 수직 정렬 방식
 * @param content 각 항목을 표시하는 컴포저블
 */
@Composable
fun <T> IenAnimatedRow(
    items: List<T>,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable (T) -> Unit,
) {
    IenAnimatedItems(
        items = items,
        itemKey = key,
        modifier = modifier,
        orientation = AnimatedLayoutOrientation.Horizontal,
        layout = { animatedModifier, animatedContent ->
            Row(
                modifier = animatedModifier,
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = verticalAlignment,
            ) {
                animatedContent()
            }
        },
        content = content,
    )
}

internal data class AnimatedLayoutItem<T>(
    val key: Any,
    val value: T,
    val visible: Boolean,
)

private enum class AnimatedLayoutOrientation {
    Vertical,
    Horizontal,
}

internal fun <T> mergeAnimatedLayoutItems(
    currentItems: List<AnimatedLayoutItem<T>>,
    incomingItems: List<T>,
    itemKey: (T) -> Any,
): List<AnimatedLayoutItem<T>> {
    val nextItems = incomingItems.map { item ->
        AnimatedLayoutItem(
            key = itemKey(item),
            value = item,
            visible = true,
        )
    }
    val incomingKeys = nextItems.mapTo(mutableSetOf()) { it.key }
    val currentItemsByKey = currentItems.associateBy { it.key }

    return buildList {
        nextItems.forEach { incomingItem ->
            add(currentItemsByKey[incomingItem.key]?.copy(
                value = incomingItem.value,
                visible = true,
            ) ?: incomingItem)
        }
        currentItems
            .filter { it.key !in incomingKeys }
            .forEach { add(it.copy(visible = false)) }
    }
}

@Composable
private fun <T> IenAnimatedItems(
    items: List<T>,
    itemKey: (T) -> Any,
    modifier: Modifier,
    orientation: AnimatedLayoutOrientation,
    layout: @Composable (Modifier, @Composable () -> Unit) -> Unit,
    content: @Composable (T) -> Unit,
) {
    var animatedItems by remember {
        mutableStateOf(
            items.map { item ->
                AnimatedLayoutItem(
                    key = itemKey(item),
                    value = item,
                    visible = true,
                )
            },
        )
    }

    LaunchedEffect(items) {
        animatedItems = mergeAnimatedLayoutItems(
            currentItems = animatedItems,
            incomingItems = items,
            itemKey = itemKey,
        )
    }

    val motion = IenTheme.motion
    val enterTransition = if (orientation == AnimatedLayoutOrientation.Vertical) {
        fadeIn(
            animationSpec = tween(
                durationMillis = motion.normalMillis,
                easing = motion.standardEasing,
            ),
        ) + expandVertically(
            animationSpec = tween(
                durationMillis = motion.normalMillis,
                easing = motion.standardEasing,
            ),
        )
    } else {
        fadeIn(
            animationSpec = tween(
                durationMillis = motion.normalMillis,
                easing = motion.standardEasing,
            ),
        ) + expandHorizontally(
            animationSpec = tween(
                durationMillis = motion.normalMillis,
                easing = motion.standardEasing,
            ),
        )
    }
    val exitTransition = if (orientation == AnimatedLayoutOrientation.Vertical) {
        fadeOut(
            animationSpec = tween(
                durationMillis = motion.fastMillis,
                easing = motion.standardEasing,
            ),
        ) + shrinkVertically(
            animationSpec = tween(
                durationMillis = motion.fastMillis,
                easing = motion.standardEasing,
            ),
        )
    } else {
        fadeOut(
            animationSpec = tween(
                durationMillis = motion.fastMillis,
                easing = motion.standardEasing,
            ),
        ) + shrinkHorizontally(
            animationSpec = tween(
                durationMillis = motion.fastMillis,
                easing = motion.standardEasing,
            ),
        )
    }
    layout(
        modifier.animateContentSize(
            animationSpec = tween(
                durationMillis = motion.normalMillis,
                easing = motion.standardEasing,
            ),
        ),
    ) {
        animatedItems.forEach { animatedItem ->
            key(animatedItem.key) {
                val visibleState = remember {
                    MutableTransitionState(false)
                }

                LaunchedEffect(animatedItem.visible) {
                    visibleState.targetState = animatedItem.visible
                }
                LaunchedEffect(animatedItem.visible, visibleState.isIdle) {
                    if (!animatedItem.visible && visibleState.isIdle && !visibleState.currentState) {
                        animatedItems = animatedItems.filterNot { item ->
                            item.key == animatedItem.key && !item.visible
                        }
                    }
                }

                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = enterTransition,
                    exit = exitTransition,
                ) {
                    content(animatedItem.value)
                }
            }
        }
    }
}
