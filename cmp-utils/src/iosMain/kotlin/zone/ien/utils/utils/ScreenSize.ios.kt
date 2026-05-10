package zone.ien.utils.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import platform.UIKit.UIScreen

/**
 * iOS용 화면 너비 가져오기.
 * 
 * 이 함수는 iOS 플랫폼에서 화면 너비를 가져옵니다.
 * 결과는 Dp (density-independent pixels) 단위로 반환됩니다.
 * 
 * @return 화면 너비 (Dp 단위)
 */
@Composable
actual fun getScreenWidth(): Dp = LocalWindowInfo.current.containerSize.width.pxToPoint().dp

/**
 * iOS implementation of getScreenHeight.
 *
 * This function retrieves the screen height on iOS platform using the window information.
 * The result is returned in Dp (density-independent pixels) units.
 *
 * @return The screen height as a Dp value
 */
@Composable
actual fun getScreenHeight(): Dp = LocalWindowInfo.current.containerSize.height.pxToPoint().dp

/**
 * Converts pixels to points for iOS platform.
 *
 * This helper function converts pixel values to points using the screen's scale factor.
 *
 * @return The converted value in points
 */
fun Int.pxToPoint(): Double = this.toDouble() / UIScreen.mainScreen.scale