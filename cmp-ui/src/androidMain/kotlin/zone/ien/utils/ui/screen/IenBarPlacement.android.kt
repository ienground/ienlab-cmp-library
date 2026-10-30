package zone.ien.utils.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState

@Composable
internal actual fun rememberIenBarPlacement(): State<IenBarPlacement?> = rememberUpdatedState(null)
