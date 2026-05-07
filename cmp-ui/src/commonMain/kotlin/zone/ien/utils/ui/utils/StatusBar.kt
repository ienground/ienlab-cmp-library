package zone.ien.utils.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun setStatusBarStyle(
    isDarkTheme: Boolean
): Boolean