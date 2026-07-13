package zone.ien.utils.ui.primitives

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.toneColor
import zone.ien.utils.ui.interactive.toneWeakColor
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface

enum class IenAssetFrameSize {
    Small,
    Medium,
    Large,
    ExtraLarge,
}

sealed interface IenAssetFrameShape {
    data object Rounded : IenAssetFrameShape
    data object Circle : IenAssetFrameShape
}

@Composable
fun IenAssetFrame(
    modifier: Modifier = Modifier,
    size: IenAssetFrameSize = IenAssetFrameSize.Medium,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    shape: IenAssetFrameShape = IenAssetFrameShape.Rounded,
    bordered: Boolean = false,
    contentDescription: String? = null,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit,
) {
    val containerColor = if (tone == IenSemanticTone.Neutral) {
        IenTheme.colors.surfaceWeak
    } else {
        toneWeakColor(tone)
    }
    val contentColor = if (tone == IenSemanticTone.Neutral) {
        IenTheme.colors.textPrimary
    } else {
        toneColor(tone)
    }

    IenSurface(
        modifier = modifier
            .size(size.value)
            .then(
                if (contentDescription != null) {
                    Modifier.semantics { this.contentDescription = contentDescription }
                } else {
                    Modifier
                },
            ),
        color = containerColor,
        contentColor = contentColor,
        shape = shape.toShape(),
        border = if (bordered) BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border) else null,
    ) {
        Box(contentAlignment = contentAlignment) {
            IenProvideTextStyle(
                style = IenTheme.typography.title3,
                color = LocalContentColor.current,
                content = content,
            )
        }
    }
}

private val IenAssetFrameSize.value: Dp
    get() = when (this) {
        IenAssetFrameSize.Small -> 32.dp
        IenAssetFrameSize.Medium -> 44.dp
        IenAssetFrameSize.Large -> 56.dp
        IenAssetFrameSize.ExtraLarge -> 72.dp
    }

@Composable
private fun IenAssetFrameShape.toShape(): Shape = when (this) {
    IenAssetFrameShape.Rounded -> RoundedCornerShape(IenTheme.radius.lg)
    IenAssetFrameShape.Circle -> CircleShape
}
