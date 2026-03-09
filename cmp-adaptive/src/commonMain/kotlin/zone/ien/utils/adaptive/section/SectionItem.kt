package zone.ien.utils.adaptive.section

import androidx.annotation.IntRange
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import com.slapps.cupertino.CupertinoCheckBox
import com.slapps.cupertino.CupertinoSlider
import com.slapps.cupertino.CupertinoSwitch
import com.slapps.cupertino.CupertinoTextField
import com.slapps.cupertino.CupertinoTextFieldColors
import com.slapps.cupertino.CupertinoTextFieldDefaults
import com.slapps.cupertino.ExperimentalCupertinoApi
import com.slapps.cupertino.ProvideTextStyle
import com.slapps.cupertino.adaptive.Adaptation
import com.slapps.cupertino.adaptive.AdaptationScope
import com.slapps.cupertino.adaptive.AdaptiveWidget
import com.slapps.cupertino.adaptive.ExperimentalAdaptiveApi
import com.slapps.cupertino.adaptive.Theme
import com.slapps.cupertino.adaptive.currentTheme
import com.slapps.cupertino.section.CupertinoSectionDefaults
import com.slapps.cupertino.section.SectionItem
import com.slapps.cupertino.section.SectionLink
import com.slapps.cupertino.section.SectionScope
import com.slapps.cupertino.theme.CupertinoColors
import com.slapps.cupertino.theme.CupertinoTheme
import com.slapps.cupertino.theme.systemBlue
import com.slapps.cupertino.theme.systemGray
import com.slapps.cupertino.theme.systemRed
import zone.ien.hig.CupertinoCheckBox
import zone.ien.hig.CupertinoSwitch
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.currentTheme
import zone.ien.hig.section.CupertinoSectionDefaults
import zone.ien.hig.section.SectionItem
import zone.ien.hig.section.SectionLink
import zone.ien.hig.section.SectionScope
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.utils.ui.section.M3SectionCheckboxItem
import zone.ien.utils.ui.section.M3SectionItem
import zone.ien.utils.ui.section.M3SectionLink
import zone.ien.utils.ui.section.M3SectionSwitchItem
import zone.ien.utils.ui.utils.AsteriskTextWrapper
import zone.ien.utils.ui.view.adaptive.AdaptiveIcon
import zone.ien.utils.ui.view.adaptive.button.AdaptiveTextFieldClearButton
import zone.ien.utils.ui.view.cupertino.section.SectionSecureTextField
import zone.ien.utils.ui.view.cupertino.section.SectionTextField
import zone.ien.utils.ui.view.m3.section.M3SectionButton
import zone.ien.utils.ui.view.m3.section.M3SectionCheckboxItem
import zone.ien.utils.ui.view.m3.section.M3SectionItem
import zone.ien.utils.ui.view.m3.section.M3SectionLink
import zone.ien.utils.ui.view.m3.section.M3SectionLinkButton
import zone.ien.utils.ui.view.m3.section.M3SectionSecureTextField
import zone.ien.utils.ui.view.m3.section.M3SectionSlider
import zone.ien.utils.ui.view.m3.section.M3SectionSwitchItem
import zone.ien.utils.ui.view.m3.section.M3SectionTextField

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.AdaptiveSectionItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    adaptation: AdaptationScope<HigSectionItemAdaptation, M3SectionItemAdaptation>.() -> Unit = {},
    title: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { SectionItemAdaptation() },
        adaptationScope = adaptation,
        material = {
            M3SectionItem(
                modifier = modifier,
                enabled = enabled,
                leadingContent = leadingContent,
                trailingContent = trailingContent,
                supportingContent = supportingContent,
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

class M3SectionItemAdaptation internal constructor()
class HigSectionItemAdaptation internal constructor(
    showLeadingContent: Boolean = false,
    showSupportingContent: Boolean = false,
    paddingValues: PaddingValues
) {
    var showLeadingContent by mutableStateOf(showLeadingContent)
    var showSupportingContent by mutableStateOf(showSupportingContent)
    var paddingValues by mutableStateOf(paddingValues)
}

@OptIn(ExperimentalAdaptiveApi::class)
internal class SectionItemAdaptation: Adaptation<HigSectionItemAdaptation, M3SectionItemAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigSectionItemAdaptation {
        val showLeadingContent = false
        val showSupportingContent = false
        val paddingValues = CupertinoSectionDefaults.PaddingValues

        return remember(showLeadingContent, showSupportingContent, paddingValues) {
            HigSectionItemAdaptation(
                showLeadingContent = showLeadingContent,
                showSupportingContent = showSupportingContent,
                paddingValues = paddingValues
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): M3SectionItemAdaptation {
        return remember { M3SectionItemAdaptation() }
    }
}

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
    isIosCaption: Boolean = true,
    showIosSupporting: Boolean = false,
    trailingContent: @Composable (() -> Unit)? = if (currentTheme == Theme.Material3) null else { { CupertinoSectionDefaults.LabelChevron() }
    },
    title: @Composable () -> Unit,
) {
    AdaptiveWidget(
        material = {
            M3SectionLink(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                leadingIcon = leadingIcon,
                onClickLabel = onClickLabel,
                indication = indication,
                interactionSource = interactionSource,
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
                caption = { if (isIosCaption) caption?.invoke() },
                chevron = { trailingContent?.invoke() },
                title = {
                    Column {
                        title()
                        if (showIosSupporting && !isIosCaption) {
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

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.AdaptiveSectionSwitchItem(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = CupertinoSectionDefaults.PaddingValues,
    leadingContent: (@Composable () -> Unit)? = null,
    enableCupertinoLeadingContent: Boolean = false,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit
) {
    AdaptiveWidget(
        material = {
            M3SectionSwitchItem(
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
                modifier,
                paddingValues,
                if (enableCupertinoLeadingContent) leadingContent ?: {} else {{}},
                trailingContent = {
                    CupertinoSwitch(
                        checked = checked,
                        onCheckedChange = onCheckedChange,
                        enabled = enabled,
                    )
                },
                title
            )
        }
    )
}

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.AdaptiveSectionCheckboxItem(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = CupertinoSectionDefaults.PaddingValues,
    leadingContent: (@Composable () -> Unit)? = null,
    enableCupertinoLeadingContent: Boolean = false,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit
) {
    AdaptiveWidget(
        material = {
            M3SectionCheckboxItem(
                modifier = modifier,
                leadingContent = leadingContent,
                checked = checked,
                onCheckedChange = onCheckedChange,
                supportingContent = supportingContent,
                title = title
            )
        },
        cupertino = {
            SectionItem(
                modifier,
                paddingValues,
                if (enableCupertinoLeadingContent) leadingContent ?: {} else {{}},
                trailingContent = {
                    CupertinoCheckBox(
                        checked = checked,
                        onCheckedChange = onCheckedChange
                    )
                },
                title
            )
        }
    )
}

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
    materialColors: TextFieldColors = TextFieldDefaults.colors(),
    cupertinoColors: CupertinoTextFieldColors? = null,
) {
    AdaptiveWidget(
        material = {
            M3SectionTextField(
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
                colors = materialColors
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
                    placeholder = placeholder?.let { { if (isRequired) AsteriskTextWrapper { it() } else it() } },
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    interactionSource = interactionSource,
                    colors = cupertinoColors
                )
            }

        }
    )
}

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
    materialColors: TextFieldColors = TextFieldDefaults.colors(),
    textObfuscationMode: TextObfuscationMode = TextObfuscationMode.RevealLastTyped,
    textObfuscationCharacter: Char = '\u2022',
) {
    AdaptiveWidget(
        material = {
            M3SectionSecureTextField(
                state = state,
                modifier = modifier,
                enabled = enabled,
                textStyle = textStyle,
                placeholder = placeholder,
                isRequired = isRequired,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                isError = isError,
                keyboardOptions = keyboardOptions,
                interactionSource = interactionSource,
                colors = materialColors,
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
                    textObfuscationMode = textObfuscationMode,
                    textObfuscationCharacter = textObfuscationCharacter,
                )
            }

        }
    )
}

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
    materialColors: TextFieldColors = TextFieldDefaults.colors(),
    cupertinoColors: CupertinoTextFieldColors? = null,
) {
    AdaptiveWidget(
        material = {
            M3SectionTextField(
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
                colors = materialColors
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
                    colors = cupertinoColors
                )
            }

        }
    )
}

@ExperimentalCupertinoApi
@Composable
private fun SectionScope.SectionTextField(
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
                val actualInteractionSource =
                    interactionSource ?: remember {
                        MutableInteractionSource()
                    }

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
                    placeholder = placeholder?.let { { if (isRequired) AsteriskTextWrapper { it() } else it() } },
                    interactionSource = actualInteractionSource,
                    trailingIcon =
                        trailingIcon?.let {
                            { it(actualInteractionSource) }
                        },
                )
            }
        }
    },
)

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class,)
@Composable
fun SectionScope.AdaptiveSectionLinkButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    materialIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    materialColors: ListItemColors = ListItemDefaults.colors(
        headlineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
        leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
        supportingColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
        overlineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
        trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
    ),
    label: @Composable () -> Unit,
) {
    AdaptiveWidget(
        material = {
            M3SectionLinkButton(
                modifier = modifier,
                onClick = onClick,
                icon = materialIcon,
                enabled = enabled,
                colors = materialColors,
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

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class,)
@Composable
fun SectionScope.AdaptiveSectionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    materialIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    materialColors: ButtonColors = ButtonDefaults.buttonColors(),
    label: @Composable () -> Unit,
) {
    AdaptiveWidget(
        material = {
            M3SectionButton(
                modifier = modifier,
                onClick = onClick,
                icon = materialIcon,
                enabled = enabled,
                colors = materialColors,
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

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun SectionScope.AdaptiveSectionSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0) steps: Int = 0,
    adaptation: AdaptationScope<CupertinoSectionSliderAdaptation, MaterialSectionSliderAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { SectionSliderAdaptation() },
        material = {
            M3SectionSlider(
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
            SectionItem(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it.startIcon?.let {
                            AdaptiveIcon(
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
                            modifier = Modifier.weight(1f)
                        )
                        it.endIcon?.let {
                            AdaptiveIcon(
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
                    LinearProgressIndicator()
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

class CupertinoSectionSliderAdaptation(
    startIcon: ImageVector?,
    endIcon: ImageVector?,
) {
    var startIcon: ImageVector? by mutableStateOf(startIcon)
    var endIcon: ImageVector? by mutableStateOf(endIcon)
}

class MaterialSectionSliderAdaptation(
    title: String? = null,
    icon: ImageVector?
) {
    var title: String? by mutableStateOf(title)
    var icon: ImageVector? by mutableStateOf(icon)
}

@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private class SectionSliderAdaptation: Adaptation<CupertinoSectionSliderAdaptation, MaterialSectionSliderAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): CupertinoSectionSliderAdaptation {
        val startIcon: ImageVector? = null
        val endIcon: ImageVector? = null

        return remember(startIcon, endIcon) {
            CupertinoSectionSliderAdaptation(
                startIcon, endIcon
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): MaterialSectionSliderAdaptation {
        val title: String? = null
        val icon: ImageVector? = null

        return remember(title, icon) {
            MaterialSectionSliderAdaptation(
                title, icon
            )
        }
    }
}