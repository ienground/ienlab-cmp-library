package zone.ien.utils.ui.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kyant.capsule.ContinuousCapsule
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface

/**
 * [IenBadge]와 [IenIconBadge]의 콘텐츠 및 패딩 크기를 정의하는 열거형 클래스.
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
    IenBadge(
        text = { Text(text = text) },
        modifier = modifier,
        size = size,
        variant = variant,
        tone = tone,
        leadingIcon = leadingIcon,
    )
}

/**
 * 배지의 텍스트 영역을 컴포저블로 구성합니다.
 *
 * @param text 배지 콘텐츠 슬롯. 크기와 톤에 맞는 기본 스타일을 제공합니다.
 */
@Composable
fun IenBadge(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    size: IenBadgeSize = IenBadgeSize.Medium,
    variant: IenBadgeVariant = IenBadgeVariant.Weak,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    IenBadgeContainer(modifier = modifier, variant = variant, tone = tone) {
        IenProvideTextStyle(size.textStyle(), LocalContentColor.current) {
            Row(
                modifier = Modifier.padding(size.padding()),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.invoke()
                text()
            }
        }
    }
}

/**
 * 텍스트 없이 아이콘으로 상태를 표시하는 원형 배지입니다.
 * [IenBadge]와 크기 규격, 색상 톤 및 비주얼 스타일을 공유하며 클릭 동작은 제공하지 않습니다.
 *
 * @param icon 배지 내부에 표시할 아이콘 데이터입니다.
 * @param modifier 배지 전체에 적용할 [Modifier]입니다.
 * @param size 아이콘과 패딩의 크기 규격입니다.
 * @param variant 배지의 비주얼 스타일입니다.
 * @param tone 배지의 의미적 색상 톤입니다.
 * @param contentDescription 아이콘이 전달하는 상태의 접근성 설명입니다. 장식용이면 null을 사용합니다.
 */
@Composable
fun IenIconBadge(
    icon: IconData,
    modifier: Modifier = Modifier,
    size: IenBadgeSize = IenBadgeSize.Medium,
    variant: IenBadgeVariant = IenBadgeVariant.Weak,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    contentDescription: String? = null,
) {
    val iconSize = when (size) {
        IenBadgeSize.Small -> IenTheme.icon.xs
        IenBadgeSize.Medium -> IenTheme.icon.sm
        IenBadgeSize.Large -> IenTheme.icon.md
    }
    IenBadgeContainer(modifier = modifier, variant = variant, tone = tone) {
        Box(
            modifier = Modifier.padding(size.padding().calculateTopPadding()),
            contentAlignment = Alignment.Center,
        ) {
            IenIcon(icon = icon, contentDescription = contentDescription, size = iconSize)
        }
    }
}

@Composable
private fun IenBadgeContainer(
    modifier: Modifier,
    variant: IenBadgeVariant,
    tone: IenSemanticTone,
    content: @Composable () -> Unit,
) {
    val contentColor = when (variant) {
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
        backgroundBrush = when (variant) {
            IenBadgeVariant.Fill -> toneGradientBrush(tone)
            IenBadgeVariant.Weak -> toneWeakGradientBrush(tone)
            IenBadgeVariant.Line -> null
        },
        contentColor = contentColor,
        shape = ContinuousCapsule(),
        border = border,
    ) {
        content()
    }
}

@Composable
private fun IenBadgeSize.textStyle() = when (this) {
    IenBadgeSize.Small -> IenTheme.typography.caption
    IenBadgeSize.Medium -> IenTheme.typography.label2
    IenBadgeSize.Large -> IenTheme.typography.label1
}

@Composable
private fun IenBadgeSize.padding() = when (this) {
    IenBadgeSize.Small -> PaddingValues(horizontal = IenTheme.spacing.xxs + IenTheme.spacing.xxxs, vertical = IenTheme.spacing.xxxs)
    IenBadgeSize.Medium -> PaddingValues(horizontal = IenTheme.spacing.xs, vertical = IenTheme.spacing.xxxs + IenTheme.spacing.xxxs / 2f)
    IenBadgeSize.Large -> PaddingValues(horizontal = IenTheme.spacing.sm - IenTheme.spacing.xxxs, vertical = IenTheme.spacing.xxs + IenTheme.spacing.xxxs / 2f)
}
