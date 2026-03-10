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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import zone.ien.utils.ui.section.M3SectionButton
import zone.ien.utils.ui.section.M3SectionCheckboxItem
import zone.ien.utils.ui.section.M3SectionColors
import zone.ien.utils.ui.section.M3SectionItem
import zone.ien.utils.ui.section.M3SectionLink
import zone.ien.utils.ui.section.M3SectionLinkDefault
import zone.ien.utils.ui.section.M3SectionSecureTextField
import zone.ien.utils.ui.section.M3SectionSlider
import zone.ien.utils.ui.section.M3SectionSwitchItem
import zone.ien.utils.ui.section.M3SectionTextField

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

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.AdaptiveSectionSwitchItem(
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    adaptation: AdaptationScope<HigSectionItemAdaptation, M3SectionItemAdaptation>.() -> Unit = {},
    title: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { SectionItemAdaptation() },
        adaptationScope = adaptation,
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

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.AdaptiveSectionCheckboxItem(
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    adaptation: AdaptationScope<HigSectionItemAdaptation, M3SectionItemAdaptation>.() -> Unit = {},
    title: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { SectionItemAdaptation() },
        adaptationScope = adaptation,
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
                modifier = modifier,
                paddingValues = it.paddingValues,
                leadingContent = if (it.showLeadingContent && leadingContent != null) leadingContent else {{}},
                trailingContent = {
                    CupertinoCheckBox(
                        checked = checked,
                        onCheckedChange = onCheckedChange
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
    adaptation: AdaptationScope<HigSectionTextFieldAdaptation, M3SectionTextFieldAdaptation>.() -> Unit = {},
) {
    AdaptiveWidget(
        adaptation = remember { SectionTextFieldAdaptation() },
        adaptationScope = adaptation,
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
                colors = it.colors
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
    adaptation: AdaptationScope<HigSectionTextFieldAdaptation, M3SectionTextFieldAdaptation>.() -> Unit = {},
) {
    AdaptiveWidget(
        adaptation = remember { SectionTextFieldAdaptation() },
        adaptationScope = adaptation,
        material = {
            M3SectionSecureTextField(
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
                colors = it.colors,
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
    adaptation: AdaptationScope<HigSectionTextFieldAdaptation, M3SectionTextFieldAdaptation>.() -> Unit = {},
) {
    AdaptiveWidget(
        adaptation = remember { SectionTextFieldAdaptation() },
        adaptationScope = adaptation,
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
                colors = it.colors
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

class M3SectionTextFieldAdaptation internal constructor(
    colors: TextFieldColors
) {
    var colors by mutableStateOf(colors)
}
class HigSectionTextFieldAdaptation internal constructor(
    colors: CupertinoTextFieldColors
) {
    var colors by mutableStateOf(colors)
}
@OptIn(ExperimentalAdaptiveApi::class)
internal class SectionTextFieldAdaptation: Adaptation<HigSectionTextFieldAdaptation, M3SectionTextFieldAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigSectionTextFieldAdaptation {
        val colors = CupertinoTextFieldDefaults.colors()

        return remember(colors) { HigSectionTextFieldAdaptation(colors = colors) }
    }

    @Composable
    override fun rememberMaterialAdaptation(): M3SectionTextFieldAdaptation {
        val colors = TextFieldDefaults.colors()

        return remember(colors) { M3SectionTextFieldAdaptation(colors = colors) }
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
    trailingContent: @Composable (() -> Unit)? = if (currentTheme == Theme.Material3) null else { { CupertinoSectionDefaults.LabelChevron() } },
    adaptation: AdaptationScope<HigSectionLinkAdaptation, M3SectionLinkAdaptation>.() -> Unit = {},
    title: @Composable () -> Unit,
) {
    AdaptiveWidget(
        adaptation = remember { SectionLinkAdaptation() },
        adaptationScope = adaptation,
        material = {
            M3SectionLink(
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

class M3SectionLinkAdaptation internal constructor(
    colors: M3SectionColors
) {
    var colors by mutableStateOf(colors)
}
class HigSectionLinkAdaptation internal constructor(
    isCaption: Boolean = true,
    showSupportingContent: Boolean = false
) {
    var isCaption by mutableStateOf(isCaption)
    var showSupportingContent by mutableStateOf(showSupportingContent)
}
@OptIn(ExperimentalAdaptiveApi::class)
internal class SectionLinkAdaptation: Adaptation<HigSectionLinkAdaptation, M3SectionLinkAdaptation>() {
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

    @Composable
    override fun rememberMaterialAdaptation(): M3SectionLinkAdaptation {
        val colors = M3SectionLinkDefault.colors()
        return remember(colors) { M3SectionLinkAdaptation(colors = colors) }
    }
}

@OptIn(ExperimentalCupertinoApi::class, ExperimentalAdaptiveApi::class,)
@Composable
fun SectionScope.AdaptiveSectionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
    adaptation: AdaptationScope<HigSectionButtonAdaptation, M3SectionButtonAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { SectionButtonAdaptation() },
        adaptationScope = adaptation,
        material = {
            M3SectionButton(
                modifier = modifier,
                onClick = onClick,
                icon = it.icon,
                enabled = enabled,
                colors = it.colors,
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

class M3SectionButtonAdaptation internal constructor(
    icon: @Composable (() -> Unit)? = null,
    colors: ButtonColors
) {
    var icon by mutableStateOf(icon)
    var colors by mutableStateOf(colors)
}
class HigSectionButtonAdaptation internal constructor()
@OptIn(ExperimentalAdaptiveApi::class)
internal class SectionButtonAdaptation: Adaptation<HigSectionButtonAdaptation, M3SectionButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigSectionButtonAdaptation {
        return remember { HigSectionButtonAdaptation() }
    }

    @Composable
    override fun rememberMaterialAdaptation(): M3SectionButtonAdaptation {
        val icon: @Composable (() -> Unit)? = null
        val colors = ButtonDefaults.buttonColors()

        return remember(icon, colors) {
            M3SectionButtonAdaptation(
                icon = icon,
                colors = colors
            )
        }
    }
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
    adaptation: AdaptationScope<HigSectionSliderAdaptation, M3SectionSliderAdaptation>.() -> Unit = {}
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

class HigSectionSliderAdaptation(
    startIcon: ImageVector?,
    endIcon: ImageVector?,
) {
    var startIcon: ImageVector? by mutableStateOf(startIcon)
    var endIcon: ImageVector? by mutableStateOf(endIcon)
}
class M3SectionSliderAdaptation(
    title: String? = null,
    icon: ImageVector?
) {
    var title: String? by mutableStateOf(title)
    var icon: ImageVector? by mutableStateOf(icon)
}
@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private class SectionSliderAdaptation: Adaptation<HigSectionSliderAdaptation, M3SectionSliderAdaptation>() {
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

    @Composable
    override fun rememberMaterialAdaptation(): M3SectionSliderAdaptation {
        val title: String? = null
        val icon: ImageVector? = null

        return remember(title, icon) {
            M3SectionSliderAdaptation(
                title, icon
            )
        }
    }
}