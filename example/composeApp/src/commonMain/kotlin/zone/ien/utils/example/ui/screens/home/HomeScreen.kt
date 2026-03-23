package zone.ien.utils.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kyant.backdrop.backdrops.layerBackdrop
import org.jetbrains.compose.resources.stringResource
import zone.ien.hig.adaptive.AdaptiveSwitch
import zone.ien.hig.adaptive.AdaptiveTextButton
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.adaptiveComponent
import zone.ien.hig.adaptive.currentTheme
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.GeneratedAdaptiveTheme
import zone.ien.utils.adaptive.view.AdaptiveDropdownBox
import zone.ien.utils.adaptive.view.AdaptiveDropdownMenu
import zone.ien.utils.adaptive.view.DropdownMenuSection
import zone.ien.utils.example.Android
import zone.ien.utils.example.ui.navigation.RootRoute
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.screen.TopBarSize
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.material.MaterialIcons
import zone.ien.utils.ui.utils.conditional
import zone.ien.utils.utils.ifEmptyOrNull

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey>
) {
    var isMaterialTheme by remember { mutableStateOf(true) }
    var isDropdownMenu by remember { mutableStateOf(true) }
    var showVisible by remember { mutableStateOf(true) }
    var showSingle by remember { mutableStateOf(false) }
    val backdrop = rememberDefaultBackdrop()
    var childExpanded by remember { mutableStateOf(false) }
    val children = listOf("Hi", "Hello")

    GeneratedAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            actions = if (isDropdownMenu) listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 1,
                    icon = IconData.Vector(Android)
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 2,
                    icon = IconData.Vector(Android)
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 3,
                    icon = IconData.Vector(Android)
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 4,
                    icon = IconData.Vector(Android)
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 4,
                    icon = IconData.Vector(Android)
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 4,
                    icon = IconData.Vector(Android)
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 4,
                    icon = IconData.Vector(Android)
                ),
//                ActionMenuItem.IconMenuItem.ShownIfRoom(
//                    title = "Text",
//                    onClick = {},
//                    icon = IconData.Vector(Android)
//                ),
//                ActionMenuItem.IconMenuItem.ShownIfRoom(
//                    title = "Text",
//                    onClick = {},
//                    icon = IconData.Vector(Android)
//                ),
            ) else if (showSingle) listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 1,
                    icon = IconData.Vector(Android)
                ),
            ) else listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 1,
                    icon = IconData.Vector(Android)
                ),
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 2,
                    visible = showVisible,
                    icon = IconData.Vector(Android)
                ),
            ),
            navigationIcon = {
                AdaptiveDropdownBox(
                    expanded = childExpanded,
                    trigger = {
                        AdaptiveTextButton(
                            onClick = { childExpanded = !childExpanded },
                            adaptation = {
                                cupertino {
                                    this.backdrop = backdrop
                                }
                            }
                        ) {
                            Text(
                                text = "Hi",
                            )
                            Icon(
                                painter = AdaptiveIcons.painter(
                                    material = { MaterialIcons.ArrowDropDown },
                                    cupertino = { "chevron.down" }
                                ),
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.conditional(currentTheme == Theme.Cupertino) { padding(start = 16.dp) }
                ) {
                    AdaptiveDropdownMenu(
                        expanded = childExpanded,
                        onDismissRequest = { childExpanded = false },
                        adaptation = { cupertino { this.backdrop = backdrop } },
                        items = listOf(
                            DropdownMenuSection(
                                items = children.fastMap { child ->
                                    DropdownMenuSection.Action(
                                        text = { Text(text = child) },
                                        onClick = {
                                            childExpanded = false
                                        }
                                    )
                                }
                            )
                        )
                    )
                }
            },
            title = { Text(text = "IENGROUND") },
            subtitle = { Text(text = "Sub Title") },
            adaptation = {
                material {
                    size = TopBarSize.Medium
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                    isCenterAligned = true
                }
                cupertino {
                    this.backdrop = backdrop
                    showNavTitle = true
                }
            },
            bottomBar = {
                BottomAppBar {
                    TextField(
                        state = rememberTextFieldState(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },

            modifier = modifier
        ) { pv, title ->
            Column(
                modifier = Modifier
                    .layerBackdrop(backdrop).consumeWindowInsets(WindowInsets.ime)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "isDropdownMenu",
                        modifier = Modifier.weight(1f)
                    )
                    AdaptiveSwitch(
                        checked = isDropdownMenu,
                        onCheckedChange = { isDropdownMenu = it }
                    )
                }
                AnimatedVisibility(
                    visible = !isDropdownMenu,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "showVisible",
                            modifier = Modifier.weight(1f)
                        )
                        AdaptiveSwitch(
                            checked = showVisible,
                            onCheckedChange = { showVisible = it }
                        )
                    }
                }
                AnimatedVisibility(
                    visible = !isDropdownMenu,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "real single",
                            modifier = Modifier.weight(1f)
                        )
                        AdaptiveSwitch(
                            checked = showSingle,
                            onCheckedChange = { showSingle = it }
                        )
                    }
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
                        .clickable { backStack.add(RootRoute.Settings) }
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Blue)
                ) {
                    Text(text = "Settings")
                }
                Box(
                    modifier = Modifier
                        .clickable { backStack.add(RootRoute.LazySection) }
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Green)
                ) {
                    Text(text = "Lazy")
                }
                Box(
                    modifier = Modifier
                        .clickable { backStack.add(RootRoute.Playground) }
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Cyan)
                ) {
                    Text(text = "Playground")
                }
                TextField(
                    state = rememberTextFieldState(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}