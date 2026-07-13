package zone.ien.utils.ui.section

import androidx.annotation.IntRange
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import zone.ien.hig.section.SectionScope
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButtonContainer
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenCircleCheckbox
import zone.ien.utils.ui.interactive.IenSlider
import zone.ien.utils.ui.interactive.IenSwitch
import zone.ien.utils.ui.feedback.IenLinearProgressIndicator
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.view.IenAsteriskTextWrapper
import zone.ien.utils.ui.view.textfield.IenTextFieldClearButton
import zone.ien.utils.ui.view.textfield.PlaceholderBasicSecureTextField
import zone.ien.utils.ui.view.textfield.PlaceholderBasicTextField

/**
 * 섹션 항목 컴포저블
 * 
 * 이 컴포저블은 섹션 내부에 표시되는 항목을 정의합니다.
 * 제목, 지원 콘텐츠, 앞쪽/뒤쪽 콘텐츠를 포함할 수 있습니다.
 * 
 * @param modifier 적용할 Modifier
 * @param enabled 활성화 상태
 * @param leadingContent 앞쪽 콘텐츠
 * @param trailingContent 뒤쪽 콘텐츠
 * @param supportingContent 지원 콘텐츠
 * @param colors 항목의 색상
 * @param title 제목
 */
@Composable
fun SectionScope.IenSectionItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    colors: IenSectionColors = IenSectionLinkDefault.colors(),
    title: @Composable () -> Unit
) {
    IenSurface(
        modifier = Modifier
            .clip(RoundedCornerShape(IenTheme.radius.sm))
            .then(modifier),
        color = colors.containerColor(),
        contentColor = colors.headlineColor(enabled),
        shape = RoundedCornerShape(IenTheme.radius.sm),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingContent != null) {
                CompositionLocalProvider(LocalContentColor provides colors.leadingIconColor(enabled)) {
                    Box(
                        modifier = Modifier.heightIn(min = IenTheme.icon.lg),
                        contentAlignment = Alignment.Center,
                    ) {
                        leadingContent()
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxxs),
            ) {
                IenProvideTextStyle(IenTheme.typography.body2, colors.headlineColor(enabled)) {
                    title()
                }
                if (supportingContent != null) {
                    IenProvideTextStyle(IenTheme.typography.caption, colors.supportingColor(enabled)) {
                        supportingContent()
                    }
                }
            }
            if (trailingContent != null) {
                CompositionLocalProvider(LocalContentColor provides colors.trailingIconColor(enabled)) {
                    Box(contentAlignment = Alignment.Center) {
                        trailingContent()
                    }
                }
            }
        }
    }
}

/**
 * 섹션 스위치 항목 컴포저블
 * 
 * 이 컴포저블은 스위치 형태의 섹션 항목을 표시합니다.
 * 스위치의 상태를 변경할 수 있으며, 클릭 시 스위치 상태를 토글합니다.
 * 
 * @param modifier 적용할 Modifier
 * @param leadingContent 앞쪽 콘텐츠
 * @param checked 스위치의 현재 상태 (켜짐/꺼짐)
 * @param onCheckedChange 스위치 상태가 변경될 때 호출되는 함수
 * @param enabled 활성화 상태
 * @param supportingContent 지원 콘텐츠
 * @param title 제목
 */
@Composable
fun SectionScope.IenSectionSwitchItem(
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit
) {
    IenSectionItem(
        leadingContent = leadingContent,
        trailingContent = {
            IenSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
            )
        },
        supportingContent = supportingContent,
        title = title,
        modifier = modifier.clickable(enabled = enabled) { onCheckedChange(!checked) }
    )
}

/**
 * 섹션 체크박스 항목 컴포저블
 * 
 * 이 컴포저블은 체크박스 형태의 섹션 항목을 표시합니다.
 * 체크박스의 상태를 변경할 수 있으며, 클릭 시 체크박스 상태를 토글합니다.
 * 
 * @param modifier 적용할 Modifier
 * @param leadingContent 앞쪽 콘텐츠
 * @param checked 체크박스의 현재 상태 (선택됨/해제됨)
 * @param onCheckedChange 체크박스 상태가 변경될 때 호출되는 함수
 * @param supportingContent 지원 콘텐츠
 * @param title 제목
 */
@Composable
fun SectionScope.IenSectionCheckboxItem(
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit
) {
    IenSectionItem(
        leadingContent = leadingContent,
        trailingContent = {
            IenCircleCheckbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        },
        supportingContent = supportingContent,
        title = title,
        modifier = modifier.clickable { onCheckedChange(!checked) }
    )
}

/**
 * 섹션 텍스트 필드 컴포저블 (String 타입 버전)
 * 
 * 이 컴포저블은 섹션 내부에 표시되는 텍스트 필드를 제공합니다.
 * 필수 입력 여부, 아이콘 표시, 오류 상태 등을 지원합니다.
 * 
 * @param value 텍스트 필드의 현재 값
 * @param onValueChange 텍스트 값이 변경될 때 호출되는 함수
 * @param modifier 적용할 Modifier
 * @param enabled 활성화 상태
 * @param readOnly 읽기 전용 상태
 * @param textStyle 텍스트 스타일
 * @param placeholder PlaceHolder 텍스트
 * @param isRequired 필수 입력 여부
 * @param leadingIcon 좌측 아이콘
 * @param trailingIcon 우측 아이콘 (InteractionSource를 인자로 받음)
 * @param isError 오류 상태
 * @param visualTransformation 입력 텍스트의 시각적 변환
 * @param keyboardOptions 키보드 설정
 * @param keyboardActions 키보드 액션
 * @param singleLine 단일 라인 입력 여부
 * @param maxLines 최대 라인 수
 * @param minLines 최소 라인 수
 * @param interactionSource 상호작용 소스
 */
@Composable
fun SectionScope.IenSectionTextField(
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

        IenTextFieldClearButton(
            visible = focused && value.isNotEmpty(),
            onClick = { updatedValueChange.invoke("") }
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
) {
    val focused by interactionSource.collectIsFocusedAsState()
    val fieldColor = ienSectionTextFieldColor(
        enabled = enabled,
        focused = focused,
        isError = isError,
    )
    ProvideTextStyle(
        (textStyle ?: LocalTextStyle.current).copy(color = fieldColor)
    ) {
        IenSectionItem(
            title = {
                PlaceholderBasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = (textStyle ?: LocalTextStyle.current),
                    placeholder = placeholder,
                    isRequired = isRequired,
                    leadingIcon = leadingIcon,
                    trailingIcon = {
                        trailingIcon?.invoke(interactionSource)
                    },
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(
                        if (isError) IenTheme.colors.danger
                        else IenTheme.colors.brand
                    ),
                    modifier = modifier
                        .heightIn(min = 48.dp)
                        .fillMaxWidth()
                )
            },
        )
    }
}

/**
 * 섹션 텍스트 필드 컴포저블 (TextFieldValue 타입 버전)
 * 
 * 이 컴포저블은 TextFieldValue 타입을 사용하는 버전의 섹션 텍스트 필드입니다.
 * 
 * @param value 텍스트 필드의 현재 값 (TextFieldValue 타입)
 * @param onValueChange 텍스트 값이 변경될 때 호출되는 함수
 * @param modifier 적용할 Modifier
 * @param enabled 활성화 상태
 * @param readOnly 읽기 전용 상태
 * @param textStyle 텍스트 스타일
 * @param placeholder PlaceHolder 텍스트
 * @param isRequired 필수 입력 여부
 * @param trailingIcon 우측 아이콘 (InteractionSource를 인자로 받음)
 * @param isError 오류 상태
 * @param visualTransformation 입력 텍스트의 시각적 변환
 * @param keyboardOptions 키보드 설정
 * @param keyboardActions 키보드 액션
 * @param singleLine 단일 라인 입력 여부
 * @param maxLines 최대 라인 수
 * @param minLines 최소 라인 수
 * @param interactionSource 상호작용 소스
 */
@Composable
fun SectionScope.IenSectionTextField(
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

        IenTextFieldClearButton(
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
) {
    val focused by interactionSource.collectIsFocusedAsState()
    val fieldColor = ienSectionTextFieldColor(
        enabled = enabled,
        focused = focused,
        isError = isError,
    )
    ProvideTextStyle(
        (textStyle ?: LocalTextStyle.current).copy(color = fieldColor)
    ) {
        IenSectionItem(
            title = {
                PlaceholderBasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier.heightIn(min = 48.dp),
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = (textStyle ?: LocalTextStyle.current),
                    placeholder = placeholder?.let { { if (isRequired) IenAsteriskTextWrapper { it() } else it() } },
                    trailingIcon = {
                        trailingIcon?.invoke(interactionSource)
                    },
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(
                        if (isError) IenTheme.colors.danger
                        else IenTheme.colors.brand
                    )
                )
            },
        )
    }
}

/**
 * 섹션 보안 텍스트 필드 컴포저블
 * 
 * 이 컴포저블은 보안 텍스트 필드(예: 비밀번호)를 섹션 내부에 표시합니다.
 * 텍스트가 마스킹 처리되어 보안이 필요한 입력에 사용됩니다.
 * 
 * @param state 텍스트 필드의 상태
 * @param modifier 적용할 Modifier
 * @param enabled 활성화 상태
 * @param readOnly 읽기 전용 상태
 * @param textStyle 텍스트 스타일
 * @param placeholder PlaceHolder 텍스트
 * @param isRequired 필수 입력 여부
 * @param leadingIcon 좌측 아이콘
 * @param trailingIcon 우측 아이콘 (InteractionSource를 인자로 받음)
 * @param isError 오류 상태
 * @param keyboardOptions 키보드 설정
 * @param interactionSource 상호작용 소스
 * @param textObfuscationMode 텍스트 가려짐 모드
 * @param textObfuscationCharacter 가려질 때 사용할 문자
 */
@Composable
fun SectionScope.IenSectionSecureTextField(
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

        IenTextFieldClearButton(
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
) {
    val focused by interactionSource.collectIsFocusedAsState()
    val fieldColor = ienSectionTextFieldColor(
        enabled = enabled,
        focused = focused,
        isError = isError,
    )
    ProvideTextStyle(
        (textStyle ?: LocalTextStyle.current).copy(color = fieldColor)
    ) {
        IenSectionItem(
            title = {
                PlaceholderBasicSecureTextField(
                    state = state,
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = (textStyle ?: LocalTextStyle.current),
                    placeholder = placeholder?.let { { if (isRequired) IenAsteriskTextWrapper { it() } else it() } },
                    leadingIcon = leadingIcon,
                    trailingIcon = {
                        trailingIcon?.invoke(interactionSource)
                    },
                    keyboardOptions = keyboardOptions,
                    interactionSource = interactionSource,
                    textObfuscationMode = textObfuscationMode,
                    textObfuscationCharacter = textObfuscationCharacter
                )
            },
        )
    }
}

/**
 * 섹션 링크 컴포저블
 * 
 * 이 컴포저블은 클릭 가능한 링크 형태의 섹션 항목을 표시합니다.
 * 클릭 시 지정된 콜백 함수가 호출됩니다.
 * 
 * @param onClick 클릭 시 호출되는 콜백 함수
 * @param modifier 적용할 Modifier
 * @param enabled 활성화 상태
 * @param leadingIcon 좌측 아이콘
 * @param trailingContent 우측 콘텐츠
 * @param onClickLabel 클릭에 대한 설명 텍스트
 * @param indication 상호작용 시 표시할 인디케이션
 * @param interactionSource 상호작용 소스
 * @param colors 항목 색상
 * @param caption 캡션 텍스트
 * @param title 제목
 */
@Composable
fun SectionScope.IenSectionLink(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    colors: IenSectionColors = IenSectionLinkDefault.colors(),
    caption: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
) {
    IenSectionItem(
        modifier =
            modifier
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    role = Role.Button,
                    onClickLabel = onClickLabel,
                    interactionSource = interactionSource ?: remember { MutableInteractionSource() },
                    indication = indication,
                ),
        enabled = enabled,
        title = title,
        leadingContent = leadingIcon,
        trailingContent = trailingContent,
        supportingContent = caption,
        colors = colors
    )
}


@Immutable
class IenSectionColors(
    val containerColor: Color,
    val headlineColor: Color,
    val leadingIconColor: Color,
    val overlineColor: Color,
    val supportingTextColor: Color,
    val trailingIconColor: Color,
    val disabledHeadlineColor: Color,
    val disabledLeadingIconColor: Color,
    val disabledOverlineColor: Color,
    val disabledSupportingTextColor: Color,
    val disabledTrailingIconColor: Color,
) {
    /**
     * Returns a copy of this ListItemColors, optionally overriding some of the values. This uses
     * the Color.Unspecified to mean “use the value from the source”
     */
    fun copy(
        containerColor: Color = this.containerColor,
        headlineColor: Color = this.headlineColor,
        leadingIconColor: Color = this.leadingIconColor,
        overlineColor: Color = this.overlineColor,
        supportingTextColor: Color = this.supportingTextColor,
        trailingIconColor: Color = this.trailingIconColor,
        disabledHeadlineColor: Color = this.disabledHeadlineColor,
        disabledLeadingIconColor: Color = this.disabledLeadingIconColor,
        disabledOverlineColor: Color = this.disabledOverlineColor,
        disabledSupportingTextColor: Color = this.disabledSupportingTextColor,
        disabledTrailingIconColor: Color = this.disabledTrailingIconColor,
    ) =
        IenSectionColors(
            containerColor = containerColor.takeOrElse { this.containerColor },
            headlineColor = headlineColor.takeOrElse { this.headlineColor },
            leadingIconColor = leadingIconColor.takeOrElse { this.leadingIconColor },
            overlineColor = overlineColor.takeOrElse { this.overlineColor },
            supportingTextColor = supportingTextColor.takeOrElse { this.supportingTextColor },
            trailingIconColor = trailingIconColor.takeOrElse { this.trailingIconColor },
            disabledHeadlineColor = disabledHeadlineColor.takeOrElse { this.disabledHeadlineColor },
            disabledLeadingIconColor = disabledLeadingIconColor.takeOrElse { this.disabledLeadingIconColor },
            disabledOverlineColor = disabledOverlineColor.takeOrElse { this.disabledOverlineColor },
            disabledSupportingTextColor = disabledSupportingTextColor.takeOrElse { this.disabledSupportingTextColor },
            disabledTrailingIconColor = disabledTrailingIconColor.takeOrElse { this.disabledTrailingIconColor },
        )

    fun toListItemColors(enabled: Boolean) = ListItemColors(
        containerColor = containerColor(),
        headlineColor = headlineColor(enabled),
        leadingIconColor = leadingIconColor(enabled),
        overlineColor = overlineColor(enabled),
        supportingTextColor = supportingColor(enabled),
        trailingIconColor = trailingIconColor(enabled),
        disabledHeadlineColor = disabledHeadlineColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
    )

    /** The container color of this [ListItem] based on enabled state */
    internal fun containerColor(): Color = containerColor

    /** The color of this [ListItem]'s headline text based on enabled state */
    @Stable internal fun headlineColor(enabled: Boolean): Color = if (enabled) headlineColor else disabledHeadlineColor

    /** The color of this [ListItem]'s leading content based on enabled state */
    @Stable internal fun leadingIconColor(enabled: Boolean): Color = if (enabled) leadingIconColor else disabledLeadingIconColor

    /** The color of this [ListItem]'s overline text based on enabled state */
    @Stable internal fun overlineColor(enabled: Boolean): Color = if (enabled) overlineColor else disabledOverlineColor

    /** The color of this [ListItem]'s supporting text based on enabled state */
    @Stable internal fun supportingColor(enabled: Boolean): Color = if (enabled) supportingTextColor else disabledSupportingTextColor

    /** The color of this [ListItem]'s trailing content based on enabled state */
    @Stable internal fun trailingIconColor(enabled: Boolean): Color = if (enabled) trailingIconColor else disabledTrailingIconColor
}

object IenSectionLinkDefault {
    @Composable
    fun colors() = IenSectionColors(
        containerColor = IenTheme.colors.surface,
        headlineColor = IenTheme.colors.textPrimary,
        leadingIconColor = IenTheme.colors.textSecondary,
        overlineColor = IenTheme.colors.textSecondary,
        supportingTextColor = IenTheme.colors.textSecondary,
        trailingIconColor = IenTheme.colors.textSecondary,
        disabledHeadlineColor = IenTheme.colors.textDisabled,
        disabledLeadingIconColor = IenTheme.colors.textDisabled,
        disabledSupportingTextColor = IenTheme.colors.textDisabled,
        disabledOverlineColor = IenTheme.colors.textDisabled,
        disabledTrailingIconColor = IenTheme.colors.textDisabled,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SectionScope.IenSectionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
) {
    IenButtonContainer(
        onClick = onClick,
        state = IenButtonState(enabled = enabled),
        variant = IenButtonVariant.Fill,
        tone = IenSemanticTone.Brand,
        shape = RoundedCornerShape(IenTheme.radius.default),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.invoke()
            label.invoke()
        }
    }
}

@Composable
private fun ienSectionTextFieldColor(
    enabled: Boolean,
    focused: Boolean,
    isError: Boolean,
): Color {
    return when {
        isError -> IenTheme.colors.danger
        !enabled -> IenTheme.colors.textDisabled
        focused -> IenTheme.colors.textPrimary
        else -> IenTheme.colors.textPrimary
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SectionScope.IenSectionSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0) steps: Int = 0,
    title: String? = null,
    icon: ImageVector? = null
) {
    IenSectionItem(
        title = { title?.let { Text(text = it) } },
        supportingContent = {
            IenSlider(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                valueRange = valueRange,
                steps = steps,
                enabled = enabled,
            )
        },
        modifier = modifier
    )
}

@Composable
private fun SectionScope.AdaptiveSectionProgressBar(
    modifier: Modifier = Modifier
) {
    IenSectionItem(
        title = {},
        supportingContent = { IenLinearProgressIndicator() },
        modifier = modifier
    )
}
