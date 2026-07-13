package zone.ien.utils.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenTheme

/**
 * 말풍선(Bubble)의 배경 스타일을 나타내는 열거형 클래스입니다.
 */
enum class IenBubbleBackground {
    /** 브랜드 색상 배경 */
    Brand,
    /** 회색 배경 */
    Grey
}

/**
 * 사용자 정의 콘텐츠를 내부에 배치할 수 있는 말풍선 UI 컴포넌트입니다.
 *
 * @param background 말풍선의 배경 스타일 ([IenBubbleBackground])
 * @param modifier 레이아웃 및 스타일 수정을 위한 [Modifier]
 * @param withTail 말풍선 모서리를 꼬리 모양으로 둥글게 처리할지 여부 (기본값: true)
 * @param children 말풍선 내부에 들어갈 Composable 콘텐츠
 */
@Composable
fun IenBubble(
    background: IenBubbleBackground,
    modifier: Modifier = Modifier,
    withTail: Boolean = true,
    children: @Composable () -> Unit
) {
    val backgroundColor = if (background == IenBubbleBackground.Brand) {
        IenTheme.colors.brand
    } else {
        IenTheme.colors.surfaceVariant
    }

    val contentColor = if (background == IenBubbleBackground.Brand) {
        IenTheme.colors.onBrand
    } else {
        IenTheme.colors.textPrimary
    }

    val isLeft = (background == IenBubbleBackground.Grey)

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topStart = if (isLeft) 4.dp else 16.dp,
                    topEnd = if (isLeft) 16.dp else 4.dp,
                    bottomStart = if (isLeft && !withTail) 4.dp else 16.dp,
                    bottomEnd = if (!isLeft && !withTail) 4.dp else 16.dp,
                )
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            children()
        }
    }
}
