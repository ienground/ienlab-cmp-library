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



