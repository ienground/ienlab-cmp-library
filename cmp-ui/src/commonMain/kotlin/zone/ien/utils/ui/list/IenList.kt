package zone.ien.utils.ui.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.utils.instantPress

/**
 * 리스트 헤더 부제목(Description)의 수직 노출 위치를 정의하는 열거형 클래스입니다.
 */
enum class IenListHeaderDescriptionPosition {
    Top, Bottom
}

/**
 * 리스트 헤더 컴포넌트의 기본 스타일 및 텍스트 구성을 제공하는 기본 오브젝트입니다.
 */
object IenListHeaderDefaults {
    @Composable
    fun Title(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = IenTheme.colors.textPrimary,
        fontWeight: FontWeight = FontWeight.Bold
    ) {
        IenText(
            text = text,
            modifier = modifier,
            style = IenTheme.typography.title3.copy(fontWeight = fontWeight),
            color = color
        )
    }

    @Composable
    fun Description(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = IenTheme.colors.textSecondary,
    ) {
        IenText(
            text = text,
            modifier = modifier,
            style = IenTheme.typography.caption,
            color = color
        )
    }
}

/**
 * 리스트(List)의 상단 영역에 배치하여 그룹 제목 및 설명을 나타내는 헤더 컴포저블입니다.
 *
 * @param title 제목 영역 컴포저블
 * @param modifier 적용할 Modifier
 * @param description 설명 영역 컴포저블 (선택사항)
 * @param descriptionPosition 설명 영역의 노출 위치 ([IenListHeaderDescriptionPosition])
 * @param right 헤더 우측 영역 컴포저블 슬롯 (예: 더보기 버튼 등)
 * @param rightAlignment 우측 컴포저블의 수직 정렬 방식
 */
@Composable
fun IenListHeader(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)? = null,
    descriptionPosition: IenListHeaderDescriptionPosition = IenListHeaderDescriptionPosition.Top,
    right: (@Composable RowScope.() -> Unit)? = null,
    rightAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = rightAlignment,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            if (description != null && descriptionPosition == IenListHeaderDescriptionPosition.Top) {
                description()
                Box(modifier = Modifier.padding(bottom = 2.dp))
            }
            title()
            if (description != null && descriptionPosition == IenListHeaderDescriptionPosition.Bottom) {
                Box(modifier = Modifier.padding(top = 2.dp))
                description()
            }
        }
        if (right != null) {
            Row(
                verticalAlignment = rightAlignment,
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs)
            ) {
                right()
            }
        }
    }
}

/**
 * 문자열 형식의 제목과 부제목을 받아 리스트 헤더를 간편하게 구성하는 편의성 컴포저블입니다.
 *
 * @param title 제목 텍스트
 * @param modifier 적용할 Modifier
 * @param description 설명(부제목) 텍스트 (선택사항)
 * @param descriptionPosition 설명 영역의 노출 위치 ([IenListHeaderDescriptionPosition])
 * @param right 헤더 우측 영역 컴포저블 슬롯 (예: 더보기 버튼 등)
 * @param rightAlignment 우측 컴포저블의 수직 정렬 방식
 */
@Composable
fun IenListHeader(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    descriptionPosition: IenListHeaderDescriptionPosition = IenListHeaderDescriptionPosition.Top,
    right: (@Composable RowScope.() -> Unit)? = null,
    rightAlignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    IenListHeader(
        title = { IenListHeaderDefaults.Title(text = title) },
        modifier = modifier,
        description = description?.let { { IenListHeaderDefaults.Description(text = it) } },
        descriptionPosition = descriptionPosition,
        right = right,
        rightAlignment = rightAlignment
    )
}

/**
 * 리스트 푸터(Footer)의 상단 구분선 스타일을 정의하는 열거형 클래스입니다.
 */
enum class IenListFooterBorder {
    Full, Indented, None
}

/**
 * 리스트 푸터 컴포넌트의 기본 데코레이션(구분선, 그림자 등)을 제공하는 기본 오브젝트입니다.
 */
object IenListFooterDefaults {
    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = IenTheme.colors.brand,
        fontWeight: FontWeight = FontWeight.Medium
    ) {
        IenText(
            text = text,
            modifier = modifier,
            style = IenTheme.typography.body2.copy(fontWeight = fontWeight),
            color = color
        )
    }

    @Composable
    fun Hairline(
        modifier: Modifier = Modifier,
        indent: Dp = 0.dp,
        color: Color = IenTheme.colors.border.copy(alpha = 0.4f)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = indent)
                .height(IenTheme.stroke.thin)
                .background(color)
        )
    }

    @Composable
    fun Shadow(
        modifier: Modifier = Modifier,
        color: Color = IenTheme.colors.brand.copy(alpha = 0.08f)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .drawBehind {
                    val radius = maxOf(size.width, size.height)
                    drawCircle(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(color, Color.Transparent),
                            center = center,
                            radius = radius
                        ),
                        radius = radius,
                        center = center
                    )
                }
        )
    }
}

/**
 * 리스트(List)의 하단 영역에 배치하여 더보기, 전체보기 등을 제공하는 클릭 가능한 푸터 컴포저블입니다.
 *
 * @param onClick 클릭 이벤트 콜백 (null인 경우 비활성화되고 클릭 피드백이 노출되지 않음)
 * @param modifier 적용할 Modifier
 * @param border 푸터 상단 구분선 종류 ([IenListFooterBorder])
 * @param textColor 텍스트 색상
 * @param iconColor 우측 배치 아이콘 색상
 * @param icon 텍스트 우측에 표시할 아이콘 컴포저블 (선택사항)
 * @param hairline 상단 구분선을 직접 지정할 수 있는 컴포저블 슬롯 (기본 스타일 대신 사용)
 * @param shadow 눌림 효과 시 표시될 배경 그림자 슬롯 (기본 스타일 대신 사용)
 * @param shape 터치 영역 피드백의 모서리 모양 ([Shape])
 * @param content 푸터 본문 컴포저블
 */
@Composable
fun IenListFooter(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    border: IenListFooterBorder = IenListFooterBorder.Full,
    textColor: Color = IenTheme.colors.brand,
    iconColor: Color = IenTheme.colors.brand,
    icon: (@Composable () -> Unit)? = null,
    hairline: (@Composable () -> Unit)? = null,
    shadow: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(0.dp),
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 1. Divider/Hairline 렌더링
        if (hairline != null) {
            hairline()
        } else {
            when (border) {
                IenListFooterBorder.Full -> {
                    IenListFooterDefaults.Hairline()
                }
                IenListFooterBorder.Indented -> {
                    IenListFooterDefaults.Hairline(indent = IenTheme.spacing.md)
                }
                IenListFooterBorder.None -> {
                    // 구분선 없음
                }
            }
        }

        var isPressed by remember { mutableStateOf(false) }
        val shadowAlpha by animateFloatAsState(
            targetValue = if (isPressed) 1f else 0f,
            animationSpec = tween(durationMillis = 200)
        )
        val interactionSource = remember { MutableInteractionSource() }

        // 2. 본체 콘텐츠 (중앙 정렬)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .instantPress(onClick != null) { isPressed = it }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = onClick != null,
                    onClick = { onClick?.invoke() }
                )
        ) {
            // shadow 커스텀 슬롯 렌더링 (눌림 상태일 때 노출)
            if (shadowAlpha > 0f && onClick != null) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer(alpha = shadowAlpha)
                ) {
                    if (shadow != null) {
                        shadow()
                    } else {
                        IenListFooterDefaults.Shadow()
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .padding(horizontal = IenTheme.spacing.md, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(LocalContentColor provides textColor) {
                    content()
                }
                if (icon != null) {
                    Box(modifier = Modifier.padding(start = 4.dp))
                    CompositionLocalProvider(LocalContentColor provides iconColor) {
                        icon()
                    }
                }
            }
        }
    }
}

/**
 * 문자열 텍스트를 전달받아 리스트 푸터를 간편하게 구성하는 편의성 컴포저블입니다.
 *
 * @param text 푸터 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param border 푸터 상단 구분선 종류 ([IenListFooterBorder])
 * @param textColor 텍스트 색상
 * @param icon 우측 배치 아이콘 컴포저블
 * @param shape 터치 영역 피드백의 모서리 모양 ([Shape])
 */
@Composable
fun IenListFooter(
    text: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    border: IenListFooterBorder = IenListFooterBorder.Full,
    textColor: Color = IenTheme.colors.brand,
    icon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(0.dp),
) {
    IenListFooter(
        onClick = onClick,
        modifier = modifier,
        border = border,
        textColor = textColor,
        icon = icon,
        shape = shape
    ) {
        IenListFooterDefaults.Text(
            text = text,
            color = textColor
        )
    }
}
