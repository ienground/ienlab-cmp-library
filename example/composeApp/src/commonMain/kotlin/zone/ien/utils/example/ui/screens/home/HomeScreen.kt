package zone.ien.utils.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kyant.backdrop.backdrops.layerBackdrop
import zone.ien.hig.adaptive.AdaptiveSwitch
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.GeneratedAdaptiveTheme
import zone.ien.utils.example.Android
import zone.ien.utils.example.ui.navigation.RootRoute
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.screen.TopBarSize
import zone.ien.utils.ui.utils.IconData

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>
) {
    var isMaterialTheme by remember { mutableStateOf(true) }
    val backdrop = rememberDefaultBackdrop()

    GeneratedAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            actions = listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    icon = IconData.Vector(Android)
                )
            ),
            title = { Text(text = "IENGROUND") },
            subtitle = { Text(text = "Sub Title") },
            adaptation = {
                material {
                    size = TopBarSize.Medium
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                }
                cupertino {
                    this.backdrop = backdrop
                    showNavTitle = true
                }
            },
            modifier = modifier
        ) { pv, title ->
            Column(
                modifier = Modifier
                    .layerBackdrop(backdrop)
                    .verticalScroll(rememberScrollState())
                    .padding(pv)
            ) {
                title()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "set material3",
                        modifier = Modifier.weight(1f)
                    )
                    AdaptiveSwitch(
                        checked = isMaterialTheme,
                        onCheckedChange = { isMaterialTheme = it }
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable { backStack.add(RootRoute.Section) }
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Red)
                ) {
                    Text(text = "Section")
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Blue)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Green)
                )
            }
        }
    }
}