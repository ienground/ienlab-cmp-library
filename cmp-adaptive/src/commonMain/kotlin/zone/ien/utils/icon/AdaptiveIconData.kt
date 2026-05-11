package zone.ien.utils.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import zone.ien.hig.adaptive.icons.AdaptiveIcons

/**
 * 적응형 아이콘 데이터 생성자
 *
 * Material과 Cupertino 스타일의 아이콘 데이터를 제공하는 팩토리 함수.
 *
 * @param material Material 디자인 스타일의 아이콘
 * @param cupertino Cupertino 디자인 스타일의 아이콘
 * @return 아이콘 데이터의 페인트 객체
 */
@Composable
fun IconData.Companion.Adaptive(
    material: @Composable () -> ImageVector,
    cupertino: @Composable () -> String
): IconData.Paint {
    return IconData.Paint(
        painter = AdaptiveIcons.painter(
            material = material,
            cupertino = cupertino
        )
    )
}