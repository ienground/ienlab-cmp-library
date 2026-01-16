package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ProgressDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    isLoadingIndicator: Boolean = true,
    isWavyIndicator: Boolean = true
)

@Composable
expect fun ProgressDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    progress: () -> Float,
    isWavyIndicator: Boolean = true
)