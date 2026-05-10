package zone.ien.utils.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Android용 화면 너비 가져오기.
 * 
 * 이 함수는 Android 플랫폼에서 화면 너비를 가져옵니다.
 * 결과는 Dp (density-independent pixels) 단위로 반환됩니다.
 * 
 * @return 화면 너비 (Dp 단위)
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
actual fun getScreenWidth(): Dp = LocalConfiguration.current.screenWidthDp.dp

/**
 * Android용 화면 높이 가져오기.
 *
 * 이 함수는 LocalConfiguration을 사용하여 Android 플랫폼에서 화면 높이를 가져옵니다.
 * 결과는 Dp (density-independent pixels) 단위로 반환됩니다.
 *
 * @return 화면 높이 (Dp 단위)
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
actual fun getScreenHeight(): Dp = LocalConfiguration.current.screenHeightDp.dp