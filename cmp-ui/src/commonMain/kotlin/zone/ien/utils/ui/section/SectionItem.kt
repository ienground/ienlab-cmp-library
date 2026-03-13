package zone.ien.utils.ui.section

import androidx.annotation.IntRange
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import zone.ien.hig.section.SectionScope
import zone.ien.utils.ui.view.M3AsteriskTextWrapper
import zone.ien.utils.ui.view.textfield.M3TextFieldClearButton
import zone.ien.utils.ui.view.textfield.PlaceholderBasicSecureTextField
import zone.ien.utils.ui.view.textfield.PlaceholderBasicTextField

@Composable
fun SectionScope.M3SectionItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(
        headlineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
        leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
        supportingColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
        overlineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
        trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (enabled) 1f else 0.35f),
    ),
    title: @Composable () -> Unit
) {
    ListItem(
        headlineContent = title,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        colors = colors,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .then(modifier)
    )
}

@Composable
fun SectionScope.M3SectionSwitchItem(
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    supportingContent: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit
) {
    M3SectionItem(
        leadingContent = leadingContent,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
            )
        },
        supportingContent = supportingContent,
        title = title,
        modifier = modifier.clickable { onCheckedChange(!checked) }
    )
}

@Composable
fun SectionScope.M3SectionCheckboxItem(
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit
) {
    M3SectionItem(
        leadingContent = leadingContent,
        trailingContent = {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
        supportingContent = supportingContent,
        title = title,
        modifier = modifier.clickable { onCheckedChange(!checked) }
    )
}

@Composable
fun SectionScope.M3SectionTextField(
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

        M3TextFieldClearButton(
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
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    ProvideTextStyle(
        (textStyle ?: LocalTextStyle.current).copy(
            color =
                if (isError) colors.errorTextColor
                else {
                    val focused by interactionSource.collectIsFocusedAsState()

                    if (enabled) {
                        if (focused) colors.focusedTextColor
                        else colors.unfocusedTextColor
                    } else {
                        colors.disabledTextColor
                    }
                }
        )
    ) {
        M3SectionItem(
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
                        if (isError) colors.errorCursorColor
                        else colors.cursorColor
                    ),
                    modifier = modifier
                        .heightIn(min = 48.dp)
                        .fillMaxWidth()
                )
            },
        )
    }
}

@Composable
fun SectionScope.M3SectionTextField(
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

        M3TextFieldClearButton(
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
    colors: TextFieldColors = TextFieldDefaults.colors(),
) {
    ProvideTextStyle(
        (textStyle ?: LocalTextStyle.current).copy(
            color =
                if (isError) colors.errorTextColor
                else {
                    val focused by interactionSource.collectIsFocusedAsState()

                    if (enabled) {
                        if (focused) colors.focusedTextColor
                        else colors.unfocusedTextColor
                    } else {
                        colors.disabledTextColor
                    }
                }
        )
    ) {
        M3SectionItem(
            title = {
                PlaceholderBasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = modifier.heightIn(min = 48.dp),
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = (textStyle ?: LocalTextStyle.current),
                    placeholder = placeholder?.let { { if (isRequired) M3AsteriskTextWrapper { it() } else it() } },
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
                        if (isError) colors.errorCursorColor
                        else colors.cursorColor
                    )
                )
            },
        )
    }
}

@Composable
fun SectionScope.M3SectionSecureTextField(
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

        M3TextFieldClearButton(
            visible = focused && state.text.isNotEmpty(),
            onClick = {
                state.clearText()
            },
        )
    },
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.colors(),
    textObfuscationMode: TextObfuscationMode = TextObfuscationMode.RevealLastTyped,
    textObfuscationCharacter: Char = '\u2022',
) {
    ProvideTextStyle(
        (textStyle ?: LocalTextStyle.current).copy(
            color =
                if (isError) colors.errorTextColor
                else {
                    val focused by interactionSource.collectIsFocusedAsState()

                    if (enabled) {
                        if (focused) colors.focusedTextColor
                        else colors.unfocusedTextColor
                    } else {
                        colors.disabledTextColor
                    }
                }
        )
    ) {
        M3SectionItem(
            title = {
                PlaceholderBasicSecureTextField(
                    state = state,
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = (textStyle ?: LocalTextStyle.current),
                    placeholder = placeholder?.let { { if (isRequired) M3AsteriskTextWrapper { it() } else it() } },
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

@Composable
fun SectionScope.M3SectionLink(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    colors: M3SectionColors = M3SectionLinkDefault.colors(),
    caption: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
) {
    M3SectionItem(
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
        colors = colors.toListItemColors(enabled)
    )
}


@Immutable
class M3SectionColors(
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
        M3SectionColors(
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

object M3SectionLinkDefault {
    @Composable
    fun colors() = M3SectionColors(
        containerColor = MaterialTheme.colorScheme.surface,
        headlineColor = MaterialTheme.colorScheme.onSurface,
        leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        overlineColor = MaterialTheme.colorScheme.onSurfaceVariant,
        supportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledHeadlineColor = MaterialTheme.colorScheme.onSurface.copy(0.35f),
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.35f),
        disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.35f),
        disabledOverlineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.35f),
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.35f)
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SectionScope.M3SectionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    label: @Composable () -> Unit,
) {
    Button(
        onClick = onClick,
        shapes = ButtonDefaults.shapes(),
        enabled = enabled,
        colors = colors,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        modifier = modifier
    ) {
        icon?.let {
            it.invoke()
            Spacer(Modifier.width(8.dp))
        }
        label.invoke()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SectionScope.M3SectionSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0) steps: Int = 0,
    title: String? = null,
    icon: ImageVector? = null
) {
    M3SectionItem(
        title = { title?.let { Text(text = it) } },
        supportingContent = {
            Slider(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                valueRange = valueRange,
                steps = steps,
                thumb = { sliderState ->
                    SliderDefaults.Thumb(
                        interactionSource = remember { MutableInteractionSource() },
                        sliderState = sliderState,
                        enabled = enabled,
                        thumbSize = DpSize(4.dp, 52.dp)
                    )
                },
                track = { sliderState ->
                    val icon = icon?.let { rememberVectorPainter(it) }
                    val iconSize = DpSize(24.dp, 24.dp)
                    val iconPadding = 10.dp
                    val thumbTrackGapSize = 6.dp
                    val activeIconColor = SliderDefaults.colors().activeTickColor
                    val inactiveIconColor = SliderDefaults.colors().inactiveTickColor
                    val trackIcon: (DrawScope.(Offset, Color) -> Unit)? = icon?.let { { offset, color ->
                        translate(offset.x - iconPadding.toPx() - iconSize.toSize().width, offset.y) {
                            with (it) {
                                draw(iconSize.toSize(), colorFilter = ColorFilter.tint(color))
                            }
                        }
                    } }


                    SliderDefaults.Track(
                        enabled = enabled,
                        sliderState = sliderState,
                        trackCornerSize = 12.dp,
                        drawStopIndicator = {

                        },
                        modifier = Modifier
                            .height(40.dp)
                            .drawWithContent {
                                drawContent()

                                val yOffset = size.height / 2 - iconSize.toSize().height / 2
                                val activeTrackEnd =
                                    size.width * sliderState.coercedValueAsFraction -
                                            thumbTrackGapSize.toPx()
                                val inactiveTrackStart = activeTrackEnd + thumbTrackGapSize.toPx() * 2
                                val inactiveTrackEnd = size.width

                                val inactiveTrackWidth = inactiveTrackEnd - inactiveTrackStart

                                if (
                                    iconSize.toSize().width <
                                    inactiveTrackWidth - iconPadding.toPx() * 2
                                ) {
                                    trackIcon?.invoke(this, Offset(inactiveTrackEnd, yOffset), inactiveIconColor)
                                } else {
                                    trackIcon?.invoke(this, Offset(activeTrackEnd, yOffset), activeIconColor)
                                }
                            },
                    )
                },
            )
        },
        modifier = modifier
    )
}

@Composable
private fun SectionScope.AdaptiveSectionProgressBar(
    modifier: Modifier = Modifier
) {
    M3SectionItem(
        title = {},
        supportingContent = { LinearProgressIndicator() },
        modifier = modifier
    )
}