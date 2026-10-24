package zone.ien.utils.pref.item

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.Preferences
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.HigSectionLinkAdaptation
import zone.ien.utils.adaptive.section.IenSectionLinkAdaptation
import zone.ien.utils.ui.section.IenSectionColors
import zone.ien.utils.ui.section.IenSectionLinkDefault

/** 기존 DateSelectPref 호출을 유지하며 DatePickerPref를 사용합니다. */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SectionScope.DateSelectPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable (Long) -> String,
    key: Preferences.Key<Long>,
    defaultValue: Long,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    colors: IenSectionColors = IenSectionLinkDefault.colors(),
    trailingContent: @Composable () -> Unit = {},
    adaptation: AdaptationScope<HigSectionLinkAdaptation, IenSectionLinkAdaptation>.() -> Unit = {},
    dialogModifier: Modifier = Modifier,
    yearRange: IntRange = DatePickerDefaults.YearRange,
) {
    DatePickerPref(
        modifier = modifier,
        title = title,
        summary = summary,
        key = key,
        defaultValue = defaultValue,
        enabled = enabled,
        leadingContent = leadingContent,
        showIcon = showIcon,
        onClickLabel = onClickLabel,
        indication = indication,
        interactionSource = interactionSource,
        colors = colors,
        trailingContent = trailingContent,
        adaptation = adaptation,
        dialogModifier = dialogModifier,
        yearRange = yearRange,
    )
}

/** 기존 DateSelectPref 호출을 유지하며 DatePickerPref를 사용합니다. */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SectionScope.DateSelectPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable ((Long) -> String),
    key: Preferences.Key<Long>,
    defaultValue: Long,
    enabled: Pair<Preferences.Key<Boolean>, Boolean>,
    leadingContent: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    colors: IenSectionColors = IenSectionLinkDefault.colors(),
    trailingContent: @Composable () -> Unit = {},
    adaptation: AdaptationScope<HigSectionLinkAdaptation, IenSectionLinkAdaptation>.() -> Unit = {},
    dialogModifier: Modifier = Modifier,
    yearRange: IntRange = DatePickerDefaults.YearRange,
) {
    DatePickerPref(
        modifier = modifier,
        title = title,
        summary = summary,
        key = key,
        defaultValue = defaultValue,
        enabled = enabled,
        leadingContent = leadingContent,
        showIcon = showIcon,
        onClickLabel = onClickLabel,
        indication = indication,
        interactionSource = interactionSource,
        colors = colors,
        trailingContent = trailingContent,
        adaptation = adaptation,
        dialogModifier = dialogModifier,
        yearRange = yearRange,
    )
}
