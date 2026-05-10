package zone.ien.utils.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Android implementation of getScreenWidth.
 *
 * This function retrieves the screen width on Android platform using the LocalConfiguration.
 * The result is returned in Dp (density-independent pixels) units.
 *
 * @return The screen width as a Dp value
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
actual fun getScreenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp

/**
 * Android implementation of getScreenHeight.
 *
 * This function retrieves the screen height on Android platform using the LocalConfiguration.
 * The result is returned in Dp (density-independent pixels) units.
 *
 * @return The screen height as a Dp value
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
actual fun getScreenHeight(): Dp = LocalConfiguration.current.screenHeightDp.dp