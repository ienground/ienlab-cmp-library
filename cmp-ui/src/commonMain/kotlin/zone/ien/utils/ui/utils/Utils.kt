package zone.ien.utils.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 조건에 따라 Modifier를 적용하는 확장 함수
 * 
 * 이 함수는 특정 조건이 참일 경우에만 Modifier를 적용합니다.
 * 조건이 거짓일 경우 원래 Modifier를 그대로 반환합니다.
 * 
 * 사용 예시:
 * ```kotlin
 * modifier
 *     .conditional(shouldApplyPadding) {
 *         padding(16.dp)
 *     }
 *     .conditional(shouldApplyBackground) {
 *         background(Color.Blue)
 *     }
 * ```
 * 
 * @param condition 적용할 조건
 * @param modifier 조건이 참일 때 적용할 Modifier 블록
 * @return 조건에 따라 수정된 Modifier
 */
@Composable
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}