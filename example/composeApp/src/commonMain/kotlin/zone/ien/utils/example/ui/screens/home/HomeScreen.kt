package zone.ien.utils.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.foundation.IenTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import zone.ien.hig.adaptive.AdaptiveTextButton
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.currentTheme
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.component.AdaptiveSwitch
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.adaptive.view.AdaptiveDropdownBox
import zone.ien.utils.adaptive.view.AdaptiveDropdownMenuNative
import zone.ien.utils.adaptive.view.DropdownMenuSectionNative
import zone.ien.utils.example.Android
import zone.ien.utils.example.ui.navigation.RootRoute
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import zone.ien.utils.ui.screen.TopBarMode
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.navigation.result.ResultStore
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.menu.IenMenu
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.utils.conditional
import zone.ien.utils.utils.moveToBackground
import zone.ien.utils.utils.shareText
import zone.ien.utils.utils.ui.enableNativeInput
import zone.ien.utils.utils.ui.rememberRepeatClick


private data class HomeMenuItem(
    val name: String,
    val route: RootRoute,
    val color: Color,
)

private val menuItems = listOf(
    HomeMenuItem("Design System", RootRoute.DesignSystem, Color(0xFFE91E63)),
    HomeMenuItem("Color Tokens", RootRoute.ColorTokens, Color(0xFF3182F6)),
    HomeMenuItem("Section", RootRoute.Section, Color(0xFFE53935)),
    HomeMenuItem("Settings", RootRoute.Settings, Color(0xFF1E88E5)),
    HomeMenuItem("Lazy Section", RootRoute.LazySection, Color(0xFF43A047)),
    HomeMenuItem("Playground", RootRoute.Playground, Color(0xFF00ACC1)),
    HomeMenuItem("Ien Playground", RootRoute.IenPlayground, Color(0xFF8B5CF6)),
    HomeMenuItem("Adaptive Playground", RootRoute.AdaptivePlayground, Color(0xFF0F766E)),
    HomeMenuItem("Navigation", RootRoute.Navigation, Color(0xFFFDD835)),
    HomeMenuItem("Firebase Auth", RootRoute.FirebaseAuth, Color(0xFFE65100)),
)

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
    var bottomText by remember { mutableStateOf("") }
    var nativeInputText by remember { mutableStateOf("") }
    var textFieldText by remember { mutableStateOf("") }

    IenAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            snackbarHost = { SnackbarHost(snackbarState) },
            contentEdge = IenScaffoldContentEdge(
                scrollState = scrollState,
            ),
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
                            IenText(
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
                        adaptation = {
                            material { this.placement = IenMenu.Placement.AnchorTopStart }
                            cupertino { this.backdrop = backdrop }
                        },
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
            title = { IenText(text = "IENGROUND") },
            subtitle = { IenText(text = "Sub Title") },
            adaptation = {
                material {
                    mode = TopBarMode.Expanded
                    isCenterAligned = true
                }
                cupertino {
                    this.backdrop = backdrop
                    mode = TopBarMode.Expanded
                }
            },
            bottomBar = {
                BottomAppBar {
                    IenTextField(
                        value = bottomText,
                        onValueChange = { bottomText = it },
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
                    IenText(
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
                    IenText(
                        text = "isDropdownMenu",
                        modifier = Modifier.weight(1f)
                    )
                    AdaptiveSwitch(
                        checked = isDropdownMenu,
                        onCheckedChange = { isDropdownMenu = it }
                    )
                }
                IenText(
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
                ) {
                    IenText("move to background")
                }
                IenButton(
                    onClick = {
                        shareText("text share")
                    },
                    variant = IenButtonVariant.Fill,
                    display = IenButtonDisplay.Full,
                ) {
                    IenText("Text share")
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
                        IenText(
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
                        IenText(
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
                    IenText(
                        text = "all invisible",
                        modifier = Modifier.weight(1f)
                    )
                    AdaptiveSwitch(
                        checked = allInvisible,
                        onCheckedChange = { allInvisible = it }
                    )
                }

                // --- 2-column grid menu ---
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    IenText(
                        text = "MENU",
                        style = IenTheme.typography.title2.copy(fontWeight = FontWeight.Bold),
                        color = IenTheme.colors.textPrimary,
                    )
                    menuItems.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            rowItems.forEach { item ->
                                HomeMenuCard(
                                    name = item.name,
                                    color = item.color,
                                    onClick = { backStack.add(item.route) },
                                    modifier = Modifier.weight(1f),
                                )
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
                IenTextField(
                    value = nativeInputText,
                    onValueChange = { nativeInputText = it },
                    keyboardOptions = KeyboardOptions.Default.enableNativeInput(),
                    modifier = Modifier.fillMaxWidth()
                )
                IenTextField(
                    value = textFieldText,
                    onValueChange = { textFieldText = it },
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

@Composable
private fun HomeMenuCard(
    name: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IenSurface(
        modifier = modifier
            .clickable(onClick = onClick)
            .height(100.dp),
        shape = ContinuousRoundedRectangle(IenTheme.radius.lg),
        color = IenTheme.colors.surfaceRaised,
        border = BorderStroke(IenTheme.stroke.thin, color.copy(alpha = 0.35f)),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            contentAlignment = Alignment.Center,
        ) {
            IenText(
                text = name,
                style = IenTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                color = IenTheme.colors.textPrimary,
                textAlign = TextAlign.Center,
            )
        }
    }
}