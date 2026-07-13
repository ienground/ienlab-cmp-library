package zone.ien.utils.adaptive.section

import androidx.annotation.IntRange
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import zone.ien.hig.CupertinoCheckBox
import zone.ien.hig.CupertinoSlider
import zone.ien.hig.CupertinoSwitch
import zone.ien.hig.CupertinoTextFieldColors
import zone.ien.hig.CupertinoTextFieldDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.currentTheme
import zone.ien.hig.section.CupertinoSectionDefaults
import zone.ien.hig.section.LocalSectionStyle
import zone.ien.hig.section.SectionItem
import zone.ien.hig.section.SectionLink
import zone.ien.hig.section.SectionScope
import zone.ien.hig.theme.CupertinoColors
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.hig.theme.systemBlue
import zone.ien.hig.theme.systemGray
import zone.ien.hig.theme.systemRed
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.view.AsteriskTextWrapper
import zone.ien.utils.adaptive.view.textfield.AdaptiveTextFieldClearButton
import zone.ien.utils.hig.section.SectionSecureTextField
import zone.ien.utils.hig.section.SectionTextField
import zone.ien.utils.ui.section.IenSectionButton
import zone.ien.utils.ui.section.IenSectionCheckboxItem
import zone.ien.utils.ui.section.IenSectionColors
import zone.ien.utils.ui.section.IenSectionItem
import zone.ien.utils.ui.section.IenSectionLink
import zone.ien.utils.ui.section.IenSectionLinkDefault
import zone.ien.utils.ui.section.IenSectionSecureTextField
import zone.ien.utils.ui.section.IenSectionSlider
import zone.ien.utils.ui.section.IenSectionSwitchItem
import zone.ien.utils.ui.section.IenSectionTextField
import zone.ien.utils.ui.feedback.IenLinearProgressIndicator

/**
 * 적응형 섹션 항목 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param enabled 항목 활성화 여부
 * @param leadingContent 항목 앞에 표시할 콘텐츠
 * @param trailingContent 항목 뒤에 표시할 콘텐츠
 * @param supportingContent 보조 콘텐츠
 * @param adaptation 적응형 스타일 설정을 위한 범위
 * @param title 항목 제목
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.AdaptiveSectionItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    adaptation: AdaptationScope<HigSectionItemAdaptation, IenSectionItemAdaptation>.() -> Unit = {},
    title: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { SectionItemAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenSectionItem(
                modifier = modifier,
                enabled = enabled,
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                supportingContent = supportingContent,
                colors = it.colors,
                title = title
            )
        },
        cupertino = {
            SectionItem(
                modifier = modifier,
                paddingValues = it.paddingValues,
                leadingContent = {
                    if (it.showLeadingContent) {
                        leadingContent?.invoke()
                    }
                },
                trailingContent = { trailingContent?.invoke() },
                title = {
                    Column {
                        title()
                        if (it.showSupportingContent) {
                            ProvideTextStyle(
                                value = MaterialTheme.typography.bodyMedium.copy(color = CupertinoTheme.colorScheme.secondaryLabel)
                            ) {
                                supportingContent?.invoke()
                            }
                        }
                    }
                }
            )
        }
    )
}

/**
 * Material3 섹션 항목 적응성 클래스
 *
 * @param colors Material3 색상
 */
class IenSectionItemAdaptation internal constructor(
    colors: IenSectionColors
) {
    var colors by mutableStateOf(colors)
}

/**
 * HIG 섹션 항목 적응성 클래스
 *
 * @param showLeadingContent 앞 콘텐츠 표시 여부
 * @param showSupportingContent 보조 콘텐츠 표시 여부
 * @param paddingValues 패딩 값
 */
class HigSectionItemAdaptation internal constructor(
    showLeadingContent: Boolean = false,
    showSupportingContent: Boolean = false,
    paddingValues: PaddingValues,
) {
    var showLeadingContent by mutableStateOf(showLeadingContent)
    var showSupportingContent by mutableStateOf(showSupportingContent)
    var paddingValues by mutableStateOf(paddingValues)
}

/**
 * 섹션 항목 적응성 클래스
 */
@OptIn(ExperimentalAdaptiveApi::class)
internal class SectionItemAdaptation: Adaptation<HigSectionItemAdaptation, IenSectionItemAdaptation>() {
    /**
     * Cupertino 적응성 설정 메서드
     *
     * @return HIG 섹션 항목 적응성 객체
     */
    @Composable
    override fun rememberCupertinoAdaptation(): HigSectionItemAdaptation {
        val showLeadingContent = false
        val showSupportingContent = false
        val paddingValues = CupertinoSectionDefaults.PaddingValues

        return remember(showLeadingContent, showSupportingContent, paddingValues) {
            HigSectionItemAdaptation(
                showLeadingContent = showLeadingContent,
                showSupportingContent = showSupportingContent,
                paddingValues = paddingValues,
            )
        }
    }

    /**
     * Material 적응성 설정 메서드
     *
     * @return Material3 섹션 항목 적응성 객체
     */
    @Composable
    override fun rememberMaterialAdaptation(): IenSectionItemAdaptation {
        val colors = IenSectionLinkDefault.colors()
        return remember(colors) {
            IenSectionItemAdaptation(colors = colors)
        }
    }
}

/**
 * 적응형 섹션 스위치 항목 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param leadingContent 항목 앞에 표시할 콘텐츠
 * @param checked 스위치 체크 여부
 * @param onCheckedChange 체크 상태 변경 콜백
 * @param enabled 항목 활성화 여부
 * @param supportingContent 보조 콘텐츠
 * @param adaptation 적응형 스타일 설정을 위한 범위
 * @param title 항목 제목
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.AdaptiveSectionSwitchItem(
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    adaptation: AdaptationScope<HigSectionItemAdaptation, IenSectionItemAdaptation>.() -> Unit = {},
    title: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { SectionItemAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenSectionSwitchItem(
                modifier = modifier,
                leadingContent = leadingContent,
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                supportingContent = supportingContent,
                title = title
            )
        },
        cupertino = {
            val backgroundColor = CupertinoSectionDefaults.containerColor(LocalSectionStyle.current)
            val backdrop = rememberCanvasBackdrop { drawRect(backgroundColor) }

            SectionItem(
                modifier = modifier,
                paddingValues = it.paddingValues,
                leadingContent = if (it.showLeadingContent && leadingContent != null) leadingContent else {{}},
                trailingContent = {
                    CupertinoSwitch(
                        checked = checked,
                        onCheckedChange = onCheckedChange,
                        enabled = enabled,
                        backdrop = backdrop
                    )
                },
                title = {
                    Column {
                        title()
                        if (it.showSupportingContent) {
                            ProvideTextStyle(
                                value = MaterialTheme.typography.bodyMedium.copy(color = CupertinoTheme.colorScheme.secondaryLabel)
                            ) {
                                supportingContent?.invoke()
                            }
                        }
                    }
                }
            )
        }
    )
}

/**
 * 적응형 섹션 체크박스 항목 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param leadingContent 항목 앞에 표시할 콘텐츠
 * @param checked 체크 여부
 * @param onCheckedChange 체크 상태 변경 콜백
 * @param enabled 항목 활성화 여부
 * @param supportingContent 보조 콘텐츠
 * @param adaptation 적응형 스타일 설정을 위한 범위
 * @param title 항목 제목
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.AdaptiveSectionCheckboxItem(
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    adaptation: AdaptationScope<HigSectionItemAdaptation, IenSectionItemAdaptation>.() -> Unit = {},
    title: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { SectionItemAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenSectionCheckboxItem(
                modifier = modifier,
                leadingContent = leadingContent,
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                supportingContent = supportingContent,
                title = title
            )
        },
        cupertino = {
            SectionItem(
                modifier = modifier,
                paddingValues = it.paddingValues,
                leadingContent = if (it.showLeadingContent && leadingContent != null) leadingContent else {{}},
                trailingContent = {
                    CupertinoCheckBox(
                        checked = checked,
                        onCheckedChange = onCheckedChange,
                        enabled = enabled
                    )
                },
                title = {
                    Column {
                        title()
                        if (it.showSupportingContent) {
                            ProvideTextStyle(
                                value = MaterialTheme.typography.bodyMedium.copy(color = CupertinoTheme.colorScheme.secondaryLabel)
                            ) {
                                supportingContent?.invoke()
                            }
                        }
                    }
                }
            )
        }
    )
}

/**
 * 적응형 섹션 텍스트 필드 컴포저블
 *
 * @param value 텍스트 필드의 현재 값
 * @param onValueChange 값 변경 콜백
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param enabled 항목 활성화 여부
 * @param readOnly 읽기 전용 여부
 * @param textStyle 텍스트 스타일
 * @param placeholder 플레이스홀더 텍스트
 * @param isRequired 필수 입력 여부
 * @param leadingIcon 앞에 표시할 아이콘
 * @param trailingIcon 뒤에 표시할 아이콘
 * @param isError 에러 상태 여부
 * @param visualTransformation 시각적 변환
 * @param keyboardOptions 키보드 옵션
 * @param keyboardActions 키보드 액션
 * @param singleLine 단일 줄 입력 여부
 * @param maxLines 최대 줄 수
 * @param minLines 최소 줄 수
 * @param interactionSource 상호작용 소스
 * @param adaptation 적응형 스타일 설정을 위한 범위
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun SectionScope.AdaptiveSectionTextField(
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

        AdaptiveTextFieldClearButton(
            visible = focused && value.isNotEmpty(),
            onClick = {
                updatedValueChange.invoke("")
            },
        )
    },
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    adaptation: AdaptationScope<HigSectionTextFieldAdaptation, IenSectionTextFieldAdaptation>.() -> Unit = {},
) {
    AdaptiveWidget(
        adaptation = remember { SectionTextFieldAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenSectionTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                placeholder = placeholder,
                isRequired = isRequired,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                isError = isError,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                minLines = minLines,
                interactionSource = interactionSource,
            )
        },
        cupertino = {
            ProvideTextStyle(
                (textStyle ?: LocalTextStyle.current).copy(
                    color = if (isError) CupertinoColors.systemRed else Color.Unspecified
                )
            ) {
                SectionTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier,
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = (textStyle ?: LocalTextStyle.current),
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    interactionSource = interactionSource,
                    colors = it.colors
                )
            }

        }
    )
}

/**
 * 적응형 섹션 보안 텍스트 필드 컴포저블
 *
 * @param state 텍스트 필드 상태
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param enabled 항목 활성화 여부
 * @param readOnly 읽기 전용 여부
 * @param textStyle 텍스트 스타일
 * @param placeholder 플레이스홀더 텍스트
 * @param isRequired 필수 입력 여부
 * @param leadingIcon 앞에 표시할 아이콘
 * @param trailingIcon 뒤에 표시할 아이콘
 * @param isError 에러 상태 여부
 * @param keyboardOptions 키보드 옵션
 * @param interactionSource 상호작용 소스
 * @param textObfuscationMode 텍스트 가려짐 모드
 * @param textObfuscationCharacter 텍스트 가려짐 문자
 * @param adaptation 적응형 스타일 설정을 위한 범위
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun SectionScope.AdaptiveSectionSecureTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle? = null,
    placeholder: @Composable (() -> Unit)? = null,
    isRequired: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable ((InteractionSource) -> Unit)? = {
        val focused by it.collectIsFocusedAsState()

        AdaptiveTextFieldClearButton(
            visible = focused && state.text.isNotEmpty(),
            onClick = {
                state.clearText()
            },
        )
    },
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textObfuscationMode: TextObfuscationMode = TextObfuscationMode.RevealLastTyped,
    textObfuscationCharacter: Char = '\u2022',
    adaptation: AdaptationScope<HigSectionTextFieldAdaptation, IenSectionTextFieldAdaptation>.() -> Unit = {},
) {
    AdaptiveWidget(
        adaptation = remember { SectionTextFieldAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenSectionSecureTextField(
                state = state,
                modifier = modifier,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                placeholder = placeholder,
                isRequired = isRequired,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                isError = isError,
                keyboardOptions = keyboardOptions,
                interactionSource = interactionSource,
                textObfuscationMode = textObfuscationMode,
                textObfuscationCharacter = textObfuscationCharacter,
            )
        },
        cupertino = {
            ProvideTextStyle(
                (textStyle ?: LocalTextStyle.current).copy(
                    color = if (isError) CupertinoColors.systemRed else Color.Unspecified
                )
            ) {
                SectionSecureTextField(
                    state = state,
                    modifier = modifier,
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = (textStyle ?: LocalTextStyle.current),
                    placeholder = placeholder?.let { { if (isRequired) AsteriskTextWrapper { it() } else it() } },
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    keyboardOptions = keyboardOptions,
                    interactionSource = interactionSource,
                    colors = it.colors,
                    textObfuscationMode = textObfuscationMode,
                    textObfuscationCharacter = textObfuscationCharacter,
                )
            }

        }
    )
}

/**
 * 적응형 섹션 텍스트 필드 컴포저블 (TextFieldValue 버전)
 *
 * @param value 텍스트 필드의 현재 값
 * @param onValueChange 값 변경 콜백
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param enabled 항목 활성화 여부
 * @param readOnly 읽기 전용 여부
 * @param textStyle 텍스트 스타일
 * @param placeholder 플레이스홀더 텍스트
 * @param isRequired 필수 입력 여부
 * @param trailingIcon 뒤에 표시할 아이콘
 * @param isError 에러 상태 여부
 * @param visualTransformation 시각적 변환
 * @param keyboardOptions 키보드 옵션
 * @param keyboardActions 키보드 액션
 * @param singleLine 단일 줄 입력 여부
 * @param maxLines 최대 줄 수
 * @param minLines 최소 줄 수
 * @param interactionSource 상호작용 소스
 * @param adaptation 적응형 스타일 설정을 위한 범위
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun SectionScope.AdaptiveSectionTextField(
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
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    adaptation: AdaptationScope<HigSectionTextFieldAdaptation, IenSectionTextFieldAdaptation>.() -> Unit = {},
) {
    AdaptiveWidget(
        adaptation = remember { SectionTextFieldAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenSectionTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle,
                placeholder = placeholder,
                isRequired = isRequired,
                trailingIcon = trailingIcon,
                isError = isError,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                singleLine = singleLine,
                maxLines = maxLines,
                minLines = minLines,
                interactionSource = interactionSource,
            )
        },
        cupertino = {
            ProvideTextStyle(
                (textStyle ?: LocalTextStyle.current).copy(
                    color = if (isError) CupertinoColors.systemRed else Color.Unspecified
                )
            ) {
                SectionTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier,
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = (textStyle ?: LocalTextStyle.current),
                    placeholder = placeholder,
                    isRequired = isRequired,
                    trailingIcon = trailingIcon,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    interactionSource = interactionSource,
                    colors = it.colors
                )
            }

        }
    )
}

/**
 * IEN 섹션 텍스트 필드 적응성 클래스
 */
class IenSectionTextFieldAdaptation internal constructor()

/**
 * HIG 섹션 텍스트 필드 적응성 클래스
 *
 * @param colors 텍스트 필드 색상
 */
class HigSectionTextFieldAdaptation internal constructor(
    colors: CupertinoTextFieldColors
) {
    var colors by mutableStateOf(colors)
}

/**
 * 섹션 텍스트 필드 적응성 클래스
 */
@OptIn(ExperimentalAdaptiveApi::class)
internal class SectionTextFieldAdaptation: Adaptation<HigSectionTextFieldAdaptation, IenSectionTextFieldAdaptation>() {
    /**
     * Cupertino 적응성 설정 메서드
     *
     * @return HIG 섹션 텍스트 필드 적응성 객체
     */
    @Composable
    override fun rememberCupertinoAdaptation(): HigSectionTextFieldAdaptation {
        val colors = CupertinoTextFieldDefaults.colors()

        return remember(colors) { HigSectionTextFieldAdaptation(colors = colors) }
    }

    /**
     * Material 적응성 설정 메서드
     *
     * @return IEN 섹션 텍스트 필드 적응성 객체
     */
    @Composable
    override fun rememberMaterialAdaptation(): IenSectionTextFieldAdaptation {
        return remember { IenSectionTextFieldAdaptation() }
    }
}

/**
 * 적응형 섹션 링크 컴포저블
 *
 * @param onClick 클릭 콜백
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param enabled 항목 활성화 여부
 * @param leadingIcon 앞에 표시할 아이콘
 * @param onClickLabel 클릭 시 접근성 레이블
 * @param indication 표시 방법
 * @param interactionSource 상호작용 소스
 * @param caption 캡션
 * @param trailingContent 뒤에 표시할 콘텐츠
 * @param adaptation 적응형 스타일 설정을 위한 범위
 * @param title 항목 제목
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun SectionScope.AdaptiveSectionLink(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    caption: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = if (currentTheme == Theme.Material3) null else { { CupertinoSectionDefaults.LabelChevron() } },
    adaptation: AdaptationScope<HigSectionLinkAdaptation, IenSectionLinkAdaptation>.() -> Unit = {},
    title: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember { SectionLinkAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenSectionLink(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                leadingIcon = leadingIcon,
                onClickLabel = onClickLabel,
                indication = indication,
                interactionSource = interactionSource,
                colors = it.colors,
                caption = caption,
                trailingContent = trailingContent,
                title = title
            )
        },
        cupertino = {
            SectionLink(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                icon = { leadingIcon?.invoke() },
                onClickLabel = onClickLabel,
                indication = indication,
                interactionSource = interactionSource,
                caption = { if (it.isCaption) caption?.invoke() },
                chevron = { trailingContent?.invoke() },
                title = {
                    Column {
                        title()
                        if (it.showSupportingContent && !it.isCaption) {
                            ProvideTextStyle(
                                value = MaterialTheme.typography.bodyMedium.copy(color = CupertinoTheme.colorScheme.secondaryLabel)
                            ) {
                                caption?.invoke()
                            }
                        }
                    }
                }
            )
        }
    )
}

/**
 * Material3 섹션 링크 적응성 클래스
 *
 * @param colors 링크 색상
 */
class IenSectionLinkAdaptation internal constructor(
    colors: IenSectionColors
) {
    var colors by mutableStateOf(colors)
}

/**
 * HIG 섹션 링크 적응성 클래스
 *
 * @param isCaption 캡션 여부
 * @param showSupportingContent 보조 콘텐츠 표시 여부
 */
class HigSectionLinkAdaptation internal constructor(
    isCaption: Boolean = true,
    showSupportingContent: Boolean = false
) {
    var isCaption by mutableStateOf(isCaption)
    var showSupportingContent by mutableStateOf(showSupportingContent)
}

/**
 * 섹션 링크 적응성 클래스
 */
@OptIn(ExperimentalAdaptiveApi::class)
internal class SectionLinkAdaptation: Adaptation<HigSectionLinkAdaptation, IenSectionLinkAdaptation>() {
    /**
     * Cupertino 적응성 설정 메서드
     *
     * @return HIG 섹션 링크 적응성 객체
     */
    @Composable
    override fun rememberCupertinoAdaptation(): HigSectionLinkAdaptation {
        val isCaption = false
        val showSupportingContent = false
        return remember(isCaption, showSupportingContent) {
            HigSectionLinkAdaptation(
                isCaption = isCaption,
                showSupportingContent = showSupportingContent
            )
        }
    }

    /**
     * Material 적응성 설정 메서드
     *
     * @return Material3 섹션 링크 적응성 객체
     */
    @Composable
    override fun rememberMaterialAdaptation(): IenSectionLinkAdaptation {
        val colors = IenSectionLinkDefault.colors()
        return remember(colors) { IenSectionLinkAdaptation(colors = colors) }
    }
}

/**
 * 적응형 섹션 버튼 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param onClick 클릭 콜백
 * @param enabled 항목 활성화 여부
 * @param label 버튼 라벨
 * @param adaptation 적응형 스타일 설정을 위한 범위
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class,)
@Composable
fun SectionScope.AdaptiveSectionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
    adaptation: AdaptationScope<HigSectionButtonAdaptation, IenSectionButtonAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { SectionButtonAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenSectionButton(
                modifier = modifier,
                onClick = onClick,
                icon = it.icon,
                enabled = enabled,
                label = label
            )
        },
        cupertino = {
            SectionLink(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                chevron = {},
                title = {
                    ProvideTextStyle(
                        value = TextStyle.Default.copy(
                            color =
                                if (enabled) {
                                    CupertinoColors.systemBlue
                                } else {
                                    CupertinoColors.systemGray
                                }
                        ),
                        content = label
                    )
                }
            )
        }
    )
}

/**
 * IEN 섹션 버튼 적응성 클래스
 *
 * @param icon 아이콘
 */
class IenSectionButtonAdaptation internal constructor(
    icon: @Composable (() -> Unit)? = null,
) {
    var icon by mutableStateOf(icon)
}

/**
 * HIG 섹션 버튼 적응성 클래스
 */
class HigSectionButtonAdaptation internal constructor()
    
/**
 * 섹션 버튼 적응성 클래스
 */
@OptIn(ExperimentalAdaptiveApi::class)
internal class SectionButtonAdaptation: Adaptation<HigSectionButtonAdaptation, IenSectionButtonAdaptation>() {
    /**
     * Cupertino 적응성 설정 메서드
     *
     * @return HIG 섹션 버튼 적응성 객체
     */
    @Composable
    override fun rememberCupertinoAdaptation(): HigSectionButtonAdaptation {
        return remember { HigSectionButtonAdaptation() }
    }

    /**
     * Material 적응성 설정 메서드
     *
     * @return IEN 섹션 버튼 적응성 객체
     */
    @Composable
    override fun rememberMaterialAdaptation(): IenSectionButtonAdaptation {
        val icon: @Composable (() -> Unit)? = null

        return remember(icon) {
            IenSectionButtonAdaptation(
                icon = icon,
            )
        }
    }
}


/**
 * 적응형 섹션 슬라이더 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param value 슬라이더 현재 값
 * @param onValueChange 값 변경 콜백
 * @param enabled 항목 활성화 여부
 * @param valueRange 값 범위
 * @param steps 스텝 수
 * @param adaptation 적응형 스타일 설정을 위한 범위
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun SectionScope.AdaptiveSectionSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0) steps: Int = 0,
    adaptation: AdaptationScope<HigSectionSliderAdaptation, IenSectionSliderAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { SectionSliderAdaptation() },
        material = {
            IenSectionSlider(
                modifier = modifier,
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                valueRange = valueRange,
                steps = steps,
                title = it.title,
                icon = it.icon
            )
        },
        cupertino = {
            val backgroundColor = CupertinoSectionDefaults.containerColor(LocalSectionStyle.current)
            val backdrop = rememberCanvasBackdrop { drawRect(backgroundColor) }

            SectionItem(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it.startIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                            )
                        }
                        CupertinoSlider(
                            value = value,
                            onValueChange = onValueChange,
                            enabled = enabled,
                            valueRange = valueRange,
                            steps = steps,
                            backdrop = backdrop,
                            modifier = Modifier.weight(1f)
                        )
                        it.endIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                            )
                        }
                    }
                },
                modifier = modifier
            )
        },
        adaptationScope = adaptation,
    )
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
private fun SectionScope.AdaptiveSectionProgressBar(
    modifier: Modifier = Modifier
) {
    AdaptiveWidget(
        material = {
            AdaptiveSectionItem(
                title = {},
                supportingContent = {
                    IenLinearProgressIndicator()
                },
                modifier = modifier
            )
        },
        cupertino = {
            SectionItem(
                title = {
                },
                modifier = modifier
            )
        }
    )
}

/**
 * HIG 섹션 슬라이더 적응성 클래스
 *
 * @param startIcon 시작 아이콘
 * @param endIcon 끝 아이콘
 */
class HigSectionSliderAdaptation(
    startIcon: ImageVector?,
    endIcon: ImageVector?,
) {
    var startIcon: ImageVector? by mutableStateOf(startIcon)
    var endIcon: ImageVector? by mutableStateOf(endIcon)
}

/**
 * Material3 섹션 슬라이더 적응성 클래스
 *
 * @param title 제목
 * @param icon 아이콘
 */
class IenSectionSliderAdaptation(
    title: String? = null,
    icon: ImageVector?
) {
    var title: String? by mutableStateOf(title)
    var icon: ImageVector? by mutableStateOf(icon)
}

/**
 * 섹션 슬라이더 적응성 클래스
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private class SectionSliderAdaptation: Adaptation<HigSectionSliderAdaptation, IenSectionSliderAdaptation>() {
    /**
     * Cupertino 적응성 설정 메서드
     *
     * @return HIG 섹션 슬라이더 적응성 객체
     */
    @Composable
    override fun rememberCupertinoAdaptation(): HigSectionSliderAdaptation {
        val startIcon: ImageVector? = null
        val endIcon: ImageVector? = null

        return remember(startIcon, endIcon) {
            HigSectionSliderAdaptation(
                startIcon, endIcon
            )
        }
    }

    /**
     * Material 적응성 설정 메서드
     *
     * @return Material3 섹션 슬라이더 적응성 객체
     */
    @Composable
    override fun rememberMaterialAdaptation(): IenSectionSliderAdaptation {
        val title: String? = null
        val icon: ImageVector? = null

        return remember(title, icon) {
            IenSectionSliderAdaptation(
                title, icon
            )
        }
    }
}
