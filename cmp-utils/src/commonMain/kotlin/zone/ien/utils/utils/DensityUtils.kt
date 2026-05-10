package zone.ien.utils.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

/**
 * Dp를 픽셀로 변환합니다.
 *
 * 이 함수는 현재 디스플레이 밀도를 사용하여 Dp(밀도 독립적 픽셀)를 픽셀로 변환합니다.
 * Compose에서 픽셀 값으로 작업할 필요가 있을 때 유용합니다.
 *
 * @param size Dp 단위의 크기
 * @return 픽셀 단위의 크기 (Float)
 */
@Composable
fun dpToPx(size: Dp): Float = with (LocalDensity.current) { size.toPx() }

/**
 * 픽셀을 Dp로 변환합니다.
 *
 * 이 함수는 현재 디스플레이 밀도를 사용하여 픽셀 값을 Dp(밀도 독립적 픽셀)로 변환합니다.
 * Compose의 Dp 단위로 작업할 필요가 있을 때 유용합니다.
 *
 * @param size 픽셀 단위의 크기
 * @return Dp 단위의 크기 (Dp 값)
 */
@Composable
fun pxToDp(size: Int): Dp = with (LocalDensity.current) { size.toDp() }

/**
 * 픽셀을 Dp로 변환합니다.
 *
 * 이 함수는 현재 디스플레이 밀도를 사용하여 픽셀 값을 Dp(밀도 독립적 픽셀)로 변환합니다.
 * Compose의 Dp 단위로 작업할 필요가 있을 때 유용합니다.
 *
 * @param size Float 픽셀 단위의 크기
 * @return Dp 단위의 크기 (Dp 값)
 */
@Composable
fun pxToDp(size: Float): Dp = with (LocalDensity.current) { size.toDp() }

/**
 * 픽셀을 텍스트에 사용하는 Sp(배율 가능 픽셀)로 변환합니다.
 *
 * 이 함수는 현재 디스플레이 밀도를 사용하여 픽셀 값을 텍스트 크기용 Sp(배율 가능 픽셀)로 변환합니다.
 *
 * @param size 픽셀 단위의 크기
 * @return Sp 단위의 크기 (TextUnit 값)
 */
@Composable
fun pxToSp(size: Int): TextUnit = with (LocalDensity.current) { size.toSp() }

/**
 * 픽셀을 텍스트에 사용하는 Sp(배율 가능 픽셀)로 변환합니다.
 *
 * 이 함수는 현재 디스플레이 밀도를 사용하여 픽셀 값을 텍스트 크기용 Sp(배율 가능 픽셀)로 변환합니다.
 *
 * @param size Float 픽셀 단위의 크기
 * @return Sp 단위의 크기 (TextUnit 값)
 */
@Composable
fun pxToSp(size: Float): TextUnit = with (LocalDensity.current) { size.toSp() }