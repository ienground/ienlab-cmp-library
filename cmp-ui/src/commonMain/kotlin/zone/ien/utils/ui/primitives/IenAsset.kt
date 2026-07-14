package zone.ien.utils.ui.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.toneColor
import zone.ien.utils.ui.interactive.toneWeakColor

/**
 * 자산 프레임([IenAssetFrame])의 크기 유형을 정의하는 Enum 클래스입니다.
 */
enum class IenAssetFrameSize {
    Small,
    Medium,
    Large,
    ExtraLarge,
}

/**
 * 자산 프레임의 배경 형상을 정의하는 봉인된(sealed) 인터페이스입니다.
 */
sealed interface IenAssetFrameShape {
    /** 모서리가 둥글게 처리된 사각형 테두리 형상 */
    data object Rounded : IenAssetFrameShape
    /** 원형 테두리 형상 */
    data object Circle : IenAssetFrameShape
}

/**
 * 특정 아이콘, 로고 또는 텍스트 자산을 강조하여 감싸고 돋보이게 해주는 프레임 영역 컴포저블입니다.
 *
 * @param modifier 프레임 레이아웃에 적용할 [Modifier]
 * @param size 프레임 크기 단계 ([IenAssetFrameSize])
 * @param tone 프레임 배경에 반영할 테 테마/시맨틱 톤 ([IenSemanticTone])
 * @param shape 프레임의 형상 ([IenAssetFrameShape])
 * @param bordered 테두리선을 얇게 표시할지 여부
 * @param contentDescription 시각장애인 접근성 지원용 설명 텍스트
 * @param contentAlignment 내부 요소들의 배치 기준 정렬 방식 ([Alignment])
 * @param content 내부 콘텐츠 컴포저블 블록
 */
@Composable
fun IenAssetFrame(
    modifier: Modifier = Modifier,
    size: IenAssetFrameSize = IenAssetFrameSize.Medium,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    shape: IenAssetFrameShape = IenAssetFrameShape.Rounded,
    bordered: Boolean = false,
    contentDescription: String? = null,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit,
) {
    val containerColor = if (tone == IenSemanticTone.Neutral) {
        IenTheme.colors.surfaceWeak
    } else {
        toneWeakColor(tone)
    }
    val contentColor = if (tone == IenSemanticTone.Neutral) {
        IenTheme.colors.textPrimary
    } else {
        toneColor(tone)
    }

    IenSurface(
        modifier = modifier
            .size(size.value)
            .then(
                if (contentDescription != null) {
                    Modifier.semantics { this.contentDescription = contentDescription }
                } else {
                    Modifier
                },
            ),
        color = containerColor,
        contentColor = contentColor,
        shape = shape.toShape(),
        border = if (bordered) BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border) else null,
    ) {
        Box(contentAlignment = contentAlignment) {
            IenProvideTextStyle(
                style = IenTheme.typography.title3,
                color = LocalContentColor.current,
                content = content,
            )
        }
    }
}

private val IenAssetFrameSize.value: Dp
    get() = when (this) {
        IenAssetFrameSize.Small -> 32.dp
        IenAssetFrameSize.Medium -> 44.dp
        IenAssetFrameSize.Large -> 56.dp
        IenAssetFrameSize.ExtraLarge -> 72.dp
    }

@Composable
private fun IenAssetFrameShape.toShape(): Shape = when (this) {
    IenAssetFrameShape.Rounded -> ContinuousRoundedRectangle(IenTheme.radius.lg)
    IenAssetFrameShape.Circle -> CircleShape
}
