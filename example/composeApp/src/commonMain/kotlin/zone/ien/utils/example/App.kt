package zone.ien.utils.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.kashif_e.backdrop.backdrops.rememberBackdrop
import com.kashif_e.backdrop.backdrops.rememberCanvasBackdrop
import com.kashif_e.backdrop.backdrops.rememberLayerBackdrop
import ienlab_cmp_library.example.composeapp.generated.resources.Res
import ienlab_cmp_library.example.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import zone.ien.utils.adaptive.components.LiquidButton
import zone.ien.utils.adaptive.components.LiquidSlider
import zone.ien.utils.adaptive.components.LiquidToggle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
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
            var value by remember { mutableStateOf(0.5f) }

            Box(
                modifier = Modifier.padding(it)
            ) {
                Image(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(16.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            text = "label",
//                            modifier = Modifier.weight(1f)
//                        )
//                        LiquidToggle(
//                            selected = { true },
//                            onSelect = {},
//                            backdrop = backdrop
//                        )
//                    }
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
                        Button(
                            onClick = { showDialog = true },
//                        tint = Color(0xFF0088FF),
//                            backdrop = backdrop
                        ) {
                            Text(text = "hi")
                        }
//                        LiquidToggle(
//                            selected = { true },
//                            onSelect = {},
//                            backdrop = backdrop
//                        )
                    }
//                    LiquidSlider(
//                        value = { value },
//                        onValueChange = { value = it },
//                        backdrop = backdrop,
//                        valueRange = 0f..1f,
//                        visibilityThreshold = 0.01f,
//                        modifier = Modifier.fillMaxWidth()
//                    )
                }
            }



            AlertDialog(
                visible = showDialog,
                onCancel = { showDialog = false }
            )
        }
    }
}

@Composable
expect fun AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onCancel: () -> Unit
)