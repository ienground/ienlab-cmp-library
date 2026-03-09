package zone.ien.utils.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import kotlinx.coroutines.launch
import zone.ien.hig.CupertinoDropdownMenu
import zone.ien.hig.CupertinoIcon
import zone.ien.hig.CupertinoText
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.FabPosition
import zone.ien.hig.MenuAction
import zone.ien.hig.MenuSection
import zone.ien.hig.adaptive.AdaptiveSwitch
import zone.ien.hig.adaptive.AdaptiveTextButton
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.adaptive.icons.AdaptiveIcons
import zone.ien.hig.icons.CupertinoIcons
import zone.ien.hig.icons.outlined.CheckmarkCircle
import zone.ien.hig.icons.outlined.PersonCropCircle
import zone.ien.hig.icons.outlined.Pin
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.component.AdaptiveMediumFloatingActionButton
import zone.ien.utils.adaptive.menu.adaptiveSaveButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.GeneratedAdaptiveTheme
import zone.ien.utils.adaptive.view.AdaptiveDropdownBox
import zone.ien.utils.adaptive.view.AdaptiveDropdownMenu
import zone.ien.utils.adaptive.view.DropdownMenuSection
import zone.ien.utils.adaptive.view.DropdownMenuSection.Action
import zone.ien.utils.adaptive.wrapper.RootWrapper
import zone.ien.utils.icon.material.MaterialIcons
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.menu.IconData
import zone.ien.utils.ui.section.M3ProvideSectionStyle
import zone.ien.utils.ui.section.M3Section
import zone.ien.utils.ui.section.M3SectionItem
import zone.ien.utils.ui.utils.conditional
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

    var isMaterialTheme by remember { mutableStateOf(false) }
    val backdrop = rememberDefaultBackdrop()
    val snackbarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    GeneratedAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino,
    ) {
        RootWrapper(
            showKeyboardDirection = false
        ) {
            val scrollState = rememberScrollState()
            var expanded by remember { mutableStateOf(false) }
            var expanded2 by remember { mutableStateOf(false) }

            AdaptiveTopAppBarScaffold(
                modifier = it,
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
                    onClick = { coroutineScope.launch { snackbarState.showSnackbar("primary action clicked") } },
                    enabled = false
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
                val children = listOf("abcdefg", "abcdefg2", "abcdefg3")
                Column(
                    modifier = Modifier
                        .layerBackdrop(backdrop)
                        .verticalScroll(scrollState)
                        .padding(it)
                ) {
                    Text(
                        text = "Hello World"
                    )
                    val textFieldState = rememberTextFieldState()
                    TextField(
                        state = textFieldState,
                        modifier = Modifier.fillMaxWidth()
                    )
                    M3ProvideSectionStyle(
                        scrollable = false,
                        fullHeight = false,
                        modifier = Modifier.height(400.dp)
                    ) {
                        M3Section {
                            M3SectionItem(
                                title = { Text(text = "title") }
                            )
                            M3SectionItem(
                                title = { Text(text = "title") }
                            )
                            M3SectionItem(
                                title = { Text(text = "title") }
                            )
                        }
                    }
                    AdaptiveDropdownBox(
                        expanded = expanded,
                        trigger = {
                            AdaptiveTextButton(
                                onClick = { expanded = !expanded },
                                adaptation = {
                                    cupertino {}
                                },
                                modifier = Modifier.conditional(currentTheme == Theme.Cupertino) {
                                    padding(start = 16.dp)
                                }
                            ) {
                                Text(
                                    text = "Trigger",
                                )
                                Icon(
                                    imageVector = MaterialIcons.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.padding(start = 0.dp)
                    ) {
                        AdaptiveDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            items = listOf(
                                DropdownMenuSection(
                                    items = listOf(
                                        DropdownMenuSection.Action(
                                            text = { Text(text = "hi") },
                                            icon = { Icon(imageVector = Android, contentDescription = null) },
                                            onClick = { expanded = false }
                                        ),
                                        DropdownMenuSection.Action(
                                            text = { Text(text = "hi2") },
                                            icon = { Icon(imageVector = Android, contentDescription = null) },
                                            onClick = { expanded = false }
                                        ),
                                        DropdownMenuSection.Action(
                                            text = { Text(text = "hi3") },
                                            icon = { Icon(imageVector = Android, contentDescription = null) },
                                            onClick = { expanded = false }
                                        ),
                                    )
                                ),
                                DropdownMenuSection(
                                    items = listOf(
                                        Action(
                                            text = { Text(text = "hi5") },
                                            icon = { Icon(imageVector = Android, contentDescription = null) },
                                            onClick = { expanded = false }
                                        ),
                                        DropdownMenuSection.Action(
                                            text = { Text(text = "hi62") },
                                            icon = { Icon(imageVector = Android, contentDescription = null) },
                                            onClick = { expanded = false }
                                        ),
                                        DropdownMenuSection.Action(
                                            text = { Text(text = "hi73") },
                                            icon = { Icon(imageVector = Android, contentDescription = null) },
                                            onClick = { expanded = false }
                                        ),
                                    )
                                )
                            )
                        )
                    }
                    Box(
                        modifier = Modifier.padding(start = 0.dp)
                    ) {
                        AdaptiveTextButton(
                            onClick = { expanded2 = !expanded2 },
                            adaptation = {
                                cupertino {}
                            },
                            modifier = Modifier.conditional(currentTheme == Theme.Cupertino) {
                                padding(start = 16.dp)
                            }
                        ) {
                            Text(
                                text = "Trigger",
                            )
                            Icon(
                                imageVector = MaterialIcons.ArrowDropDown,
                                contentDescription = null
                            )
                        }


                        CupertinoDropdownMenu(
                            expanded = expanded2,
                            onDismissRequest = { expanded2 = false },
                            backdrop = rememberDefaultBackdrop()
                        ) {
                            MenuSection {
                                MenuAction(
                                    onClick = { expanded = false },
                                    icon = {
                                        CupertinoIcon(
                                            imageVector = CupertinoIcons.Default.CheckmarkCircle,
                                            contentDescription = null
                                        )
                                    },
                                ) {
                                    CupertinoText("메시지 선택")
                                }
                                MenuAction(
                                    onClick = { expanded = false },
                                    icon = {
                                        CupertinoIcon(
                                            imageVector = CupertinoIcons.Default.Pin,
                                            contentDescription = null
                                        )
                                    }
                                ) {
                                    CupertinoText("고정 편집")
                                }
                                MenuAction(
                                    onClick = { expanded = false },
                                    icon = {
                                        CupertinoIcon(
                                            imageVector = CupertinoIcons.Default.PersonCropCircle,
                                            contentDescription = null
                                        )
                                    }
                                ) {
                                    CupertinoText("이름 및 사진 설정")
                                }
                            }
//                    MenuDivider()
//                    MenuSection(
////                        title = {
////                            Text("Menu")
////                        }
//                    ) {
//                        MenuAction(
//                            onClick = {
//                                expanded = false
//                            },
//                            icon = {
//                                CupertinoIcon(
//                                    imageVector = CupertinoIcons.Default.SquareAndArrowUp,
//                                    contentDescription = null
//                                )
//                            }
//                        ) {
//                            CupertinoText("Share")
//                        }
//                        MenuAction(
//                            enabled = false,
//                            onClick = {
//                                expanded = false
//                            },
//                            icon = {
//                                CupertinoIcon(
//                                    imageVector = CupertinoIcons.Default.Bookmark,
//                                    contentDescription = null
//                                )
//                            }
//                        ) {
//                            CupertinoText("Add to Favorites")
//                        }
//                    }
                        }
                    }

                    /*
                    Box {
                        var buttonWidthPx by remember { mutableStateOf(0) }
                        var buttonHeightPx by remember { mutableStateOf(0) }

                        LaunchedEffect(buttonWidthPx, buttonHeightPx) {
                            Dlog.d(TAG, "buttonWidthPx: $buttonWidthPx, buttonHeightPx: $buttonHeightPx")
                        }

                        Button(
                            onClick = { expanded2 = true }
                        ) {
                            Text(text = "trigger")
                        }

                        Popup(
                            onDismissRequest = { expanded2 = false }
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = expanded2,
                                enter = expandIn(spring(1.2f)),
                                exit = shrinkOut(spring(1.2f)),
                                modifier = Modifier.onSizeChanged {
                                    buttonWidthPx = it.width
                                    buttonHeightPx = it.height
                                }
                            ) {
                                Column {
                                    children.fastForEach { child ->
                                        DropdownMenuItem(
                                            text = { Text(text = child) },
                                            onClick = {
                                                expanded2 = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                     */
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

}
