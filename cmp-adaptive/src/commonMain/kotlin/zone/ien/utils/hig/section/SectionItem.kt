package zone.ien.utils.hig.section

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import zone.ien.hig.CupertinoSecureTextField
import zone.ien.hig.CupertinoTextField
import zone.ien.hig.CupertinoTextFieldColors
import zone.ien.hig.CupertinoTextFieldDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.ProvideTextStyle
import zone.ien.hig.section.SectionItem
import zone.ien.hig.section.SectionScope
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.utils.adaptive.view.textfield.AdaptiveTextFieldClearButton
import zone.ien.utils.hig.view.HigAsteriskTextWrapper
import zone.ien.utils.hig.view.textfield.HigTextFieldClearButton

/**
 * 섹션 아이템 텍스트 필드 컴포저블
 * 
 * 섹션 내부에 사용되는 텍스트 필드를 제공합니다.
 * 
 * @param value 현재 텍스트 필드의 값
 * @param onValueChange 텍스트 필드 값 변경 시 실행할 함수
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param enabled 텍스트 필드 활성화 상태 여부
 * @param readOnly 텍스트 필드 읽기 전용 상태 여부
 * @param textStyle 텍스트 스타일
 * @param placeholder 텍스트 필드의 placeholder 텍스트
 * @param isRequired 필드가 필수 입력 항목인지 여부
 * @param leadingIcon 텍스트 필드의 앞에 나타나는 아이콘
 * @param trailingIcon 텍스트 필드의 뒤에 나타나는 아이콘
 * @param visualTransformation 텍스트 변환 (예: 비밀번호 표시 방식)
 * @param keyboardOptions 키보드 옵션
 * @param keyboardActions 키보드 액션
 * @param singleLine 단일 행 입력 여부
 * @param maxLines 최대 행 수
 * @param minLines 최소 행 수
 * @param interactionSource 상호작용 소스
 * @param colors 텍스트 필드의 색상
 * @return 섹션 텍스트 필드 컴포저블
 */
@ExperimentalCupertinoApi
@Composable
fun SectionScope.SectionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle? = null,
    placeholder: @Composable (() -> Unit)? = null,
    isRequired: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable ((InteractionSource) -> Unit)? = {
        val focused by it.collectIsFocusedAsState()

        val updatedValueChange by rememberUpdatedState(onValueChange)

        HigTextFieldClearButton(
            visible = focused && value.isNotEmpty(),
            onClick = {
                updatedValueChange.invoke("")
            },
        )
    },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    colors: CupertinoTextFieldColors? = null,
) = SectionItem(
    modifier = modifier,
    title = {
        ProvideTextStyle(
            textStyle ?: CupertinoTheme.typography.body,
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
            ) {
                val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }

                CupertinoTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    colors = colors ?: CupertinoTextFieldDefaults.colors(),
                    enabled = enabled,
                    readOnly = readOnly,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    placeholder = placeholder?.let { { if (isRequired) HigAsteriskTextWrapper { it() } else it() } },
                    interactionSource = actualInteractionSource,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon?.let { { it(actualInteractionSource) } }
                )
            }
        }
    },
)

/**
 * 섹션 아이템 텍스트 필드 컴포저블 (TextFieldValue 버전)
 * 
 * 섹션 내부에 사용되는 텍스트 필드를 제공합니다.
 * 
 * @param value 현재 텍스트 필드의 값
 * @param onValueChange 텍스트 필드 값 변경 시 실행할 함수
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param enabled 텍스트 필드 활성화 상태 여부
 * @param readOnly 텍스트 필드 읽기 전용 상태 여부
 * @param textStyle 텍스트 스타일
 * @param placeholder 텍스트 필드의 placeholder 텍스트
 * @param isRequired 필드가 필수 입력 항목인지 여부
 * @param trailingIcon 텍스트 필드의 뒤에 나타나는 아이콘
 * @param visualTransformation 텍스트 변환 (예: 비밀번호 표시 방식)
 * @param keyboardOptions 키보드 옵션
 * @param keyboardActions 키보드 액션
 * @param singleLine 단일 행 입력 여부
 * @param maxLines 최대 행 수
 * @param minLines 최소 행 수
 * @param interactionSource 상호작용 소스
 * @param colors 텍스트 필드의 색상
 * @return 섹션 텍스트 필드 컴포저블
 */
@ExperimentalCupertinoApi
@Composable
fun SectionScope.SectionTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle? = null,
    placeholder: @Composable (() -> Unit)? = null,
    isRequired: Boolean = false,
    trailingIcon: @Composable ((InteractionSource) -> Unit)? = {
        val focused by it.collectIsFocusedAsState()
        val updatedValueChange by rememberUpdatedState(onValueChange)

        AdaptiveTextFieldClearButton(
            visible = focused && value.text.isNotEmpty(),
            onClick = {
                updatedValueChange.invoke(TextFieldValue(""))
            },
        )
    },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    colors: CupertinoTextFieldColors? = null,
) = SectionItem(
    modifier = modifier,
    title = {
        ProvideTextStyle(
            textStyle ?: CupertinoTheme.typography.body,
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
            ) {
                val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }

                CupertinoTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    colors = colors ?: CupertinoTextFieldDefaults.colors(),
                    enabled = enabled,
                    readOnly = readOnly,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    placeholder = placeholder?.let { { if (isRequired) HigAsteriskTextWrapper { it() } else it() } },
                    interactionSource = actualInteractionSource,
                    trailingIcon = trailingIcon?.let { { it(actualInteractionSource) } }
                )
            }
        }
    },
)

/**
 * 섹션 아이템 보안 텍스트 필드 컴포저블
 * 
 * 보안 텍스트 필드를 제공합니다.
 * 
 * @param state 텍스트 필드의 상태
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param enabled 텍스트 필드 활성화 상태 여부
 * @param readOnly 텍스트 필드 읽기 전용 상태 여부
 * @param textStyle 텍스트 스타일
 * @param placeholder 텍스트 필드의 placeholder 텍스트
 * @param leadingIcon 텍스트 필드의 앞에 나타나는 아이콘
 * @param trailingIcon 텍스트 필드의 뒤에 나타나는 아이콘
 * @param keyboardOptions 키보드 옵션
 * @param interactionSource 상호작용 소스
 * @param colors 텍스트 필드의 색상
 * @param textObfuscationMode 문자 표시 방식 (보이기, 가리기 등)
 * @param textObfuscationCharacter 문자 가리기 기호
 * @return 섹션 보안 텍스트 필드 컴포저블
 */
@ExperimentalCupertinoApi
@Composable
fun SectionScope.SectionSecureTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable ((InteractionSource) -> Unit)? = {
        val focused by it.collectIsFocusedAsState()

        HigTextFieldClearButton(
            visible = focused && state.text.isNotEmpty(),
            onClick = {
                state.clearText()
            },
        )
    },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    interactionSource: MutableInteractionSource? = null,
    colors: CupertinoTextFieldColors = CupertinoTextFieldDefaults.colors(),
    textObfuscationMode: TextObfuscationMode = TextObfuscationMode.RevealLastTyped,
    textObfuscationCharacter: Char = '\u2022',
) = SectionItem(
    modifier = modifier,
    title = {
        ProvideTextStyle(
            textStyle ?: CupertinoTheme.typography.body,
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
            ) {
                val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }

                CupertinoSecureTextField(
                    state = state,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    readOnly = readOnly,
                    keyboardOptions = keyboardOptions,
                    placeholder = placeholder,
                    interactionSource = actualInteractionSource,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon?.let { { it(actualInteractionSource) } },
                    colors = colors,
                    textObfuscationMode = textObfuscationMode,
                    textObfuscationCharacter = textObfuscationCharacter,
                )
            }
        }
    },
)



