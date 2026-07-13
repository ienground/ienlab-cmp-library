package zone.ien.utils.ui.view.textfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.view.M3AsteriskTextWrapper

/**
 * PlaceHolder가 적용된 기본 텍스트 필드 컴포저블
 * 
 * 이 컴포저블은 텍스트 필드에 PlaceHolder 효과를 적용한 기본 텍스트 필드입니다.
 * 필수 입력 여부를 표시할 수 있으며, 아이콘과 텍스트 입력 기능을 지원합니다.
 * 
 * @param value 텍스트 필드의 현재 값
 * @param onValueChange 텍스트 값이 변경될 때 호출되는 함수
 * @param modifier 텍스트 필드에 적용할 Modifier
 * @param enabled 텍스트 필드의 활성화 상태
 * @param readOnly 텍스트 필드의 읽기 전용 상태
 * @param textStyle 텍스트 스타일
 * @param placeholder PlaceHolder 텍스트
 * @param isRequired 필수 입력 여부 (true일 경우 별표 표시)
 * @param leadingIcon 왼쪽에 표시되는 아이콘
 * @param trailingIcon 오른쪽에 표시되는 아이콘
 * @param keyboardOptions 키보드 설정
 * @param keyboardActions 키보드 액션
 * @param singleLine 단일 라인 입력 여부
 * @param maxLines 최대 라인 수
 * @param minLines 최소 라인 수
 * @param visualTransformation 입력 텍스트의 시각적 변환
 * @param onTextLayout 텍스트 레이아웃 변경 시 호출되는 함수
 * @param interactionSource 상호작용 소스
 * @param cursorBrush 커서 색상
 * @param contentPadding 내용에 대한 패딩
 */
@Composable
fun PlaceholderBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = IenTheme.colors.textPrimary),
    placeholder: @Composable (() -> Unit)? = null,
    isRequired: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(IenTheme.colors.textPrimary),
    contentPadding: PaddingValues? = null
) {
    val iS = interactionSource ?: remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = { ProvideTextStyle(textStyle.copy(color = textStyle.color.copy(0.55f))) { placeholder?.let { if (isRequired) M3AsteriskTextWrapper { it() } else it() } } },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                interactionSource = iS,
                contentPadding = contentPadding ?: (if (leadingIcon != null || trailingIcon != null) PaddingValues(vertical = 16.dp) else PaddingValues(0.dp)),
                container = {}
            )
        }
    )
}

/**
 * PlaceHolder가 적용된 기본 텍스트 필드 컴포저블 (TextFieldValue 버전)
 * 
 * 이 컴포저블은 TextFieldValue 타입을 사용하는 버전의 PlaceHolder가 적용된 기본 텍스트 필드입니다.
 * 
 * @param value 텍스트 필드의 현재 값 (TextFieldValue 타입)
 * @param onValueChange 텍스트 값이 변경될 때 호출되는 함수
 * @param modifier 텍스트 필드에 적용할 Modifier
 * @param enabled 텍스트 필드의 활성화 상태
 * @param readOnly 텍스트 필드의 읽기 전용 상태
 * @param textStyle 텍스트 스타일
 * @param placeholder PlaceHolder 텍스트
 * @param leadingIcon 왼쪽에 표시되는 아이콘
 * @param trailingIcon 오른쪽에 표시되는 아이콘
 * @param keyboardOptions 키보드 설정
 * @param keyboardActions 키보드 액션
 * @param singleLine 단일 라인 입력 여부
 * @param maxLines 최대 라인 수
 * @param minLines 최소 라인 수
 * @param visualTransformation 입력 텍스트의 시각적 변환
 * @param onTextLayout 텍스트 레이아웃 변경 시 호출되는 함수
 * @param interactionSource 상호작용 소스
 * @param cursorBrush 커서 색상
 * @param contentPadding 내용에 대한 패딩
 */
@Composable
fun PlaceholderBasicTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = IenTheme.colors.textPrimary),
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(IenTheme.colors.textPrimary),
    contentPadding: PaddingValues? = null
) {
    val iS = interactionSource ?: remember { MutableInteractionSource() }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value.text,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = { ProvideTextStyle(textStyle.copy(color = textStyle.color.copy(0.55f))) { placeholder?.invoke() } },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                interactionSource = iS,
                contentPadding = contentPadding ?: (if (leadingIcon != null || trailingIcon != null) PaddingValues(vertical = 16.dp) else PaddingValues(0.dp)),
                container = {}
            )
        }
    )
}

/**
 * PlaceHolder가 적용된 보안 텍스트 필드 컴포저블
 * 
 * 이 컴포저블은 보안 텍스트 필드에 PlaceHolder 효과를 적용한 컴포저블입니다.
 * 일반적으로 비밀번호 입력에 사용되며, 텍스트 입력 시 마스킹 처리가 됩니다.
 * 
 * @param state 텍스트 필드의 상태
 * @param modifier 텍스트 필드에 적용할 Modifier
 * @param enabled 텍스트 필드의 활성화 상태
 * @param readOnly 텍스트 필드의 읽기 전용 상태
 * @param textStyle 텍스트 스타일
 * @param labelPosition 라벨 위치
 * @param label 라벨 컴포저블
 * @param placeholder PlaceHolder 텍스트
 * @param leadingIcon 왼쪽에 표시되는 아이콘
 * @param trailingIcon 오른쪽에 표시되는 아이콘
 * @param prefix 접두사
 * @param suffix 접미사
 * @param supportingText 보조 텍스트
 * @param isError 입력 오류 상태
 * @param inputTransformation 입력 변환
 * @param textObfuscationMode 텍스트 가려짐 모드
 * @param textObfuscationCharacter 가려질 때 사용할 문자
 * @param keyboardOptions 키보드 설정
 * @param onKeyboardAction 키보드 액션 처리
 * @param onTextLayout 텍스트 레이아웃 변경 시 호출되는 함수
 * @param colors 텍스트 필드 색상
 * @param contentPadding 내용에 대한 패딩
 * @param interactionSource 상호작용 소스
 */
@Composable
fun PlaceholderBasicSecureTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Attached(),
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    inputTransformation: InputTransformation? = null,
    textObfuscationMode: TextObfuscationMode = TextObfuscationMode.RevealLastTyped,
    textObfuscationCharacter: Char = '\u2022',
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        autoCorrectEnabled = false,
        keyboardType = KeyboardType.Password
    ),
    onKeyboardAction: KeyboardActionHandler? = null,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    contentPadding: PaddingValues? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val iS = interactionSource ?: remember { MutableInteractionSource() }
    val textColor =
        textStyle.color.takeOrElse {
            val focused = iS.collectIsFocusedAsState().value
            with (colors) {
                when {
                    !enabled -> disabledTextColor
                    isError -> errorTextColor
                    focused -> focusedTextColor
                    else -> unfocusedTextColor
                }
            }
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicSecureTextField(
            state = state,
            modifier =
                modifier
                    .defaultMinSize(
                        minWidth = TextFieldDefaults.MinWidth,
                        minHeight = TextFieldDefaults.MinHeight,
                    ),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            cursorBrush = SolidColor(with (colors) { if (isError) errorCursorColor else cursorColor }),
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            inputTransformation = inputTransformation,
            textObfuscationMode = textObfuscationMode,
            textObfuscationCharacter = textObfuscationCharacter,
            decorator =
                TextFieldDefaults.decorator(
                    state = state,
                    enabled = enabled,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    outputTransformation = null,
                    interactionSource = iS,
                    labelPosition = labelPosition,
                    label = label,
                    placeholder = { ProvideTextStyle(textStyle.copy(color = textStyle.color.copy(0.55f))) { placeholder?.invoke() } },
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    isError = isError,
                    colors = colors,
                    contentPadding = contentPadding ?: (if (leadingIcon != null || trailingIcon != null) PaddingValues(vertical = 16.dp) else PaddingValues(0.dp)),
                    container = {},
                ),
        )
    }
}
