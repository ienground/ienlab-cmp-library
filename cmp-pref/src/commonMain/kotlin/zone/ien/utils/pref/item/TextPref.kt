package zone.ien.utils.pref.item

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.CupertinoSectionDefaults
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.AdaptiveSectionItem
import zone.ien.utils.adaptive.section.AdaptiveSectionLink
import zone.ien.utils.adaptive.section.HigSectionItemAdaptation
import zone.ien.utils.adaptive.section.HigSectionLinkAdaptation
import zone.ien.utils.adaptive.section.M3SectionItemAdaptation
import zone.ien.utils.adaptive.section.M3SectionLinkAdaptation


@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.TextPref(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    summary: String? = null,
    chevron: @Composable () -> Unit = {
        AdaptiveWidget(
            material = {},
            cupertino = { CupertinoSectionDefaults.LabelChevron() }
        )
    },
    adaptation: AdaptationScope<HigSectionLinkAdaptation, M3SectionLinkAdaptation>.() -> Unit = {},
    title: String,
) {
    AdaptiveSectionLink(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingIcon,
        onClickLabel = onClickLabel,
        indication = indication,
        interactionSource = interactionSource,
        caption = summary?.let { { Text(text = it) } },
        trailingContent = chevron,
        adaptation = adaptation,
        title = { Text(text = title) }
    )
}

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.TextPref(
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    summary: String? = null,
    adaptation: AdaptationScope<HigSectionItemAdaptation, M3SectionItemAdaptation>.() -> Unit = {},
    title: String,
) {
    AdaptiveSectionItem(
        modifier = modifier,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        supportingContent = summary?.let { { Text(text = it) } },
        adaptation = adaptation,
        title = { Text(text = title) }
    )
}