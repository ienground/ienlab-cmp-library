package zone.ien.utils.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMapIndexed
import androidx.navigation3.runtime.NavBackStack
import com.kyant.backdrop.backdrops.layerBackdrop
import kotlinx.coroutines.launch
import zone.ien.hig.adaptive.AdaptiveSwitch
import zone.ien.hig.adaptive.AdaptiveTextButton
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.currentTheme
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.adaptive.view.AdaptiveDropdownBox
import zone.ien.utils.adaptive.view.AdaptiveDropdownMenuNative
import zone.ien.utils.adaptive.view.DropdownMenuSectionNative
import zone.ien.utils.example.Android
import zone.ien.utils.example.ui.navigation.RootRoute
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.screen.TopBarSize
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.navigation.result.ResultStore
import zone.ien.utils.ui.components.interactive.IenButton
import zone.ien.utils.ui.components.interactive.IenButtonDisplay
import zone.ien.utils.ui.components.interactive.IenButtonSize
import zone.ien.utils.ui.components.interactive.IenButtonVariant
import zone.ien.utils.ui.utils.conditional
import zone.ien.utils.utils.moveToBackground
import zone.ien.utils.utils.shareText
import zone.ien.utils.utils.ui.enableNativeInput
import zone.ien.utils.utils.ui.rememberRepeatClick

@Suppress("FrequentlyChangingValue")
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<RootRoute>,
    resultStore: ResultStore,
) {
    var isMaterialTheme by rememberSaveable { mutableStateOf(true) }
    var isDropdownMenu by remember { mutableStateOf(true) }
    var showVisible by remember { mutableStateOf(true) }
    var showSingle by remember { mutableStateOf(false) }
    var allInvisible by remember { mutableStateOf(false) }
    val backdrop = rememberDefaultBackdrop()
    var childExpanded by remember { mutableStateOf(false) }
    val children = listOf("Hi", "Hello")
    val snackbarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()


    IenAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            snackbarHost = { SnackbarHost(snackbarState) },
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
            ) else if (allInvisible) listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "Text",
                    onClick = {},
                    badge = 1,
                    icon = IconData.Vector(Android),
                    visible = false
                )
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
                                    material = { M3SystemIcons.ArrowDropDown },
                                    cupertino = { "chevron.down" }
                                ),
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.conditional(currentTheme == Theme.Cupertino) { padding(start = 16.dp) }
                ) {
                    AdaptiveDropdownMenuNative(
                        expanded = childExpanded,
                        onDismissRequest = { childExpanded = false },
                        adaptation = { cupertino { this.backdrop = backdrop } },
                        items = children.fastMapIndexed { index, child ->
                            DropdownMenuSectionNative.Action(
                                text = child,
                                onClick = {
                                    childExpanded = false
                                },
                                icon = IconData.Vector(Android),
                                isDestructive = index == 1
                            )
                        },
                        sections = listOf(
                            DropdownMenuSectionNative(
                                title = "section",
                                items = children.fastMapIndexed { index, child ->
                                    DropdownMenuSectionNative.Action(
                                        text = child,
                                        onClick = {
                                            childExpanded = false
                                        },
                                        icon = IconData.Vector(Android),
                                        isDestructive = index == 1
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
                    scrollableState = scrollState
                }
                cupertino {
                    this.backdrop = backdrop
                    this.scrollableState = scrollState
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
                    .verticalScroll(scrollState)
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
                Text(
                    text = "result: ${resultStore.getResult<String>("text")}"
                )
                val onBackPressed = rememberRepeatClick(
                    onClick = {
                        coroutineScope.launch {
                            snackbarState.showSnackbar("한번더")
                        }
                    },
                    onNthClick = {
                        moveToBackground()
                    }
                )

                IenButton(
                    onClick = onBackPressed,
                    variant = IenButtonVariant.Weak,
                    display = IenButtonDisplay.Full,
                    text = "move to background"
                )
                IenButton(
                    onClick = {
                        shareText("text share")
                    },
                    variant = IenButtonVariant.Fill,
                    display = IenButtonDisplay.Full,
                    text = "Text share"
                )
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "all invisible",
                        modifier = Modifier.weight(1f)
                    )
                    AdaptiveSwitch(
                        checked = allInvisible,
                        onCheckedChange = { allInvisible = it }
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable { backStack.add(RootRoute.DesignSystem) }
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Magenta)
                ) {
                    Text(text = "Design System")
                }
                Box(
                    modifier = Modifier
                        .clickable { backStack.add(RootRoute.ColorTokens) }
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color(0xFF3182F6))
                ) {
                    Text(text = "Color Tokens")
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
                Box(
                    modifier = Modifier
                        .clickable { backStack.add(RootRoute.M3Playground) }
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color(0xFF8B5CF6))
                ) {
                    Text(text = "M3 Playground")
                }
                Box(
                    modifier = Modifier
                        .clickable { backStack.add(RootRoute.Navigation) }
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Yellow)
                ) {
                    Text(text = "Navigation")
                }
                TextField(
                    state = rememberTextFieldState(),
                    keyboardOptions = KeyboardOptions.Default.enableNativeInput(),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    state = rememberTextFieldState(),
                    modifier = Modifier.fillMaxWidth()
                )
                BasicTextField(
                    state = rememberTextFieldState(),
                    keyboardOptions = KeyboardOptions.Default.enableNativeInput(),
                    decorator = TextFieldDecorator { innerTextField ->
                        Box(
                            modifier = Modifier.background(Color.Yellow)
                        ) {
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                BasicTextField(
                    state = rememberTextFieldState(),
                    decorator = TextFieldDecorator { innerTextField ->
                        Box(
                            modifier = Modifier.background(Color.Yellow)
                        ) {
                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
