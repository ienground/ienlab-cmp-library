package zone.ien.utils.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.utils.shakeOnDisabledClick
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.agreement_optional
import zone.ien.utils.cmp_ui.generated.resources.agreement_required
import zone.ien.utils.cmp_ui.generated.resources.agreement_terms
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.interactive.IenCircleCheckbox
import zone.ien.utils.ui.interactive.IenDotCheckbox
import zone.ien.utils.ui.primitives.IenIcon

/**
 * 약관 동의 컴포넌트의 스타일 및 크기 유형을 정의하는 열거형 클래스입니다.
 */
enum class IenAgreementVariant {
    XLarge,
    Large,
    Medium,
    MediumTitle,
    Small,
    SmallLast
}

/**
 * 약관 동의 컴포넌트에 표시할 체크박스의 유형을 정의하는 열거형 클래스입니다.
 */
enum class IenAgreementCheckboxVariant {
    Checkbox,
    Dot,
    Hidden
}

/**
 * 약관 동의 체크박스의 상태 전환 애니메이션의 강도를 정의하는 열거형 클래스입니다.
 */
enum class IenAgreementCheckboxMotionVariant {
    Strong,
    Weak
}

/**
 * 약관 동의의 필수 여부를 정의하는 열거형 클래스입니다.
 */
enum class IenAgreementNecessityVariant {
    Optional,
    Mandatory
}

/**
 * 약관 동의 설명(Description) 영역의 레이아웃 스타일을 정의하는 열거형 클래스입니다.
 */
enum class IenAgreementDescriptionVariant {
    Box,
    Normal
}

/**
 * 약관 동의 헤더 컴포넌트의 스타일 및 크기 유형을 정의하는 열거형 클래스입니다.
 */
enum class IenAgreementHeaderVariant {
    XLarge,
    Large,
    Medium,
    MediumTitle,
    Small,
    SmallLast,
}

/** 약관 동의 컴포넌트의 들여쓰기 깊이를 제공하기 위한 CompositionLocal입니다. */
val LocalIenAgreementIndent = staticCompositionLocalOf { 0 }
/** 약관 동의 컴포넌트의 스타일 변형을 제공하기 위한 CompositionLocal입니다. */
val LocalIenAgreementVariant = staticCompositionLocalOf { IenAgreementVariant.Large }
/** 약관 동의 컴포넌트의 접기/펼치기 상태를 제공하기 위한 CompositionLocal입니다. */
val LocalIenAgreementCollapsibleState = staticCompositionLocalOf { false }
/** 약관 동의 컴포넌트의 접기/펼치기 토글 콜백을 제공하기 위한 CompositionLocal입니다. */
val LocalIenAgreementCollapsibleToggle = staticCompositionLocalOf<() -> Unit> { {} }
/** 약관 동의 컴포넌트의 하위 항목 펼침 토글 콜백을 제공하기 위한 CompositionLocal입니다. */
val LocalIenAgreementPushableToggle = staticCompositionLocalOf<() -> Unit> { {} }

/**
 * 개별 약관 동의 항목 또는 약관 그룹의 타이틀을 표시하기 위한 기본 행(Row) 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param variant 컴포넌트의 크기 및 스타일 변형 ([IenAgreementVariant])
 * @param indent 추가적인 들여쓰기 수준
 * @param onClick 클릭 이벤트 콜백
 * @param onPressEnd 눌림 완료 시 이벤트 콜백 (기본값은 onClick)
 * @param left 좌측 영역 컴포저블 (예: 체크박스)
 * @param middle 중앙 영역 컴포저블 (예: 약관 제목 텍스트)
 * @param right 우측 영역 컴포저블 (예: 상세화살표, 배지 등)
 */
@Composable
fun IenAgreement(
    modifier: Modifier = Modifier,
    variant: IenAgreementVariant = IenAgreementVariant.Large,
    indent: Int = 0,
    onClick: (() -> Unit)? = null,
    onPressEnd: (() -> Unit)? = onClick,
    left: (@Composable RowScope.() -> Unit)? = null,
    middle: (@Composable RowScope.() -> Unit)? = null,
    right: (@Composable RowScope.() -> Unit)? = null,
) {
    val currentIndent = LocalIenAgreementIndent.current + indent
    val minHeight = variant.agreementMinHeight()
    val verticalPadding = variant.agreementVerticalPadding()
    val startPadding = IenTheme.spacing.md + currentIndent.agreementIndentPadding()
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressAlpha by animateFloatAsState(
        targetValue = if (pressed && onPressEnd != null) 0.72f else 1f,
        animationSpec = tween(durationMillis = 120, easing = IenTheme.motion.standardEasing),
        label = "ienAgreementPressAlpha",
    )

    CompositionLocalProvider(LocalIenAgreementVariant provides variant) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight)
                .then(
                    if (onPressEnd != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.Button,
                            onClick = onPressEnd,
                        )
                    } else {
                        Modifier
                    }
                )
                .graphicsLayer { alpha = pressAlpha }
                .padding(start = startPadding, end = IenTheme.spacing.md)
                .padding(vertical = verticalPadding),
            horizontalArrangement = Arrangement.spacedBy(variant.agreementSlotGap()),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (left != null) {
                Row(
                    modifier = Modifier.width(24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    left.invoke(this)
                }
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                middle?.invoke(this)
            }
            if (right != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    right.invoke(this)
                }
            }
        }
    }
}

/**
 * 약관 동의 체크박스 컴포저블입니다. 설정에 따라 동그라미 체크박스 또는 점(Dot) 체크박스로 노출됩니다.
 *
 * @param checked 체크 여부
 * @param onCheckedChange 체크 상태 변경 콜백
 * @param modifier 적용할 Modifier
 * @param variant 체크박스의 형태 변형 ([IenAgreementCheckboxVariant])
 * @param motionVariant 애니메이션 동작 강도 ([IenAgreementCheckboxMotionVariant])
 * @param transitionDelay 애니메이션 지연 시간 (초 단위)
 * @param enabled 체크박스 활성화 여부
 */
@Composable
fun IenAgreementCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    variant: IenAgreementCheckboxVariant = IenAgreementCheckboxVariant.Checkbox,
    motionVariant: IenAgreementCheckboxMotionVariant = IenAgreementCheckboxMotionVariant.Weak,
    transitionDelay: Float = 0f,
    enabled: Boolean = true,
) {
    val delayMillis = ((transitionDelay + 0.1f) * 1000).toInt().coerceAtLeast(0)
    val scale by animateFloatAsState(
        targetValue = if (checked) 1f else 0.86f,
        animationSpec = when (motionVariant) {
            IenAgreementCheckboxMotionVariant.Strong -> spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium,
            )
            IenAgreementCheckboxMotionVariant.Weak -> tween(
                durationMillis = 180,
                delayMillis = delayMillis,
                easing = IenTheme.motion.standardEasing,
            )
        },
        label = "ienAgreementCheckboxScale",
    )

    when (variant) {
        IenAgreementCheckboxVariant.Checkbox -> {
            IenCircleCheckbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                modifier = modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            )
        }
        IenAgreementCheckboxVariant.Dot -> {
            IenDotCheckbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                modifier = modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            )
        }
        IenAgreementCheckboxVariant.Hidden -> {
            Spacer(modifier = modifier.size(IenTheme.spacing.xl))
        }
    }
}

/**
 * 약관 동의 항목의 텍스트 컴포저블입니다. 필수 여부를 나타내는 텍스트와 함께 표시될 수 있습니다.
 *
 * @param text 표시할 약관 설명 텍스트
 * @param modifier 적용할 Modifier
 * @param necessity 필수/선택 표시 컴포저블 ([IenAgreementNecessity] 등)
 * @param onPressEnd 텍스트 클릭 시 동작할 콜백
 * @param enabled 활성화 여부
 */
@Composable
fun IenAgreementText(
    text: String,
    modifier: Modifier = Modifier,
    necessity: (@Composable () -> Unit)? = null,
    onPressEnd: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val alpha by animateFloatAsState(
        targetValue = if (pressed && onPressEnd != null) 0.64f else 1f,
        animationSpec = tween(durationMillis = 120, easing = IenTheme.motion.standardEasing),
        label = "ienAgreementTextPressAlpha",
    )
    Row(
        modifier = modifier
            .shakeOnDisabledClick(enabled)
            .then(
                if (onPressEnd != null) {
                    Modifier.clickable(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onPressEnd,
                    )
                } else Modifier
            )
            .graphicsLayer { this.alpha = alpha },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        necessity?.invoke()
        Text(
            text = text,
            modifier = Modifier.weight(1f, fill = false),
            style = LocalIenAgreementVariant.current.agreementTextStyle(),
            color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            fontWeight = if (LocalIenAgreementVariant.current == IenAgreementVariant.MediumTitle) FontWeight.Bold else null,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/**
 * 약관 동의 항목의 필수/선택 여부를 텍스트 배지 형태로 표시하는 컴포저블입니다.
 *
 * @param variant 필수/선택 구분 변형 ([IenAgreementNecessityVariant])
 * @param modifier 적용할 Modifier
 * @param text 표시할 텍스트 (기본값은 '[필수]' 또는 '[선택]')
 */
@Composable
fun IenAgreementNecessity(
    variant: IenAgreementNecessityVariant,
    modifier: Modifier = Modifier,
    text: String = if (variant == IenAgreementNecessityVariant.Mandatory) stringResource(Res.string.agreement_required) else stringResource(Res.string.agreement_optional)
) {
    val isMandatory = variant == IenAgreementNecessityVariant.Mandatory
    Text(
        text = text,
        modifier = modifier,
        style = IenTheme.typography.caption,
        color = if (isMandatory) IenTheme.colors.brand else IenTheme.colors.textSecondary,
        fontWeight = FontWeight.Bold,
    )
}

/**
 * 약관 동의 배지의 배경 스타일 변형을 정의하는 열거형 클래스입니다.
 */
enum class IenAgreementBadgeVariant {
    Fill,
    Clear
}

/**
 * 약관 동의 항목 우측 등에 표시할 작은 텍스트 배지 컴포저블입니다.
 *
 * @param text 배지 텍스트
 * @param modifier 적용할 Modifier
 * @param variant 배지 스타일 변형 ([IenAgreementBadgeVariant])
 * @param bgColor 배경 색상
 * @param textColor 텍스트 색상
 */
@Composable
fun IenAgreementBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: IenAgreementBadgeVariant = IenAgreementBadgeVariant.Clear,
    bgColor: Color? = null,
    textColor: Color? = null,
) {
    val resolvedBg = if (variant == IenAgreementBadgeVariant.Fill) (bgColor ?: IenTheme.colors.brandWeak) else Color.Transparent
    val resolvedText = textColor ?: IenTheme.colors.brand
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(IenTheme.radius.xs))
            .background(resolvedBg)
            .padding(
                horizontal = IenTheme.spacing.xxs + IenTheme.spacing.xxxs,
                vertical = IenTheme.spacing.xxxs + IenTheme.spacing.xxxs / 2f,
            ),
    ) {
        Text(text = text, style = IenTheme.typography.caption, color = resolvedText)
    }
}

/**
 * 접기/펼치기 및 상세보기를 위해 약관 동의 항목 우측에 배치되는 화살표 아이콘 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param collapsed 접힘 여부 (null인 경우 CompositionLocal 상태를 사용)
 * @param onArrowClick 화살표 클릭 시 동작할 콜백
 * @param onClick 전체 클릭 시 동작할 콜백 (기본값은 onArrowClick)
 */
@Composable
fun IenAgreementRightArrow(
    modifier: Modifier = Modifier,
    collapsed: Boolean? = null,
    onArrowClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = onArrowClick,
) {
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    val resolvedCollapsed = collapsed ?: LocalIenAgreementCollapsibleState.current
    val rotation by animateFloatAsState(
        targetValue = if (resolvedCollapsed) 180f else 0f,
        animationSpec = tween(durationMillis = IenTheme.motion.fastMillis, easing = IenTheme.motion.standardEasing),
        label = "ienAgreementRightArrowRotation",
    )
    IenIcon(
        icon = IconData.Vector(SystemIcons.ChevronDown),
        contentDescription = null,
        tint = IenTheme.colors.textDisabled,
        modifier = modifier
            .rotate(rotation)
            .then(clickableModifier)
    )
}

/**
 * 약관 동의 항목의 상세 설명이나 추가 안내 문구를 박스형 또는 텍스트 형태로 표시하는 컴포저블입니다.
 *
 * @param text 안내 문구 텍스트
 * @param modifier 적용할 Modifier
 * @param variant 설명 영역의 레이아웃 스타일 변형 ([IenAgreementDescriptionVariant])
 * @param indent 추가 들여쓰기 값
 */
@Composable
fun IenAgreementDescription(
    text: String,
    modifier: Modifier = Modifier,
    variant: IenAgreementDescriptionVariant = IenAgreementDescriptionVariant.Normal,
    indent: Int = 0,
) {
    val startPadding =
        (LocalIenAgreementIndent.current + indent).agreementIndentPadding() +
            IenTheme.spacing.md +
            IenTheme.spacing.xxl
    if (variant == IenAgreementDescriptionVariant.Box) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = startPadding, end = IenTheme.spacing.md, top = IenTheme.spacing.xs, bottom = IenTheme.spacing.xs)
                .clip(RoundedCornerShape(IenTheme.radius.default))
                .background(IenTheme.colors.surfaceWeak)
                .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        ) {
            Text(
                text = text,
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textSecondary
            )
        }
    } else {
        Text(
            text = text,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = startPadding, end = IenTheme.spacing.md, top = IenTheme.spacing.xxs),
            style = IenTheme.typography.caption,
            color = IenTheme.colors.textTertiary,
        )
    }
}

/**
 * 약관 동의 목록 또는 그룹 상단에 배치되는 헤더 텍스트 컴포저블입니다.
 *
 * @param text 헤더 텍스트
 * @param modifier 적용할 Modifier
 * @param variant 헤더의 크기 및 스타일 변형 ([IenAgreementHeaderVariant])
 * @param indent 추가 들여쓰기 값
 */
@Composable
fun IenAgreementHeader(
    text: String,
    modifier: Modifier = Modifier,
    variant: IenAgreementHeaderVariant = IenAgreementHeaderVariant.Medium,
    indent: Int = 0,
) {
    val currentIndent = LocalIenAgreementIndent.current + indent
    val agreementVariant = variant.toAgreementVariant()
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = currentIndent.agreementIndentPadding() + IenTheme.spacing.md,
                top = agreementVariant.agreementVerticalPadding(),
                end = IenTheme.spacing.md,
                bottom = IenTheme.spacing.xs,
            ),
        style = agreementVariant.agreementTextStyle(),
        color = IenTheme.colors.textPrimary,
        fontWeight = if (agreementVariant == IenAgreementVariant.XLarge || agreementVariant == IenAgreementVariant.MediumTitle) FontWeight.Bold else null,
    )
}

private fun IenAgreementHeaderVariant.toAgreementVariant(): IenAgreementVariant = when (this) {
    IenAgreementHeaderVariant.XLarge -> IenAgreementVariant.XLarge
    IenAgreementHeaderVariant.Large -> IenAgreementVariant.Large
    IenAgreementHeaderVariant.Medium -> IenAgreementVariant.Medium
    IenAgreementHeaderVariant.MediumTitle -> IenAgreementVariant.MediumTitle
    IenAgreementHeaderVariant.Small -> IenAgreementVariant.Small
    IenAgreementHeaderVariant.SmallLast -> IenAgreementVariant.SmallLast
}

/**
 * 사용자의 터치 피드백(눌림 효과)을 지원하는 약관 동의용 Row 컨테이너 컴포저블입니다.
 *
 * @param onPressEnd 클릭 완료 시 동작할 콜백
 * @param modifier 적용할 Modifier
 * @param content 내부 콘텐츠 컴포저블
 */
@Composable
fun IenAgreementPressable(
    onPressEnd: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val alpha by animateFloatAsState(
        targetValue = if (pressed) 0.72f else 1f,
        animationSpec = tween(durationMillis = 120, easing = IenTheme.motion.standardEasing),
        label = "ienAgreementPressableAlpha",
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onPressEnd,
            )
            .graphicsLayer { this.alpha = alpha },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

/**
 * 관련 약관 항목들을 그룹화하고 필요한 경우 좌측에 트리 형태의 가이드 라인을 그려주는 그룹 컨테이너 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param showGradient 좌측 가이드 라인 렌더링 여부
 * @param content 그룹 내에 들어갈 약관 항목들
 */
@Composable
fun IenAgreementGroup(
    modifier: Modifier = Modifier,
    showGradient: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val lineStart = IenTheme.spacing.md + IenTheme.spacing.sm
    val lineWidth = IenTheme.stroke.thick
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (showGradient) {
                    Modifier.drawBehind {
                        val x = lineStart.toPx()
                        drawLine(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFFE5E8EB),
                                    Color(0xFFE5E8EB),
                                    Color.Transparent,
                                ),
                            ),
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = lineWidth.toPx(),
                            cap = StrokeCap.Round,
                        )
                    }
                } else Modifier
            ),
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.none)
    ) {
        content()
    }
}

/**
 * 하위 약관 목록을 접고 펼칠 수 있는 접이식 약관 컨테이너 컴포저블입니다.
 *
 * @param collapsed 접힘 상태 (비제어 시 null 제공)
 * @param modifier 적용할 Modifier
 * @param defaultCollapsed 초기 접힘 상태 (기본값은 false)
 * @param onCollapsedChange 접힘 상태 변경 시 호출될 콜백
 * @param content 컨테이너 내부 컴포저블
 */
@Composable
fun IenAgreementCollapsible(
    collapsed: Boolean? = null,
    modifier: Modifier = Modifier,
    defaultCollapsed: Boolean = false,
    onCollapsedChange: ((Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    var localCollapsed by remember { mutableStateOf(defaultCollapsed) }
    val resolvedCollapsed = collapsed ?: localCollapsed
    val changeCollapsed: (Boolean) -> Unit = { next ->
        if (collapsed == null) {
            localCollapsed = next
        }
        onCollapsedChange?.invoke(next)
    }
    CompositionLocalProvider(
        LocalIenAgreementCollapsibleState provides resolvedCollapsed,
        LocalIenAgreementCollapsibleToggle provides { changeCollapsed(!resolvedCollapsed) }
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.none)
        ) {
            content()
        }
    }
}

/**
 * 접이식 약관 컨테이너([IenAgreementCollapsible])를 접고 펼치기 위한 터치 트리거 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param content 트리거 영역 내부 콘텐츠
 */
@Composable
fun IenAgreementCollapsibleTrigger(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val toggle = LocalIenAgreementCollapsibleToggle.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = toggle)
    ) {
        content()
    }
}

/**
 * 접이식 약관 컨테이너([IenAgreementCollapsible])의 상태에 따라 여닫히는 내부 본문 콘텐츠 영역 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param content 접혀 숨겨지거나 펼쳐질 콘텐츠 내용
 */
@Composable
fun IenAgreementCollapsibleContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val collapsed = LocalIenAgreementCollapsibleState.current
    AnimatedVisibility(
        visible = !collapsed,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.none)
        ) {
            content()
        }
    }
}

/**
 * 하위 항목을 접어 들여쓰기 상태를 한 단계 더 깊게 적용하고 토글할 수 있는 컨테이너 컴포저블입니다.
 *
 * @param pushed 들여쓰기 및 하위 콘텐츠 노출 활성화 여부
 * @param modifier 적용할 Modifier
 * @param defaultPushed 초기 노출 상태
 * @param onPushedChange 상태 변경 시 콜백
 * @param content 하위 구성 요소들
 */
@Composable
fun IenAgreementIndentPushable(
    pushed: Boolean? = null,
    modifier: Modifier = Modifier,
    defaultPushed: Boolean = false,
    onPushedChange: ((Boolean) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    var localPushed by remember { mutableStateOf(defaultPushed) }
    val resolvedPushed = pushed ?: localPushed
    val currentIndent = LocalIenAgreementIndent.current
    val nextIndent = if (resolvedPushed) currentIndent + 1 else currentIndent
    val changePushed: (Boolean) -> Unit = { next ->
        if (pushed == null) {
            localPushed = next
        }
        onPushedChange?.invoke(next)
    }
    CompositionLocalProvider(
        LocalIenAgreementIndent provides nextIndent,
        LocalIenAgreementPushableToggle provides { changePushed(!resolvedPushed) },
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.none)
        ) {
            content()
        }
    }
}

/**
 * 들여쓰기 토글 컨테이너([IenAgreementIndentPushable])의 노출 상태를 전환하기 위한 투명 터치 트리거 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param content 트리거 영역 내부 컴포저블
 */
@Composable
fun IenAgreementIndentPushableTrigger(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val toggle = LocalIenAgreementPushableToggle.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = toggle,
            )
    ) {
        content()
    }
}

/**
 * 들여쓰기 토글 컨테이너([IenAgreementIndentPushable]) 내부에 들어갈 콘텐츠를 포함하는 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param content 내부 콘텐츠 컴포저블
 */
@Composable
fun IenAgreementIndentPushableContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.none)
    ) {
        content()
    }
}

@Composable
private fun IenAgreementVariant.agreementTextStyle(): TextStyle = when (this) {
    IenAgreementVariant.XLarge -> IenTheme.typography.title3
    IenAgreementVariant.Large -> IenTheme.typography.body1
    IenAgreementVariant.Medium -> IenTheme.typography.body2
    IenAgreementVariant.MediumTitle -> IenTheme.typography.body1
    IenAgreementVariant.Small,
    IenAgreementVariant.SmallLast -> IenTheme.typography.body2
}

private fun IenAgreementVariant.agreementMinHeight(): Dp = when (this) {
    IenAgreementVariant.XLarge -> 64.dp
    IenAgreementVariant.Large -> 56.dp
    IenAgreementVariant.Medium,
    IenAgreementVariant.MediumTitle -> 48.dp
    IenAgreementVariant.Small -> 40.dp
    IenAgreementVariant.SmallLast -> 36.dp
}

private fun IenAgreementVariant.agreementVerticalPadding(): Dp = when (this) {
    IenAgreementVariant.XLarge -> 14.dp
    IenAgreementVariant.Large -> 10.dp
    IenAgreementVariant.Medium,
    IenAgreementVariant.MediumTitle -> 8.dp
    IenAgreementVariant.Small -> 6.dp
    IenAgreementVariant.SmallLast -> 4.dp
}

private fun IenAgreementVariant.agreementSlotGap(): Dp = when (this) {
    IenAgreementVariant.XLarge,
    IenAgreementVariant.Large -> 10.dp
    IenAgreementVariant.Medium,
    IenAgreementVariant.MediumTitle -> 8.dp
    IenAgreementVariant.Small,
    IenAgreementVariant.SmallLast -> 8.dp
}

private fun Int.agreementIndentPadding(): Dp = (coerceAtLeast(0) * 24).dp

/**
 * 복수 약관 동의 컴포넌트 목록을 생성하는 데 사용할 개별 약관 항목 정보를 정의하는 데이터 클래스입니다.
 *
 * @property id 항목 고유 식별자(ID)
 * @property title 약관 명칭
 * @property checked 동의(체크) 여부
 * @property required 필수 동의 여부 (true이면 '[필수]', false이면 '[선택]')
 * @property description 하단에 노출될 설명 문구 (선택사항)
 * @property enabled 활성화 여부
 * @property indent 하위 요소로서 들여쓰기 처리할지 여부
 */
@Immutable
data class IenAgreementItem(
    val id: String,
    val title: String,
    val checked: Boolean,
    val required: Boolean = false,
    val description: String? = null,
    val enabled: Boolean = true,
    val indent: Boolean = false,
)

/**
 * 여러 개의 약관 동의 항목([IenAgreementItem]) 목록을 카드 리스트 형태로 일괄 제공하는 고수준 약관 컴포저블입니다.
 * 상단에 '전체 동의' 체크박스가 포함되어 있습니다.
 *
 * @param items 표시할 개별 약관 데이터 리스트
 * @param onItemCheckedChange 약관 항목의 체크 상태 변경 시 호출되는 콜백
 * @param modifier 적용할 Modifier
 * @param title 전체 동의 영역에 표시될 타이틀 문구
 * @param onAllCheckedChange 전체 동의 체크 상태 변경 시 호출되는 콜백 (null이면 내부에서 개별 아이템 체크를 일괄 적용)
 * @param variant 전체 동의 카드 헤더의 스타일
 * @param itemVariant 개별 약관 항목의 스타일 변형
 */
@Composable
fun IenAgreement(
    items: List<IenAgreementItem>,
    onItemCheckedChange: (id: String, checked: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.agreement_terms),
    onAllCheckedChange: ((Boolean) -> Unit)? = null,
    variant: IenAgreementVariant = IenAgreementVariant.Large,
    itemVariant: IenAgreementVariant = IenAgreementVariant.Small,
) {
    val allChecked = items.isNotEmpty() && items.all { it.checked }
    val shape = RoundedCornerShape(IenTheme.radius.lg)
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
        shape = shape,
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)
        ) {
            IenAgreement(
                variant = IenAgreementVariant.XLarge,
                onClick = {
                    val nextChecked = !allChecked
                    if (onAllCheckedChange != null) {
                        onAllCheckedChange(nextChecked)
                    } else {
                        items.forEach { onItemCheckedChange(it.id, nextChecked) }
                    }
                },
                left = {
                    IenAgreementCheckbox(
                        checked = allChecked,
                        onCheckedChange = { checked ->
                            if (onAllCheckedChange != null) {
                                onAllCheckedChange(checked)
                            } else {
                                items.forEach { onItemCheckedChange(it.id, checked) }
                            }
                        }
                    )
                },
                middle = {
                    Text(
                        text = title,
                        style = IenTheme.typography.body1,
                        color = IenTheme.colors.textPrimary,
                    )
                }
            )
            IenDivider.Horizontal()
            items.forEach { item ->
                IenAgreement(
                    variant = if (item.indent) IenAgreementVariant.Small else itemVariant,
                    indent = if (item.indent) 1 else 0,
                    onClick = {
                        if (item.enabled) onItemCheckedChange(item.id, !item.checked)
                    },
                    left = {
                        IenAgreementCheckbox(
                            checked = item.checked,
                            onCheckedChange = { if (item.enabled) onItemCheckedChange(item.id, it) },
                            enabled = item.enabled,
                            variant = if (item.indent) IenAgreementCheckboxVariant.Dot else IenAgreementCheckboxVariant.Checkbox
                        )
                    },
                    middle = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                        ) {
                            IenAgreementText(
                                text = item.title,
                                enabled = item.enabled,
                                necessity = if (item.required) {
                                    { IenAgreementNecessity(IenAgreementNecessityVariant.Mandatory) }
                                } else null
                            )
                            if (item.description != null) {
                                Text(
                                    text = item.description,
                                    style = IenTheme.typography.caption,
                                    color = if (item.enabled) IenTheme.colors.textTertiary else IenTheme.colors.textDisabled,
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}
