package zone.ien.utils.ui.interactive

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.clear_input
import zone.ien.utils.cmp_ui.generated.resources.clear_search
import zone.ien.utils.cmp_ui.generated.resources.hide
import zone.ien.utils.cmp_ui.generated.resources.hide_password
import zone.ien.utils.cmp_ui.generated.resources.search
import zone.ien.utils.cmp_ui.generated.resources.search_input
import zone.ien.utils.cmp_ui.generated.resources.segmented_input
import zone.ien.utils.cmp_ui.generated.resources.show
import zone.ien.utils.cmp_ui.generated.resources.show_password
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.fill.Close
import zone.ien.utils.icon.remix.line.ArrowDownWide
import zone.ien.utils.icon.remix.line.Search
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenDivider
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

/**
 * 텍스트 필드의 상태(정상, 오류, 성공)를 표현하는 실드 인터페이스.
 */
sealed interface IenFieldStatus {
    /** 기본 일반 상태 */
    data object Normal : IenFieldStatus
    /** 오류 상태 (오류 메시지를 포함함) */
    data class Error(val message: String) : IenFieldStatus
    /** 성공 상태 (선택적인 안내 메시지를 포함할 수 있음) */
    data class Success(val message: String? = null) : IenFieldStatus
}

/**
 * 텍스트 필드의 인터랙션 가능 여부와 시각적 오류/성공 상태를 관리하는 데이터 클래스.
 *
 * @property enabled 사용자가 필드를 조작할 수 있는지 여부.
 * @property readOnly 읽기 전용 상태 여부. true일 경우 텍스트를 수정할 수 없습니다.
 * @property status 필드의 유효성 상태 ([IenFieldStatus]).
 */
@Immutable
data class IenTextFieldState(
    val enabled: Boolean = true,
    val readOnly: Boolean = false,
    val status: IenFieldStatus = IenFieldStatus.Normal,
)

/**
 * 텍스트 필드의 프레임 스타일 종류를 정의하는 열거형 클래스.
 */
enum class IenTextFieldVariant {
    /** 둥근 테두리 상자 형태 */
    Box,
    /** 하단 선만 표시되는 심플한 형태 */
    Line,
    /** 다소 큰 크기의 둥근 테두리 상자 형태 */
    Big,
    /** 큰 폰트 크기가 적용된 매우 강조되는 디자인 형태 */
    Hero,
}

/**
 * 상단 라벨이 노출되는 인터랙티브 타이밍을 정의하는 열거형 클래스.
 */
enum class IenTextFieldLabelOption {
    /** 포커스 되거나 값이 입력되었을 때 위로 나타나는 애니메이션 형태 */
    Appear,
    /** 포커스나 값 입력 여부와 상관없이 항상 노출되어 유지되는 형태 */
    Sustain,
}

/**
 * 입력된 텍스트의 포맷팅 규칙(예: 전화번호 하이픈 자동 삽입 등)을 지정하는 설정 클래스.
 *
 * @property transform 입력값을 포맷팅된 문자열로 변환하는 함수.
 * @property reset 포맷팅된 문자열에서 원래 값으로 되돌리는(언포맷팅) 선택적 함수.
 */
@Immutable
data class IenTextFieldFormat(
    val transform: (value: String) -> String,
    val reset: ((formattedValue: String) -> String)? = null,
)

/**
 * 텍스트 필드의 최대 길이 처리 방식을 정의합니다.
 */
sealed interface IenTextFieldLengthLimit {
    /**
     * 길이 제한과 카운터를 사용하지 않습니다.
     */
    data object None : IenTextFieldLengthLimit

    /**
     * 최대 길이를 초과해도 입력은 허용하되 오류 상태와 카운터를 표시합니다.
     *
     * @param maxLength 허용할 최대 텍스트 길이
     */
    data class Error(val maxLength: Int) : IenTextFieldLengthLimit

    /**
     * 최대 길이를 초과하는 입력을 막고 카운터를 표시합니다.
     *
     * @param maxLength 허용할 최대 텍스트 길이
     */
    data class Block(val maxLength: Int) : IenTextFieldLengthLimit
}

private fun IenTextFieldLengthLimit.maxLengthOrNull(): Int? = when (this) {
    IenTextFieldLengthLimit.None -> null
    is IenTextFieldLengthLimit.Error -> maxLength.coerceAtLeast(0)
    is IenTextFieldLengthLimit.Block -> maxLength.coerceAtLeast(0)
}

private fun IenTextFieldLengthLimit.blocks(value: String): Boolean {
    return this is IenTextFieldLengthLimit.Block &&
        value.length > maxLength.coerceAtLeast(0)
}

/**
 * IEN 라이브러리의 범용 기본 단일/다중 행 텍스트 필드 컴포저블.
 *
 * @param value 필드의 현재 텍스트 값.
 * @param onValueChange 텍스트 변경 시 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param label 필드 상단에 표시될 라벨 명칭.
 * @param required true이면 라벨 뒤에 필수 입력 표시(*)를 노출합니다.
 * @param labelOption 라벨의 상시 노출 또는 애니메이션 등장 옵션 ([IenTextFieldLabelOption]). 기본값은 [IenTextFieldLabelOption.Appear].
 * @param placeholder 텍스트가 비어있을 때 내부에 표시될 힌트 문자열.
 * @param help 필드 하단에 노출될 부가 설명 또는 에러 텍스트 (단순 지원용).
 * @param hasError 오류 발생 여부 설정. true일 경우 컴포저블 내부 상태를 강제로 [IenFieldStatus.Error]로 취급합니다.
 * @param variant 필드의 테두리 디자인 형태 ([IenTextFieldVariant]). 기본값은 [IenTextFieldVariant.Box].
 * @param prefix 필드 입력 영역 왼쪽에 텍스트에 밀접하게 고정 노출될 접두사.
 * @param suffix 필드 입력 영역 오른쪽에 텍스트에 밀접하게 고정 노출될 접미사.
 * @param right 필드 내부 가장 오른쪽에 고정으로 그릴 컴포저블 (주로 클릭 가능 버튼 등).
 * @param format 입력 포맷 필터 지정 규칙 ([IenTextFieldFormat]).
 * @param paddingTop 내부 위쪽 추가 여백.
 * @param paddingBottom 내부 아래쪽 추가 여백.
 * @param state 필드의 제어 상태 지정 및 유효성 메시지 상태 ([IenTextFieldState]).
 * @param leading 필드 내 앞쪽에 들어갈 커스텀 컴포저블 (아이콘 등).
 * @param trailing 필드 내 뒤쪽에 들어갈 커스텀 컴포저블.
 * @param supportingText 필드 하단에 보여줄 부가 안내 텍스트.
 * @param lengthLimit 필드 하단 카운터와 최대 길이 처리 방식입니다.
 * @param singleLine 한 줄로만 입력할지 여부.
 * @param keyboardOptions 소프트 키보드 속성 설정 ([KeyboardOptions]).
 * @param keyboardActions 키보드 완료/검색 액션 정의 ([KeyboardActions]).
 * @param visualTransformation 입력값 가공 규칙 (비밀번호 가리기 등).
 * @param readOnlyTextSelectionEnabled 읽기 전용 상태에서 텍스트 선택을 허용할지 여부.
 * @param interactionSource 필드의 인터랙션 이벤트를 전달할 [MutableInteractionSource].
 * @param minLines 최소 높이를 보장할 행 수.
 * @param maxLines 최대 행 수.
 * @param onFocus 필드가 포커스를 받았을 때 호출할 선택적 콜백 함수.
 * @param onBlur 필드가 포커스를 잃었을 때 호출할 선택적 콜백 함수.
 */
@Composable
fun IenTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    required: Boolean = false,
    labelOption: IenTextFieldLabelOption = IenTextFieldLabelOption.Appear,
    placeholder: String? = null,
    help: String? = null,
    hasError: Boolean = false,
    variant: IenTextFieldVariant = IenTextFieldVariant.Box,
    prefix: String? = null,
    suffix: String? = null,
    right: (@Composable () -> Unit)? = null,
    format: IenTextFieldFormat? = null,
    paddingTop: Dp? = null,
    paddingBottom: Dp? = null,
    state: IenTextFieldState = IenTextFieldState(),
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    supportingText: String? = null,
    lengthLimit: IenTextFieldLengthLimit = IenTextFieldLengthLimit.None,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnlyTextSelectionEnabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    val focused by interactionSource.collectIsFocusedAsState()
    var wasFocused by remember { mutableStateOf(false) }
    val maxLength = lengthLimit.maxLengthOrNull()
    val lengthExceeded = lengthLimit is IenTextFieldLengthLimit.Error &&
        maxLength != null &&
        value.length > maxLength
    val effectiveStatus = when {
        hasError && state.status !is IenFieldStatus.Error -> {
            IenFieldStatus.Error(help ?: supportingText ?: "")
        }
        lengthExceeded && state.status !is IenFieldStatus.Error -> {
            IenFieldStatus.Error(help ?: supportingText ?: "")
        }
        else -> state.status
    }
    val formattedValue = format?.transform?.invoke(value) ?: value
    var fieldValue by remember { mutableStateOf(TextFieldValue(formattedValue)) }
    SyncTextFieldValue(
        text = formattedValue,
        fieldValue = fieldValue,
        onFieldValueChange = { fieldValue = it },
    )
    val showLabel = label != null && (labelOption == IenTextFieldLabelOption.Sustain || value.isNotEmpty() || focused)

    val labelAlpha by animateFloatAsState(
        targetValue = if (showLabel) 1f else 0f,
        animationSpec = tween(durationMillis = 150)
    )

    val shakeOffset = remember { Animatable(0f) }
    androidx.compose.runtime.LaunchedEffect(effectiveStatus) {
        if (effectiveStatus is IenFieldStatus.Error) {
            val shakeSequence = listOf(-8f, 8f, -6f, 6f, -3f, 3f, 0f)
            shakeSequence.forEach { target ->
                shakeOffset.animateTo(
                    targetValue = target,
                    animationSpec = tween(durationMillis = 40, easing = LinearEasing)
                )
            }
        }
    }

    val fieldTextStyle = when (variant) {
        IenTextFieldVariant.Box,
        IenTextFieldVariant.Line -> IenTheme.typography.body1
        IenTextFieldVariant.Big -> IenTheme.typography.title2
        IenTextFieldVariant.Hero -> IenTheme.typography.display
    }.fieldInputTextStyle()
    val errorStatus = effectiveStatus as? IenFieldStatus.Error
    val successStatus = effectiveStatus as? IenFieldStatus.Success
    val isNormal = effectiveStatus === IenFieldStatus.Normal

    val borderColor = when {
        errorStatus != null -> IenTheme.colors.danger
        successStatus != null -> IenTheme.colors.success
        isNormal -> if (focused) IenTheme.colors.brand else IenTheme.colors.border
        else -> error("Unknown IenFieldStatus: $effectiveStatus")
    }

    val supporting = when {
        errorStatus != null -> errorStatus.message
        successStatus != null -> successStatus.message ?: help ?: supportingText
        isNormal -> help ?: supportingText
        else -> error("Unknown IenFieldStatus: $effectiveStatus")
    }

    val supportingColor = when {
        errorStatus != null -> IenTheme.colors.danger
        successStatus != null -> IenTheme.colors.success
        isNormal -> IenTheme.colors.textTertiary
        else -> error("Unknown IenFieldStatus: $effectiveStatus")
    }
    val lengthCounterText = maxLength?.let { "${value.length}/$it" }
    val lengthCounterColor = if (maxLength != null && value.length > maxLength) {
        IenTheme.colors.danger
    } else {
        IenTheme.colors.textTertiary
    }
    val textColor = if (state.enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled

    Column(modifier = modifier.semantics {
        if (effectiveStatus is IenFieldStatus.Error) error(effectiveStatus.message)
    }) {
        if (label != null) {
            IenTextFieldLabel(
                label = label,
                required = required,
                modifier = Modifier.graphicsLayer { alpha = labelAlpha },
            )
            Spacer(Modifier.height(IenTheme.spacing.xxs))
        }
        IenTextFieldContainer(
            variant = variant,
            enabled = state.enabled,
            borderColor = borderColor,
            modifier = Modifier.offset(x = shakeOffset.value.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 28.dp)
                    .padding(
                        PaddingValues(
                            start = if (variant == IenTextFieldVariant.Line) 0.dp else 14.dp,
                            top = fieldTopPadding(variant, paddingTop),
                            end = if (variant == IenTextFieldVariant.Line) 0.dp else 14.dp,
                        bottom = fieldBottomPadding(variant, paddingBottom),
                    ),
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leading?.invoke()
                prefix?.let {
                    IenText(
                        text = it,
                        style = fieldTextStyle,
                        color = if (state.enabled) IenTheme.colors.textSecondary else IenTheme.colors.textDisabled,
                    )
                    Spacer(Modifier.width(IenTheme.spacing.xs))
                }
                BasicTextField(
                    value = fieldValue,
                    onValueChange = { next ->
                        val nextValue = format?.reset?.invoke(next.text) ?: next.text
                        if (lengthLimit.blocks(nextValue)) return@BasicTextField

                        fieldValue = next
                        onValueChange(nextValue)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            if (it.isFocused && !wasFocused) {
                                onFocus?.invoke()
                            }
                            if (!it.isFocused && wasFocused) {
                                onBlur?.invoke()
                            }
                            wasFocused = it.isFocused
                        }
                        .padding(
                            horizontal = if (
                                leading != null ||
                                trailing != null ||
                                right != null ||
                                prefix != null ||
                                suffix != null
                            ) {
                                IenTheme.spacing.xs
                            } else {
                                0.dp
                            },
                        ),
                    enabled = state.enabled && (!state.readOnly || readOnlyTextSelectionEnabled),
                    readOnly = state.readOnly,
                    singleLine = singleLine,
                    minLines = minLines,
                    maxLines = maxLines,
                    textStyle = fieldTextStyle.copy(color = textColor),
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(IenTheme.colors.brand),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(
                                    if (singleLine) {
                                        Modifier.height(fieldContentMinHeight(variant))
                                    } else {
                                        Modifier.defaultMinSize(minHeight = fieldContentMinHeight(variant))
                                    },
                                ),
                            contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart,
                        ) {
                            if (fieldValue.text.isEmpty() && placeholder != null) {
                                IenText(
                                    text = placeholder,
                                    style = fieldTextStyle,
                                    color = IenTheme.colors.textTertiary,
                                    maxLines = if (singleLine) 1 else maxLines,
                                )
                            }
                            innerTextField()
                        }
                    },
                )
                suffix?.let {
                    Spacer(Modifier.width(IenTheme.spacing.xs))
                    IenText(
                        text = it,
                        style = fieldTextStyle,
                        color = if (state.enabled) IenTheme.colors.textSecondary else IenTheme.colors.textDisabled,
                    )
                }
                trailing?.invoke()
                right?.invoke()
            }
        }
        if (!supporting.isNullOrBlank() || lengthCounterText != null) {
            Spacer(Modifier.height(IenTheme.spacing.xxs))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = IenTheme.spacing.xs),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                verticalAlignment = Alignment.Top,
            ) {
                if (!supporting.isNullOrBlank()) {
                    IenText(
                        text = supporting,
                        modifier = Modifier.weight(1f),
                        style = IenTheme.typography.caption,
                        color = supportingColor,
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                lengthCounterText?.let {
                    IenText(
                        text = it,
                        style = IenTheme.typography.caption,
                        color = lengthCounterColor,
                    )
                }
            }
        }
    }
}

/**
 * 긴 본문 텍스트 입력을 위해 기본 4줄에서 8줄 크기로 구성된 다중 행 텍스트 영역 컴포저블.
 *
 * @param value 영역의 현재 텍스트 값.
 * @param onValueChange 텍스트 변경 시 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param label 영역 상단에 표시될 라벨 명칭.
 * @param required true이면 라벨 뒤에 필수 입력 표시(*)를 노출합니다.
 * @param placeholder 텍스트가 비어있을 때 표시될 힌트 문자열.
 * @param state 영역의 제어 상태 ([IenTextFieldState]).
 * @param supportingText 영역 하단 부가 안내 텍스트.
 * @param lengthLimit 영역 하단 카운터와 최대 길이 처리 방식입니다.
 * @param minLines 최소 노출 행 수. 기본값은 4.
 * @param maxLines 최대 확장 노출 행 수. 기본값은 8.
 */
@Composable
fun IenTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    required: Boolean = false,
    placeholder: String? = null,
    state: IenTextFieldState = IenTextFieldState(),
    supportingText: String? = null,
    lengthLimit: IenTextFieldLengthLimit = IenTextFieldLengthLimit.None,
    minLines: Int = 4,
    maxLines: Int = 8,
) {
    IenTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        required = required,
        placeholder = placeholder,
        state = state,
        supportingText = supportingText,
        lengthLimit = lengthLimit,
        singleLine = false,
        minLines = minLines,
        maxLines = maxLines,
    )
}

/**
 * 텍스트 입력 시 우측에 일괄 삭제용 클리어(x) 버튼이 자동 노출되는 편리한 텍스트 필드 컴포저블.
 *
 * @param value 필드의 현재 텍스트 값.
 * @param onValueChange 텍스트 변경 시 호출되는 콜백 함수.
 * @param onClear 클리어 버튼이 눌려 텍스트가 지워졌을 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param label 필드 상단에 표시될 라벨 명칭.
 * @param required true이면 라벨 뒤에 필수 입력 표시(*)를 노출합니다.
 * @param labelOption 라벨 노출 옵션 ([IenTextFieldLabelOption]).
 * @param placeholder 힌트 문자열.
 * @param help 부가 설명 또는 에러 설명 텍스트.
 * @param hasError 오류 발생 여부 설정.
 * @param variant field 디자인 형태 ([IenTextFieldVariant]).
 * @param prefix 고정 노출 접두사.
 * @param suffix 고정 노출 접미사.
 * @param state 필드 제어 상태 ([IenTextFieldState]).
 * @param lengthLimit 필드 하단 카운터와 최대 길이 처리 방식입니다.
 * @param keyboardOptions 소프트 키보드 속성 설정.
 * @param keyboardActions 키보드 액션 정의.
 */
@Composable
fun IenClearableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    required: Boolean = false,
    labelOption: IenTextFieldLabelOption = IenTextFieldLabelOption.Appear,
    placeholder: String? = null,
    help: String? = null,
    hasError: Boolean = false,
    variant: IenTextFieldVariant = IenTextFieldVariant.Box,
    prefix: String? = null,
    suffix: String? = null,
    state: IenTextFieldState = IenTextFieldState(),
    lengthLimit: IenTextFieldLengthLimit = IenTextFieldLengthLimit.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    IenTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        required = required,
        labelOption = labelOption,
        placeholder = placeholder,
        help = help,
        hasError = hasError,
        variant = variant,
        prefix = prefix,
        suffix = suffix,
        state = state,
        lengthLimit = lengthLimit,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        right = if (value.isNotEmpty() && state.enabled && !state.readOnly) {
            {
                IenTextFieldClearButton(
                    onClick = {
                        onValueChange("")
                        onClear()
                    },
                )
            }
        } else {
            null
        },
    )
}

/**
 * 비밀번호 입력을 위해 텍스트 마스킹 처리 및 우측 보기/숨기기 토글 버튼이 포함된 비밀번호 전용 텍스트 필드 컴포저블.
 *
 * @param value 필드의 현재 비밀번호 텍스트 값.
 * @param onValueChange 비밀번호 변경 시 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param label 필드 상단에 표시될 라벨 명칭.
 * @param required true이면 라벨 뒤에 필수 입력 표시(*)를 노출합니다.
 * @param labelOption 라벨 노출 옵션 ([IenTextFieldLabelOption]).
 * @param placeholder 힌트 문자열.
 * @param help 부가 설명 또는 에러 설명 텍스트.
 * @param hasError 오류 발생 여부 설정.
 * @param variant 필드 디자인 형태 ([IenTextFieldVariant]).
 * @param state 필드 제어 상태 ([IenTextFieldState]).
 * @param lengthLimit 필드 하단 카운터와 최대 길이 처리 방식입니다.
 * @param visible 외부 상태로 비밀번호 노출 여부를 직접 제어하고 싶을 때 전달할 값. null이면 컴포저블 내부 상태로 자동 동작합니다.
 * @param onVisibilityChange 노출 여부 토글 시 호출되는 선택적 콜백 함수.
 * @param keyboardOptions 키보드 입력 유형. 기본적으로 비밀번호 전용 키보드가 나타납니다.
 * @param keyboardActions 키보드 액션 정의.
 */
@Composable
fun IenPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    required: Boolean = false,
    labelOption: IenTextFieldLabelOption = IenTextFieldLabelOption.Appear,
    placeholder: String? = null,
    help: String? = null,
    hasError: Boolean = false,
    variant: IenTextFieldVariant = IenTextFieldVariant.Box,
    state: IenTextFieldState = IenTextFieldState(),
    lengthLimit: IenTextFieldLengthLimit = IenTextFieldLengthLimit.None,
    visible: Boolean? = null,
    onVisibilityChange: ((visible: Boolean) -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var internalVisible by remember { mutableStateOf(false) }
    val resolvedVisible = visible ?: internalVisible
    IenTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        required = required,
        labelOption = labelOption,
        placeholder = placeholder,
        help = help,
        hasError = hasError,
        variant = variant,
        state = state,
        lengthLimit = lengthLimit,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = if (resolvedVisible) VisualTransformation.None else PasswordVisualTransformation(),
        right = {
            IenTextFieldPasswordButton(
                visible = resolvedVisible,
                onClick = {
                    val next = !resolvedVisible
                    if (visible == null) internalVisible = next
                    onVisibilityChange?.invoke(next)
                },
            )
        },
    )
}

/**
 * 텍스트 필드 형태로 표현되지만 직접 타이핑하는 대신 클릭하여 바텀 시트나 새 화면을 여는 링크 액션 전용 버튼식 필드 컴포저블.
 *
 * @param onClick 필드를 클릭했을 때 실행할 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param value 필드 내부에 표시될 값 문자열.
 * @param label 필드 상단에 표시될 라벨 명칭.
 * @param required true이면 라벨 뒤에 필수 입력 표시(*)를 노출합니다.
 * @param labelOption 라벨 노출 옵션 ([IenTextFieldLabelOption]).
 * @param placeholder 값이 없을 때 표시할 힌트 문자열.
 * @param help 부가 설명 텍스트.
 * @param variant 필드 디자인 형태 ([IenTextFieldVariant]).
 * @param prefix 고정 노출 접두사.
 * @param suffix 고정 노출 접미사.
 * @param state 필드 제어 상태입니다. 클릭 전용 필드라 내부적으로 읽기 전용으로 처리됩니다.
 * @param lengthLimit 필드 하단 카운터와 최대 길이 처리 방식입니다.
 * @param right 필드 우측에 표시할 아이콘 컴포저블. 기본값은 아래쪽 화살표([IenTextFieldArrowDown])입니다.
 */
@Composable
fun IenTextFieldButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    value: String? = null,
    label: String? = null,
    required: Boolean = false,
    labelOption: IenTextFieldLabelOption = IenTextFieldLabelOption.Appear,
    placeholder: String? = null,
    help: String? = null,
    variant: IenTextFieldVariant = IenTextFieldVariant.Box,
    prefix: String? = null,
    suffix: String? = null,
    state: IenTextFieldState = IenTextFieldState(),
    lengthLimit: IenTextFieldLengthLimit = IenTextFieldLengthLimit.None,
    right: (@Composable () -> Unit)? = { IenTextFieldArrowDown() },
) {
    val buttonInteractionSource = remember { MutableInteractionSource() }
    val readOnlyState = state.copy(readOnly = true)

    IenTextField(
        value = value.orEmpty(),
        onValueChange = {},
        modifier = modifier.clickable(
            interactionSource = buttonInteractionSource,
            indication = null,
            enabled = readOnlyState.enabled,
            role = Role.Button,
            onClick = onClick,
        ),
        label = label,
        required = required,
        labelOption = labelOption,
        placeholder = placeholder,
        help = help,
        variant = variant,
        prefix = prefix,
        suffix = suffix,
        state = readOnlyState,
        lengthLimit = lengthLimit,
        readOnlyTextSelectionEnabled = false,
        right = right,
    )
}

@Composable
private fun IenTextFieldLabel(
    label: String,
    required: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IenText(
            text = label,
            style = IenTheme.typography.label2,
            color = IenTheme.colors.textSecondary,
        )
        if (required) {
            IenText(
                text = "*",
                style = IenTheme.typography.label2,
                color = IenTheme.colors.danger,
            )
        }
    }
}

@Composable
private fun IenTextFieldContainer(
    variant: IenTextFieldVariant,
    enabled: Boolean,
    borderColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    when (variant) {
        IenTextFieldVariant.Box,
        IenTextFieldVariant.Big,
        IenTextFieldVariant.Hero -> {
            IenSurface(
                modifier = modifier.fillMaxWidth(),
                color = if (enabled) IenTheme.colors.surface else IenTheme.colors.surfaceWeak,
                border = BorderStroke(IenTheme.stroke.thin, borderColor),
            ) {
                content()
            }
        }
        IenTextFieldVariant.Line -> {
            Column(modifier = modifier.fillMaxWidth()) {
                content()
                IenDivider(color = borderColor, thickness = IenTheme.stroke.thin)
            }
        }
    }
}

private fun fieldDefaultVerticalPadding(variant: IenTextFieldVariant): Dp {
    return when (variant) {
        IenTextFieldVariant.Box,
        IenTextFieldVariant.Line -> 14.dp
        IenTextFieldVariant.Big -> 18.dp
        IenTextFieldVariant.Hero -> 22.dp
    }
}

private fun fieldTopPadding(variant: IenTextFieldVariant, paddingTop: Dp?): Dp {
    return paddingTop ?: fieldDefaultVerticalPadding(variant)
}

private fun fieldBottomPadding(variant: IenTextFieldVariant, paddingBottom: Dp?): Dp {
    return paddingBottom ?: fieldDefaultVerticalPadding(variant)
}

private fun fieldContentMinHeight(variant: IenTextFieldVariant): Dp {
    return when (variant) {
        IenTextFieldVariant.Box,
        IenTextFieldVariant.Line -> 24.dp
        IenTextFieldVariant.Big -> 28.dp
        IenTextFieldVariant.Hero -> 38.dp
    }
}

private fun TextStyle.fieldInputTextStyle(): TextStyle {
    return copy(
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both,
        ),
    )
}

@Composable
private fun SyncTextFieldValue(
    text: String,
    fieldValue: TextFieldValue,
    onFieldValueChange: (TextFieldValue) -> Unit,
) {
    LaunchedEffect(text) {
        if (text != fieldValue.text && fieldValue.composition == null) {
            onFieldValueChange(
                fieldValue.copy(
                    text = text,
                    selection = fieldValue.selection.constrainToText(text),
                    composition = null,
                ),
            )
        }
    }
}

private fun androidx.compose.ui.text.TextRange.constrainToText(text: String): androidx.compose.ui.text.TextRange {
    val start = start.coerceIn(0, text.length)
    val end = end.coerceIn(0, text.length)
    return androidx.compose.ui.text.TextRange(start, end)
}

@Composable
private fun IenTextFieldClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescription = stringResource(Res.string.clear_input)
    Box(
        modifier = modifier
            .size(28.dp)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { this.contentDescription = contentDescription }
            .background(color = Color(0xFFE5E8EB), shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        IenIcon(
            imageVector = RemixIcons.Fill.Close,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFF8B95A1),
        )
    }
}

@Composable
private fun IenTextFieldPasswordButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescription = stringResource(if (visible) Res.string.hide_password else Res.string.show_password)
    IenText(
        text = stringResource(if (visible) Res.string.hide else Res.string.show),
        modifier = modifier
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = IenTheme.spacing.xs, vertical = IenTheme.spacing.xxs)
            .semantics { this.contentDescription = contentDescription },
        style = IenTheme.typography.label1,
        color = IenTheme.colors.brand,
    )
}

@Composable
private fun IenTextFieldArrowDown(
    modifier: Modifier = Modifier,
) {
    IenIcon(
        imageVector = RemixIcons.Line.ArrowDownWide,
        contentDescription = null,
        modifier = modifier.size(24.dp),
        tint = Color(0xFF6B7684),
    )
}

/**
 * 인증번호 등의 입력을 위해 정해진 글자 수만큼 분할된 여러 박스 형태로 표시되는 분할 텍스트 필드 컴포저블.
 *
 * @param value 현재 입력된 텍스트.
 * @param onValueChange 텍스트 변경 시 호출되는 콜백 함수.
 * @param length 입력받을 총 글자 수 제한 (박스의 개수).
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param state 필드 상태 정보 ([IenTextFieldState]).
 * @param placeholderChar 미입력 빈 박스 내부에 표기할 보조 기호. 기본값은 '•'.
 * @param mask 입력된 실문자를 감추고 [placeholderChar]로 강제 표시할지 여부.
 * @param keyboardOptions 키보드 타입 설정. 기본적으로 숫자 비밀번호 입력 스타일을 제공합니다.
 */
@Composable
fun IenSplitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    length: Int,
    modifier: Modifier = Modifier,
    state: IenTextFieldState = IenTextFieldState(),
    placeholderChar: Char = '•',
    mask: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
) {
    val contentDescription = stringResource(Res.string.segmented_input, value.length, length)
    BasicTextField(
        value = value.take(length),
        onValueChange = { next ->
        onValueChange = { next ->
            onValueChange(next.take(length))
        },
        },
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
            if (state.status is IenFieldStatus.Error) error(state.status.message)
        },
        enabled = state.enabled,
        readOnly = state.readOnly,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        textStyle = IenTheme.typography.body1.copy(color = IenTheme.colors.textPrimary),
        cursorBrush = SolidColor(IenTheme.colors.brand),
        decorationBox = {
            Row {
                repeat(length) { index ->
                    val char = value.getOrNull(index)
                    IenSurface(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        color = if (state.enabled) IenTheme.colors.surface else IenTheme.colors.surfaceWeak,
                        border = BorderStroke(
                            width = IenTheme.stroke.thin,
                            color = when {
                                state.status is IenFieldStatus.Error -> IenTheme.colors.danger
                                char != null -> IenTheme.colors.brand
                                else -> IenTheme.colors.border
                            },
                        ),
                    ) {
                        Row(
                            modifier = Modifier.padding(IenTheme.spacing.sm),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(Modifier.weight(1f))
                            IenText(
                                text = when {
                                    char == null -> placeholderChar.toString()
                                    mask -> placeholderChar.toString()
                                    else -> char.toString()
                                },
                                style = IenTheme.typography.title3,
                                color = if (char == null) IenTheme.colors.textDisabled else IenTheme.colors.textPrimary,
                            )
                            Spacer(Modifier.weight(1f))
                        }
                    }
                    if (index != length - 1) {
                        Spacer(Modifier.width(IenTheme.spacing.xs))
                    }
                }
            }
        },
    )
    if (state.status is IenFieldStatus.Error) {
        Spacer(Modifier.height(IenTheme.spacing.xxs))
        IenText(
            text = state.status.message,
            style = IenTheme.typography.caption,
            color = IenTheme.colors.danger,
        )
    }
}

/**
 * 검색을 위한 전용 필드로 돋보기 아이콘, 텍스트 일괄 삭제 버튼이 제공되는 검색 필드 컴포저블.
 *
 * @param value 검색어 입력 값.
 * @param onValueChange 검색어 변경 시 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param placeholder 힌트 텍스트. 기본적으로 '검색'에 대응되는 다국어 리소스가 제공됩니다.
 * @param state 필드 제어 상태 ([IenTextFieldState]).
 * @param contentDescription 접근성 스크린 리더용 설명 명칭.
 * @param fixed 가로 패딩 및 상하 마진 레이아웃을 고정한 고밀도 검색바 형태로 출력할지 여부.
 * @param takeSpace 레이아웃 할당 공간 조절용 매개변수.
 * @param onDeleteClick 일괄 지우기 버튼을 클릭했을 때 호출되는 선택적 콜백 함수.
 * @param leading 필드 내 앞쪽에 고정 노출할 아이콘 컴포저블. 기본값은 돋보기 아이콘([IenSearchFieldSearchIcon])입니다.
 * @param trailing 필드 내 뒤쪽에 노출할 추가 컴포저블.
 * @param deleteButton 지우기 버튼을 커스텀하고자 할 때 제공할 컴포저블.
 * @param keyboardOptions 키보드 옵션. 기본적으로 검색(Search) 작업에 적합한 옵션이 지정됩니다.
 * @param keyboardActions 키보드 액션 정의.
 */
@Composable
fun IenSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = stringResource(Res.string.search),
    state: IenTextFieldState = IenTextFieldState(),
    contentDescription: String = stringResource(Res.string.search_input),
    fixed: Boolean = false,
    takeSpace: Boolean = true,
    onDeleteClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = { IenSearchFieldSearchIcon() },
    trailing: (@Composable () -> Unit)? = null,
    deleteButton: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val showDelete = value.isNotEmpty() && state.enabled && !state.readOnly
    val resolvedTrailing: (@Composable () -> Unit)? = when {
        trailing != null -> trailing
        showDelete -> deleteButton ?: {
            IenSearchFieldDeleteButton(
                onClick = {
                    onValueChange("")
                    onDeleteClick?.invoke()
                },
            )
        }
        else -> null
    }
    val field: @Composable () -> Unit = {
        IenSearchFieldInput(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.semantics { this.contentDescription = contentDescription },
            placeholder = placeholder,
            state = state,
            leading = leading,
            trailing = resolvedTrailing,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
    }

    if (fixed) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .then(if (takeSpace) Modifier else Modifier)
                .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        ) {
            field()
        }
    } else {
        Box(modifier = modifier) {
            field()
        }
    }
}

@Composable
private fun IenSearchFieldInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String,
    state: IenTextFieldState,
    leading: (@Composable () -> Unit)?,
    trailing: (@Composable () -> Unit)?,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
) {
    val textStyle = IenTheme.typography.body2.fieldInputTextStyle()
    val textColor = if (state.enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled
    val placeholderColor = if (state.enabled) IenTheme.colors.textTertiary else IenTheme.colors.textDisabled
    var fieldValue by remember { mutableStateOf(TextFieldValue(value)) }
    SyncTextFieldValue(
        text = value,
        fieldValue = fieldValue,
        onFieldValueChange = { fieldValue = it },
    )

    IenSurface(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        color = searchFieldContainerColor(),
        shape = ContinuousRoundedRectangle(IenTheme.radius.lg),
    ) {
        Row(
            modifier = Modifier.padding(start = 14.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leading?.invoke()
            BasicTextField(
                value = fieldValue,
                onValueChange = { next ->
                    fieldValue = next
                    onValueChange(next.text)
                },
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 24.dp)
                    .padding(horizontal = IenTheme.spacing.xs),
                enabled = state.enabled,
                readOnly = state.readOnly,
                singleLine = true,
                textStyle = textStyle.copy(color = textColor),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                cursorBrush = SolidColor(IenTheme.colors.brand),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 24.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (fieldValue.text.isEmpty()) {
                            IenText(
                                text = placeholder,
                                style = textStyle,
                                color = placeholderColor,
                                maxLines = 1,
                            )
                        }
                        innerTextField()
                    }
                },
            )
            trailing?.invoke()
        }
    }
}

/**
 * 검색 필드 내부에 표시되는 돋보기 검색 아이콘 컴포저블.
 *
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param contentDescription 접근성 스크린 리더용 설명 명칭.
 * @param size 아이콘의 크기 규격. 기본값은 18.dp.
 */
@Composable
fun IenSearchFieldSearchIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 18.dp,
) {
    val color = searchFieldIconColor()
    IenIcon(
        imageVector = RemixIcons.Line.Search,
        contentDescription = contentDescription,
        modifier = modifier
            .width(size)
            .height(size),
        tint = color,
    )
}

/**
 * 검색 필드 내부에 표시되는 검색어 일괄 삭제용 클리어 버튼 컴포저블.
 *
 * @param onClick 삭제 버튼 클릭 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param contentDescription 접근성 설명 명칭.
 */
@Composable
fun IenSearchFieldDeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(Res.string.clear_search),
) {
    val backgroundColor = searchFieldDeleteButtonColor()
    val contentColor = searchFieldDeleteIconColor()
    Box(
        modifier = modifier
            .size(28.dp)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            )
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Button
            },
        contentAlignment = Alignment.Center,
    ) {
        IenSurface(
            modifier = Modifier.size(18.dp),
            color = backgroundColor,
            shape = ContinuousCapsule(),
        ) {
            IenIcon(
                imageVector = RemixIcons.Fill.Close,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun searchFieldContainerColor(): Color {
    return if (IenTheme.colors.background == Color(0xFFFFFFFF)) {
        Color(0xFFF2F4F6)
    } else {
        Color(0xFF20252B)
    }
}

@Composable
private fun searchFieldIconColor(): Color {
    return if (IenTheme.colors.background == Color(0xFFFFFFFF)) {
        Color(0xFF8B95A1)
    } else {
        Color(0xFF6B7684)
    }
}

@Composable
private fun searchFieldDeleteButtonColor(): Color {
    return if (IenTheme.colors.background == Color(0xFFFFFFFF)) {
        Color(0xFFD1D6DB)
    } else {
        Color(0xFF3A414A)
    }
}

@Composable
private fun searchFieldDeleteIconColor(): Color {
    return if (IenTheme.colors.background == Color(0xFFFFFFFF)) {
        Color(0xFFFFFFFF)
    } else {
        Color(0xFFB0B8C1)
    }
}
