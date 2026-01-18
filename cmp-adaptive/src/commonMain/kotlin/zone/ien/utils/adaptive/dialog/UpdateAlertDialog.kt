package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun UpdateAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    appName: String,
    onDismiss: () -> Unit,
)