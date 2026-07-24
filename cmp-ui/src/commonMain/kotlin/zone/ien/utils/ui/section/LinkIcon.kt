package zone.ien.utils.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zone.ien.utils.ui.foundation.IenTheme

/**
 * Material3 링크 아이콘 컴포저블
 * 
 * 이 컴포저블은 섹션 내에서 링크 아이콘을 표시하기 위한 컴포저블입니다.
 * 다양한 형태의 아이콘을 표시할 수 있으며, 컨테이너 색상과 틴트 색상을 설정할 수 있습니다.
 * 
 * @param painter 아이콘을 표시하기 위한 painter
 * @param modifier 적용할 Modifier
 * @param containerColor 컨테이너의 색상
 * @param tint 아이콘의 틴트 색상
 * @param shape 아이콘의 모양
 * @param contentDescription 콘텐츠 설명
 */
@Composable
fun IenLinkIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialLabelIconDefaults.ContainerColor,
    tint: Color = MaterialLabelIconDefaults.Tint,
    shape: Shape = MaterialLabelIconDefaults.Shape,
    contentDescription: String? = null,
) = Icon(
    painter = painter,
    contentDescription = contentDescription,
    tint = tint,
    modifier =
        modifier
            .clip(shape)
            .background(containerColor)
            .padding(6.dp)
            .size(20.dp)
)

/**
 * Material3 링크 아이콘 컴포저블 (ImageVector 버전)
 * 
 * 이 컴포저블은 ImageVector를 사용하는 버전의 링크 아이콘입니다.
 * 
 * @param imageVector 표시할 아이콘의 ImageVector
 * @param modifier 적용할 Modifier
 * @param containerColor 컨테이너의 색상
 * @param tint 아이콘의 틴트 색상
 * @param shape 아이콘의 모양
 * @param contentDescription 콘텐츠 설명
 */
@Composable
fun IenLinkIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialLabelIconDefaults.ContainerColor,
    tint: Color = MaterialLabelIconDefaults.Tint,
    shape: Shape = MaterialLabelIconDefaults.Shape,
    contentDescription: String? = null,
) = IenLinkIcon(
    painter = rememberVectorPainter(imageVector),
    modifier = modifier,
    containerColor = containerColor,
    tint = tint,
    shape = shape,
    contentDescription = contentDescription,
)

/**
 * Material3 링크 아이콘 텍스트 컴포저블
 * 
 * 이 컴포저블은 텍스트로 된 링크 아이콘을 표시합니다. 주로 섹션 내에서 텍스트 기반 링크를 표시할 때 사용됩니다.
 * 
 * @param text 표시할 텍스트
 * @param modifier 적용할 Modifier
 * @param containerColor 컨테이너의 색상
 * @param tint 텍스트의 색상
 * @param shape 아이콘의 모양
 */
@Composable
fun IenLinkIconText(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialLabelIconDefaults.ContainerColor,
    tint: Color = MaterialLabelIconDefaults.Tint,
    shape: Shape = MaterialLabelIconDefaults.Shape,
) = Text(
    text = text,
    color = tint,
    textAlign = TextAlign.Center,
    fontSize = 14.sp,
    modifier =
        modifier
            .clip(shape)
            .background(containerColor)
            .padding(6.dp)
            .size(20.dp)
            .wrapContentHeight(align = Alignment.CenterVertically, unbounded = true)
)

/**
 * 링크 아이콘에 적용되는 기본 스타일 값을 정의하는 오브젝트
 */
@Immutable
object MaterialLabelIconDefaults {
    /**
     * 링크 아이콘의 기본 컨테이너 배경 색상
     */
    val ContainerColor: Color
        @Composable
        get() = IenTheme.colors.brandWeak

    /**
     * 링크 아이콘의 기본 틴트/콘텐츠 색상
     */
    val Tint: Color
        @Composable
        get() = IenTheme.colors.onBrandWeak

    /**
     * 링크 아이콘의 기본 모양
     */
    val Shape: Shape
        @Composable
        get() = CircleShape
}
