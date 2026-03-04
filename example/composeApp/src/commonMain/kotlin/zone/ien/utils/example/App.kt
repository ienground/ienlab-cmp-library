package zone.ien.utils.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import kotlinx.coroutines.launch
import zone.ien.hig.CupertinoLiquidButton
import zone.ien.hig.CupertinoLiquidButtonDefaults
import zone.ien.hig.CupertinoLiquidIconButton
import zone.ien.hig.CupertinoNavigationTitle
import zone.ien.hig.CupertinoScaffold
import zone.ien.hig.CupertinoTopAppBar
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.FabPosition
import zone.ien.hig.adaptive.AdaptiveSwitch
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.component.AdaptiveLargeFloatingActionButton
import zone.ien.utils.adaptive.component.AdaptiveMediumFloatingActionButton
import zone.ien.utils.adaptive.component.AdaptiveSmallFloatingActionButton
import zone.ien.utils.adaptive.menu.adaptiveDeleteButton
import zone.ien.utils.adaptive.menu.adaptiveSaveButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBar
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.GeneratedAdaptiveTheme
import zone.ien.utils.icon.hig.Ellipsis
import zone.ien.utils.icon.hig.HigIcons
import zone.ien.utils.icon.material.MaterialIcons
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.menu.IconData
import zone.ien.utils.utils.Dlog

const val TAG = "CmpLibTAG"
expect val currentTheme: Theme
expect val isIos: Boolean

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAdaptiveApi::class,
    ExperimentalCupertinoApi::class, ExperimentalMaterial3ExpressiveApi::class
)
@Composable
@Preview
fun App() {
    Dlog.init(isDebug = true)

    var isMaterialTheme by remember { mutableStateOf(!isIos) }
    val backdrop = rememberLayerBackdrop()
    val snackbarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    GeneratedAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
//        target = Theme.Cupertino
    ) {

        val scrollState = rememberScrollState()

        AdaptiveTopAppBarScaffold(
            navigationIcon = {
                AdaptiveBackButton(backdrop = backdrop) {}
            },
            actions = listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Hi",
                    onClick = {
                        coroutineScope.launch {
                            snackbarState.showSnackbar("hi")
                        }
                    },
                    icon = IconData.Vector(Android)
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Hi",
                    onClick = {
                        coroutineScope.launch {
                            snackbarState.showSnackbar("delete")
                        }
                    },
                    icon = null//IconData.Vector(MaterialIcons.Delete),
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Hi",
                    onClick = {
                        coroutineScope.launch {
                            snackbarState.showSnackbar("delete")
                        }
                    },
                    icon = null//IconData.Vector(MaterialIcons.Delete),
                ),
            ),
            primaryAction = adaptiveSaveButton(
                onClick = {}
            ),
            snackbarHost = { SnackbarHost(snackbarState) },
            title = {
                Text(
                    text = "title"
                )
            },
            adaptation = {
                material {
                    isCenterAligned = true
                }
                cupertino {
                    this.isBackgroundGradient = false
                    this.backdrop = backdrop
                    this.scrollableState = scrollState
                }
            },
            fabPosition = FabPosition.End,
            floatingActionButton = {
                AdaptiveMediumFloatingActionButton(
                    onClick = {},
                    adaptation = {
                        cupertino {
                            this.backdrop = backdrop
                        }
                    }
                ) {
                    Icon(
                        painter = AdaptiveIcons.painter(
                            material = { Android },
                            cupertino = { "plus" }
                        ),
                        contentDescription = null
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .layerBackdrop(backdrop)
                    .verticalScroll(scrollState)
                    .padding(it)
            ) {
                Text(
                    text = "Hello World"
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AdaptiveSwitch(
                        checked = isMaterialTheme,
                        onCheckedChange = { isMaterialTheme = it }
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Black)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Red)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Yellow)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Black)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Red)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Yellow)
                )
            }
        }
    }
}
