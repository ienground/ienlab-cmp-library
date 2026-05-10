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
 * iOS용 화면 높이 가져오기.
 *
 * 이 함수는 창 정보를 사용하여 iOS 플랫폼에서 화면 높이를 가져옵니다.
 * 결과는 Dp (density-independent pixels) 단위로 반환됩니다.
 *
 * @return 화면 높이 (Dp 단위)
 */
@Composable
actual fun getScreenHeight(): Dp = LocalWindowInfo.current.containerSize.height.pxToPoint().dp

/**
 * iOS 플랫폼을 위해 픽셀을 포인트로 변환합니다.
 *
 * 이 헬퍼 함수는 화면의 스케일 팩터를 사용하여 픽셀 값을 포인트로 변환합니다.
 *
 * @return 포인트로 변환된 값
 */
fun Int.pxToPoint(): Double = this.toDouble() / UIScreen.mainScreen.scale