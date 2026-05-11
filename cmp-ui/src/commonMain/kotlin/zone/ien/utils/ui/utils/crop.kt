package zone.ien.utils.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * crop은 Modifier에 대한 확장 함수로, 뷰의 크기를 지정된 크기만큼 자르는 기능을 제공합니다.
 *
 * @param horizontal 가로 방향 자르기 크기
 * @param vertical 세로 방향 자르기 크기
 * @return 자른 Modifier
 */
fun Modifier.crop(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val horizontalPx = horizontal.roundToPx()
    val verticalPx = vertical.roundToPx()

    layout(
        (placeable.width - horizontalPx * 2).coerceAtLeast(0),
        (placeable.height - verticalPx * 2).coerceAtLeast(0)
    ) {
        placeable.placeRelative(-horizontalPx, -verticalPx)
    }
}
