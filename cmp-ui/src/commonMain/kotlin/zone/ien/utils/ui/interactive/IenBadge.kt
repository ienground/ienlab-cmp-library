package zone.ien.utils.ui.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

/**
 * [IenBadge]의 텍스트 스타일 및 패딩 크기를 정의하는 열거형 클래스.
 */
enum class IenBadgeSize {
    /** 작은 크기 */
    Small,
    /** 중간 크기 */
    Medium,
    /** 큰 크기 */
    Large,
}

/**
 * [IenBadge]의 비주얼 스타일 유형을 정의하는 인터페이스.
 */
sealed interface IenBadgeVariant {
    /** 배경색이 가득 채워지는 형태 */
    data object Fill : IenBadgeVariant
    /** 옅은 배경색이 채워지는 형태 */
    data object Weak : IenBadgeVariant
    /** 테두리선만 있는 형태 */
    data object Line : IenBadgeVariant
}

/**
 * 카테고리 태그나 상태 요약 등을 시각적으로 보여주기 위한 알약 모양의 배지 컴포저블.
 *
 * @param text 배지 내부에 표시할 문자열.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param size 배지의 크기 규격 ([IenBadgeSize]). 기본값은 [IenBadgeSize.Medium].
 * @param variant 배지의 비주얼 스타일 변형 ([IenBadgeVariant]). 기본값은 [IenBadgeVariant.Weak].
 * @param tone 배지의 시각적 어조 또는 의미적 강조 색상 ([IenSemanticTone]). 기본값은 [IenSemanticTone.Brand].
 * @param leadingIcon 배지 텍스트 왼쪽에 추가로 표시할 아이콘 등의 컴포저블.
 */
@Composable
fun IenBadge(
    text: String,
    modifier: Modifier = Modifier,
    size: IenBadgeSize = IenBadgeSize.Medium,
    variant: IenBadgeVariant = IenBadgeVariant.Weak,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val content = when (variant) {
        IenBadgeVariant.Fill -> Color.White
        IenBadgeVariant.Weak, IenBadgeVariant.Line -> toneColor(tone)
    }
    val container = when (variant) {
        IenBadgeVariant.Fill -> toneColor(tone)
        IenBadgeVariant.Weak -> toneWeakColor(tone)
        IenBadgeVariant.Line -> Color.Transparent
    }
    val border = if (variant == IenBadgeVariant.Line) BorderStroke(IenTheme.stroke.thin, toneColor(tone)) else null

    IenSurface(
        modifier = modifier,
        color = container,
        contentColor = content,
        shape = ContinuousCapsule(),
        border = border,
    ) {
        IenProvideTextStyle(size.textStyle(), LocalContentColor.current) {
            Row(
                modifier = Modifier.padding(size.padding()),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.invoke()
                IenText(text = text, style = size.textStyle(), color = LocalContentColor.current)
            }
        }
    }
}

@Composable
private fun IenBadgeSize.textStyle() = when (this) {
    IenBadgeSize.Small -> IenTheme.typography.caption
    IenBadgeSize.Medium -> IenTheme.typography.label2
    IenBadgeSize.Large -> IenTheme.typography.label1
}

private fun IenBadgeSize.padding() = when (this) {
    IenBadgeSize.Small -> PaddingValues(horizontal = 6.dp, vertical = 2.dp)
    IenBadgeSize.Medium -> PaddingValues(horizontal = 8.dp, vertical = 3.dp)
    IenBadgeSize.Large -> PaddingValues(horizontal = 10.dp, vertical = 5.dp)
}
