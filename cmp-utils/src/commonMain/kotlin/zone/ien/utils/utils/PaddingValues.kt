package zone.ien.utils.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

/**
 * 현재 패딩 값을 기반으로 새 PaddingValues를 생성합니다.
 *
 * 이 함수는 현재 패딩 값을 기반으로 새로운 PaddingValues 인스턴스를 생성하며,
 * 각 차원에 대해 지정된 값으로 업데이트합니다. 명시적으로 제공되지 않은 경우 현재 값을 기준으로 합니다.
 *
 * @param top 위쪽 패딩 값 (기본값: 현재 위쪽 패딩)
 * @param start 왼쪽 패딩 값 (기본값: 현재 왼쪽 패딩)
 * @param bottom 아래쪽 패딩 값 (기본값: 현재 아래쪽 패딩)
 * @param end 오른쪽 패딩 값 (기본값: 현재 오른쪽 패딩)
 * @return 업데이트된 값을 가진 새로운 PaddingValues 인스턴스
 */
@Composable
@ReadOnlyComposable
fun PaddingValues.copy(
    top: Dp = calculateTopPadding(),
    start: Dp = calculateStartPadding(LocalLayoutDirection.current),
    bottom: Dp = calculateBottomPadding(),
    end: Dp = calculateEndPadding(LocalLayoutDirection.current),
): PaddingValues =
    PaddingValues(
        top = top,
        start = start,
        end = end,
        bottom = bottom,
    )

/**
 * 두 패딩 값을 더하여 병합합니다.
 *
 * 이 연산자 함수는 두 PaddingValues를 더하여 각각의 위쪽, 아래쪽, 왼쪽, 오른쪽 패딩 값을 합한 새로운
 * PaddingValues 인스턴스를 생성합니다.
 *
 * @param other 더할 다른 PaddingValues
 * @return 더해진 값들을 가진 새로운 PaddingValues 인스턴스
 */
@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current

    return PaddingValues(
        top = calculateTopPadding() + other.calculateTopPadding(),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
        start = calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
        end = calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection),
    )
}