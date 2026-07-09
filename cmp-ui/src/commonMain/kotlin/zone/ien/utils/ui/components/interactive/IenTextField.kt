package zone.ien.utils.ui.components.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
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

@Composable
fun IenTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
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
) {
    val focused by interactionSource.collectIsFocusedAsState()
    val borderColor = when (state.status) {
        is IenFieldStatus.Error -> IenTheme.colors.danger
        is IenFieldStatus.Success -> IenTheme.colors.success
        IenFieldStatus.Normal -> if (focused) IenTheme.colors.brand else IenTheme.colors.border
    }
    val supporting = when (val status = state.status) {
        is IenFieldStatus.Error -> status.message
        is IenFieldStatus.Success -> status.message ?: supportingText
        IenFieldStatus.Normal -> supportingText
    }
    val supportingColor = when (state.status) {
        is IenFieldStatus.Error -> IenTheme.colors.danger
        is IenFieldStatus.Success -> IenTheme.colors.success
        IenFieldStatus.Normal -> IenTheme.colors.textTertiary
    }
    val textColor = if (state.enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled

    Column(modifier = modifier.semantics {
        if (state.status is IenFieldStatus.Error) error(state.status.message)
    }) {
        if (label != null) {
            IenText(text = label, style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
            Spacer(Modifier.height(IenTheme.spacing.xxs))
        }
        IenSurface(
            modifier = Modifier.fillMaxWidth(),
            color = if (state.enabled) IenTheme.colors.surface else IenTheme.colors.surfaceWeak,
            border = BorderStroke(IenTheme.stroke.thin, borderColor),
            contentColor = textColor,
        ) {
            Row(
                modifier = Modifier.padding(PaddingValues(horizontal = 14.dp, vertical = 12.dp)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leading?.invoke()
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = if (leading != null || trailing != null) IenTheme.spacing.xs else 0.dp),
                    enabled = state.enabled,
                    readOnly = state.readOnly,
                    singleLine = singleLine,
                    minLines = minLines,
                    maxLines = maxLines,
                    textStyle = IenTheme.typography.body1.copy(color = textColor),
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    cursorBrush = SolidColor(IenTheme.colors.brand),
                    decorationBox = { innerTextField ->
                        if (value.isEmpty() && placeholder != null) {
                            IenText(
                                text = placeholder,
                                style = IenTheme.typography.body1,
                                color = IenTheme.colors.textTertiary,
                            )
                        }
                        innerTextField()
                    },
                )
                trailing?.invoke()
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
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    IenTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        placeholder = placeholder,
        state = state,
        leading = leading,
        trailing = trailing,
        singleLine = true,
    )
}
