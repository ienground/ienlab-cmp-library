package zone.ien.utils.ui.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.loading
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.IenSpacing
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.list.IenListRowTexts
import zone.ien.utils.ui.list.IenListRowTextsType
import zone.ien.utils.ui.shimmer.IenPlaceholderDefaults
import zone.ien.utils.ui.shimmer.IenPlaceholderHighlight
import zone.ien.utils.ui.shimmer.LocalIenShimmerShape
import kotlin.math.PI
import kotlin.math.sin

/**
 * 데이터를 로딩 중일 때 표시할 수 있는 스켈레톤(스위머) 자리 표시자 컴포저블입니다.
 * 프리뷰에서는 애니메이션 없이 원래 크기와 배경 색상으로 표시합니다.
 *
 * @param modifier 적용할 [Modifier]
 * @param height 단일 블록의 스켈레톤을 만들 때 사용하는 높이. null이 아니면 해당 높이의 단일 블록이 렌더링됩니다.
 * @param radius 둥근 테두리 반경 ([Dp])
 * @param pattern 사전에 지정된 레이아웃 템플릿 ([IenSkeletonPattern])
 * @param custom 직접 지정하고자 하는 스켈레톤 요소([IenSkeletonElement]) 목록
 * @param repeatLastItemCount 마지막 줄 요소를 반복해 뿌릴 횟수 또는 방식 ([IenSkeletonRepeat])
 * @param play 스켈레톤을 노출할지 감출지 여부 ([IenSkeletonPlay])
 * @param background 스켈레톤의 배경 컬러 톤 ([IenSkeletonBackground])
 * @param width 단일 블록 너비. null이면 사용 가능한 너비를 채웁니다.
 */
@Composable
fun IenSkeleton(
    modifier: Modifier = Modifier,
    height: Dp? = null,
    radius: Dp = IenTheme.radius.sm,
    pattern: IenSkeletonPattern = IenSkeletonPattern.TopList,
    custom: List<IenSkeletonElement>? = null,
    repeatLastItemCount: IenSkeletonRepeat = IenSkeletonRepeat.Count(3),
    play: IenSkeletonPlay = IenSkeletonPlay.Show,
    background: IenSkeletonBackground = IenSkeletonBackground.Grey,
    width: Dp? = null,
) {
    if (play == IenSkeletonPlay.Hide) {
        return
    }

    val phase = rememberIenSkeletonPhase()
    val color = resolveIenSkeletonColor(background, IenTheme.colors)

    if (height != null) {
        IenSkeletonBlock(
            modifier = if (width == null) modifier.fillMaxWidth() else modifier.width(width),
            height = height,
            radius = radius,
            color = color,
            phase = phase,
            animationIndex = 0,
        )
        return
    }

    val elements = (custom ?: pattern.elements()).withRepeatedLast(repeatLastItemCount)
    val contentDescription = stringResource(Res.string.loading)
    Column(
        modifier = modifier
            .semantics {
                this.contentDescription = contentDescription
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
    ) {
        elements.forEachIndexed { index, element ->
            IenSkeletonElementView(
                element = element,
                index = index,
                radius = radius,
                color = color,
                phase = phase,
            )
        }
    }
}

/**
 * 임의의 컴포저블 영역을 IenSkeleton과 같은 바운스 효과가 있는 자리 표시자로 표시합니다.
 *
 * @param enabled 자리 표시자를 표시할지 여부
 * @param color 자리 표시자 색상
 * @param shape 자리 표시자 모양
 * @param highlight null이면 바운스 효과를 끕니다.
 * @param animationIndex 바운스 효과의 시작 시점을 늦추는 인덱스
 */
@Composable
fun Modifier.placeholder(
    enabled: Boolean = true,
    color: Color = IenTheme.colors.surfaceVariant,
    shape: Shape = LocalIenShimmerShape.current,
    highlight: IenPlaceholderHighlight? = IenPlaceholderDefaults.fade,
    animationIndex: Int = 0,
): Modifier {
    if (!enabled) return this

    val motion = if (highlight != null && LocalIenSkeletonBlockMotionEnabled.current) {
        val phase = rememberIenSkeletonPhase()
        ienSkeletonMotion(phase = phase, animationIndex = animationIndex)
    } else {
        this
    }

    return motion.drawWithCache {
        val outline = shape.createOutline(size, layoutDirection, this)
        onDrawWithContent {
            drawOutline(outline, color)
        }
    }
}

/**
 * [IenListRowTexts]의 행 수, 정렬 및 텍스트 스타일에 대응하는 스켈레톤을 표시합니다.
 *
 * @param pattern 리스트 행 텍스트 유형에 대응하는 레이아웃
 * @param modifier 적용할 [Modifier]
 * @param height null이 아니면 텍스트 패턴 대신 해당 높이의 단일 블록을 표시합니다.
 * @param radius 블록의 모서리 반경
 * @param custom 지정하면 패턴 대신 사용할 스켈레톤 요소 목록
 * @param repeatLastItemCount 마지막 요소의 반복 횟수 또는 방식
 * @param play 스켈레톤 노출 여부
 * @param background 스켈레톤 배경 색상
 * @param width 단일 블록 너비. null이면 사용 가능한 너비를 채웁니다.
 */
@Composable
fun IenSkeleton(
    pattern: IenSkeletonPattern.ListRowTexts,
    modifier: Modifier = Modifier,
    height: Dp? = null,
    radius: Dp = IenTheme.radius.sm,
    custom: List<IenSkeletonElement>? = null,
    repeatLastItemCount: IenSkeletonRepeat = IenSkeletonRepeat.Count(3),
    play: IenSkeletonPlay = IenSkeletonPlay.Show,
    background: IenSkeletonBackground = IenSkeletonBackground.Grey,
    width: Dp? = null,
) {
    IenSkeleton(
        modifier = modifier,
        height = height,
        radius = radius,
        custom = custom ?: listOf(IenSkeletonElement.ListRowTexts(pattern.type)),
        repeatLastItemCount = repeatLastItemCount,
        play = play,
        background = background,
        width = width,
    )
}

/**
 * 스켈레톤 화면 구조를 쉽게 구성할 수 있도록 지원하는 미리 설정된 레이아웃 템플릿 목록입니다.
 */
enum class IenSkeletonPattern {
    TopList,
    TopListWithIcon,
    AmountTopList,
    AmountTopListWithIcon,
    SubtitleList,
    SubtitleListWithIcon,
    ListOnly,
    ListWithIconOnly,
    CardOnly;

    /**
     * [IenListRowTextsType]의 모든 텍스트 조합에 대응하는 스켈레톤 패턴입니다.
     *
     * @property type 대응하는 리스트 행 텍스트 유형
     */
    enum class ListRowTexts {
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
        TwoRowTypeG,
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
        ThreeRowTypeF;

        val type: IenListRowTextsType
            get() = IenListRowTextsType.valueOf(name)
    }
}

/**
 * 스켈레톤 컴포저블의 애니메이션 및 노출 여부를 조작하는 재생 제어 상태입니다.
 */
enum class IenSkeletonPlay {
    Show,
    Hide,
}

/**
 * 스켈레톤 블록의 배경에 사용할 색상 세트를 결정하는 Enum 클래스입니다.
 */
enum class IenSkeletonBackground {
    White,
    Grey,
    GreyOpacity100,
}

/**
 * 자식 스켈레톤 요소를 하나로 묶어 파동형 애니메이션 페이즈를 동기화하기 위해 사용하는 모션 그룹 컴포저블입니다.
 * 프리뷰에서는 투명도와 크기를 변경하지 않습니다.
 *
 * @param modifier 적용할 [Modifier]
 * @param animationIndex 애니메이션 딜레이 기준 인덱스
 * @param content 내부 콘텐츠 컴포저블
 */
@Composable
fun IenSkeletonMotionGroup(
    modifier: Modifier = Modifier,
    animationIndex: Int = 0,
    content: @Composable BoxScope.() -> Unit,
) {
    val phase = rememberIenSkeletonPhase()
    Box(
        modifier = modifier.ienSkeletonMotion(phase = phase, animationIndex = animationIndex),
    ) {
        CompositionLocalProvider(LocalIenSkeletonBlockMotionEnabled provides false) {
            content()
        }
    }
}

/**
 * 스켈레톤 리스트 형태에서 마지막 요소를 반복하는 횟수나 방식을 정의하는 봉인된(sealed) 인터페이스입니다.
 */
sealed interface IenSkeletonRepeat {
    /**
     * 특정 횟수만큼 마지막 요소를 반복합니다.
     */
    data class Count(val value: Int) : IenSkeletonRepeat
    /**
     * 무한(또는 최대 수량)만큼 마지막 요소를 반복합니다.
     */
    data object Infinite : IenSkeletonRepeat
}

/**
 * 스켈레톤 화면 구조를 구성하기 위한 기본 UI 구성 요소 종류를 정의한 봉인된(sealed) 인터페이스입니다.
 */
sealed interface IenSkeletonElement {
    /** 제목 스타일의 얇은 스켈레톤 바 */
    data object Title : IenSkeletonElement
    /** 부제목 스타일의 더 얇고 짧은 스켈레톤 바 */
    data object Subtitle : IenSkeletonElement
    /** 리스트 아이템 형태의 스켈레톤 바 */
    data object List : IenSkeletonElement
    /** 아이콘과 함께 있는 리스트 형태의 스켈레톤 요소 */
    data object ListWithIcon : IenSkeletonElement
    /**
     * 리스트 행 텍스트의 행 수, 정렬 및 스타일을 따르는 스켈레톤 요소입니다.
     *
     * @property type 대응하는 리스트 행 텍스트 유형
     */
    data class ListRowTexts(val type: IenListRowTextsType) : IenSkeletonElement
    /** 카드 형태의 커다란 스켈레톤 블록 */
    data object Card : IenSkeletonElement
    /**
     * 너비, 높이와 모양을 직접 지정하는 스켈레톤 블록입니다.
     *
     * @property height 블록 높이. 지정하면 typography보다 우선합니다.
     * @property width 지정한 경우 사용할 블록의 너비. null이면 modifier에 따라 너비를 정합니다.
     * @property shape 지정하면 기본 둥근 사각형 대신 사용할 모양
     * @property modifier 블록의 너비 비율 등 레이아웃 속성. width가 null이면 기본적으로 부모 너비를 채웁니다.
     * @property typography 높이에 사용할 텍스트 스타일. lineHeight, fontSize 순서로 사용하며,
     * 둘 다 미지정이면 테마 body2의 lineHeight를 사용합니다. height 또는 typography를 지정해야 합니다.
     */
    data class Block(
        val height: Dp = Dp.Unspecified,
        val width: Dp? = null,
        val shape: Shape? = null,
        val modifier: Modifier = Modifier,
        val typography: TextStyle? = null,
    ) : IenSkeletonElement {
        init {
            require(height != Dp.Unspecified || typography != null) {
                "height 또는 typography를 지정해야 합니다."
            }
        }
    }
    /**
     * 자식 요소를 가로로 배치하는 컨테이너입니다.
     *
     * @property elements 배치할 스켈레톤 요소
     * @property modifier 컨테이너에 적용할 [Modifier]
     * @property horizontalArrangement 자식 요소의 가로 배치 방식
     * @property verticalAlignment 자식 요소의 세로 정렬 방식
     */
    data class Row(
        val elements: kotlin.collections.List<IenSkeletonElement>,
        val modifier: Modifier = Modifier.fillMaxWidth(),
        val horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        val verticalAlignment: Alignment.Vertical = Alignment.Top,
    ) : IenSkeletonElement
    /**
     * 자식 요소를 세로로 배치하는 컨테이너입니다.
     *
     * @property elements 배치할 스켈레톤 요소
     * @property modifier 컨테이너에 적용할 [Modifier]
     * @property verticalArrangement 자식 요소의 세로 배치 방식
     * @property horizontalAlignment 자식 요소의 가로 정렬 방식
     */
    data class Column(
        val elements: kotlin.collections.List<IenSkeletonElement>,
        val modifier: Modifier = Modifier.fillMaxWidth(),
        val verticalArrangement: Arrangement.Vertical = Arrangement.Top,
        val horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    ) : IenSkeletonElement
    /**
     * 자식 요소를 겹쳐 배치하는 컨테이너입니다.
     *
     * @property elements 겹쳐 배치할 스켈레톤 요소
     * @property modifier 컨테이너에 적용할 [Modifier]
     * @property contentAlignment 자식 요소의 정렬 방식
     */
    data class Box(
        val elements: kotlin.collections.List<IenSkeletonElement>,
        val modifier: Modifier = Modifier.fillMaxWidth(),
        val contentAlignment: Alignment = Alignment.TopStart,
    ) : IenSkeletonElement
    /** 요소들 사이의 여백을 나타내는 여백 스켈레톤 */
    data class Spacer(val height: Dp, val width: Dp = IenSpacing().none) : IenSkeletonElement
}

private const val IenSkeletonMotionDurationMillis = 1200L

private val LocalIenSkeletonBlockMotionEnabled = staticCompositionLocalOf { true }

@Composable
private fun IenSkeletonPattern.elements(): List<IenSkeletonElement> = when (this) {
    IenSkeletonPattern.TopList -> listOf(
        IenSkeletonElement.Title,
        IenSkeletonElement.Spacer(IenTheme.spacing.xxs),
        IenSkeletonElement.List,
    )

    IenSkeletonPattern.TopListWithIcon -> listOf(
        IenSkeletonElement.Title,
        IenSkeletonElement.Spacer(IenTheme.spacing.xxs),
        IenSkeletonElement.ListWithIcon,
    )

    IenSkeletonPattern.AmountTopList -> listOf(
        IenSkeletonElement.Title,
        IenSkeletonElement.Subtitle,
        IenSkeletonElement.Spacer(IenTheme.spacing.xs),
        IenSkeletonElement.List,
    )

    IenSkeletonPattern.AmountTopListWithIcon -> listOf(
        IenSkeletonElement.Title,
        IenSkeletonElement.Subtitle,
        IenSkeletonElement.Spacer(IenTheme.spacing.xs),
        IenSkeletonElement.ListWithIcon,
    )

    IenSkeletonPattern.SubtitleList -> listOf(
        IenSkeletonElement.Subtitle,
        IenSkeletonElement.Spacer(IenTheme.spacing.xxs),
        IenSkeletonElement.List,
    )

    IenSkeletonPattern.SubtitleListWithIcon -> listOf(
        IenSkeletonElement.Subtitle,
        IenSkeletonElement.Spacer(IenTheme.spacing.xxs),
        IenSkeletonElement.ListWithIcon,
    )

    IenSkeletonPattern.ListOnly -> listOf(IenSkeletonElement.List)
    IenSkeletonPattern.ListWithIconOnly -> listOf(IenSkeletonElement.ListWithIcon)
    IenSkeletonPattern.CardOnly -> listOf(IenSkeletonElement.Card)
}

private fun List<IenSkeletonElement>.withRepeatedLast(repeat: IenSkeletonRepeat): List<IenSkeletonElement> {
    if (isEmpty()) {
        return this
    }
    val repeatCount = when (repeat) {
        is IenSkeletonRepeat.Count -> repeat.value.coerceAtLeast(1)
        IenSkeletonRepeat.Infinite -> 30
    }
    return dropLast(1) + List(repeatCount) { last() }
}

internal fun resolveIenSkeletonColor(
    background: IenSkeletonBackground,
    colors: IenColorScheme,
): Color = when (background) {
    IenSkeletonBackground.White -> colors.surfaceRaised
    IenSkeletonBackground.Grey -> colors.surfaceVariant
    IenSkeletonBackground.GreyOpacity100 -> colors.textPrimary.copy(alpha = 0.05f)
}

@Composable
private fun IenSkeletonElementView(
    element: IenSkeletonElement,
    index: Int,
    radius: Dp,
    color: Color,
    phase: Float,
) {
    when (element) {
        IenSkeletonElement.Title -> IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(0.42f),
            height = 24.dp,
            radius = radius,
            color = color,
            phase = phase,
            animationIndex = index,
        )

        IenSkeletonElement.Subtitle -> IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(0.34f),
            height = 16.dp,
            radius = radius,
            color = color,
            phase = phase,
            animationIndex = index,
        )

        IenSkeletonElement.List -> IenSkeletonListRow(
            index = index,
            radius = radius,
            color = color,
            phase = phase,
        )

        IenSkeletonElement.ListWithIcon -> IenSkeletonListRowWithIcon(
            index = index,
            radius = radius,
            color = color,
            phase = phase,
        )

        is IenSkeletonElement.ListRowTexts -> IenSkeletonListRowTexts(
            type = element.type,
            index = index,
            radius = radius,
            color = color,
            phase = phase,
        )

        IenSkeletonElement.Card -> IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(),
            height = 132.dp,
            radius = IenTheme.radius.default,
            color = color,
            phase = phase,
            animationIndex = index,
        )

        is IenSkeletonElement.Block -> IenSkeletonBlock(
            modifier = element.modifier.then(
                element.width?.let(Modifier::width) ?: Modifier.fillMaxWidth(),
            ),
            height = if (element.height != Dp.Unspecified) {
                element.height
            } else {
                val typography = requireNotNull(element.typography)
                val fontSize = typography.fontSize.takeIf { it.isSp }
                    ?: IenTheme.typography.body2.fontSize
                val lineHeight = when {
                    typography.lineHeight.isSp -> typography.lineHeight
                    typography.lineHeight.isEm -> fontSize * typography.lineHeight.value
                    typography.fontSize.isSp -> typography.fontSize
                    else -> IenTheme.typography.body2.lineHeight
                }
                with(LocalDensity.current) { lineHeight.toDp() }
            },
            radius = radius,
            color = color,
            phase = phase,
            animationIndex = index,
            shape = element.shape,
        )

        is IenSkeletonElement.Row -> Row(
            modifier = element.modifier,
            horizontalArrangement = element.horizontalArrangement,
            verticalAlignment = element.verticalAlignment,
        ) {
            element.elements.forEachIndexed { childIndex, child ->
                IenSkeletonElementView(
                    element = child,
                    index = index + childIndex + 1,
                    radius = radius,
                    color = color,
                    phase = phase,
                )
            }
        }

        is IenSkeletonElement.Column -> Column(
            modifier = element.modifier,
            verticalArrangement = element.verticalArrangement,
            horizontalAlignment = element.horizontalAlignment,
        ) {
            element.elements.forEachIndexed { childIndex, child ->
                IenSkeletonElementView(
                    element = child,
                    index = index + childIndex + 1,
                    radius = radius,
                    color = color,
                    phase = phase,
                )
            }
        }

        is IenSkeletonElement.Box -> Box(
            modifier = element.modifier,
            contentAlignment = element.contentAlignment,
        ) {
            element.elements.forEachIndexed { childIndex, child ->
                IenSkeletonElementView(
                    element = child,
                    index = index + childIndex + 1,
                    radius = radius,
                    color = color,
                    phase = phase,
                )
            }
        }

        is IenSkeletonElement.Spacer -> Spacer(
            modifier = Modifier.width(element.width).height(element.height),
        )
    }
}

@Composable
private fun IenSkeletonListRow(
    index: Int,
    radius: Dp,
    color: Color,
    phase: Float,
) {
    val widthFraction = when (index % 3) {
        0 -> 0.92f
        1 -> 0.78f
        else -> 0.86f
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(widthFraction),
            height = 18.dp,
            radius = radius,
            color = color,
            phase = phase,
            animationIndex = index,
        )
    }
}

@Composable
private fun IenSkeletonListRowWithIcon(
    index: Int,
    radius: Dp,
    color: Color,
    phase: Float,
) {
    val primaryWidthFraction = when (index % 3) {
        0 -> 0.70f
        1 -> 0.56f
        else -> 0.64f
    }
    val secondaryWidthFraction = when (index % 3) {
        0 -> 0.38f
        1 -> 0.44f
        else -> 0.32f
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .ienSkeletonMotion(phase = phase, animationIndex = index)
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            IenSkeletonBlock(
                modifier = Modifier.fillMaxWidth(primaryWidthFraction),
                height = 18.dp,
                radius = radius,
                color = color,
                phase = phase,
                animationIndex = index,
                animate = false,
            )
            IenSkeletonBlock(
                modifier = Modifier.fillMaxWidth(secondaryWidthFraction),
                height = 14.dp,
                radius = radius,
                color = color,
                phase = phase,
                animationIndex = index,
                animate = false,
            )
        }
    }
}

@Composable
private fun IenSkeletonListRowTexts(
    type: IenListRowTextsType,
    index: Int,
    radius: Dp,
    color: Color,
    phase: Float,
) {
    Box(modifier = Modifier.ienSkeletonMotion(phase = phase, animationIndex = index)) {
        IenListRowTexts(
            type = type,
            top = {
                IenSkeletonTextBlock(0.7f, radius, color, phase, index)
            },
            middle = {
                IenSkeletonTextBlock(0.55f, radius, color, phase, index)
            },
            bottom = {
                IenSkeletonTextBlock(0.4f, radius, color, phase, index)
            },
        )
    }
}

@Composable
private fun IenSkeletonTextBlock(
    widthFraction: Float,
    radius: Dp,
    color: Color,
    phase: Float,
    index: Int,
) {
    val style = LocalTextStyle.current
    val density = LocalDensity.current
    val fontHeight = with(density) { style.fontSize.toDp() }
    val lineHeight = with(density) { style.lineHeight.toDp() }
    Box(
        modifier = Modifier.fillMaxWidth().height(lineHeight),
        contentAlignment = if (style.textAlign == TextAlign.End) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        IenSkeletonBlock(
            modifier = Modifier.fillMaxWidth(widthFraction),
            height = fontHeight,
            radius = radius,
            color = color,
            phase = phase,
            animationIndex = index,
            animate = false,
        )
    }
}

@Composable
private fun rememberIenSkeletonPhase(): Float {
    if (LocalInspectionMode.current) return 0f

    var phase by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillis { frameMillis ->
                val normalized = (frameMillis % IenSkeletonMotionDurationMillis).toFloat() / IenSkeletonMotionDurationMillis
                phase = normalized * (PI * 2.0).toFloat()
            }
        }
    }
    return phase
}

@Composable
private fun IenSkeletonBlock(
    modifier: Modifier,
    height: Dp,
    radius: Dp,
    color: Color,
    phase: Float,
    animationIndex: Int,
    animate: Boolean = true,
    shape: Shape? = null,
) {
    val motionEnabled = LocalIenSkeletonBlockMotionEnabled.current
    val motionModifier = if (animate && motionEnabled) {
        Modifier.ienSkeletonMotion(phase = phase, animationIndex = animationIndex)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(motionModifier)
            .height(height)
            .clip(shape ?: ContinuousRoundedRectangle(radius))
            .background(color),
    )
}

@Composable
private fun Modifier.ienSkeletonMotion(
    phase: Float,
    animationIndex: Int,
): Modifier {
    if (LocalInspectionMode.current) return this

    val delayedPhase = phase - animationIndex * 0.78f
    val wave = ((sin(delayedPhase) + 1f) / 2f).coerceIn(0f, 1f)
    val alpha = 0.2f + 0.8f * wave
    val scale = 0.96f + 0.04f * wave

    return graphicsLayer {
        this.alpha = alpha
        scaleX = scale
        scaleY = scale
    }
}
