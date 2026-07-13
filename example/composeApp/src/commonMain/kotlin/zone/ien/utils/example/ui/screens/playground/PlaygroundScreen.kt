package zone.ien.utils.example.ui.screens.playground

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.revenuecat.placeholder.placeholder
import zone.ien.hig.CupertinoLiquidButton
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.dialog.TextFieldDialog
import zone.ien.utils.adaptive.menu.adaptiveSaveButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.example.Android
import zone.ien.utils.example.isIos
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.shimmer.m3Placeholder
import zone.ien.utils.ui.utils.TextFieldDialogData

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun PlaygroundScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val backdrop = rememberDefaultBackdrop()
    var isMaterialTheme by remember { mutableStateOf(!isIos) }
    var visible by remember { mutableStateOf(true) }
    IenAdaptiveTheme(
        target = Theme.Cupertino
//        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
//        /*
        AdaptiveTopAppBarScaffold(
            navigationIcon = { AdaptiveBackButton(backdrop = backdrop, visible = visible) { navigateBack() } },
            title = { IenText(text = "Playground") },
            subtitle = { IenText(text = "IENGROUND") },
            actions = listOf(
                ActionMenuItem.IconMenuItem.ShownIfRoom(
                    icon = IconData.Vector(Android),
                    onClick = { visible = !visible },
                    title = "d"
                )
            ),
//            actions = {
//                AdaptiveSwitch(
//                    checked = isMaterialTheme,
//                    onCheckedChange = { isMaterialTheme = it },
//                    adaptation = {
//                        cupertino {
//                            this.backdrop = backdrop
//                        }
//                    }
//                )
//                AdaptiveSwitch(
//                    checked = visible,
//                    onCheckedChange = { visible = it },
//                    adaptation = {
//                        cupertino {
//                            this.backdrop = backdrop
//                        }
//                    }
//                )
//            },
            primaryAction = adaptiveSaveButton(
                visible = visible,
                onClick = {}
            ),
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

//         */
//        CupertinoScaffold(
//            topBar = {
//                CupertinoTopAppBar(
//                    navigationIcon = {
//                        AdaptiveBackButton(
//                            backdrop = backdrop,
//                            visible = visible,
//                            onClick = navigateBack
//                        )
////                        AnimatedVisibility(
////                            visible = visible,
////                            enter = slideInHorizontally(tween(3000)) { -it },
////                            exit = slideOutHorizontally(tween(3000)) { -it }
////                        ) {
////                            CupertinoLiquidIconButton(
////                                onClick = {},
////                                backdrop = backdrop,
////                                modifier = Modifier.padding(start = 16.dp)
////                            ) {
////                                CupertinoIcon(
////                                    imageVector = CupertinoIcons.Default.ChevronBackward,
////                                    contentDescription = null,
////                                    modifier = Modifier.size(24.dp)
////                                )
////                            }
////                        }
//                    },
//                    actions = {
//                        AdaptiveSwitch(
//                            checked = isMaterialTheme,
//                            onCheckedChange = { isMaterialTheme = it },
//                            adaptation = {
//                                cupertino {
//                                    this.backdrop = backdrop
//                                }
//                            }
//                        )
//                        AdaptiveSwitch(
//                            checked = visible,
//                            onCheckedChange = { visible = it },
//                            adaptation = {
//                                cupertino {
//                                    this.backdrop = backdrop
//                                }
//                            }
//                        )
//                    },
//                    title = {},
//                    backdrop = backdrop
//                )
//            }
//        ) {pv ->
//            val title = @Composable {}
            ScreenBody(
                title = title,
                visible = visible,
                modifier = Modifier
                    .layerBackdrop(backdrop)
                    .padding(pv)
            )
        }
    }
}

@OptIn(ExperimentalCupertinoApi::class)
@Composable
private fun ScreenBody(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    visible: Boolean = true
) {
    var showTextFieldDialog by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        title()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .m3Placeholder()
        )
        IenText(
            text = "Hello World!",
            modifier = Modifier.placeholder()
        )

        Box(
            modifier = Modifier
                .shadow(4.dp)
                .background(Color.Red)
                .size(100.dp)
        )

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(tween(3000)),// fadeIn(tween(3000)),// + expandHorizontally(tween(700)),
            exit = slideOutHorizontally(tween(3000))// fadeOut(tween(3000))// + shrinkHorizontally(tween(700))
            ,
            modifier = Modifier.graphicsLayer {
                compositingStrategy = CompositingStrategy.ModulateAlpha
            }
        ) {
            Box(
                modifier = Modifier
                    .shadow(4.dp)
                    .background(Color.Red)
                    .size(100.dp)
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(tween(3000)),// + expandHorizontally(tween(700)),
            exit = slideOutHorizontally(tween(3000))// + shrinkHorizontally(tween(700))
        ) {
            CupertinoLiquidButton(
                onClick = { showTextFieldDialog = true },
                backdrop = rememberDefaultBackdrop(),
                modifier = Modifier
//                    .padding(32.dp)
            ) {
                IenIcon(
                    imageVector = Android,
                    contentDescription = null
                )
            }
        }
    }

    var text by remember { mutableStateOf("") }
    TextFieldDialog(
        visible = showTextFieldDialog,
        title = "Hello Title",
        textFields = mapOf(
            "text" to TextFieldDialogData(
                initialValue = text,
//                onValueChange = { text = it; it },
                valid = { it.isNotBlank() },
                placeholder = "placeholder",
                keyboardType = KeyboardType.Password
            )
        ),
        onDismiss = { showTextFieldDialog = false },
        onConfirm = {
            val newText = it["text"]
            text = newText.orEmpty()
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    ScreenBody(
        title = {},
        modifier = Modifier.fillMaxSize()
    )
}
