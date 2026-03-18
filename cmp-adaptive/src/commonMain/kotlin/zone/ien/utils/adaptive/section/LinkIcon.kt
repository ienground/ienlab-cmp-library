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
import zone.ien.utils.ui.section.M3LinkIcon
import zone.ien.utils.ui.section.M3LinkIconText
import zone.ien.utils.ui.section.MaterialLabelIconDefaults

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveLinkIcon(
    icon: IconData,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<CupertinoLinkIconAdaptation, MaterialLinkIconAdaptation>.() -> Unit = { },
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

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveLinkIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<CupertinoLinkIconAdaptation, MaterialLinkIconAdaptation>.() -> Unit = { },
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
            M3LinkIcon(
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

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveLinkIconText(
    text: String,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<CupertinoLinkIconAdaptation, MaterialLinkIconAdaptation>.() -> Unit = { },
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
            M3LinkIconText(
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
    adaptation: AdaptationScope<CupertinoLinkIconAdaptation, MaterialLinkIconAdaptation>.() -> Unit = { },
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
            M3LinkIcon(
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

class CupertinoLinkIconAdaptation(
    containerColor: Color,
    tint: Color,
    shape: Shape,
) {
    var containerColor: Color by mutableStateOf(containerColor)
    var tint: Color by mutableStateOf(tint)
    var shape: Shape by mutableStateOf(shape)
}

class MaterialLinkIconAdaptation(
    containerColor: Color,
    tint: Color,
    shape: Shape,
) {
    var containerColor: Color by mutableStateOf(containerColor)
    var tint: Color by mutableStateOf(tint)
    var shape: Shape by mutableStateOf(shape)
}

@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private class LinkIconAdaptation: Adaptation<CupertinoLinkIconAdaptation, MaterialLinkIconAdaptation>() {
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

    @Composable
    override fun rememberMaterialAdaptation(): MaterialLinkIconAdaptation {
        val containerColor = MaterialLabelIconDefaults.ContainerColor
        val tint = MaterialLabelIconDefaults.Tint
        val shape = MaterialLabelIconDefaults.Shape

        return remember(containerColor, tint, shape) {
            MaterialLinkIconAdaptation(
                containerColor, tint, shape
            )
        }
    }
}

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