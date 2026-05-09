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


/**
 * A Composable function that creates a text preference item with a clickable link.
 * 
 * This preference item displays a title and optional summary text. When clicked,
 * it triggers the onClick callback, making it useful for opening dialogs or navigation.
 * 
 * @param onClick Callback function that is triggered when the item is clicked
 * @param modifier Modifier to be applied to the layout
 * @param enabled Whether the preference is enabled or disabled
 * @param leadingIcon Optional icon to display before the title
 * @param onClickLabel Optional accessibility label for the click action
 * @param indication Optional visual indication for the interaction
 * @param interactionSource Optional source for interaction tracking
 * @param summary Optional summary text to display
 * @param chevron Composable that displays a chevron icon at the end
 * @param adaptation Adaptation configuration for different platforms (iOS/Android)
 * @param title The title text for this preference item
 */
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

/**
 * A Composable function that creates a text preference item with custom content.
 * 
 * This variant is used to display simple text items without interaction. It's useful
 * for displaying static information or group headers within preferences.
 * 
 * @param modifier Modifier to be applied to the layout
 * @param leadingContent Optional content to display before the title
 * @param trailingContent Optional content to display after the title
 * @param summary Optional summary text to display
 * @param adaptation Adaptation configuration for different platforms (iOS/Android)
 * @param title The title text for this preference item
 */
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