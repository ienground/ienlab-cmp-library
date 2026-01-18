package zone.ien.utils.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kashif_e.backdrop.backdrops.layerBackdrop
import com.kashif_e.backdrop.backdrops.rememberLayerBackdrop
import ienlab_cmp_library.example.composeapp.generated.resources.Res
import ienlab_cmp_library.example.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import zone.ien.utils.adaptive.components.LiquidButton
import zone.ien.utils.adaptive.components.LiquidSlider
import zone.ien.utils.adaptive.components.LiquidToggle
import zone.ien.utils.adaptive.dialog.DeleteAlertDialog
import zone.ien.utils.adaptive.dialog.NetworkAlertDialog
import zone.ien.utils.adaptive.dialog.ProgressDialog
import zone.ien.utils.adaptive.dialog.SaveAlertDialog
import zone.ien.utils.adaptive.dialog.UIAlertActionStyle
import zone.ien.utils.adaptive.dialog.UpdateAlertDialog
import zone.ien.utils.ui.dialog.M3DeleteAlertDialog
import zone.ien.utils.utils.Dlog
import zone.ien.utils.utils.getDataDirectory
import zone.ien.utils.utils.openAppStoreUrl
import zone.ien.utils.utils.openUrl

const val TAG = "CmpLibTA"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    Dlog.init(isDebug = true)

    MaterialTheme {
        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = {}
//                )
//            }
        ) {
            var text1 by remember { mutableStateOf("") }
            val text2 = rememberTextFieldState()
            var showDialog by remember { mutableStateOf(false) }
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
                    painter = painterResource(Res.drawable.compose_multiplatform),
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
                            selected = { !switchValue },
                            onSelect = { switchValue = !it },
                            backdrop = backdrop
                        )
                    }
                    TextField(
                        value = text1,
                        onValueChange = { text1 = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    SecureTextField(
                        state = text2,
                        modifier = Modifier
                    )
                    BasicSecureTextField(
                        state = text2,
                        decorator = {
                            Box(

                            ) {
                                it()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row {
                        LiquidToggle(
                            selected = { switchValue },
                            onSelect = { switchValue = it },
                            backdrop = backdrop
                        )
                        LiquidButton(
                            onClick = {
//                                showDialog = true
                                openUrl("https://www.naver.com")
                            },
                            tint = Color(0xFF0088FF),
                            backdrop = backdrop
                        ) {
                            Text(text = "hi")
                        }
                        LiquidButton(
                            onClick = {
//                                showDialog = true
                                openAppStoreUrl("org.realsoup.lovehero", "6755778247")
                            },
                            tint = Color(0xFF0088FF),
                            backdrop = backdrop
                        ) {

                            Text(text = "hi")
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

            UpdateAlertDialog(
                visible = showDialog,
                appName = "App Name",
                onDismiss = { showDialog = false },
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
            NetworkAlertDialog(
                visible = showDialog,
                onDismiss = { showDialog = false },
                onConfirm = { showDialog = false }
            )


//            AlertDialog(
//                visible = showDialog,
//                onCancel = { showDialog = false }
//            )
        }
    }
}

@Composable
expect fun AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onCancel: () -> Unit
)

@Composable
expect fun DeviceBox(
    modifier: Modifier = Modifier
)