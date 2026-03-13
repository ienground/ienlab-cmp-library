package zone.ien.utils.example.ui.screens.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.section.SectionStyle
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.section.AdaptiveProvideSectionStyle
import zone.ien.utils.adaptive.section.AdaptiveSection
import zone.ien.utils.adaptive.section.AdaptiveSectionItem
import zone.ien.utils.adaptive.section.AdaptiveSectionLink
import zone.ien.utils.adaptive.section.AdaptiveSectionSwitchItem
import zone.ien.utils.adaptive.theme.GeneratedAdaptiveTheme
import zone.ien.utils.ui.section.M3ProvideSectionStyle

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
            adaptation = { cupertino { this.backdrop = backdrop } },
            modifier = modifier
        ) { pv, title ->
            Column(
                modifier = Modifier
                    .layerBackdrop(backdrop)
                    .verticalScroll(rememberScrollState())
                    .padding(pv)
            ) {
                Text(
                    text = "Hello World",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Red)
                )
//                M3ProvideSectionStyle(
                AdaptiveProvideSectionStyle(
                style = SectionStyle.InsetGrouped,
                scrollState = null,
                fullHeight = true,
//                scrollState = scrollState,
                    modifier = Modifier
                ) {
                    title()
                    AdaptiveSection {
                        AdaptiveSectionItem {
                            Text(text = "Section1")
                        }
                        AdaptiveSectionItem {
                            Text(text = "Section2")
                        }
                        AdaptiveSectionLink(
                            onClick = {}
                        ) {
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
                    /*
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

                     */
                }
                Text(
                    text = "Hello World",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Red)
                )
            }
        }
    }
}