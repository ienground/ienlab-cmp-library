package zone.ien.utils.ui.dialog

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun M3ProgressDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    isLoadingIndicator: Boolean = true,
    isWavyIndicator: Boolean = true
) {
    val dialogShape = LocalDialogShape.current ?: LocalDialogProviderDefault.Shape
    val dialogBorder = LocalDialogBorder.current
    val dialogBackgroundColor = LocalDialogBackgroundColor.current ?: LocalDialogProviderDefault.BackgroundColor

    if (visible) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = true)
        ) {
            Surface(
                shape = dialogShape,
                color = dialogBackgroundColor,
                border = dialogBorder,
                tonalElevation = 6.dp,
                modifier = modifier.height(IntrinsicSize.Min)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    if (isLoadingIndicator) {
                        LoadingIndicator(modifier = Modifier.padding(16.dp))
                    } else if (isWavyIndicator) {
                        CircularWavyProgressIndicator(modifier = Modifier.padding(16.dp))
                    } else {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun M3ProgressDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    progress: () -> Float,
    isWavyIndicator: Boolean = true
) {
    val dialogShape = LocalDialogShape.current ?: LocalDialogProviderDefault.Shape
    val dialogBorder = LocalDialogBorder.current
    val dialogBackgroundColor = LocalDialogBackgroundColor.current ?: LocalDialogProviderDefault.BackgroundColor

    val currentProgress by animateFloatAsState(
        targetValue = progress.invoke().let { if (it > 1f) 1f else if (it < 0f) 0f else it }
    )

    if (visible) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = true)
        ) {
            Surface(
                shape = dialogShape,
                color = dialogBackgroundColor,
                border = dialogBorder,
                tonalElevation = 6.dp,
                modifier = modifier.height(IntrinsicSize.Min)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    if (isWavyIndicator) {
                        CircularWavyProgressIndicator(
                            progress = { currentProgress },
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        CircularProgressIndicator(
                            progress = { currentProgress },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}