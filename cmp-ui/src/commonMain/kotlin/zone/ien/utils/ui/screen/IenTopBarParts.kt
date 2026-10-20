package zone.ien.utils.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenBadge
import zone.ien.utils.ui.interactive.IenBadgeSize
import zone.ien.utils.ui.interactive.IenBadgeVariant
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.LocalIenButtonPressedReporter
import zone.ien.utils.ui.interactive.LocalIenButtonScalePressedOverride
import zone.ien.utils.ui.interactive.IenTextButton
import zone.ien.utils.ui.interactive.IenTextButtonSize
import zone.ien.utils.ui.interactive.IenTextButtonVariant
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.style.LineBreak

internal val LocalIenTopBarFloatingSlotHiddenRequester = staticCompositionLocalOf<((Boolean) -> Unit)?> { null }
/**
 * 상단 앱 바 제목의 정렬 방식을 정의하는 열거형 클래스입니다.
 */
enum class IenTopBarTitleAlignment {
    /** 시작 부분 정렬 */
    Start,
    /** 중앙 정렬 */
    Center,
}

/**
 * 상단 영역 우측 콘텐츠의 수직 정렬 방식을 정의하는 열거형 클래스입니다.
 */
enum class IenTopRightVerticalAlign {
    /** 중앙 정렬 */
    Center,
    /** 하단 정렬 */
    End,
}

/**
 * 상단 타이틀 텍스트의 크기 스타일을 정의하는 열거형 클래스입니다.
 */
enum class IenTopTitleSize {
    /** 기본 크기 */
    Default,
    /** 큰 크기 */
    Large,
}

/**
 * 상단 서브타이틀 텍스트의 크기 스타일을 정의하는 열거형 클래스입니다.
 */
enum class IenTopSubtitleSize {
    /** 작은 크기 */
    Small,
    /** 중간 크기 */
    Medium,
    /** 큰 크기 */
    Large,
}

/**
 * 상단 선택기 컴포넌트의 타입(아이콘 종류)을 정의하는 열거형 클래스입니다.
 */
enum class IenTopSelectorType {
    /** 화살표 아이콘 */
    Arrow,
    /** 클리어(삭제) 아이콘 */
    Clear,
}

/**
 * 상단 서브타이틀 영역에 표시할 배지 정보를 담는 데이터 클래스입니다.
 *
 * @property text 배지에 표시할 텍스트
 * @property tone 배지의 의미론적 색상 톤 ([IenSemanticTone])
 * @property variant 배지의 스타일 변형 ([IenBadgeVariant])
 */
@Immutable
data class IenTopSubtitleBadge(
    val text: String,
    val tone: IenSemanticTone = IenSemanticTone.Brand,
    val variant: IenBadgeVariant = IenBadgeVariant.Weak,
)

/**
 * 문자열 형식의 타이틀을 사용하는 상단 앱 바 컴포저블입니다.
 *
 * @param title 제목 텍스트
 * @param modifier 적용할 Modifier
 * @param subtitle 부제목 텍스트 (선택사항)
 * @param navigationIcon 네비게이션 아이콘 영역 컴포저블
 * @param actions 우측 액션 영역 컴포저블
 * @param titleAlignment 타이틀 정렬 방식 ([IenTopBarTitleAlignment])
 * @param showDivider 하단 구분선 표시 여부
 * @param windowInsets 상단 바에 적용할 윈도우 인셋
 * @param contentPadding 상단 바 내부 패딩
 * @param contentHeight 상단 바의 높이 (기본값 64.dp)
 * @param containerColor 상단 바 배경 색상
 * @param floatingSlots 플로팅 스타일 적용 여부
 */
@Composable
fun IenTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    titleAlignment: IenTopBarTitleAlignment = IenTopBarTitleAlignment.Start,
    showDivider: Boolean = false,
    windowInsets: WindowInsets = WindowInsets.statusBars,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = IenTheme.spacing.md,
        vertical = IenTheme.spacing.xxs + IenTheme.spacing.xxxs,
    ),
    contentHeight: Dp = 64.dp,
    containerColor: Color = Color.Transparent,
    floatingSlots: Boolean = true,
) {
    IenTopBar(
        title = {
            Text(
                text = title,
                style = IenTheme.typography.title3,
                textAlign = if (titleAlignment == IenTopBarTitleAlignment.Center) TextAlign.Center else null,
            )
        },
        modifier = modifier,
        subtitle = subtitle?.let {
            {
                Text(
                    text = it,
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.textSecondary,
                    textAlign = if (titleAlignment == IenTopBarTitleAlignment.Center) TextAlign.Center else null,
                )
            }
        },
        navigationIcon = navigationIcon,
        actions = actions,
        titleAlignment = titleAlignment,
        showDivider = showDivider,
        windowInsets = windowInsets,
        contentPadding = contentPadding,
        contentHeight = contentHeight,
        containerColor = containerColor,
        floatingSlots = floatingSlots,
    )
}

/**
 * 컴포저블 형식의 타이틀을 사용하는 상단 앱 바 컴포저블입니다.
 *
 * @param title 제목 영역 컴포저블
 * @param modifier 적용할 Modifier
 * @param subtitle 부제목 영역 컴포저블
 * @param navigationIcon 네비게이션 아이콘 영역 컴포저블
 * @param actions 우측 액션 영역 컴포저블
 * @param titleAlignment 타이틀 정렬 방식 ([IenTopBarTitleAlignment])
 * @param showDivider 하단 구분선 표시 여부
 * @param windowInsets 상단 바에 적용할 윈도우 인셋
 * @param contentPadding 상단 바 내부 패딩
 * @param contentHeight 상단 바의 높이 (기본값 64.dp)
 * @param containerColor 상단 바 배경 색상
 * @param floatingSlots 플로팅 스타일 적용 여부
 */
@Composable
fun IenTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    titleAlignment: IenTopBarTitleAlignment = IenTopBarTitleAlignment.Start,
    showDivider: Boolean = false,
    windowInsets: WindowInsets = WindowInsets.statusBars,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = IenTheme.spacing.md,
        vertical = IenTheme.spacing.xxs + IenTheme.spacing.xxxs,
    ),
    contentHeight: Dp = 64.dp,
    containerColor: Color = Color.Transparent,
    floatingSlots: Boolean = true,
) {
    val insetPadding = windowInsets.asPaddingValues()
    val topPadding = insetPadding.calculateTopPadding()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(topPadding + contentHeight)
            .background(containerColor),
    ) {
        if (titleAlignment == IenTopBarTitleAlignment.Center) {
            IenCenterAlignedTopBarContent(
                title = title,
                subtitle = subtitle,
                navigationIcon = navigationIcon,
                actions = actions,
                contentPadding = contentPadding,
                topPadding = topPadding,
                contentHeight = contentHeight,
                floatingSlots = floatingSlots,
            )
        } else {
            IenStartAlignedTopBarContent(
                title = title,
                subtitle = subtitle,
                navigationIcon = navigationIcon,
                actions = actions,
                contentPadding = contentPadding,
                topPadding = topPadding,
                contentHeight = contentHeight,
                floatingSlots = floatingSlots,
            )
        }
        if (showDivider) {
            IenDivider.Horizontal()
        }
    }
}

@Composable
private fun IenStartAlignedTopBarContent(
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)?,
    navigationIcon: (@Composable () -> Unit)?,
    actions: (@Composable RowScope.() -> Unit)?,
    contentPadding: PaddingValues,
    topPadding: Dp,
    contentHeight: Dp,
    floatingSlots: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(topPadding + contentHeight)
            .padding(top = topPadding)
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (navigationIcon != null) {
            IenTopBarNavigationIconSlot {
                navigationIcon()
            }
        }
        IenTopBarTitleColumn(
            title = title,
            subtitle = subtitle,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f),
        )
        if (actions != null) {
            IenTopBarFloatingSlot(enabled = floatingSlots) {
                IenTopBarActionsRow(actions)
            }
        }
    }
}

@Composable
private fun IenCenterAlignedTopBarContent(
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)?,
    navigationIcon: (@Composable () -> Unit)?,
    actions: (@Composable RowScope.() -> Unit)?,
    contentPadding: PaddingValues,
    topPadding: Dp,
    contentHeight: Dp,
    floatingSlots: Boolean,
) {
    val sideSpacing = IenTheme.spacing.sm

    SubcomposeLayout(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(topPadding + contentHeight)
            .padding(top = topPadding)
            .padding(contentPadding),
    ) { constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val navigationPlaceables = navigationIcon?.let {
            subcompose("navigation") {
                IenTopBarNavigationIconSlot {
                    it()
                }
            }.map { measurable -> measurable.measure(looseConstraints) }
        }.orEmpty()
        val actionsPlaceables = actions?.let {
            subcompose("actions") {
                IenTopBarFloatingSlot(enabled = floatingSlots) {
                    IenTopBarActionsRow(it)
                }
            }.map { measurable -> measurable.measure(looseConstraints) }
        }.orEmpty()

        val navigationWidth = navigationPlaceables.maxOfOrNull { it.width } ?: 0
        val actionsWidth = actionsPlaceables.maxOfOrNull { it.width } ?: 0
        val sideSpacingPx = sideSpacing.roundToPx()
        val titleLeftLimit = if (navigationWidth > 0) navigationWidth + sideSpacingPx else 0
        val titleRightLimit = if (actionsWidth > 0) constraints.maxWidth - actionsWidth - sideSpacingPx else constraints.maxWidth
        val titleMaxWidth = (titleRightLimit - titleLeftLimit).coerceAtLeast(0)
        val titlePlaceables = subcompose("title") {
            IenTopBarTitleColumn(
                title = title,
                subtitle = subtitle,
                horizontalAlignment = Alignment.CenterHorizontally,
            )
        }.map { measurable ->
            measurable.measure(
                Constraints(
                    minWidth = 0,
                    maxWidth = titleMaxWidth,
                    minHeight = 0,
                    maxHeight = constraints.maxHeight,
                )
            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            navigationPlaceables.forEach { placeable ->
                placeable.placeRelative(
                    x = 0,
                    y = (constraints.maxHeight - placeable.height) / 2,
                )
            }
            actionsPlaceables.forEach { placeable ->
                placeable.placeRelative(
                    x = constraints.maxWidth - placeable.width,
                    y = (constraints.maxHeight - placeable.height) / 2,
                )
            }
            titlePlaceables.forEach { placeable ->
                val centeredX = (constraints.maxWidth - placeable.width) / 2
                val maxTitleX = titleRightLimit - placeable.width
                placeable.placeRelative(
                    x = centeredX.coerceIn(titleLeftLimit, maxTitleX.coerceAtLeast(titleLeftLimit)),
                    y = (constraints.maxHeight - placeable.height) / 2,
                )
            }
        }
    }
}

@Composable
private fun IenTopBarTitleColumn(
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)?,
    horizontalAlignment: Alignment.Horizontal,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
    ) {
        IenProvideTextStyle(IenTheme.typography.title3, IenTheme.colors.textPrimary) {
            title()
        }
        if (subtitle != null) {
            IenProvideTextStyle(IenTheme.typography.caption, IenTheme.colors.textSecondary) {
                subtitle()
            }
        }
    }
}

@Composable
private fun IenTopBarActionsRow(actions: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .clipToBounds()
            .animateContentSize(animationSpec = tween(durationMillis = 110, easing = IenTheme.motion.standardEasing)),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentColor provides IenTheme.colors.textPrimary) {
            actions.invoke(this)
        }
    }
}

@Composable
private fun IenTopBarNavigationIconSlot(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides IenTheme.colors.textPrimary,
        LocalIenNavigationButtonVariant provides IenButtonVariant.Ghost,
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IenTopBarFloatingSlot(
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    if (!enabled) {
        content()
        return
    }

    var pressedTokens by remember { mutableStateOf(setOf<Any>()) }
    var hiddenRequested by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressedTokens.isNotEmpty()) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
    )
    val pressedOverlayAlpha by animateFloatAsState(
        targetValue = if (pressedTokens.isNotEmpty()) 0.08f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
    )
    val visibleScale by animateFloatAsState(
        targetValue = if (hiddenRequested) 0.001f else 1f,
        animationSpec = spring(1.2f),
    )
    val pressedReporter: (Any, Boolean) -> Unit = { token, pressed ->
        pressedTokens = if (pressed) {
            pressedTokens + token
        } else {
            pressedTokens - token
        }
    }

    Box(
        modifier = Modifier.graphicsLayer {
            clip = false
        },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = scale * visibleScale
                    scaleY = scale * visibleScale
                    clip = false
                }
                .shadow(elevation = IenTheme.elevation.floating, shape = CircleShape, clip = false)
                .background(IenTheme.colors.surface.copy(alpha = 0.92f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (pressedOverlayAlpha > 0f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(IenTheme.colors.textPrimary.copy(alpha = pressedOverlayAlpha)),
                )
            }
        }
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .padding(horizontal = IenTheme.spacing.xxs, vertical = IenTheme.spacing.xxs),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(
                LocalIenButtonPressedReporter provides pressedReporter,
                LocalIenButtonScalePressedOverride provides 1f,
                LocalIenTopBarFloatingSlotHiddenRequester provides { hiddenRequested = it },
                LocalIenNavigationButtonVariant provides IenButtonVariant.Ghost,
                LocalRippleConfiguration provides null,
            ) {
                content()
            }
        }
    }
}

/**
 * 상단 헤더 영역을 커스텀 레이아웃으로 표시하기 위한 컴포저블입니다.
 *
 * @param title 제목 컴포저블
 * @param modifier 적용할 Modifier
 * @param upperGap 타이틀 상단 간격
 * @param lowerGap 타이틀 하단 간격
 * @param upper 타이틀 위쪽에 위치할 컴포저블 (예: 뒤로가기 버튼)
 * @param lower 타이틀 아래쪽에 위치할 컴포저블
 * @param subtitleTop 타이틀 바로 위에 위치할 서브타이틀 컴포저블
 * @param subtitleBottom 타이틀 바로 아래에 위치할 서브타이틀 컴포저블
 * @param right 타이틀 우측에 위치할 컴포저블
 * @param rightVerticalAlign 우측 컴포저블의 수직 정렬 방식 ([IenTopRightVerticalAlign])
 * @param contentPadding 전체 콘텐츠의 내부 패딩
 */
@Composable
fun IenTop(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    upperGap: Dp = IenTheme.spacing.xl,
    lowerGap: Dp = IenTheme.spacing.xl,
    upper: (@Composable () -> Unit)? = null,
    lower: (@Composable () -> Unit)? = null,
    subtitleTop: (@Composable () -> Unit)? = null,
    subtitleBottom: (@Composable () -> Unit)? = null,
    right: (@Composable () -> Unit)? = null,
    rightVerticalAlign: IenTopRightVerticalAlign = IenTopRightVerticalAlign.Center,
    contentPadding: PaddingValues = PaddingValues(horizontal = IenTheme.spacing.xl),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .padding(top = upperGap, bottom = lowerGap),
    ) {
        if (upper != null) {
            upper()
            Spacer(Modifier.height(IenTheme.spacing.md))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            verticalAlignment = when (rightVerticalAlign) {
                IenTopRightVerticalAlign.Center -> Alignment.CenterVertically
                IenTopRightVerticalAlign.End -> Alignment.Bottom
            },
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
            ) {
                subtitleTop?.invoke()
                title()
                subtitleBottom?.invoke()
            }
            if (right != null) {
                right()
            }
        }
        if (lower != null) {
            Spacer(Modifier.height(IenTheme.spacing.md))
            lower()
        }
    }
}

/**
 * 문자열 형식의 타이틀을 제공하는 간편한 상단 헤더 컴포저블입니다.
 *
 * @param title 제목 텍스트
 * @param modifier 적용할 Modifier
 * @param subtitle 부제목 텍스트 (선택사항)
 * @param navigation 상단 네비게이션 컴포저블
 * @param actions 우측 액션 컴포저블 목록
 */
@Composable
fun IenTop(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    navigation: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    IenTop(
        modifier = modifier,
        upperGap = IenTheme.spacing.sm,
        lowerGap = IenTheme.spacing.sm,
        title = { IenTopTitleParagraph(title, size = IenTopTitleSize.Large) },
        subtitleBottom = subtitle?.let { { IenTopSubtitleParagraph(it, size = IenTopSubtitleSize.Small) } },
        upper = navigation,
        right = actions?.let { action -> { Row(content = action) } },
        contentPadding = PaddingValues(horizontal = IenTheme.spacing.md),
    )
}

/**
 * 상단 헤더 영역에 사용하는 타이틀 텍스트 단락 컴포저블입니다.
 *
 * @param text 표시할 텍스트
 * @param modifier 적용할 Modifier
 * @param size 타이틀 크기 타입 ([IenTopTitleSize])
 * @param color 텍스트 색상
 * @param style 텍스트 스타일 ([TextStyle])
 * @param fontWeight 텍스트 굵기
 * @param maxLines 최대 표시 줄 수
 */
@Composable
fun IenTopTitleParagraph(
    text: String,
    modifier: Modifier = Modifier,
    size: IenTopTitleSize = IenTopTitleSize.Default,
    color: Color = IenTheme.colors.textPrimary,
    style: TextStyle = size.titleStyle(),
    fontWeight: FontWeight = FontWeight.Bold,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        style = style.copy(fontWeight = fontWeight, lineBreak = LineBreak.Heading),
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * 상단 헤더 타이틀 영역을 텍스트 버튼 형태로 표시하는 컴포저블입니다.
 *
 * @param text 버튼 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param colorTone 버튼 색상 톤 ([IenSemanticTone])
 * @param variant 버튼 스타일 변형 ([IenTextButtonVariant])
 * @param size 버튼 크기 ([IenTextButtonSize])
 * @param state 버튼 상태 ([IenButtonState])
 */
@Composable
fun IenTopTitleTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorTone: IenSemanticTone = IenSemanticTone.Neutral,
    variant: IenTextButtonVariant = IenTextButtonVariant.Clear,
    size: IenTextButtonSize = IenTextButtonSize.XLarge,
    state: IenButtonState = IenButtonState(),
) {
    IenTextButton(
        onClick = onClick,
        modifier = modifier.semantics { heading() },
        size = size,
        variant = variant,
        tone = colorTone,
        state = state,
    ) {
        Text(text)
    }
}

/**
 * 상단 헤더 타이틀 영역을 선택기(Selector) 형태로 표시하는 컴포저블입니다.
 *
 * @param text 표시할 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param type 선택기 타입 ([IenTopSelectorType])
 * @param color 텍스트 및 아이콘 색상
 * @param style 텍스트 스타일
 * @param fontWeight 텍스트 굵기
 */
@Composable
fun IenTopTitleSelector(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: IenTopSelectorType = IenTopSelectorType.Arrow,
    color: Color = IenTheme.colors.textPrimary,
    style: TextStyle = IenTheme.typography.title2,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    IenTopSelector(
        text = text,
        onClick = onClick,
        modifier = modifier.semantics {
            heading()
            role = Role.Button
        },
        type = type,
        color = color,
        style = style.copy(fontWeight = fontWeight),
        iconSize = 22.dp,
    )
}

/**
 * 상단 헤더 영역에 사용하는 부제목(Subtitle) 텍스트 컴포저블입니다.
 *
 * @param text 표시할 부제목 텍스트
 * @param modifier 적용할 Modifier
 * @param size 부제목 크기 ([IenTopSubtitleSize])
 * @param color 텍스트 색상
 * @param style 텍스트 스타일
 * @param fontWeight 텍스트 굵기
 * @param maxLines 최대 표시 줄 수
 */
@Composable
fun IenTopSubtitleParagraph(
    text: String,
    modifier: Modifier = Modifier,
    size: IenTopSubtitleSize = IenTopSubtitleSize.Large,
    color: Color = IenTheme.colors.textSecondary,
    style: TextStyle = size.subtitleStyle(),
    fontWeight: FontWeight = size.subtitleWeight(),
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(fontWeight = fontWeight),
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * 상단 헤더 부제목 영역을 텍스트 버튼 형태로 표시하는 컴포저블입니다.
 *
 * @param text 버튼 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param colorTone 버튼 색상 톤 ([IenSemanticTone])
 * @param variant 버튼 스타일 변형 ([IenTextButtonVariant])
 * @param size 버튼 크기 ([IenTextButtonSize])
 * @param state 버튼 상태 ([IenButtonState])
 */
@Composable
fun IenTopSubtitleTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorTone: IenSemanticTone = IenSemanticTone.Neutral,
    variant: IenTextButtonVariant = IenTextButtonVariant.Arrow,
    size: IenTextButtonSize = IenTextButtonSize.Medium,
    state: IenButtonState = IenButtonState(),
) {
    IenTextButton(
        onClick = onClick,
        modifier = modifier,
        size = size,
        variant = variant,
        tone = colorTone,
        state = state,
    ) {
        Text(text)
    }
}

/**
 * 상단 헤더 부제목 영역을 선택기(Selector) 형태로 표시하는 컴포저블입니다.
 *
 * @param text 표시할 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param type 선택기 타입 ([IenTopSelectorType])
 * @param size 부제목 크기 ([IenTopSubtitleSize])
 * @param color 텍스트 및 아이콘 색상
 * @param style 텍스트 스타일
 * @param fontWeight 텍스트 굵기
 */
@Composable
fun IenTopSubtitleSelector(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: IenTopSelectorType = IenTopSelectorType.Arrow,
    size: IenTopSubtitleSize = IenTopSubtitleSize.Large,
    color: Color = IenTheme.colors.textSecondary,
    style: TextStyle = size.subtitleStyle(),
    fontWeight: FontWeight = size.subtitleWeight(),
) {
    IenTopSelector(
        text = text,
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        type = type,
        color = color,
        style = style.copy(fontWeight = fontWeight),
        iconSize = when (size) {
            IenTopSubtitleSize.Small -> 14.dp
            IenTopSubtitleSize.Medium -> 16.dp
            IenTopSubtitleSize.Large -> 18.dp
        },
    )
}

/**
 * 상단 부제목 영역에 여러 개의 배지를 가로로 나열하는 컴포저블입니다.
 *
 * @param badges 표시할 배지 정보 목록 ([IenTopSubtitleBadge])
 * @param modifier 적용할 Modifier
 */
@Composable
fun IenTopSubtitleBadges(
    badges: List<IenTopSubtitleBadge>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        badges.forEach { badge ->
            IenBadge(
                text = badge.text,
                size = IenBadgeSize.Small,
                variant = badge.variant,
                tone = badge.tone,
            )
        }
    }
}

/**
 * 상단 영역의 하단부에 배치하는 버튼 컴포저블입니다.
 *
 * @param text 버튼 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param size 버튼 크기 ([IenButtonSize])
 * @param variant 버튼 스타일 변형 ([IenButtonVariant])
 * @param tone 버튼 색상 톤 ([IenSemanticTone])
 * @param state 버튼 상태 ([IenButtonState])
 */
@Composable
fun IenTopLowerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Small,
    variant: IenButtonVariant = IenButtonVariant.Weak,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
) {
    IenButton(
        onClick = onClick,
        modifier = modifier,
        size = size,
        variant = variant,
        tone = tone,
        state = state,
    ) {
        Text(text)
    }
}

/**
 * 상단 영역 하단부에 배치되는 듀얼 CTA(Call To Action) 버튼 컨테이너 컴포저블입니다.
 *
 * @param leftButton 좌측 버튼 컴포저블
 * @param rightButton 우측 버튼 컴포저블
 * @param modifier 적용할 Modifier
 */
@Composable
fun IenTopLowerCTA(
    leftButton: @Composable RowScope.() -> Unit,
    rightButton: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leftButton()
        rightButton()
    }
}

/**
 * [IenTopLowerCTA] 내부에 들어갈 CTA 버튼 컴포저블입니다.
 *
 * @param text 버튼 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param size 버튼 크기 ([IenButtonSize])
 * @param variant 버튼 스타일 변형 ([IenButtonVariant])
 * @param tone 버튼 색상 톤 ([IenSemanticTone])
 * @param state 버튼 상태 ([IenButtonState])
 */
@Composable
fun RowScope.IenTopLowerCTAButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
) {
    IenButton(
        onClick = onClick,
        modifier = modifier.weight(1f),
        size = size,
        variant = variant,
        tone = tone,
        state = state,
        display = IenButtonDisplay.Block,
    ) {
        Text(text)
    }
}

/**
 * 상단 우측 영역에 배치되는 버튼 컴포저블입니다.
 *
 * @param text 버튼 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param size 버튼 크기 ([IenButtonSize])
 * @param variant 버튼 스타일 변형 ([IenButtonVariant])
 * @param tone 버튼 색상 톤 ([IenSemanticTone])
 * @param state 버튼 상태 ([IenButtonState])
 */
@Composable
fun IenTopRightButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Medium,
    variant: IenButtonVariant = IenButtonVariant.Weak,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
) {
    IenButton(
        onClick = onClick,
        modifier = modifier,
        size = size,
        variant = variant,
        tone = tone,
        state = state,
    ) {
        Text(text)
    }
}

/**
 * 상단 우측 영역에 어셋(예: 아이콘, 이미지) 콘텐츠를 표시하기 위한 래퍼 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param content 표시할 어셋 컴포저블
 */
@Composable
fun IenTopRightAssetContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.defaultMinSize(minWidth = 60.dp, minHeight = 60.dp),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/**
 * 상단 타이틀 위쪽 영역에 어셋 콘텐츠를 표시하기 위한 래퍼 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param content 표시할 어셋 컴포저블
 */
@Composable
fun IenTopUpperAssetContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier.defaultMinSize(minWidth = 72.dp, minHeight = 72.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        content()
    }
}

@Composable
private fun IenTopSelector(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    type: IenTopSelectorType,
    color: Color,
    style: TextStyle,
    iconSize: Dp,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = IenTheme.spacing.xxs),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IenProvideTextStyle(style, color) {
            Text(
                text = text,
                style = style,
                color = LocalContentColor.current,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (type == IenTopSelectorType.Arrow) {
            IenTopArrow(size = iconSize, color = color)
        }
    }
}

@Composable
private fun IenTopArrow(
    size: Dp,
    color: Color,
) {
    Canvas(modifier = Modifier.size(size)) {
        val strokeWidth = size.toPx() * 0.12f
        val startX = size.toPx() * 0.34f
        val endX = size.toPx() * 0.66f
        val topY = size.toPx() * 0.28f
        val centerY = size.toPx() * 0.50f
        val bottomY = size.toPx() * 0.72f

        drawLine(
            color = color,
            start = Offset(startX, topY),
            end = Offset(endX, centerY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(endX, centerY),
            end = Offset(startX, bottomY),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun IenTopTitleSize.titleStyle(): TextStyle = when (this) {
    IenTopTitleSize.Default -> IenTheme.typography.title2
    IenTopTitleSize.Large -> IenTheme.typography.title1
}

@Composable
private fun IenTopSubtitleSize.subtitleStyle(): TextStyle = when (this) {
    IenTopSubtitleSize.Small -> IenTheme.typography.caption
    IenTopSubtitleSize.Medium -> IenTheme.typography.label1
    IenTopSubtitleSize.Large -> IenTheme.typography.body1
}

private fun IenTopSubtitleSize.subtitleWeight(): FontWeight = when (this) {
    IenTopSubtitleSize.Small,
    IenTopSubtitleSize.Medium -> FontWeight.Normal
    IenTopSubtitleSize.Large -> FontWeight.Medium
}
