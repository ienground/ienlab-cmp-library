package zone.ien.utils.icon

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import zone.ien.utils.icon.IconData.Paint
import zone.ien.utils.icon.IconData.Vector

/**
 * 다양한 아이콘 유형을 나타내는 sealed interface
 *
 * 이 sealed interface는 서로 다른 아이콘 유형(vector 아이콘과 painter 아이콘)을 안전하게 다루기 위한
 * 일관된 방식을 제공합니다.
 *
 * @see Vector ImageVector 기반 아이콘
 * @see Paint Painter 기반 아이콘
 */
sealed interface IconData {
    /**
     * Vector 아이콘을 나타내는 데이터 클래스
     *
     * @property imageVector 표시할 ImageVector
     */
    data class Vector(val imageVector: ImageVector): IconData

    /**
     * Painter 아이콘을 나타내는 데이터 클래스
     *
     * @property painter 표시할 Painter
     */
    data class Paint(val painter: Painter): IconData

    /**
     * IconData의 Companion 객체
     */
    companion object
}

/**
 * 다양한 유형의 아이콘을 표시하는 Composable 함수
 *
 * 이 함수는 아이콘 데이터를 받아 유형에 따라 적절하게 표시합니다.
 * (vector 또는 painter 유형)
 *
 * @param icon 표시할 아이콘 데이터
 * @param contentDescription 접근성을 위한 콘텐츠 설명
 * @param modifier 아이콘에 적용할 수정자
 * @param tint 아이콘에 적용할 틴트 색상 (기본값: LocalContentColor.current)
 */
@Composable
fun ComplexIcon(
    icon: IconData,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    when (icon) {
        is Vector -> {
            androidx.compose.material3.Icon(
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }
        is Paint -> {
            androidx.compose.material3.Icon(
                painter = icon.painter,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }
    }
}

