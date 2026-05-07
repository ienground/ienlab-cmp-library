package zone.ien.utils.ui.utils

import androidx.compose.runtime.Composable

@Composable
expect fun setStatusBarStyle(
    isDarkTheme: Boolean
): Boolean
