package zone.ien.utils.example

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onCancel: () -> Unit
) {
    if (visible) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = onCancel,
            confirmButton = { TextButton(onClick = {}) { Text(text = "confirm") } },
            dismissButton = { TextButton(onClick = onCancel) { Text(text = "dismiss") } },
            title = { Text(text = "Hello World") },
            text = { Text(text = "Hello World") },
            modifier = modifier
        )
    }
}