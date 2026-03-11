package zone.ien.utils.example.ui.screens.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.section.SectionStyle
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.section.AdaptiveProvideSectionStyle
import zone.ien.utils.adaptive.section.AdaptiveSection
import zone.ien.utils.adaptive.section.AdaptiveSectionItem
import zone.ien.utils.adaptive.section.AdaptiveSectionSwitchItem
import zone.ien.utils.adaptive.theme.GeneratedAdaptiveTheme

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val backdrop = rememberDefaultBackdrop()
    val scrollState = rememberScrollState()

    var isMaterialTheme by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    GeneratedAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            navigationIcon = { AdaptiveBackButton(backdrop = backdrop) { navigateBack() } },
            title = { Text(text = "Title") },
            modifier = modifier
        ) { pv, title ->
            AdaptiveProvideSectionStyle(
                style = SectionStyle.InsetGrouped,
                scrollState = scrollState,
                backdrop = backdrop,
                modifier = Modifier.padding(pv)
            ) {
                title()
                AdaptiveSection {
                    AdaptiveSectionItem {
                        Text(text = "Section1")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section2")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section3")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section4")
                    }
                    AdaptiveSectionSwitchItem(
                        title = { Text(text = "Check") },
                        checked = isMaterialTheme,
                        onCheckedChange = { isMaterialTheme = it }
                    )
                }
                AdaptiveSection(
                    title = { Text(text = "Section Title") },
                    caption = { Text(text = "Section Caption") }
                ){
                    AdaptiveSectionItem {
                        Text(text = "Section1")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section2")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section3")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section4")
                    }
                    AdaptiveSectionSwitchItem(
                        title = { Text(text = "Check") },
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                }
                AdaptiveSection(
                    title = { Text(text = "Section Title") },
                    caption = { Text(text = "Section Caption") }
                ){
                    AdaptiveSectionItem {
                        Text(text = "Section1")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section2")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section3")
                    }
                    AdaptiveSectionItem {
                        Text(text = "Section4")
                    }
                    AdaptiveSectionSwitchItem(
                        title = { Text(text = "Check") },
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                }
            }
        }
    }
}