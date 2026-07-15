package zone.ien.utils.ui.interactive

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
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
import zone.ien.utils.ui.utils.animateContentSizeWithoutClipping
import zone.ien.utils.ui.utils.instantPress

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
 * 토글 버튼의 선택/비선택 상태별 형태를 정의합니다.
 *
 * @property checked 선택 상태의 버튼 형태입니다.
 * @property unchecked 비선택 상태의 버튼 형태입니다.
 */
data class IenToggleButtonShapes(
    val checked: Shape,
    val unchecked: Shape,
)

/**
 * 토글 버튼의 선택/비선택 상태별 비주얼 변형을 정의합니다.
 *
 * @property checked 선택 상태의 비주얼 변형입니다.
 * @property unchecked 비선택 상태의 비주얼 변형입니다.
 */
data class IenToggleButtonVariants(
    val checked: IenButtonVariant = IenButtonVariant.Fill,
    val unchecked: IenButtonVariant = IenButtonVariant.Weak,
)

/**
 * 토글 버튼의 선택/비선택 상태별 색상과 그라데이션 브러시를 정의합니다.
 *
 * @property checkedContainer 선택 상태의 단색 배경입니다.
 * @property checkedContent 선택 상태의 콘텐츠 색상입니다.
 * @property checkedBorder 선택 상태의 테두리 색상입니다.
 * @property checkedBackgroundBrush 선택 상태의 배경 브러시입니다. null이면 단색 배경을 사용합니다.
 * @property uncheckedContainer 비선택 상태의 단색 배경입니다.
 * @property uncheckedContent 비선택 상태의 콘텐츠 색상입니다.
 * @property uncheckedBorder 비선택 상태의 테두리 색상입니다.
 * @property uncheckedBackgroundBrush 비선택 상태의 배경 브러시입니다. null이면 단색 배경을 사용합니다.
 * @property disabledContainer 비활성 상태의 배경 색상입니다.
 * @property disabledContent 비활성 상태의 콘텐츠 색상입니다.
 * @property disabledBorder 비활성 상태의 테두리 색상입니다.
 */
data class IenToggleButtonColors(
    val checkedContainer: Color,
    val checkedContent: Color,
    val checkedBorder: Color,
    val checkedBackgroundBrush: Brush?,
    val uncheckedContainer: Color,
    val uncheckedContent: Color,
    val uncheckedBorder: Color,
    val uncheckedBackgroundBrush: Brush?,
    val disabledContainer: Color,
    val disabledContent: Color,
    val disabledBorder: Color,
)

/**
 * [IenToggleButton]과 [IenIconToggleButton]의 기본 토큰을 제공합니다.
 */
object IenToggleButtonDefaults {
    /**
     * 토글 버튼의 기본 형태 구성을 생성합니다.
     *
     * @param checked 선택 상태의 버튼 형태입니다.
     * @param unchecked 비선택 상태의 버튼 형태입니다.
     * @return 토글 버튼 형태 구성입니다.
     */
    @Composable
    fun shapes(
        checked: Shape = ContinuousRoundedRectangle(IenTheme.radius.default),
        unchecked: Shape = checked,
    ): IenToggleButtonShapes {
        return IenToggleButtonShapes(
            checked = checked,
            unchecked = unchecked,
        )
    }

    /**
     * 토글 버튼의 기본 비주얼 변형 구성을 생성합니다.
     *
     * @param checked 선택 상태의 비주얼 변형입니다.
     * @param unchecked 비선택 상태의 비주얼 변형입니다.
     * @return 토글 버튼 비주얼 변형 구성입니다.
     */
    fun variants(
        checked: IenButtonVariant = IenButtonVariant.Fill,
        unchecked: IenButtonVariant = IenButtonVariant.Weak,
    ): IenToggleButtonVariants {
        return IenToggleButtonVariants(
            checked = checked,
            unchecked = unchecked,
        )
    }

    /**
     * 토글 버튼의 기본 색상 구성을 생성합니다.
     *
     * @param checkedTone 선택 상태에 사용할 의미 색상 톤입니다.
     * @param uncheckedTone 비선택 상태에 사용할 의미 색상 톤입니다.
     * @param checkedContainer 선택 상태의 단색 배경입니다.
     * @param checkedContent 선택 상태의 콘텐츠 색상입니다.
     * @param checkedBorder 선택 상태의 테두리 색상입니다.
     * @param uncheckedContainer 비선택 상태의 단색 배경입니다.
     * @param uncheckedContent 비선택 상태의 콘텐츠 색상입니다.
     * @param uncheckedBorder 비선택 상태의 테두리 색상입니다.
     * @param checkedBackgroundBrush 선택 상태의 배경 브러시입니다.
     * @param uncheckedBackgroundBrush 비선택 상태의 배경 브러시입니다.
     * @param useGradient 기본 톤 기반 그라데이션 사용 여부입니다.
     * @return 토글 버튼 색상 구성입니다.
     */
    @Composable
    fun colors(
        checkedTone: IenSemanticTone = IenSemanticTone.Brand,
        uncheckedTone: IenSemanticTone = IenSemanticTone.Brand,
        checkedContainer: Color = toneColor(checkedTone),
        checkedContent: Color = toneOnColor(checkedTone),
        checkedBorder: Color = checkedContainer,
        uncheckedContainer: Color = toneWeakColor(uncheckedTone),
        uncheckedContent: Color = toneColor(uncheckedTone),
        uncheckedBorder: Color = Color.Transparent,
        checkedBackgroundBrush: Brush? = null,
        uncheckedBackgroundBrush: Brush? = null,
        useGradient: Boolean = true,
    ): IenToggleButtonColors {
        return IenToggleButtonColors(
            checkedContainer = checkedContainer,
            checkedContent = checkedContent,
            checkedBorder = checkedBorder,
            checkedBackgroundBrush = checkedBackgroundBrush ?: if (useGradient) toneGradientBrush(checkedTone) else null,
            uncheckedContainer = uncheckedContainer,
            uncheckedContent = uncheckedContent,
            uncheckedBorder = uncheckedBorder,
            uncheckedBackgroundBrush = uncheckedBackgroundBrush ?: if (useGradient) toneWeakGradientBrush(uncheckedTone) else null,
            disabledContainer = IenTheme.colors.surfaceWeak,
            disabledContent = IenTheme.colors.textDisabled.copy(alpha = 0.72f),
            disabledBorder = Color.Transparent,
        )
    }
}

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
 * 선택 상태를 표현하는 IEN 토글 버튼 컴포저블입니다.
 *
 * @param checked 현재 선택 상태입니다.
 * @param onCheckedChange 선택 상태 변경 콜백입니다.
 * @param text 버튼 내부에 표시할 문자열입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param size 버튼의 높이 및 내부 패딩 크기 규격입니다.
 * @param state 버튼의 활성화 및 로딩 진행 상태입니다.
 * @param icon 버튼 텍스트와 함께 표시할 아이콘 컴포저블입니다.
 * @param iconPlacement 아이콘이 배치될 위치입니다.
 * @param shapes 선택/비선택 상태별 버튼 형태 구성입니다.
 * @param variants 선택/비선택 상태별 비주얼 변형 구성입니다.
 * @param colors 선택/비선택 상태별 색상 구성입니다.
 * @param contentPadding 버튼 내부 콘텐츠 여백입니다.
 * @param interactionSource 버튼 인터랙션 정보를 전달할 [MutableInteractionSource]입니다.
 * @param display 버튼의 가로 확장 모드입니다.
 */
@Composable
fun IenToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    state: IenButtonState = IenButtonState(),
    icon: (@Composable () -> Unit)? = null,
    iconPlacement: IenIconPlacement = IenIconPlacement.Start,
    shapes: IenToggleButtonShapes? = null,
    variants: IenToggleButtonVariants = IenToggleButtonVariants(),
    colors: IenToggleButtonColors? = null,
    contentPadding: PaddingValues = size.buttonPadding(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    display: IenButtonDisplay = IenButtonDisplay.Inline,
) {
    val height = size.buttonHeight()
    val resolvedState = state
    val resolvedShapes = shapes ?: IenToggleButtonDefaults.shapes()
    val resolvedColors = colors ?: IenToggleButtonDefaults.colors()
    val buttonModifier = modifier
        .then(if (display == IenButtonDisplay.Block || display == IenButtonDisplay.Full) Modifier.fillMaxWidth() else Modifier)
        .heightIn(min = height)
        .animateContentSizeWithoutClipping(animationSpec = toggleButtonAnimationSpec())
        .semantics { selected = checked }
    val targetShape = if (display == IenButtonDisplay.Full) {
        ContinuousRoundedRectangle(0.dp)
    } else if (checked) {
        resolvedShapes.checked
    } else {
        resolvedShapes.unchecked
    }
    val resolvedShape = if (display == IenButtonDisplay.Full) {
        targetShape
    } else {
        rememberAnimatedToggleShape(
            checked = checked,
            shapes = resolvedShapes,
        )
    }

    IenButtonContainer(
        onClick = { onCheckedChange(!checked) },
        modifier = buttonModifier,
        variant = if (checked) variants.checked else variants.unchecked,
        state = resolvedState,
        shape = resolvedShape,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        scalePressed = 0.975f,
        colors = resolvedColors.resolve(checked = checked, enabled = resolvedState.enabled),
        border = resolvedColors.borderStroke(checked = checked, enabled = resolvedState.enabled),
        backgroundBrush = resolvedColors.backgroundBrush(checked = checked, enabled = resolvedState.enabled),
    ) {
        IenButtonContent(
            text = text,
            loading = resolvedState.loading,
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
 * 선택 상태를 표현하는 아이콘 전용 IEN 토글 버튼 컴포저블입니다.
 *
 * @param checked 현재 선택 상태입니다.
 * @param onCheckedChange 선택 상태 변경 콜백입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param size 버튼의 크기 규격입니다.
 * @param state 버튼의 활성화 및 로딩 진행 상태입니다.
 * @param shapes 선택/비선택 상태별 버튼 형태 구성입니다.
 * @param variants 선택/비선택 상태별 비주얼 변형 구성입니다.
 * @param colors 선택/비선택 상태별 색상 구성입니다.
 * @param interactionSource 버튼 인터랙션 정보를 전달할 [MutableInteractionSource]입니다.
 * @param content 버튼 중심에 표시할 아이콘 등의 컴포저블 콘텐츠입니다.
 */
@Composable
fun IenIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Large,
    state: IenButtonState = IenButtonState(),
    shapes: IenToggleButtonShapes? = null,
    variants: IenToggleButtonVariants = IenToggleButtonVariants(),
    colors: IenToggleButtonColors? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val buttonSize = when (size) {
        IenButtonSize.Small -> 36.dp
        IenButtonSize.Medium -> 44.dp
        IenButtonSize.Large -> 52.dp
    }
    val iconSize = when (size) {
        IenButtonSize.Small -> 18.dp
        IenButtonSize.Medium -> 24.dp
        IenButtonSize.Large -> 28.dp
    }
    val resolvedShapes = shapes ?: IenToggleButtonDefaults.shapes()
    val resolvedColors = colors ?: IenToggleButtonDefaults.colors()
    val resolvedShape = rememberAnimatedToggleShape(
        checked = checked,
        shapes = resolvedShapes,
    )

    IenButtonContainer(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier
            .size(buttonSize)
            .semantics { selected = checked },
        variant = if (checked) variants.checked else variants.unchecked,
        state = state,
        shape = resolvedShape,
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        scalePressed = 0.95f,
        colors = resolvedColors.resolve(checked = checked, enabled = state.enabled),
        border = resolvedColors.borderStroke(checked = checked, enabled = state.enabled),
        backgroundBrush = resolvedColors.backgroundBrush(checked = checked, enabled = state.enabled),
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
    if (enabled) return resolved

    val disabledContainer = when (variant) {
        IenButtonVariant.Fill,
        IenButtonVariant.Weak -> IenTheme.colors.surfaceWeak
        IenButtonVariant.Line,
        IenButtonVariant.Ghost -> Color.Transparent
    }
    val disabledContent = IenTheme.colors.textDisabled.copy(alpha = 0.72f)
    val disabledBorder = when (variant) {
        IenButtonVariant.Line -> IenTheme.colors.border.copy(alpha = 0.72f)
        IenButtonVariant.Ghost -> Color.Transparent
        IenButtonVariant.Fill,
        IenButtonVariant.Weak -> Color.Transparent
    }

    return resolved.copy(
        container = disabledContainer,
        content = disabledContent,
        border = disabledBorder,
        disabledContainer = disabledContainer,
        disabledContent = disabledContent,
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

private fun IenToggleButtonColors.resolve(
    checked: Boolean,
    enabled: Boolean,
): IenButtonResolvedColors {
    if (!enabled) {
        return IenButtonResolvedColors(
            container = disabledContainer,
            content = disabledContent,
            border = disabledBorder,
            disabledContainer = disabledContainer,
            disabledContent = disabledContent,
        )
    }
    return if (checked) {
        IenButtonResolvedColors(
            container = checkedContainer,
            content = checkedContent,
            border = checkedBorder,
            disabledContainer = disabledContainer,
            disabledContent = disabledContent,
        )
    } else {
        IenButtonResolvedColors(
            container = uncheckedContainer,
            content = uncheckedContent,
            border = uncheckedBorder,
            disabledContainer = disabledContainer,
            disabledContent = disabledContent,
        )
    }
}

private fun IenToggleButtonColors.backgroundBrush(
    checked: Boolean,
    enabled: Boolean,
): Brush? {
    if (!enabled) return null
    return if (checked) checkedBackgroundBrush else uncheckedBackgroundBrush
}

@Composable
private fun rememberAnimatedToggleShape(
    checked: Boolean,
    shapes: IenToggleButtonShapes,
): Shape {
    val progress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = toggleButtonAnimationSpec(),
    )
    return remember(shapes, progress) {
        if (shapes.checked is CornerBasedShape && shapes.unchecked is CornerBasedShape) {
            LerpCornerBasedShape(
                from = shapes.unchecked,
                to = shapes.checked,
                progress = progress,
            )
        } else if (checked) {
            shapes.checked
        } else {
            shapes.unchecked
        }
    }
}

private class LerpCornerBasedShape(
    private val from: CornerBasedShape,
    private val to: CornerBasedShape,
    private val progress: Float,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        return ContinuousRoundedRectangle(
            topStart = CornerSize(lerpCorner(from.topStart.toPx(size, density), to.topStart.toPx(size, density), progress)),
            topEnd = CornerSize(lerpCorner(from.topEnd.toPx(size, density), to.topEnd.toPx(size, density), progress)),
            bottomEnd = CornerSize(lerpCorner(from.bottomEnd.toPx(size, density), to.bottomEnd.toPx(size, density), progress)),
            bottomStart = CornerSize(lerpCorner(from.bottomStart.toPx(size, density), to.bottomStart.toPx(size, density), progress)),
            continuity = when {
                progress < 0.5f && from is ContinuousRoundedRectangle -> from.continuity
                to is ContinuousRoundedRectangle -> to.continuity
                from is ContinuousRoundedRectangle -> from.continuity
                else -> com.kyant.capsule.Continuity.Default
            },
        ).createOutline(size, layoutDirection, density)
    }
}

private fun lerpCorner(
    start: Float,
    stop: Float,
    fraction: Float,
): Float = start + (stop - start) * fraction.coerceIn(0f, 1f)

private fun <T> toggleButtonAnimationSpec() = spring<T>(
    dampingRatio = Spring.DampingRatioNoBouncy,
    stiffness = Spring.StiffnessMediumLow,
)

@Composable
private fun IenToggleButtonColors.borderStroke(
    checked: Boolean,
    enabled: Boolean,
): BorderStroke? {
    val color = if (!enabled) {
        disabledBorder
    } else if (checked) {
        checkedBorder
    } else {
        uncheckedBorder
    }
    return if (color == Color.Transparent) {
        null
    } else {
        BorderStroke(IenTheme.stroke.thin, color)
    }
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
    val contentColor = resolvedIenColors.content

    val handleOnClick: () -> Unit = {
        if (state.enabled && !state.loading) {
            onClick()
        }
    }
    val backgroundModifier = if (effectiveBackgroundBrush == null) {
        Modifier.background(containerColor, shape)
    } else {
        Modifier.background(effectiveBackgroundBrush, shape)
    }
    val borderStroke = border ?: when (variant) {
        IenButtonVariant.Line -> BorderStroke(IenTheme.stroke.thin, resolvedIenColors.border)
        IenButtonVariant.Fill,
        IenButtonVariant.Weak,
        IenButtonVariant.Ghost -> null
    }

    Box(
        modifier = buttonModifier
            .clip(shape)
            .then(backgroundModifier)
            .then(if (borderStroke != null) Modifier.border(borderStroke, shape) else Modifier)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = interactiveEnabled,
                role = Role.Button,
                onClick = handleOnClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (isPressed && interactiveEnabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(pressedLayerColor(variant)),
            )
        }
        Box(
            modifier = Modifier.padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                content()
            }
        }
    }
}

private fun pressedLayerColor(variant: IenButtonVariant): Color {
    return when (variant) {
        IenButtonVariant.Fill,
        IenButtonVariant.Weak -> Color.Black.copy(alpha = 0.08f)
        IenButtonVariant.Line,
        IenButtonVariant.Ghost -> Color.Black.copy(alpha = 0.04f)
    }
}
