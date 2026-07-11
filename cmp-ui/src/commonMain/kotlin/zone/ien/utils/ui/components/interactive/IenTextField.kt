package zone.ien.utils.ui.components.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenDivider
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

sealed interface IenFieldStatus {
    data object Normal : IenFieldStatus
    data class Error(val message: String) : IenFieldStatus
    data class Success(val message: String? = null) : IenFieldStatus
}

@Immutable
data class IenTextFieldState(
    val enabled: Boolean = true,
    val readOnly: Boolean = false,
    val status: IenFieldStatus = IenFieldStatus.Normal,
)

enum class IenTextFieldVariant {
    Box,
    Line,
    Big,
    Hero,
}

enum class IenTextFieldLabelOption {
    Appear,
    Sustain,
}

@Immutable
data class IenTextFieldFormat(
    val transform: (value: String) -> String,
    val reset: ((formattedValue: String) -> String)? = null,
)

@Composable
fun IenTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
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
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    val focused by interactionSource.collectIsFocusedAsState()
    var wasFocused by remember { mutableStateOf(false) }
    val effectiveStatus = when {
        hasError && state.status !is IenFieldStatus.Error -> {
            IenFieldStatus.Error(help ?: supportingText ?: "")
        }
        else -> state.status
    }
    val formattedValue = format?.transform?.invoke(value) ?: value
    val showLabel = label != null && (labelOption == IenTextFieldLabelOption.Sustain || value.isNotEmpty() || focused)
    val fieldTextStyle = when (variant) {
        IenTextFieldVariant.Box,
        IenTextFieldVariant.Line -> IenTheme.typography.body1
        IenTextFieldVariant.Big -> IenTheme.typography.title2
        IenTextFieldVariant.Hero -> IenTheme.typography.display
    }
    val borderColor = when (effectiveStatus) {
        is IenFieldStatus.Error -> IenTheme.colors.danger
        is IenFieldStatus.Success -> IenTheme.colors.success
        IenFieldStatus.Normal -> if (focused) IenTheme.colors.brand else IenTheme.colors.border
    }
    val supporting = when (val status = effectiveStatus) {
        is IenFieldStatus.Error -> status.message
        is IenFieldStatus.Success -> status.message ?: help ?: supportingText
        IenFieldStatus.Normal -> help ?: supportingText
    }
    val supportingColor = when (effectiveStatus) {
        is IenFieldStatus.Error -> IenTheme.colors.danger
        is IenFieldStatus.Success -> IenTheme.colors.success
        IenFieldStatus.Normal -> IenTheme.colors.textTertiary
    }
    val textColor = if (state.enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled

    Column(modifier = modifier.semantics {
        if (effectiveStatus is IenFieldStatus.Error) error(effectiveStatus.message)
    }) {
        if (showLabel) {
            IenText(text = label, style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            Spacer(Modifier.height(IenTheme.spacing.xxs))
        }
        IenTextFieldContainer(
            variant = variant,
            enabled = state.enabled,
            borderColor = borderColor,
        ) {
            Row(
                modifier = Modifier.padding(
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
                    value = formattedValue,
                    onValueChange = { next ->
                        onValueChange(format?.reset?.invoke(next) ?: next)
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
                    enabled = state.enabled,
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
                        if (formattedValue.isEmpty() && placeholder != null) {
                            IenText(
                                text = placeholder,
                                style = fieldTextStyle,
                                color = IenTheme.colors.textTertiary,
                            )
                        }
                        innerTextField()
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
        if (!supporting.isNullOrBlank()) {
            Spacer(Modifier.height(IenTheme.spacing.xxs))
            IenText(
                text = supporting,
                modifier = Modifier.padding(horizontal = IenTheme.spacing.xs),
                style = IenTheme.typography.caption,
                color = supportingColor,
            )
        }
    }
}

@Composable
fun IenTextArea(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    state: IenTextFieldState = IenTextFieldState(),
    supportingText: String? = null,
    minLines: Int = 4,
    maxLines: Int = 8,
) {
    IenTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        state = state,
        supportingText = supportingText,
        singleLine = false,
        minLines = minLines,
        maxLines = maxLines,
    )
}

@Composable
fun IenClearableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    labelOption: IenTextFieldLabelOption = IenTextFieldLabelOption.Appear,
    placeholder: String? = null,
    help: String? = null,
    hasError: Boolean = false,
    variant: IenTextFieldVariant = IenTextFieldVariant.Box,
    prefix: String? = null,
    suffix: String? = null,
    state: IenTextFieldState = IenTextFieldState(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    IenTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        labelOption = labelOption,
        placeholder = placeholder,
        help = help,
        hasError = hasError,
        variant = variant,
        prefix = prefix,
        suffix = suffix,
        state = state,
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

@Composable
fun IenPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    labelOption: IenTextFieldLabelOption = IenTextFieldLabelOption.Appear,
    placeholder: String? = null,
    help: String? = null,
    hasError: Boolean = false,
    variant: IenTextFieldVariant = IenTextFieldVariant.Box,
    state: IenTextFieldState = IenTextFieldState(),
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
        labelOption = labelOption,
        placeholder = placeholder,
        help = help,
        hasError = hasError,
        variant = variant,
        state = state,
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

@Composable
fun IenTextFieldButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    value: String? = null,
    label: String? = null,
    labelOption: IenTextFieldLabelOption = IenTextFieldLabelOption.Appear,
    placeholder: String? = null,
    help: String? = null,
    variant: IenTextFieldVariant = IenTextFieldVariant.Box,
    prefix: String? = null,
    suffix: String? = null,
    enabled: Boolean = true,
    right: (@Composable () -> Unit)? = { IenTextFieldArrowDown() },
) {
    IenTextField(
        value = value.orEmpty(),
        onValueChange = {},
        modifier = modifier.clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        label = label,
        labelOption = labelOption,
        placeholder = placeholder,
        help = help,
        variant = variant,
        prefix = prefix,
        suffix = suffix,
        state = IenTextFieldState(enabled = enabled, readOnly = true),
        right = right,
    )
}

@Composable
private fun IenTextFieldContainer(
    variant: IenTextFieldVariant,
    enabled: Boolean,
    borderColor: Color,
    content: @Composable () -> Unit,
) {
    when (variant) {
        IenTextFieldVariant.Box,
        IenTextFieldVariant.Big,
        IenTextFieldVariant.Hero -> {
            IenSurface(
                modifier = Modifier.fillMaxWidth(),
                color = if (enabled) IenTheme.colors.surface else IenTheme.colors.surfaceWeak,
                border = BorderStroke(IenTheme.stroke.thin, borderColor),
            ) {
                content()
            }
        }
        IenTextFieldVariant.Line -> {
            Column(modifier = Modifier.fillMaxWidth()) {
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

@Composable
private fun IenTextFieldClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .size(28.dp)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = "입력값 지우기" },
    ) {
        val color = Color(0xFF8B95A1)
        val radius = size.minDimension / 2.4f
        drawCircle(color = Color(0xFFE5E8EB), radius = radius, center = center)
        val arm = radius * 0.36f
        drawLine(
            color = color,
            start = Offset(center.x - arm, center.y - arm),
            end = Offset(center.x + arm, center.y + arm),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(center.x + arm, center.y - arm),
            end = Offset(center.x - arm, center.y + arm),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun IenTextFieldPasswordButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IenText(
        text = if (visible) "숨김" else "보기",
        modifier = modifier
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = IenTheme.spacing.xs, vertical = IenTheme.spacing.xxs)
            .semantics { contentDescription = if (visible) "비밀번호 숨기기" else "비밀번호 보기" },
        style = IenTheme.typography.label1,
        color = IenTheme.colors.brand,
    )
}

@Composable
private fun IenTextFieldArrowDown(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.size(24.dp)) {
        val stroke = 2.dp.toPx()
        val color = Color(0xFF6B7684)
        drawLine(
            color = color,
            start = Offset(size.width * 0.28f, size.height * 0.42f),
            end = Offset(size.width * 0.5f, size.height * 0.62f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.72f, size.height * 0.42f),
            end = Offset(size.width * 0.5f, size.height * 0.62f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
    }
}

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
    BasicTextField(
        value = value.take(length),
        onValueChange = { next ->
            if (next.length <= length) onValueChange(next)
        },
        modifier = modifier.semantics {
            contentDescription = "분할 입력 ${value.length} / $length"
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

@Composable
fun IenSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "검색",
    state: IenTextFieldState = IenTextFieldState(),
    contentDescription: String = "검색어 입력",
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
    val textStyle = IenTheme.typography.body2
    val textColor = if (state.enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled
    val placeholderColor = if (state.enabled) IenTheme.colors.textTertiary else IenTheme.colors.textDisabled

    IenSurface(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        color = searchFieldContainerColor(),
        shape = RoundedCornerShape(IenTheme.radius.lg),
    ) {
        Row(
            modifier = Modifier.padding(start = 14.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leading?.invoke()
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
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
                            .height(24.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (value.isEmpty()) {
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

@Composable
fun IenSearchFieldSearchIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 18.dp,
) {
    val color = searchFieldIconColor()
    Canvas(
        modifier = modifier
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
            }
            .width(size)
            .height(size),
    ) {
        val strokeWidth = size.toPx() * 0.11f
        val radius = size.toPx() * 0.30f
        val center = Offset(size.toPx() * 0.43f, size.toPx() * 0.43f)
        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth),
        )
        drawLine(
            color = color,
            start = Offset(size.toPx() * 0.65f, size.toPx() * 0.65f),
            end = Offset(size.toPx() * 0.86f, size.toPx() * 0.86f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
fun IenSearchFieldDeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "검색어 삭제",
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
            shape = RoundedCornerShape(IenTheme.radius.full),
        ) {
            Canvas(modifier = Modifier.size(18.dp)) {
                val strokeWidth = size.minDimension * 0.10f
                drawLine(
                    color = contentColor,
                    start = Offset(size.width * 0.35f, size.height * 0.35f),
                    end = Offset(size.width * 0.65f, size.height * 0.65f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = contentColor,
                    start = Offset(size.width * 0.65f, size.height * 0.35f),
                    end = Offset(size.width * 0.35f, size.height * 0.65f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
            }
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
