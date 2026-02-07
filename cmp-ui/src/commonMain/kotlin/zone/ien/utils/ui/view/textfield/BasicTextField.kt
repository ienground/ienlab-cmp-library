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
import androidx.compose.material3.MaterialTheme
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
import zone.ien.utils.ui.view.M3AsteriskTextWrapper

@Composable
fun PlaceholderBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
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
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSurface),
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

@Composable
fun PlaceholderBasicTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
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
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSurface),
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

@Composable
fun PlaceholderBasicSecureTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
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