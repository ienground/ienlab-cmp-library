package zone.ien.utils.example.ui.screens.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import zone.ien.hig.CupertinoNavigationBar
import zone.ien.hig.CupertinoNavigationBarItem
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.utils.adaptive.component.AdaptiveSwitch
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.adaptive.utils.getSurfaceTopAppBarAdaptation
import zone.ien.utils.adaptive.view.AdaptiveNavigationBar
import zone.ien.utils.adaptive.view.NavigationBarItem
import zone.ien.utils.icon.Adaptive
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.material.M3SystemIcons

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun NavigationScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val backdrop = rememberDefaultBackdrop()
    var selected by remember { mutableStateOf(false) }
    var isMaterialTheme by remember { mutableStateOf(true) }
    var isNative by remember { mutableStateOf(true) }

    IenAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            adaptation = getSurfaceTopAppBarAdaptation(backdrop),
            navigationIcon = { AdaptiveBackButton(backdrop = backdrop, onClick = navigateBack) },
            bottomBar = {
                AdaptiveNavigationBar(
                    selectedTabIndex = { if (selected) 0 else 1 },
                    onTabSelected = { selected = it == 0 },
                    isNative = isNative,
                    adaptation = {
                        cupertino { this.backdrop = backdrop }
                    },
                    items = listOf(
                        NavigationBarItem(
                            onClick = { selected = true },
                            icon = IconData.Adaptive(
                                material = { M3SystemIcons.Delete },
                                cupertino = { "trash.fill" }
                            ),
                            label = "Delete"
                        ),
                        NavigationBarItem(
                            onClick = { selected = false },
                            icon = IconData.Adaptive(
                                material = { M3SystemIcons.Save },
                                cupertino = { "checkmark" }
                            ),
                            label = "Save"
                        )
                    ),
                )
            },
            modifier = modifier
        ) { pv, title ->
            LazyColumn(
                contentPadding = pv,
                modifier = Modifier.layerBackdrop(backdrop)
            ) {
                item {
                    AdaptiveSwitch(
                        checked = isMaterialTheme,
                        onCheckedChange = { isMaterialTheme = it }
                    )
                }
                item {
                    AdaptiveSwitch(
                        checked = isNative,
                        onCheckedChange = { isNative = it }
                    )
                }
                items(items = (0 until 20).toList(), key = null) {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .background(if (it % 2 == 0) Color.Cyan else Color.Green)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "${it}"
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    IenAdaptiveTheme(Theme.Material3) {
        NavigationScreen(
            navigateBack = {}
        )
    }
}
