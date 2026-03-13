package zone.ien.utils.example.ui.screens.lazy

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import zone.ien.hig.adaptive.AdaptiveSwitch
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.section.SectionStyle
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.section.sectionBackground
import zone.ien.utils.adaptive.theme.GeneratedAdaptiveTheme
import zone.ien.utils.example.currentTheme
import zone.ien.utils.example.isIos
import zone.ien.utils.ui.section.lazy.empty
import zone.ien.utils.ui.section.lazy.link
import zone.ien.utils.ui.section.lazy.m3Section
import zone.ien.utils.ui.section.lazy.switch
import zone.ien.utils.ui.section.m3SectionBackground

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun LazySectionScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val backdrop = rememberDefaultBackdrop()
    val lazyListState = rememberLazyListState()
//    val isMaterialTheme = currentTheme == Theme.Material3
    var isMaterialTheme by remember { mutableStateOf(!isIos) }
    var enabled by remember { mutableStateOf(false) }

    GeneratedAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            navigationIcon = { AdaptiveBackButton(backdrop = backdrop) { navigateBack() } },
            title = {
                Text(text = "Title")
            },
            actions = {
                AdaptiveSwitch(
                    checked = isMaterialTheme,
                    onCheckedChange = { isMaterialTheme = it }
                )
            },
            adaptation = {
                cupertino {
                    this.scrollableState = lazyListState
                    this.showNavTitle = true
                }
            },
            modifier = modifier
        ) { pv, title ->
            LazyColumn(
                state = lazyListState,
                contentPadding = pv,
                modifier = Modifier
                    .m3SectionBackground()
//                    .sectionBackground(sectionStyle = SectionStyle.InsetGrouped)
                    .fillMaxSize()
            ) {
                item {
                    title()
                }
//                /*
                m3Section(
//                    isMaterialTheme = isMaterialTheme,
                    title = { Text(text = "title") }
                ) {
                    empty {
                        Text(text = "empty")
                    }
                    link(
                        onClick = {},
                        title = { Text(text = "hello") }
                    )
                    switch(
                        checked = enabled,
                        onCheckedChange = { enabled = it },
                        title = { Text(text = "switch") },
                        supportingContent = { Text(text = "supporting") }
                    )
                }
//
//                 */
            }
        }
    }
}