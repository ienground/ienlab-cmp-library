package zone.ien.utils.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/**
 * 화면 너비를 Dp 단위로 가져옵니다.
 *
 * 이 함수는 Compose Dp (밀도 독립적 픽셀) 단위로 화면 너비를 가져오는 교차 플랫폼 방식을 제공합니다.
 *
 * @return 화면 너비를 나타내는 Dp 값
 */
@Composable
expect fun getScreenWidth(): Dp

/**
 * 화면 높이를 Dp 단위로 가져옵니다.
 *
 * 이 함수는 Compose Dp (밀도 독립적 픽셀) 단위로 화면 높이를 가져오는 교차 플랫폼 방식을 제공합니다.
 *
 * @return 화면 높이를 나타내는 Dp 값
 */
@Composable
expect fun getScreenHeight(): Dp