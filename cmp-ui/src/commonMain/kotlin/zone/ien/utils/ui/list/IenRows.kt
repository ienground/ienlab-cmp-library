package zone.ien.utils.ui.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.line.ArrowRightS
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenText

/**
 * 리스트 행(Row)의 구분선 스타일을 정의하는 열거형 클래스입니다.
 */
enum class IenListRowBorder {
    /** 왼쪽에 여백이 들어간 구분선 */
    Indented,
    /** 구분선 없음 */
    None,
}

/**
 * 리스트 행이 비활성화되었을 때의 스타일 유형을 정의하는 열거형 클래스입니다.
 */
enum class IenListRowDisabledStyle {
    /** 텍스트 불투명도만 조절하는 기본 스타일 */
    Type1,
    /** 배경색과 불투명도를 모두 조절하는 스타일 */
    Type2,
}

/**
 * 리스트 행의 여백(패딩) 크기를 정의하는 열거형 클래스입니다.
 */
enum class IenListRowPadding {
    ExtraSmall,
    Small,
    Medium,
    Large,
    XLarge,
}

/**
 * 리스트 행 내부 콘텐츠의 수직 정렬 방식을 정의하는 열거형 클래스입니다.
 */
enum class IenListRowAlignment {
    Top,
    Center,
}

/**
 * 리스트 행의 스켈레톤 로더(Loader) 레이아웃 유형을 정의하는 열거형 클래스입니다.
 */
enum class IenListRowLoaderType {
    /** 사각형 이미지 영역 로더 */
    Square,
    /** 원형 이미지 영역 로더 */
    Circle,
    /** 텍스트 바 영역 로더 */
    Bar,
}

/**
 * 리스트 행에 표시할 어셋(이미지/아이콘)의 모양을 정의하는 열거형 클래스입니다.
 */
enum class IenListRowAssetShape {
    /** 원본 모양 그대로 노출 */
    Original,
    /** 둥근 모서리가 큰 스쿼클 모양 */
    Squircle,
    /** 카드 형태의 둥근 모서리 모양 */
    Card,
    /** 일반 사각형 모양 */
    Square,
    /** 원형 모양 */
    Circle,
}

/**
 * 리스트 행에 표시할 어셋의 크기를 정의하는 열거형 클래스입니다.
 */
enum class IenListRowAssetSize {
    XSmall,
    Small,
    Medium,
}

/**
 * 리스트 행에 포함되는 텍스트 조합(행 수 및 스타일 정렬)을 정의하는 열거형 클래스입니다.
 */
enum class IenListRowTextsType {
    OneRowTypeA,
    OneRowTypeB,
    OneRowTypeC,
    RightOneRowTypeA,
    RightOneRowTypeB,
    RightOneRowTypeC,
    RightOneRowTypeD,
    RightOneRowTypeE,
    TwoRowTypeA,
    TwoRowTypeB,
    TwoRowTypeC,
    TwoRowTypeD,
    TwoRowTypeE,
    TwoRowTypeF,
    RightTwoRowTypeA,
    RightTwoRowTypeB,
    RightTwoRowTypeC,
    RightTwoRowTypeD,
    RightTwoRowTypeE,
    ThreeRowTypeA,
    ThreeRowTypeB,
    ThreeRowTypeC,
    ThreeRowTypeD,
    ThreeRowTypeE,
    ThreeRowTypeF,
}

/**
 * 리스트 아이템을 구성하는 행(Row) 컴포저블의 기본 구현입니다.
 * 좌측 어셋, 중앙 커스텀 콘텐츠, 우측 상세 버튼 슬롯을 지원합니다.
 *
 * @param contents 중앙 메인 콘텐츠 영역 컴포저블
 * @param modifier 적용할 Modifier
 * @param left 좌측 영역 컴포저블 (예: 아이콘, 썸네일 등)
 * @param right 우측 영역 컴포저블 (예: 스위치, 라디오 버튼 등)
 * @param border 구분선 스타일 ([IenListRowBorder])
 * @param disabled 비활성화 여부
 * @param disabledStyle 비활성화 상태 스타일 ([IenListRowDisabledStyle])
 * @param verticalPadding 수직 여백 ([IenListRowPadding])
 * @param horizontalPadding 수평 여백 ([IenListRowPadding])
 * @param leftAlignment 좌측 영역 수직 정렬 ([IenListRowAlignment])
 * @param rightAlignment 우측 영역 수직 정렬 ([IenListRowAlignment])
 * @param withArrow 우측 끝 화살표 아이콘 노출 여부
 * @param withTouchEffect 터치 효과(피드백) 적용 여부
 * @param onClick 클릭 이벤트 콜백
 */
@Composable
fun IenListRow(
    contents: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    left: (@Composable () -> Unit)? = null,
    right: (@Composable RowScope.() -> Unit)? = null,
    border: IenListRowBorder = IenListRowBorder.Indented,
    disabled: Boolean = false,
    disabledStyle: IenListRowDisabledStyle = IenListRowDisabledStyle.Type1,
    verticalPadding: IenListRowPadding = IenListRowPadding.Medium,
    horizontalPadding: IenListRowPadding = IenListRowPadding.Medium,
    leftAlignment: IenListRowAlignment = IenListRowAlignment.Center,
    rightAlignment: IenListRowAlignment = IenListRowAlignment.Center,
    withArrow: Boolean = false,
    withTouchEffect: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val enabled = !disabled
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val hasTouchEffect = withTouchEffect || onClick != null
    val rowBackground by animateColorAsState(
        targetValue = when {
            disabled && disabledStyle == IenListRowDisabledStyle.Type2 -> IenTheme.colors.surfaceWeak
            pressed && hasTouchEffect && enabled -> IenTheme.colors.surfaceWeak
            else -> Color.Transparent
        },
        label = "ienListRowBackground",
    )
    val contentAlpha = if (disabled) 0.45f else 1f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(rowBackground)
            .then(
                if (onClick != null || withTouchEffect) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled,
                        role = Role.Button,
                        onClick = { onClick?.invoke() },
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        if (border == IenListRowBorder.Indented) {
            Box(
                modifier = Modifier
                    .padding(start = horizontalPadding.value + 20.dp)
                    .fillMaxWidth()
                    .height(IenTheme.stroke.hairline)
                    .background(IenTheme.colors.border),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
                .padding(
                    horizontal = horizontalPadding.value,
                    vertical = verticalPadding.value,
                )
                .alpha(contentAlpha),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            left?.let {
                Box(
                    modifier = Modifier.align(leftAlignment.toRowAlignment()),
                    contentAlignment = leftAlignment.toBoxAlignment(),
                ) {
                    it()
                }
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart,
            ) {
                contents()
            }
            if (right != null || withArrow) {
                Row(
                    modifier = Modifier.align(rightAlignment.toRowAlignment()),
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                    verticalAlignment = rightAlignment.toRowAlignment(),
                ) {
                    right?.invoke(this)
                    if (withArrow) {
                        CompositionLocalProvider(
                            LocalContentColor provides IenTheme.colors.textTertiary
                        ) {
                            Icon(
                                imageVector = RemixIcons.Line.ArrowRightS,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 문자열 형태의 제목과 부제목을 전달받아 리스트 행을 구성하는 편리한 컴포저블입니다.
 *
 * @param title 제목 텍스트
 * @param modifier 적용할 Modifier
 * @param subtitle 부제목 텍스트 (선택사항)
 * @param enabled 행 활성화 여부
 * @param selected 행 선택 상태 여부 (true인 경우 제목 텍스트 색상이 강조됨)
 * @param onClick 클릭 이벤트 콜백
 * @param leading 좌측 영역 컴포저블
 * @param trailing 우측 영역 컴포저블
 * @param border 구분선 스타일 ([IenListRowBorder])
 * @param verticalPadding 수직 여백 ([IenListRowPadding])
 * @param horizontalPadding 수평 여백 ([IenListRowPadding])
 * @param withArrow 우측 끝 화살표 아이콘 노출 여부
 */
@Composable
fun IenListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    border: IenListRowBorder = IenListRowBorder.Indented,
    verticalPadding: IenListRowPadding = IenListRowPadding.Medium,
    horizontalPadding: IenListRowPadding = IenListRowPadding.Medium,
    withArrow: Boolean = false,
) {
    IenListRow(
        modifier = modifier,
        left = leading,
        right = trailing,
        border = border,
        disabled = !enabled,
        verticalPadding = verticalPadding,
        horizontalPadding = horizontalPadding,
        withArrow = withArrow,
        onClick = onClick,
        contents = {
            IenListRowTexts(
                type = if (subtitle == null) IenListRowTextsType.OneRowTypeA else IenListRowTextsType.TwoRowTypeA,
                top = title,
                bottom = subtitle,
                topColor = if (selected) IenTheme.colors.brand else null,
            )
        },
    )
}

/**
 * 리스트 행 내부에서 최대 3개의 텍스트 요소를 정의된 조합 스타일([IenListRowTextsType])에 따라 수직 배치하는 컴포저블입니다.
 *
 * @param top 가장 상단(혹은 첫 번째 행)에 들어갈 텍스트
 * @param modifier 적용할 Modifier
 * @param type 행의 구성 및 정렬 방식을 지정하는 타입 ([IenListRowTextsType])
 * @param middle 중간 행에 들어갈 텍스트 (타입이 3행 스타일일 때 활성화)
 * @param bottom 하단 행에 들어갈 텍스트 (타입이 2행 이상 스타일일 때 활성화)
 * @param topColor 상단 텍스트 색상 (null인 경우 타입 기본 색상 사용)
 * @param middleColor 중간 텍스트 색상 (null인 경우 타입 기본 색상 사용)
 * @param bottomColor 하단 텍스트 색상 (null인 경우 타입 기본 색상 사용)
 * @param maxLines 상단 및 중간 텍스트의 최대 줄 수
 */
@Composable
fun IenListRowTexts(
    top: String,
    modifier: Modifier = Modifier,
    type: IenListRowTextsType = IenListRowTextsType.OneRowTypeA,
    middle: String? = null,
    bottom: String? = null,
    topColor: Color? = null,
    middleColor: Color? = null,
    bottomColor: Color? = null,
    maxLines: Int = 1,
) {
    val rightAligned = type.name.startsWith("Right")
    val textAlign = if (rightAligned) TextAlign.End else TextAlign.Start
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (rightAligned) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxxs),
    ) {
        IenText(
            text = top,
            style = type.topStyle(),
            color = topColor ?: type.topColor(),
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
        if (middle != null && type.rowCount >= 3) {
            IenText(
                text = middle,
                style = type.middleStyle(),
                color = middleColor ?: type.middleColor(),
                textAlign = textAlign,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (bottom != null && type.rowCount >= 2) {
            IenText(
                text = bottom,
                style = type.bottomStyle(),
                color = bottomColor ?: type.bottomColor(),
                textAlign = textAlign,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * 리스트 행의 좌측 어셋 영역에 표시할 텍스트 컴포저블입니다. 원형이나 사각형 등의 배경 프레임 내에 텍스트가 표시됩니다.
 *
 * @param text 표시할 텍스트
 * @param modifier 적용할 Modifier
 * @param shape 프레임 모양 ([IenListRowAssetShape])
 * @param size 프레임 크기 ([IenListRowAssetSize])
 * @param backgroundColor 프레임 배경 색상
 * @param contentColor 텍스트 색상
 */
@Composable
fun IenListRowAssetText(
    text: String,
    modifier: Modifier = Modifier,
    shape: IenListRowAssetShape = IenListRowAssetShape.Squircle,
    size: IenListRowAssetSize = IenListRowAssetSize.Medium,
    backgroundColor: Color = IenTheme.colors.surfaceWeak,
    contentColor: Color = IenTheme.colors.textPrimary,
) {
    Box(
        modifier = modifier
            .size(size.value)
            .clip(shape.toShape())
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        IenText(
            text = text,
            style = IenTheme.typography.label2.copy(fontWeight = FontWeight.Bold),
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * 리스트 행의 좌측 영역에 들어갈 커스텀 컴포저블을 일정한 프레임 규격으로 감싸주는 래퍼 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param shape 프레임 모양 ([IenListRowAssetShape])
 * @param size 프레임 크기 ([IenListRowAssetSize])
 * @param backgroundColor 프레임 배경 색상 (모양이 Original인 경우 무시됨)
 * @param content 내부 콘텐츠 컴포저블
 */
@Composable
fun IenListRowAssetFrame(
    modifier: Modifier = Modifier,
    shape: IenListRowAssetShape = IenListRowAssetShape.Squircle,
    size: IenListRowAssetSize = IenListRowAssetSize.Medium,
    backgroundColor: Color = IenTheme.colors.surfaceWeak,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(size.value)
            .clip(shape.toShape())
            .background(if (shape == IenListRowAssetShape.Original) Color.Transparent else backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/**
 * 리스트 행의 스켈레톤 로더 컴포저블입니다. 로딩 중에 깜빡이는 스켈레톤 UI를 제공합니다.
 *
 * @param modifier 적용할 Modifier
 * @param type 로더 레이아웃 형태 ([IenListRowLoaderType])
 * @param verticalPadding 수직 여백 ([IenListRowPadding])
 */
@Composable
fun IenListRowLoader(
    modifier: Modifier = Modifier,
    type: IenListRowLoaderType = IenListRowLoaderType.Square,
    verticalPadding: IenListRowPadding = IenListRowPadding.Medium,
) {
    IenListRow(
        modifier = modifier,
        border = IenListRowBorder.None,
        verticalPadding = verticalPadding,
        horizontalPadding = IenListRowPadding.Medium,
        left = if (type == IenListRowLoaderType.Bar) {
            null
        } else {
            {
                when (type) {
                    IenListRowLoaderType.Square -> IenListRowLoaderBlock(
                        modifier = Modifier.size(44.dp),
                        shape = RoundedCornerShape(IenTheme.radius.lg),
                    )

                    IenListRowLoaderType.Circle -> IenListRowLoaderBlock(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                    )

                    IenListRowLoaderType.Bar -> Unit
                }
            }
        },
        contents = {
            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                IenListRowLoaderBlock(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp),
                    shape = RoundedCornerShape(IenTheme.radius.full),
                )
                IenListRowLoaderBlock(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(14.dp),
                    shape = RoundedCornerShape(IenTheme.radius.full),
                )
            }
        },
    )
}

@Composable
private fun IenListRowLoaderBlock(
    modifier: Modifier,
    shape: Shape,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(IenTheme.colors.surfaceWeak),
    )
}

private val IenListRowPadding.value: Dp
    get() = when (this) {
        IenListRowPadding.ExtraSmall -> 4.dp
        IenListRowPadding.Small -> 8.dp
        IenListRowPadding.Medium -> 12.dp
        IenListRowPadding.Large -> 16.dp
        IenListRowPadding.XLarge -> 24.dp
    }

private val IenListRowAssetSize.value: Dp
    get() = when (this) {
        IenListRowAssetSize.XSmall -> 32.dp
        IenListRowAssetSize.Small -> 40.dp
        IenListRowAssetSize.Medium -> 52.dp
    }

@Composable
private fun IenListRowAssetShape.toShape(): Shape = when (this) {
    IenListRowAssetShape.Original -> RoundedCornerShape(0.dp)
    IenListRowAssetShape.Squircle -> RoundedCornerShape(IenTheme.radius.lg)
    IenListRowAssetShape.Card -> RoundedCornerShape(IenTheme.radius.default)
    IenListRowAssetShape.Square -> RoundedCornerShape(IenTheme.radius.sm)
    IenListRowAssetShape.Circle -> CircleShape
}

private fun IenListRowAlignment.toRowAlignment(): Alignment.Vertical = when (this) {
    IenListRowAlignment.Top -> Alignment.Top
    IenListRowAlignment.Center -> Alignment.CenterVertically
}

private fun IenListRowAlignment.toBoxAlignment(): Alignment = when (this) {
    IenListRowAlignment.Top -> Alignment.TopCenter
    IenListRowAlignment.Center -> Alignment.Center
}

private val IenListRowTextsType.rowCount: Int
    get() = when {
        name.startsWith("Three") -> 3
        name.startsWith("Two") || name.startsWith("RightTwo") -> 2
        else -> 1
    }

@Composable
private fun IenListRowTextsType.topStyle(): TextStyle = when (this) {
    IenListRowTextsType.OneRowTypeB,
    IenListRowTextsType.OneRowTypeC,
    IenListRowTextsType.TwoRowTypeD,
    IenListRowTextsType.TwoRowTypeE,
    IenListRowTextsType.ThreeRowTypeC,
    IenListRowTextsType.ThreeRowTypeD -> IenTheme.typography.label1
    else -> IenTheme.typography.body1
}

@Composable
private fun IenListRowTextsType.middleStyle(): TextStyle = IenTheme.typography.body2

@Composable
private fun IenListRowTextsType.bottomStyle(): TextStyle = when (this) {
    IenListRowTextsType.TwoRowTypeF,
    IenListRowTextsType.ThreeRowTypeF -> IenTheme.typography.label2
    else -> IenTheme.typography.caption
}

@Composable
private fun IenListRowTextsType.topColor(): Color = when {
    name.startsWith("Right") -> IenTheme.colors.textSecondary
    this == IenListRowTextsType.OneRowTypeC -> IenTheme.colors.textSecondary
    else -> IenTheme.colors.textPrimary
}

@Composable
private fun IenListRowTextsType.middleColor(): Color = IenTheme.colors.textSecondary

@Composable
private fun IenListRowTextsType.bottomColor(): Color = when (this) {
    IenListRowTextsType.TwoRowTypeC,
    IenListRowTextsType.TwoRowTypeF,
    IenListRowTextsType.RightTwoRowTypeD,
    IenListRowTextsType.RightTwoRowTypeE -> IenTheme.colors.textPrimary
    else -> IenTheme.colors.textSecondary
}

/**
 * 테이블 행 내부 항목의 수평 정렬 방식을 정의하는 열거형 클래스입니다.
 */
enum class IenTableRowAlign {
    Left,
    SpaceBetween,
}

/**
 * [IenTableRow] 내부 슬롯의 영역 수평 정렬 정보를 제공하는 스코프 클래스입니다.
 *
 * @property align 테이블 행 정렬 방식 ([IenTableRowAlign])
 */
@Immutable
data class IenTableRowScope(
    val align: IenTableRowAlign,
)

/**
 * 표(Table) 형태의 키-밸브 데이터를 간편하게 표시하기 위한 행(Row) 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param left 좌측 영역에 들어갈 기본 텍스트 (label과 동일 기능)
 * @param right 우측 영역에 들어갈 기본 텍스트 (value와 동일 기능)
 * @param label 좌측 라벨 텍스트 (레거시 지원)
 * @param value 우측 값 텍스트 (레거시 지원)
 * @param description 좌측 라벨 하단에 표시할 설명 문구 (레거시 지원)
 * @param leading 좌측 영역 텍스트 앞에 배치될 컴포저블 (레거시 지원)
 * @param trailing 우측 영역 텍스트 뒤에 배치될 컴포저블 (레거시 지원)
 * @param align 수평 정렬 방식 ([IenTableRowAlign])
 * @param leftRatio 좌측 영역이 차지할 비율 (1 ~ 99, 제공하는 경우 가로 전체를 비율 분할함)
 */
@Composable
fun IenTableRow(
    modifier: Modifier = Modifier,
    left: String? = null,
    right: String? = null,
    label: String? = null,
    value: String? = null,
    description: String? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    align: IenTableRowAlign = IenTableRowAlign.SpaceBetween,
    leftRatio: Int? = null,
) {
    val resolvedLeft = left ?: label.orEmpty()
    val resolvedRight = right ?: value.orEmpty()
    val hasLegacyAffordance = label != null || value != null || description != null || leading != null || trailing != null

    IenTableRow(
        left = {
            if (hasLegacyAffordance) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                    verticalAlignment = Alignment.Top,
                ) {
                    leading?.invoke()
                    Column {
                        IenText(resolvedLeft, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
                        if (description != null) {
                            IenText(description, style = IenTheme.typography.caption, color = IenTheme.colors.textTertiary)
                        }
                    }
                }
            } else {
                IenText(
                    text = resolvedLeft,
                    style = IenTheme.typography.body2,
                    color = IenTheme.colors.textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        right = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IenText(
                    text = resolvedRight,
                    style = IenTheme.typography.body2,
                    color = if (hasLegacyAffordance) IenTheme.colors.textPrimary else IenTheme.colors.textSecondary,
                    textAlign = if (align == IenTableRowAlign.SpaceBetween) TextAlign.End else TextAlign.Start,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                trailing?.invoke(this)
            }
        },
        modifier = modifier,
        align = align,
        leftRatio = leftRatio,
    )
}

/**
 * 표(Table) 형태의 데이터를 커스텀 컴포저블 슬롯으로 표시하기 위한 행(Row) 컴포저블입니다.
 *
 * @param left 좌측 영역에 들어갈 컴포저블 슬롯
 * @param right 우측 영역에 들어갈 컴포저블 슬롯
 * @param modifier 적용할 Modifier
 * @param align 수평 정렬 방식 ([IenTableRowAlign])
 * @param leftRatio 좌측 영역이 차지할 비율 (1 ~ 99, 제공하는 경우 가로 전체를 비율 분할함)
 */
@Composable
fun IenTableRow(
    left: @Composable IenTableRowScope.() -> Unit,
    right: @Composable IenTableRowScope.() -> Unit,
    modifier: Modifier = Modifier,
    align: IenTableRowAlign = IenTableRowAlign.SpaceBetween,
    leftRatio: Int? = null,
) {
    val scope = IenTableRowScope(align = align)
    val safeLeftRatio = leftRatio?.coerceIn(1, 99)
    val leftWeight = safeLeftRatio?.toFloat()
    val rightWeight = safeLeftRatio?.let { (100 - it).coerceAtLeast(0).toFloat() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        horizontalArrangement = when (align) {
            IenTableRowAlign.Left -> Arrangement.spacedBy(IenTheme.spacing.md)
            IenTableRowAlign.SpaceBetween -> Arrangement.SpaceBetween
        },
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = when {
                leftWeight != null && leftWeight <= 0f -> Modifier.weight(0.0001f)
                leftWeight != null -> Modifier.weight(leftWeight)
                align == IenTableRowAlign.SpaceBetween -> Modifier.weight(1f)
                else -> Modifier
            }
        ) {
            scope.left()
        }
        Box(
            modifier = when {
                rightWeight != null && rightWeight <= 0f -> Modifier.weight(0.0001f)
                rightWeight != null -> Modifier.weight(rightWeight)
                align == IenTableRowAlign.SpaceBetween -> Modifier.weight(1f)
                else -> Modifier
            },
            contentAlignment = if (align == IenTableRowAlign.SpaceBetween) Alignment.TopEnd else Alignment.TopStart,
        ) {
            scope.right()
        }
    }
}
