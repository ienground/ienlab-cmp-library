package zone.ien.utils.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
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
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.interactive.toneColor
import zone.ien.utils.ui.interactive.toneOnColor
import zone.ien.utils.ui.interactive.toneWeakColor
import zone.ien.utils.ui.primitives.IenIcon

/** 링크 아이콘의 색상 강도를 정의합니다. */
enum class IenLinkIconToneVariant {
    /** 톤의 원래 색상을 배경에 사용합니다. */
    Solid,

    /** 톤의 약한 배경색을 사용합니다. */
    Weak,
}

/** 링크 아이콘의 배경과 콘텐츠에 사용할 색상 묶음입니다. */
@Immutable
data class IenLinkIconColors(
    /** 링크 아이콘 컨테이너 색상입니다. */
    val container: Color,

    /** 링크 아이콘 내부 아이콘 또는 텍스트 색상입니다. */
    val content: Color,
)

/** [IenLinkIcon]의 기본 색상과 모양을 제공합니다. */
object IenLinkIconDefaults {
    /** 의미적 톤과 강도에 맞는 링크 아이콘 색상을 생성합니다. */
    @Composable
    fun colors(
        tone: IenSemanticTone = IenSemanticTone.Brand,
        toneVariant: IenLinkIconToneVariant = IenLinkIconToneVariant.Weak,
        container: Color = when (toneVariant) {
            IenLinkIconToneVariant.Solid -> toneColor(tone)
            IenLinkIconToneVariant.Weak -> toneWeakColor(tone)
        },
        content: Color = when (toneVariant) {
            IenLinkIconToneVariant.Solid -> toneOnColor(tone)
            IenLinkIconToneVariant.Weak -> toneColor(tone)
        },
    ): IenLinkIconColors = IenLinkIconColors(
        container = container,
        content = content,
    )

    /** 링크 아이콘의 기본 모양입니다. */
    val Shape: Shape = CircleShape
}

/**
 * 섹션에서 의미적 톤에 맞춘 링크 아이콘을 표시합니다.
 *
 * @param painter 표시할 아이콘을 그리는 Painter입니다.
 * @param modifier 컴포저블에 적용할 [Modifier]입니다.
 * @param tone 링크 아이콘의 의미적 색상 톤입니다.
 * @param toneVariant 링크 아이콘 색상의 강도입니다.
 * @param colors 컨테이너와 아이콘에 적용할 색상입니다.
 * @param shape 아이콘 컨테이너의 모양입니다.
 * @param contentDescription 아이콘의 접근성 설명입니다.
 */
@Composable
fun IenLinkIcon(
    painter: Painter,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    toneVariant: IenLinkIconToneVariant = IenLinkIconToneVariant.Weak,
    colors: IenLinkIconColors = IenLinkIconDefaults.colors(tone, toneVariant),
    shape: Shape = IenLinkIconDefaults.Shape,
    contentDescription: String? = null,
) = IenIcon(
    painter = painter,
    contentDescription = contentDescription,
    tint = colors.content,
    modifier =
        modifier
            .clip(shape)
            .background(colors.container)
            .padding(6.dp)
            .size(20.dp)
)

/** [ImageVector]를 사용하는 링크 아이콘을 표시합니다. */
@Composable
fun IenLinkIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    toneVariant: IenLinkIconToneVariant = IenLinkIconToneVariant.Weak,
    colors: IenLinkIconColors = IenLinkIconDefaults.colors(tone, toneVariant),
    shape: Shape = IenLinkIconDefaults.Shape,
    contentDescription: String? = null,
) = IenLinkIcon(
    painter = rememberVectorPainter(imageVector),
    modifier = modifier,
    tone = tone,
    toneVariant = toneVariant,
    colors = colors,
    shape = shape,
    contentDescription = contentDescription,
)

/** 링크 아이콘의 톤과 모양을 사용해 텍스트를 표시합니다. */
@Composable
fun IenLinkIconText(
    text: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    toneVariant: IenLinkIconToneVariant = IenLinkIconToneVariant.Weak,
    colors: IenLinkIconColors = IenLinkIconDefaults.colors(tone, toneVariant),
    shape: Shape = IenLinkIconDefaults.Shape,
) = Text(
    text = text,
    color = colors.content,
    textAlign = TextAlign.Center,
    fontSize = 14.sp,
    modifier =
        modifier
            .clip(shape)
            .background(colors.container)
            .padding(6.dp)
            .size(20.dp)
            .wrapContentHeight(align = Alignment.CenterVertically, unbounded = true)
)
