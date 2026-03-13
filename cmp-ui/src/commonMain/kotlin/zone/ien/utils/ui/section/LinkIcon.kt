package zone.ien.utils.ui.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
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

@Composable
fun M3LinkIcon(
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

@Composable
fun M3LinkIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialLabelIconDefaults.ContainerColor,
    tint: Color = MaterialLabelIconDefaults.Tint,
    shape: Shape = MaterialLabelIconDefaults.Shape,
    contentDescription: String? = null,
) = M3LinkIcon(
    painter = rememberVectorPainter(imageVector),
    modifier = modifier,
    containerColor = containerColor,
    tint = tint,
    shape = shape,
    contentDescription = contentDescription,
)

@Composable
fun M3LinkIconText(
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

@Immutable
object MaterialLabelIconDefaults {
    val ContainerColor: Color
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.primaryContainer

    val Tint: Color
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.onPrimaryContainer

    val Shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = CircleShape
}
