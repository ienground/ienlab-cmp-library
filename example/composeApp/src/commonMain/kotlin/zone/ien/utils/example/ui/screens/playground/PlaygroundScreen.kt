package zone.ien.utils.example.ui.screens.playground

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kyant.backdrop.backdrops.layerBackdrop
import com.revenuecat.placeholder.placeholder
import zone.ien.hig.adaptive.AdaptiveSwitch
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.GeneratedAdaptiveTheme
import zone.ien.utils.example.isIos
import zone.ien.utils.ui.shimmer.m3Placeholder

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun PlaygroundScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val backdrop = rememberDefaultBackdrop()
    var isMaterialTheme by remember { mutableStateOf(!isIos) }

    GeneratedAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            navigationIcon = { AdaptiveBackButton(backdrop = backdrop) { navigateBack() } },
            title = { Text(text = "Playground") },
            subtitle = { Text(text = "IENGROUND") },
            actions = {
                AdaptiveSwitch(
                    checked = isMaterialTheme,
                    onCheckedChange = { isMaterialTheme = it },
                    adaptation = {
                        cupertino {
                            this.backdrop = backdrop
                        }
                    }
                )
            },
            adaptation = {
                material {

                }
                cupertino {
                    this.backdrop = backdrop
                    showNavTitle = true
                }
            },
            modifier = modifier
        ) { pv, title ->
            ScreenBody(
                title = title,
                modifier = Modifier
                    .layerBackdrop(backdrop)
                    .padding(pv)
            )
        }
    }
}

@Composable
private fun ScreenBody(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        title()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .m3Placeholder()
        )
        Text(
            text = "Hello World!",
            modifier = Modifier.placeholder()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    ScreenBody(
        title = {},
        modifier = Modifier.fillMaxSize()
    )
}