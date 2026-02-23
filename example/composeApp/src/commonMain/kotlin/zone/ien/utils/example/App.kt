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



    /*
    MaterialTheme {
        val backdrop = rememberLayerBackdrop()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        LiquidButton(
                            backdrop = backdrop,
                            onClick = {}
                        ) {
                            Text(text = "hi")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                )
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .layerBackdrop(backdrop)
                    .fillMaxSize()
//                    .padding(
//                        top = it.calculateTopPadding(),
//                        start = it.calculateStartPadding(LocalLayoutDirection.current),
//                        end = it.calculateEndPadding(LocalLayoutDirection.current),
//                        bottom = it.calculateBottomPadding()
//                    )
//                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Dlog.d(TAG, "pv: ${it.calculateTopPadding()} ${it.calculateBottomPadding()}")
                    Dlog.d(TAG, "top: ${WindowInsets.statusBars.asPaddingValues().calculateTopPadding()} ${WindowInsets.statusBars.asPaddingValues().calculateBottomPadding()}")

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                it.calculateTopPadding()// - WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
//                                        + it.calculateBottomPadding() - WindowInsets.statusBars.asPaddingValues().calculateBottomPadding()
                            )
                    )

                    repeat(60) {
                        Text(
                            text = "Hello World!",
                            modifier = Modifier.background(Color.Red)
                        )
                    }
                }
            }
        }
    }

     */

    /*
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        M3TopAppBarScaffold(
            navigationIcon = { M3BackButton {  } },
            actions = listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    title = "hi",
                    onClick = {},
                    icon = Android
                )
            ),
            title = {
                Text(
                    text = "Hello World!"
                )
            },
            isCenterAligned = true,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {
                Text(
                    text = "hi"
                )
            }
        }
        /*
        Scaffold(
        ) {
            var text1 by remember { mutableStateOf("") }
            val text2 = rememberTextFieldState()
            var showDialog by remember { mutableStateOf(false) }
            var showDialog2 by remember { mutableStateOf(false) }
            var switchValue by remember { mutableStateOf(false) }
            var value by remember { mutableStateOf(0.5f) }
            val backdrop = rememberLayerBackdrop()
            var progress by remember { mutableStateOf(0f) }

            LaunchedEffect(Unit) {
                while (true) {
                    progress += 0.2f
                    if (progress > 1f) {
                        progress = 0f
                    }

                    delay(1000)
                }
            }

//            val hazeState = rememberHazeState()

            Box(
                modifier = Modifier.padding(it)
            ) {
                Image(
                    painter = painterResource(ienlab_cmp_library.example.composeapp.generated.resources.Res.drawable.compose_multiplatform),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .layerBackdrop(backdrop)
                        .fillMaxSize()
                )
                Column(
                    modifier = Modifier
//                        .hazeSource(hazeState)
                        .fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "label",
                            modifier = Modifier.weight(1f)
                        )
                        LiquidToggle(
                            checked = !switchValue,
//                            selected = { !switchValue },
                            onSelect = { switchValue = !it },
                            backdrop = backdrop
                        )
                    }
                    var currentItem by remember { mutableStateOf(1) }
                    var currentItems by remember { mutableStateOf(listOf<Int>()) }

                    ExposedDropdownMenuBox(
                        title = "Hello",
                        itemsWithLabels = mapOf(
                            1 to "item1",
                            2 to "item2",
                            3 to "item3",
                            4 to "item4",
                            5 to "item5",
                            6 to "item6",
                            7 to "item7",
                            8 to "item8",
                            9 to "item9",
                            10 to "item10",
                        ),
                        currentItem = currentItem,
                        onItemSelected = { currentItem = it },
                        modifier = Modifier.fillMaxWidth()
                    ) { value, icon ->
                        TextField(
                            value = value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = icon,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ExposedDropdownMenuBox(
                        title = "Hello",
                        itemsWithLabels = mapOf(
                            1 to "item1",
                            2 to "item2",
                            3 to "item3",
                            4 to "item4",
                            5 to "item5",
                            6 to "item6",
                            7 to "item7",
                            8 to "item8",
                            9 to "item9",
                            10 to "item10",
                            11 to "item11",
                            12 to "item12",
                            13 to "item13",
                            14 to "item14",
                            15 to "item15",
                            16 to "item16",
                        ),
                        currentItems = currentItems,
                        onItemsSelected = { Dlog.d(TAG, "new: $it"); currentItems = it },
                        modifier = Modifier.fillMaxWidth()
                    ) { value, icon ->
                        TextField(
                            value = value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = icon,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text(
                        text = "${currentItems.joinToString(",")}"
                    )
                    SecureTextField(
                        state = text2,
                        modifier = Modifier
                    )
                    BasicSecureTextField(
                        state = text2,
                        decorator = object: TextFieldDecorator {
                            @Composable
                            override fun Decoration(innerTextField: @Composable () -> Unit) {
                                Box(

                                ) {
                                    innerTextField()
                                }
                            }
                        },
//                        decorator = {
//                            Box(
//
//                            ) {
//                                it()
//                            }
//                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row {
                        LiquidToggle(
                            checked = switchValue,
//                            selected = { switchValue },
                            onSelect = { switchValue = it },
                            backdrop = backdrop
                        )
                        LiquidButton(
                            onClick = {
                                showDialog = true
                            },
                            tint = Color(0xFF0088FF),
                            backdrop = backdrop
                        ) {
                            Text(text = "dialog")
                        }
                        LiquidButton(
                            onClick = {
                                showDialog2 = true
                            },
                            tint = Color(0xFF0088FF),
                            backdrop = backdrop
                        ) {
                            Text(text = "time")
                        }
                        LiquidButton(
                            onClick = {
//                                showDialog = true
                                openAppStoreUrl("org.realsoup.lovehero", "6755778247")
                            },
                            tint = Color(0xFF0088FF),
                            backdrop = backdrop
                        ) {

                            Text(text = "store")
                        }
                    }
                    LiquidSlider(
                        value = { value },
                        onValueChange = { value = it },
                        backdrop = backdrop,
                        valueRange = 0f..1f,
                        visibilityThreshold = 0.01f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    DeviceBox(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color.Red)
                    )
                }
//                Box(
//                    modifier = Modifier
//                        .hazeEffect(state = hazeState, HazeStyle(tint = null, blurRadius = 8.dp))
//                        .fillMaxWidth().fillMaxHeight(0.3f)
//                )
            }

//            UpdateAlertDialog(
//                visible = showDialog,
//                appName = "App Name",
//                onDismiss = { showDialog = false },
//            )

//            M3BaseAlertDialog(
//                visible = showDialog,
//                icon = { Icon(imageVector = Android, contentDescription = null) },
//                title = "Hello",
//                message = "World",
//                onDismiss = { showDialog = false },
//                buttons = {
//                    TextButton(
//                        onClick = {}
//                    ) { Text(text = "hi") }
//                }
//            )

//            M3BaseTextFieldDialog(
//                visible = showDialog,
//                icon = { Icon(imageVector = Android, contentDescription = null) },
//                title = "Hello",
//                message = "World",
//                onDismiss = { showDialog = false },
//                textFields = {
////                    TextField(
////                        value
////                    )
//                },
//                buttons = {
//                    TextButton(
//                        onClick = {}
//                    ) { Text(text = "hi") }
//                }
//            )

//            TextFieldDialog(
//                visible = showDialog,
//                icon = { Icon(imageVector = Android, contentDescription = null) },
//                title = "Hello",
////                message = "World",
//                textFields = mapOf(
//                    "hi" to TextFieldDialogData(
//                        initialValue = "Hello",
//                        placeholder = "placeholder",
//                        prefix = "prefix",
//                        suffix = "suffix",
//                        keyboardType = KeyboardType.Email
//                    ),
//                    "hi2" to TextFieldDialogData(
//                        initialValue = "Hello",
//                        placeholder = "placeholder",
//                        prefix = "prefix",
//                        suffix = "suffix",
//                        keyboardType = KeyboardType.Password
//                    )
//                ),
//                onDismiss = { showDialog = false },
//                onConfirm = {
//                    Dlog.d(TAG, it.toString())
//                    showDialog = false
//                }
//            )

            DatePickerDialog(
                visible = showDialog,
                title = "Hello",
                initialSelectedDateMillis = KDate(2023, 1, 1).timeInMillis(),
                onDismiss = {
                    Dlog.d(TAG, "dimiss date picker dialog")
                    showDialog = false
                },
                onConfirm = {
                    showDialog = false
                    Dlog.d(TAG, "${KZonedDateTime.fromMillis(it)}")
                }
            )
            TimePickerDialog(
                visible = showDialog2,
                title = "Hello",
                initialHour = 5,
                initialMinute = 20,
                onDismiss = {
                    Dlog.d(TAG, "dimiss time picker dialog")
                    showDialog2 = false
                },
                onConfirm = { hour, minute ->
                    showDialog2 = false
                    Dlog.d(TAG, "${hour} $minute")
                }
            )


//            DeleteAlertDialog(
//                visible = showDialog,
//                onDismiss = { showDialog = false },
//                onConfirm = { showDialog = false }
//            )
//            SaveAlertDialog(
//                visible = showDialog,
//                onCancel = { showDialog = false },
//                onUnsave = { showDialog = false },
//                onSave = { showDialog = false }
//            )

//            ProgressDialog(
//                visible = showDialog,
//                progress = { progress }
//            )

//            zone.ien.utils.adaptive.dialog.AlertDialog(
//                visible = showDialog,
//                title = "Hello",
//                message = "World",
//                styleDismiss = UIAlertActionStyle.Default,
//                onDismiss = { showDialog = false }
//            )


//            AlertDialog(
//                visible = showDialog,
//                onCancel = { showDialog = false }
//            )
        }
        */
    }
     */

    var isMaterialTheme by remember { mutableStateOf(!isIos) }
    val backdrop = rememberLayerBackdrop()
    val snackbarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    GeneratedAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
//        target = Theme.Cupertino
    ) {

        val scrollState = rememberScrollState()

        /*
        CupertinoScaffold(
            topBar = {
                AdaptiveTopAppBar(
                    title = {
                        Text(text = "title")
                    },
                    adaptation = {
                        cupertino {
                            this.isTranslucent = false
                            this.isTransparent = false
                        }
                    },
                )
            },
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
                        contentDescription = null,
//                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            modifier = Modifier
        /*
        AdaptiveTopAppBarScaffold(
            title = {
                Text(
                    text = "Title"
                )
            },
            topBarAdaptation = {
                cupertino {
                    this.backdrop = backdrop
                }
            }

         */
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(it)
                    .layerBackdrop(backdrop)
            ) {
                Text(
                    text = "Hello World"
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1000.dp)
                )
            }
        }


         */

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
