package zone.ien.utils.ui.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import zone.ien.utils.ui.utils.instantPress
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.line.ArrowRightS
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenText

/**
 * [IenButton]의 크기 규격을 정의하는 열거형 클래스.
 */
enum class IenButtonSize { Small, Medium, Large }

/**
 * 플로팅 액션 버튼([IenFab])의 크기 규격을 정의하는 열거형 클래스.
 */
enum class IenFabSize { Small, Regular, Large }

/**
 * 버튼 내부에서 아이콘이 텍스트의 어느 쪽에 배치될지 정의하는 열거형 클래스.
 */
enum class IenIconPlacement { Start, End }

/**
 * 버튼의 가로 배치 방식을 정의하는 열거형 클래스.
 */
enum class IenButtonDisplay { Inline, Block, Full }

/**
 * 텍스트 버튼([IenTextButton])의 텍스트 및 프레임 크기를 정의하는 열거형 클래스.
 */
enum class IenTextButtonSize { XSmall, Small, Medium, Large, XLarge, XXLarge }

/**
 * 텍스트 버튼([IenTextButton])의 스타일 유형을 정의하는 열거형 클래스.
 */
enum class IenTextButtonVariant { Clear, Arrow, Underline }

/**
 * 버튼 컴포저블이 눌렸을 때의 상태 변경을 상위 컴포넌트에 알리기 위해 사용하는 [staticCompositionLocalOf].
 */
internal val LocalIenButtonPressedReporter = staticCompositionLocalOf<((Any, Boolean) -> Unit)?> { null }

/**
 * 버튼이 눌릴 때의 축소 효과 비율 스케일을 커스텀 오버라이드하기 위해 사용하는 [staticCompositionLocalOf].
 */
internal val LocalIenButtonScalePressedOverride = staticCompositionLocalOf<Float?> { null }

/**
 * 버튼의 비주얼 스타일 변형(배경색 채우기 방식 등)을 정의하는 인터페이스.
 */
sealed interface IenButtonVariant {
    /** 채워진 배경색 버튼 */
    data object Fill : IenButtonVariant
    /** 옅은 배경색 버튼 */
    data object Weak : IenButtonVariant
    /** 아웃라인 테두리만 있는 버튼 */
    data object Line : IenButtonVariant
    /** 배경이나 테두리가 없는 투명 버튼 */
    data object Ghost : IenButtonVariant
}

/**
 * 버튼의 활성화 상태 및 로딩 여부를 관리하는 상태 정의 클래스.
 *
 * @property enabled 버튼이 활성화되어 사용자와 상호작용할 수 있는지 여부.
 * @property loading 로딩 아이콘 노출 및 인터랙션 제한 상태 여부.
 */
@Immutable
data class IenButtonState(
    val enabled: Boolean = true,
    val loading: Boolean = false,
)

/**
 * IEN 라이브러리의 범용 버튼 컴포저블.
 *
 * @param text 버튼 내부에 표시할 문자열.
 * @param onClick 버튼 클릭 시 실행할 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param size 버튼의 높이 및 내부 패딩 크기 규격 ([IenButtonSize]). 기본값은 [IenButtonSize.Large].
 * @param variant 버튼의 비주얼 스타일 변형 ([IenButtonVariant]). 기본값은 [IenButtonVariant.Fill].
 * @param tone 버튼의 시각적 어조 또는 의미적 강조 색상 ([IenSemanticTone]). 기본값은 [IenSemanticTone.Brand].
 * @param state 버튼의 활성화 및 로딩 진행 상태 ([IenButtonState]).
 * @param icon 버튼 텍스트와 함께 표시할 아이콘 컴포저블.
 * @param iconPlacement 아이콘이 배치될 위치 ([IenIconPlacement]). 기본값은 [IenIconPlacement.Start].
 * @param shape 버튼의 형태 정의 ([Shape]).
 * @param contentPadding 버튼 내부 콘텐츠의 여백 ([PaddingValues]).
 * @param backgroundBrush 버튼 배경에 적용할 브러시. null이면 Fill 버튼에 tone 기반 기본 그라데이션을 사용합니다.
 * @param interactionSource 버튼 인터랙션 정보를 전달할 [MutableInteractionSource].
 * @param display 버튼의 가로 확장 모드 ([IenButtonDisplay]). 기본값은 [IenButtonDisplay.Inline].
 */
@Composable
fun IenButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    icon: (@Composable () -> Unit)? = null,
    iconPlacement: IenIconPlacement = IenIconPlacement.Start,
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    contentPadding: PaddingValues = size.buttonPadding(),
    backgroundBrush: Brush? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    display: IenButtonDisplay = IenButtonDisplay.Inline,
) {
    val height = size.buttonHeight()
    val buttonModifier = modifier
        .then(if (display == IenButtonDisplay.Block || display == IenButtonDisplay.Full) Modifier.fillMaxWidth() else Modifier)
        .heightIn(min = height)

    val resolvedShape = if (display == IenButtonDisplay.Full) {
        ContinuousRoundedRectangle(0.dp)
    } else {
        shape
    }
    IenButtonContainer(
        onClick = onClick,
        modifier = buttonModifier,
        variant = variant,
        tone = tone,
        state = state,
        shape = resolvedShape,
        contentPadding = contentPadding,
        backgroundBrush = backgroundBrush,
        interactionSource = interactionSource,
        scalePressed = 0.975f,
    ) {
        IenButtonContent(
            text = text,
            loading = state.loading,
            size = size,
            icon = icon,
            iconPlacement = iconPlacement,
        )
    }
}

/**
 * 텍스트 없이 단일 아이콘 또는 이미지 등으로 구성되는 아이콘 전용 버튼 컴포저블.
 *
 * @param onClick 버튼 클릭 시 실행할 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param size 버튼의 크기 규격 ([IenButtonSize]). 기본값은 [IenButtonSize.Large].
 * @param variant 버튼의 비주얼 스타일 변형 ([IenButtonVariant]). 기본값은 [IenButtonVariant.Fill].
 * @param tone 버튼의 시각적 어조 또는 의미적 강조 색상 ([IenSemanticTone]). 기본값은 [IenSemanticTone.Brand].
 * @param state 버튼의 활성화 및 로딩 진행 상태 ([IenButtonState]).
 * @param shape 버튼의 형태 정의 ([Shape]).
 * @param interactionSource 버튼 인터랙션 정보를 전달할 [MutableInteractionSource].
 * @param content 버튼 중심에 표시할 아이콘 등의 컴포저블 콘텐츠.
 */
@Composable
fun IenIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val buttonSize = when (size) {
        IenButtonSize.Small -> 36.dp
        IenButtonSize.Medium -> 44.dp
        IenButtonSize.Large -> 52.dp
    }

    val buttonModifier = modifier.size(buttonSize)

    val innerContent: @Composable () -> Unit = {
        val iconSize = when (size) {
            IenButtonSize.Small -> 18.dp
            IenButtonSize.Medium -> 24.dp
            IenButtonSize.Large -> 28.dp
        }
        IenProvideTextStyle(IenTheme.typography.body1, LocalContentColor.current) {
            Box(
                modifier = Modifier.size(iconSize),
                contentAlignment = Alignment.Center
            ) {
                if (state.loading) {
                    IenLoaderPrimitive(color = LocalContentColor.current)
                } else {
                    content()
                }
            }
        }
    }

    IenButtonContainer(
        onClick = onClick,
        modifier = buttonModifier,
        variant = variant,
        tone = tone,
        state = state,
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        scalePressed = 0.95f,
        content = innerContent,
    )
}

/**
 * 화면 위에 떠 있는 듯한 원형 형태의 플로팅 액션 버튼(FAB) 컴포저블.
 *
 * @param onClick 버튼 클릭 시 실행할 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param size 플로팅 버튼의 크기 규격 ([IenFabSize]). 기본값은 [IenFabSize.Regular].
 * @param variant 버튼의 비주얼 스타일 변형 ([IenButtonVariant]). 기본값은 [IenButtonVariant.Fill].
 * @param tone 버튼의 시각적 어조 또는 의미적 강조 색상 ([IenSemanticTone]). 기본값은 [IenSemanticTone.Brand].
 * @param state 버튼의 활성화 및 로딩 진행 상태 ([IenButtonState]).
 * @param shape 버튼의 형태 정의 ([Shape]). 기본값은 [CircleShape].
 * @param interactionSource 버튼 인터랙션 정보를 전달할 [MutableInteractionSource].
 * @param content 플로팅 버튼 내부에 표시할 컴포저블 콘텐츠.
 */
@Composable
fun IenFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenFabSize = IenFabSize.Regular,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = CircleShape,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val fabSize = size.fabSize()
    val iconSize = size.fabIconSize()

    IenButtonContainer(
        onClick = onClick,
        modifier = modifier.size(fabSize),
        variant = variant,
        tone = tone,
        state = state,
        shape = shape,
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        scalePressed = 0.95f,
    ) {
        IenProvideTextStyle(IenTheme.typography.body1, LocalContentColor.current) {
            Box(
                modifier = Modifier.size(iconSize),
                contentAlignment = Alignment.Center,
            ) {
                if (state.loading) {
                    IenLoaderPrimitive(color = LocalContentColor.current)
                } else {
                    content()
                }
            }
        }
    }
}

/**
 * 아이콘과 텍스트를 함께 보여주는 둥근 형태의 확장형 플로팅 액션 버튼(Extended FAB) 컴포저블.
 *
 * @param text 버튼 내부에 표시할 문자열.
 * @param onClick 버튼 클릭 시 실행할 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param variant 버튼의 비주얼 스타일 변형 ([IenButtonVariant]). 기본값은 [IenButtonVariant.Fill].
 * @param tone 버튼의 시각적 어조 또는 의미적 강조 색상 ([IenSemanticTone]). 기본값은 [IenSemanticTone.Brand].
 * @param state 버튼의 활성화 및 로딩 진행 상태 ([IenButtonState]).
 * @param icon 버튼 텍스트와 함께 표시할 아이콘 컴포저블.
 * @param iconPlacement 아이콘이 배치될 위치 ([IenIconPlacement]). 기본값은 [IenIconPlacement.Start].
 * @param shape 버튼의 형태 정의 ([Shape]). 기본값은 타원형 캡슐 형태인 [IenTheme.radius.full]을 가집니다.
 * @param contentPadding 버튼 내부 콘텐츠의 여백 ([PaddingValues]).
 * @param interactionSource 버튼 인터랙션 정보를 전달할 [MutableInteractionSource].
 */
@Composable
fun IenExtendedFab(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    icon: (@Composable () -> Unit)? = null,
    iconPlacement: IenIconPlacement = IenIconPlacement.Start,
    shape: Shape = ContinuousCapsule(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    IenButtonContainer(
        onClick = onClick,
        modifier = modifier.heightIn(min = 56.dp),
        variant = variant,
        tone = tone,
        state = state,
        shape = shape,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        scalePressed = 0.96f,
    ) {
        IenButtonContent(
            text = text,
            loading = state.loading,
            size = IenButtonSize.Large,
            icon = icon,
            iconPlacement = iconPlacement,
        )
    }
}

/**
 * 배경과 아웃라인이 없거나 투명하며 텍스트만으로 구성된 버튼 컴포저블.
 *
 * @param text 버튼에 표시할 문자열.
 * @param onClick 버튼 클릭 시 실행할 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param size 텍스트 버튼의 크기 및 텍스트 스타일 규격 ([IenTextButtonSize]). 기본값은 [IenTextButtonSize.Medium].
 * @param variant 텍스트 스타일 종류 ([IenTextButtonVariant]). 기본값은 [IenTextButtonVariant.Clear].
 * @param disabled 비활성화 여부. true일 경우 사용자와 상호작용할 수 없습니다.
 * @param tone 텍스트 및 아이콘의 시각적 어조 또는 의미적 강조 색상 ([IenSemanticTone]). 기본값은 [IenSemanticTone.Brand].
 * @param state 버튼의 활성화 상태 정보를 담는 상태 객체.
 * @param interactionSource 버튼 인터랙션 정보를 전달할 [MutableInteractionSource].
 */
@Composable
fun IenTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenTextButtonSize = IenTextButtonSize.Medium,
    variant: IenTextButtonVariant = IenTextButtonVariant.Clear,
    disabled: Boolean = false,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(enabled = !disabled),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val enabled = state.enabled && !disabled
    val contentColor = if (enabled) {
        toneColor(tone)
    } else {
        toneColor(tone).copy(alpha = IenTheme.state.disabledAlpha)
    }
    val textStyle = size.textStyle().let {
        if (variant == IenTextButtonVariant.Underline) {
            it.copy(textDecoration = TextDecoration.Underline)
        } else {
            it
        }
    }

    IenButtonContainer(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = size.minHeight()),
        variant = IenButtonVariant.Ghost,
        tone = tone,
        state = state.copy(enabled = enabled),
        shape = ContinuousRoundedRectangle(IenTheme.radius.sm),
        contentPadding = size.contentPadding(),
        interactionSource = interactionSource,
        scalePressed = 0.97f,
        colors = IenButtonResolvedColors(
            container = Color.Transparent,
            content = contentColor,
            border = Color.Transparent,
            disabledContainer = Color.Transparent,
            disabledContent = contentColor,
        ),
    ) {
        IenProvideTextStyle(textStyle, contentColor) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(size.iconGap()),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IenText(
                    text = text,
                    color = LocalContentColor.current,
                    style = textStyle,
                )
                if (variant == IenTextButtonVariant.Arrow) {
                    Icon(
                        imageVector = RemixIcons.Line.ArrowRightS,
                        contentDescription = null,
                        tint = LocalContentColor.current,
                        modifier = Modifier.size(size.chevronSize())
                    )
                }
            }
        }
    }
}

@Composable
private fun IenButtonContent(
    text: String,
    loading: Boolean,
    size: IenButtonSize,
    icon: (@Composable () -> Unit)?,
    iconPlacement: IenIconPlacement,
) {
    val textStyle = when (size) {
        IenButtonSize.Small -> IenTheme.typography.label2
        IenButtonSize.Medium -> IenTheme.typography.label1
        IenButtonSize.Large -> IenTheme.typography.body1
    }
    IenProvideTextStyle(textStyle, LocalContentColor.current) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (loading) {
                IenLoaderPrimitive(color = LocalContentColor.current)
            } else {
                if (iconPlacement == IenIconPlacement.Start) icon?.invoke()
                if (text.isNotEmpty()) IenText(text = text, color = LocalContentColor.current, style = textStyle)
                if (iconPlacement == IenIconPlacement.End) icon?.invoke()
            }
        }
    }
}

/**
 * 버튼이 그려질 때 각 상태별 배경색, 콘텐츠색, 테두리색 정보를 갖고 있는 내부 데이터 클래스.
 */
@Immutable
internal data class IenButtonResolvedColors(
    val container: Color,
    val content: Color,
    val border: Color,
    val disabledContainer: Color,
    val disabledContent: Color,
)

@Composable
private fun ienButtonColors(
    variant: IenButtonVariant,
    tone: IenSemanticTone,
    enabled: Boolean,
): IenButtonResolvedColors {
    val toneColor = toneColor(tone)
    val weakColor = toneWeakColor(tone)
    val onToneColor = toneOnColor(tone)
    val resolved = when (variant) {
        IenButtonVariant.Fill -> IenButtonResolvedColors(toneColor, onToneColor, toneColor, toneColor, onToneColor)
        IenButtonVariant.Weak -> IenButtonResolvedColors(weakColor, toneColor, weakColor, weakColor, toneColor)
        IenButtonVariant.Line -> IenButtonResolvedColors(Color.Transparent, toneColor, IenTheme.colors.borderStrong, Color.Transparent, toneColor)
        IenButtonVariant.Ghost -> IenButtonResolvedColors(Color.Transparent, toneColor, Color.Transparent, Color.Transparent, toneColor)
    }
    return if (enabled) resolved else resolved.copy(
        container = resolved.container.copy(alpha = IenTheme.state.disabledAlpha),
        content = resolved.content.copy(alpha = IenTheme.state.disabledAlpha),
        disabledContainer = resolved.disabledContainer.copy(alpha = IenTheme.state.disabledAlpha),
        disabledContent = resolved.disabledContent.copy(alpha = IenTheme.state.disabledAlpha),
    )
}

/**
 * 의미론적 색상 톤에 대입되는 라이브러리 기본 테마 색상을 반환합니다.
 */
@Composable
internal fun toneColor(tone: IenSemanticTone): Color = when (tone) {
    IenSemanticTone.Neutral -> IenTheme.colors.textPrimary
    IenSemanticTone.Brand -> IenTheme.colors.brand
    IenSemanticTone.Success -> IenTheme.colors.success
    IenSemanticTone.Warning -> IenTheme.colors.warning
    IenSemanticTone.Danger -> IenTheme.colors.danger
    IenSemanticTone.Info -> IenTheme.colors.info
}

/**
 * 의미론적 색상 톤에 대입되는 옅은 어조의 라이브러리 테마 색상을 반환합니다.
 */
@Composable
internal fun toneWeakColor(tone: IenSemanticTone): Color = when (tone) {
    IenSemanticTone.Neutral -> IenTheme.colors.surfaceWeak
    IenSemanticTone.Brand -> IenTheme.colors.brandWeak
    IenSemanticTone.Success -> IenTheme.colors.successWeak
    IenSemanticTone.Warning -> IenTheme.colors.warningWeak
    IenSemanticTone.Danger -> IenTheme.colors.dangerWeak
    IenSemanticTone.Info -> IenTheme.colors.infoWeak
}

/**
 * 의미론적 색상 톤의 배경 위에 올라갈 텍스트나 아이콘의 라이브러리 테마 색상을 반환합니다.
 */
@Composable
internal fun toneOnColor(tone: IenSemanticTone): Color = when (tone) {
    IenSemanticTone.Neutral -> IenTheme.colors.surfaceRaised
    IenSemanticTone.Brand -> IenTheme.colors.onBrand
    IenSemanticTone.Success -> IenTheme.colors.onSuccess
    IenSemanticTone.Warning -> IenTheme.colors.onWarning
    IenSemanticTone.Danger -> IenTheme.colors.onDanger
    IenSemanticTone.Info -> IenTheme.colors.onInfo
}

@Composable
internal fun toneGradientBrush(tone: IenSemanticTone): Brush {
    val base = toneColor(tone)
    val start = lerp(base, Color.White, 0.18f)
    val end = lerp(base, Color.White, 0.36f)
    return Brush.linearGradient(
        colors = listOf(start, base, end),
    )
}

@Composable
internal fun toneWeakGradientBrush(tone: IenSemanticTone): Brush {
    val base = toneWeakColor(tone)
    val accent = toneColor(tone)
    val start = lerp(base, Color.White, 0.16f)
    val end = lerp(base, accent, 0.08f)
    return Brush.linearGradient(
        colors = listOf(start, base, end),
    )
}

private fun IenButtonSize.buttonHeight(): Dp = when (this) {
    IenButtonSize.Small -> 36.dp
    IenButtonSize.Medium -> 44.dp
    IenButtonSize.Large -> 52.dp
}

private fun IenButtonSize.buttonPadding(): PaddingValues = when (this) {
    IenButtonSize.Small -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    IenButtonSize.Medium -> PaddingValues(horizontal = 16.dp, vertical = 10.dp)
    IenButtonSize.Large -> PaddingValues(horizontal = 20.dp, vertical = 14.dp)
}

private fun IenFabSize.fabSize(): Dp = when (this) {
    IenFabSize.Small -> 40.dp
    IenFabSize.Regular -> 56.dp
    IenFabSize.Large -> 96.dp
}

private fun IenFabSize.fabIconSize(): Dp = when (this) {
    IenFabSize.Small -> 20.dp
    IenFabSize.Regular -> 24.dp
    IenFabSize.Large -> 36.dp
}

@Composable
private fun IenTextButtonSize.textStyle(): TextStyle = when (this) {
    IenTextButtonSize.XSmall -> IenTheme.typography.caption
    IenTextButtonSize.Small -> IenTheme.typography.label2
    IenTextButtonSize.Medium -> IenTheme.typography.label1
    IenTextButtonSize.Large -> IenTheme.typography.body2
    IenTextButtonSize.XLarge -> IenTheme.typography.body1
    IenTextButtonSize.XXLarge -> IenTheme.typography.title3
}

private fun IenTextButtonSize.minHeight(): Dp = when (this) {
    IenTextButtonSize.XSmall -> 28.dp
    IenTextButtonSize.Small -> 32.dp
    IenTextButtonSize.Medium -> 36.dp
    IenTextButtonSize.Large -> 40.dp
    IenTextButtonSize.XLarge -> 44.dp
    IenTextButtonSize.XXLarge -> 48.dp
}

private fun IenTextButtonSize.contentPadding(): PaddingValues = when (this) {
    IenTextButtonSize.XSmall -> PaddingValues(horizontal = 4.dp, vertical = 4.dp)
    IenTextButtonSize.Small -> PaddingValues(horizontal = 5.dp, vertical = 5.dp)
    IenTextButtonSize.Medium -> PaddingValues(horizontal = 6.dp, vertical = 6.dp)
    IenTextButtonSize.Large -> PaddingValues(horizontal = 7.dp, vertical = 7.dp)
    IenTextButtonSize.XLarge -> PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    IenTextButtonSize.XXLarge -> PaddingValues(horizontal = 8.dp, vertical = 8.dp)
}

private fun IenTextButtonSize.iconGap(): Dp = when (this) {
    IenTextButtonSize.XSmall,
    IenTextButtonSize.Small,
    IenTextButtonSize.Medium -> 2.dp
    IenTextButtonSize.Large,
    IenTextButtonSize.XLarge,
    IenTextButtonSize.XXLarge -> 3.dp
}

private fun IenTextButtonSize.chevronSize(): Dp = when (this) {
    IenTextButtonSize.XSmall -> 10.dp
    IenTextButtonSize.Small -> 12.dp
    IenTextButtonSize.Medium -> 14.dp
    IenTextButtonSize.Large -> 16.dp
    IenTextButtonSize.XLarge -> 18.dp
    IenTextButtonSize.XXLarge -> 20.dp
}

/**
 * 다양한 유형의 버튼 컴포저블들의 클릭 제어, 크기 확대/축소 모션 및 실제 렌더링 뼈대를 구성하는 내부 공통 컨테이너 컴포저블.
 */
@Composable
internal fun IenButtonContainer(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    shape: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    scalePressed: Float = 0.975f,
    colors: IenButtonResolvedColors? = null,
    border: BorderStroke? = null,
    backgroundBrush: Brush? = null,
    content: @Composable () -> Unit,
) {
    val ienColors = ienButtonColors(variant, tone, state.enabled)
    val interactiveEnabled = state.enabled && !state.loading
    val pressedReporter = LocalIenButtonPressedReporter.current
    val pressedToken = remember { Any() }
    val resolvedScalePressed = LocalIenButtonScalePressedOverride.current ?: scalePressed

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && interactiveEnabled) resolvedScalePressed else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    val reportedPressed = isPressed && interactiveEnabled

    LaunchedEffect(pressedReporter, pressedToken, reportedPressed) {
        pressedReporter?.invoke(pressedToken, reportedPressed)
    }

    DisposableEffect(pressedReporter, pressedToken) {
        onDispose {
            pressedReporter?.invoke(pressedToken, false)
        }
    }

    val buttonModifier = modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .semantics { role = Role.Button }
        .semantics {
            if (!interactiveEnabled) disabled()
        }
        .instantPress(interactiveEnabled) { isPressed = it }

    val resolvedIenColors = colors ?: ienColors
    val effectiveBackgroundBrush = (backgroundBrush ?: when (variant) {
        IenButtonVariant.Fill -> toneGradientBrush(tone)
        IenButtonVariant.Weak -> toneWeakGradientBrush(tone)
        IenButtonVariant.Line, IenButtonVariant.Ghost -> null
    })
        .takeIf { state.enabled }
    val containerColor = if (effectiveBackgroundBrush == null) resolvedIenColors.container else Color.Transparent
    val disabledContainerColor = if (effectiveBackgroundBrush == null) {
        if (state.loading && state.enabled) {
            resolvedIenColors.container
        } else {
            resolvedIenColors.disabledContainer
        }
    } else {
        Color.Transparent
    }
    val resolvedColors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = resolvedIenColors.content,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = if (state.loading && state.enabled) {
            resolvedIenColors.content
        } else {
            resolvedIenColors.disabledContent
        },
    )

    val handleOnClick: () -> Unit = {
        if (state.enabled && !state.loading) {
            onClick()
        }
    }
    val paintedButtonModifier = if (effectiveBackgroundBrush == null) {
        buttonModifier
    } else {
        buttonModifier.background(effectiveBackgroundBrush, shape)
    }

    when (variant) {
        IenButtonVariant.Fill, IenButtonVariant.Weak -> Button(
            onClick = handleOnClick,
            modifier = paintedButtonModifier,
            enabled = true,
            shape = shape,
            colors = resolvedColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { content() },
        )

        IenButtonVariant.Line -> OutlinedButton(
            onClick = handleOnClick,
            modifier = buttonModifier,
            enabled = true,
            shape = shape,
            border = border ?: BorderStroke(IenTheme.stroke.thin, ienColors.border),
            colors = resolvedColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { content() },
        )

        IenButtonVariant.Ghost -> TextButton(
            onClick = handleOnClick,
            modifier = buttonModifier,
            enabled = true,
            shape = shape,
            colors = resolvedColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = { content() },
        )
    }
}
