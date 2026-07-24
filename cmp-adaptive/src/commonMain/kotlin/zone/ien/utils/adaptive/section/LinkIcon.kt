package zone.ien.utils.adaptive.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zone.ien.hig.CupertinoIconDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.CupertinoLabelIconDefaults
import zone.ien.hig.section.CupertinoLinkIcon
import zone.ien.utils.icon.IconData
import zone.ien.utils.ui.section.IenLinkIcon
import zone.ien.utils.ui.section.IenLinkIconText
import zone.ien.utils.ui.section.MaterialLabelIconDefaults

/**
 * 적응형 링크 아이콘 컴포저블
 *
 * @param icon 아이콘 데이터
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param adaptation 적응형 스타일 설정을 위한 범위
 * @param contentDescription 접근성 설명
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveLinkIcon(
    icon: IconData,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<CupertinoLinkIconAdaptation, IenLinkIconAdaptation>.() -> Unit = { },
    contentDescription: String? = null
) {
    when (icon) {
        is IconData.Vector -> {
            AdaptiveLinkIcon(
                imageVector = icon.imageVector,
                modifier = modifier,
                adaptation = adaptation,
                contentDescription = contentDescription
            )
        }
        is IconData.Paint -> {
            AdaptiveLinkIcon(
                painter = icon.painter,
                modifier = modifier,
                adaptation = adaptation,
                contentDescription = contentDescription
            )
        }
    }
}

/**
 * 적응형 링크 아이콘 컴포저블 (ImageVector 버전)
 *
 * @param imageVector 이미지 벡터
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param adaptation 적응형 스타일 설정을 위한 범위
 * @param contentDescription 접근성 설명
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveLinkIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<CupertinoLinkIconAdaptation, IenLinkIconAdaptation>.() -> Unit = { },
    contentDescription: String? = null,
) {
    AdaptiveWidget(
        adaptation = remember { LinkIconAdaptation() },
        adaptationScope = adaptation,
        cupertino = {
            CupertinoLinkIcon(
                imageVector = imageVector,
                containerColor = it.containerColor,
                tint = it.tint,
                shape = it.shape,
                contentDescription = contentDescription,
                modifier = modifier
            )
        },
        material = {
            IenLinkIcon(
                imageVector = imageVector,
                containerColor = it.containerColor,
                tint = it.tint,
                shape = it.shape,
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
    )
}

/**
 * 적응형 링크 아이콘 텍스트 컴포저블
 *
 * @param text 텍스트
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param adaptation 적응형 스타일 설정을 위한 범위
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveLinkIconText(
    text: String,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<CupertinoLinkIconAdaptation, IenLinkIconAdaptation>.() -> Unit = { },
) {
    AdaptiveWidget(
        adaptation = remember { LinkIconAdaptation() },
        adaptationScope = adaptation,
        cupertino = {
            CupertinoLinkIconText(
                text = text,
                containerColor = it.containerColor,
                tint = it.tint,
                shape = it.shape,
                modifier = modifier
            )
        },
        material = {
            IenLinkIconText(
                text = text,
                containerColor = it.containerColor,
                tint = it.tint,
                shape = it.shape,
                modifier = modifier
            )
        }
    )
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveLinkIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<CupertinoLinkIconAdaptation, IenLinkIconAdaptation>.() -> Unit = { },
    contentDescription: String? = null,
) {
    AdaptiveWidget(
        adaptation = remember { LinkIconAdaptation() },
        adaptationScope = adaptation,
        cupertino = {
            CupertinoLinkIcon(
                painter = painter,
                containerColor = it.containerColor,
                tint = it.tint,
                shape = it.shape,
                contentDescription = contentDescription,
                modifier = modifier
            )
        },
        material = {
            IenLinkIcon(
                painter = painter,
                containerColor = it.containerColor,
                tint = it.tint,
                shape = it.shape,
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
    )
}

/**
 * Cupertino 링크 아이콘 적응성 클래스
 *
 * @param containerColor 컨테이너 색상
 * @param tint 틴트 색상
 * @param shape 모양
 */
class CupertinoLinkIconAdaptation(
    containerColor: Color,
    tint: Color,
    shape: Shape,
) {
    var containerColor: Color by mutableStateOf(containerColor)
    var tint: Color by mutableStateOf(tint)
    var shape: Shape by mutableStateOf(shape)
}

/**
 * IEN 링크 아이콘 적응성 클래스
 *
 * @param containerColor 컨테이너 색상
 * @param tint 틴트 색상
 * @param shape 모양
 */
class IenLinkIconAdaptation(
    containerColor: Color,
    tint: Color,
    shape: Shape,
) {
    var containerColor: Color by mutableStateOf(containerColor)
    var tint: Color by mutableStateOf(tint)
    var shape: Shape by mutableStateOf(shape)
}

/**
 * 링크 아이콘 적응성 클래스
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private class LinkIconAdaptation: Adaptation<CupertinoLinkIconAdaptation, IenLinkIconAdaptation>() {
    /**
     * Cupertino 적응성 설정 메서드
     *
     * @return Cupertino 링크 아이콘 적응성 객체
     */
    @Composable
    override fun rememberCupertinoAdaptation(): CupertinoLinkIconAdaptation {
        val containerColor = CupertinoLabelIconDefaults.ContainerColor
        val tint = CupertinoLabelIconDefaults.Tint
        val shape = CupertinoLabelIconDefaults.Shape

        return remember(containerColor, tint, shape) {
            CupertinoLinkIconAdaptation(
                containerColor, tint, shape
            )
        }
    }

    /**
     * IEN 적응성 설정 메서드
     *
     * @return IEN 링크 아이콘 적응성 객체
     */
    @Composable
    override fun rememberMaterialAdaptation(): IenLinkIconAdaptation {
        val containerColor = MaterialLabelIconDefaults.ContainerColor
        val tint = MaterialLabelIconDefaults.Tint
        val shape = MaterialLabelIconDefaults.Shape

        return remember(containerColor, tint, shape) {
            IenLinkIconAdaptation(
                containerColor, tint, shape
            )
        }
    }
}

/**
 * Cupertino 링크 아이콘 텍스트 컴포저블
 *
 * @param text 텍스트
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param containerColor 컨테이너 색상
 * @param tint 틴트 색상
 * @param shape 모양
 */
@Composable
fun CupertinoLinkIconText(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = CupertinoLabelIconDefaults.ContainerColor,
    tint: Color = CupertinoLabelIconDefaults.Tint,
    shape: Shape = CupertinoLabelIconDefaults.Shape,
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
            .size(CupertinoIconDefaults.MediumSize)
            .wrapContentHeight(align = Alignment.CenterVertically, unbounded = true)
)
