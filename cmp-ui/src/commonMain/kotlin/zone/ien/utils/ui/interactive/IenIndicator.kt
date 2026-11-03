package zone.ien.utils.ui.interactive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme

/**
 * 알림 여부나 개수를 빨간 점 또는 숫자로 표시하는 인디케이터입니다.
 * 아이콘 위 배치 등 위치 지정은 호출부에서 처리합니다.
 *
 * @param count 0이면 숨기고, -1을 포함한 음수이면 점을 표시합니다.
 * 양수이면 숫자를 표시하며, 100 이상은 `99+`로 표시합니다.
 * @param modifier 인디케이터 전체에 적용할 수정자입니다.
 */
@Composable
fun IenIndicator(
    count: Int,
    modifier: Modifier = Modifier,
) {
    if (count == 0) return

    if (count < 0) {
        Box(
            modifier = modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(IenTheme.colors.danger),
        )
    } else {
        IenBadge(
            text = if (count > 99) "99+" else count.toString(),
            modifier = modifier,
            size = IenBadgeSize.Small,
            variant = IenBadgeVariant.Fill,
            tone = IenSemanticTone.Danger,
        )
    }
}

/**
 * 콘텐츠 오른쪽에 알림 인디케이터를 배치하고 수직 중앙으로 정렬합니다.
 * 콘텐츠와 인디케이터 사이에는 [IenTheme.spacing]의 xs 간격을 적용합니다.
 *
 * @param count [IenIndicator]에 전달할 알림 값입니다. 0이면 인디케이터와 간격을 표시하지 않습니다.
 * @param modifier 전체 행에 적용할 수정자입니다.
 * @param content 인디케이터 왼쪽에 표시할 콘텐츠입니다.
 */
@Composable
fun IenIndicatorWrapper(
    count: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
        IenIndicator(count = count)
    }
}
