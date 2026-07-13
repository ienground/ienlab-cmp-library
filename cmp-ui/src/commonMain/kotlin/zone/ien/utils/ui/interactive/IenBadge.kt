package zone.ien.utils.ui.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

enum class IenBadgeSize { Small, Medium, Large }

sealed interface IenBadgeVariant {
    data object Fill : IenBadgeVariant
    data object Weak : IenBadgeVariant
    data object Line : IenBadgeVariant
}

@Composable
fun IenBadge(
    text: String,
    modifier: Modifier = Modifier,
    size: IenBadgeSize = IenBadgeSize.Medium,
    variant: IenBadgeVariant = IenBadgeVariant.Weak,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val content = when (variant) {
        IenBadgeVariant.Fill -> Color.White
        IenBadgeVariant.Weak, IenBadgeVariant.Line -> toneColor(tone)
    }
    val container = when (variant) {
        IenBadgeVariant.Fill -> toneColor(tone)
        IenBadgeVariant.Weak -> toneWeakColor(tone)
        IenBadgeVariant.Line -> Color.Transparent
    }
    val border = if (variant == IenBadgeVariant.Line) BorderStroke(IenTheme.stroke.thin, toneColor(tone)) else null

    IenSurface(
        modifier = modifier,
        color = container,
        contentColor = content,
        shape = RoundedCornerShape(IenTheme.radius.full),
        border = border,
    ) {
        IenProvideTextStyle(size.textStyle(), LocalContentColor.current) {
            Row(
                modifier = Modifier.padding(size.padding()),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leadingIcon?.invoke()
                IenText(text = text, style = size.textStyle(), color = LocalContentColor.current)
            }
        }
    }
}

@Composable
private fun IenBadgeSize.textStyle() = when (this) {
    IenBadgeSize.Small -> IenTheme.typography.caption
    IenBadgeSize.Medium -> IenTheme.typography.label2
    IenBadgeSize.Large -> IenTheme.typography.label1
}

private fun IenBadgeSize.padding() = when (this) {
    IenBadgeSize.Small -> PaddingValues(horizontal = 6.dp, vertical = 2.dp)
    IenBadgeSize.Medium -> PaddingValues(horizontal = 8.dp, vertical = 3.dp)
    IenBadgeSize.Large -> PaddingValues(horizontal = 10.dp, vertical = 5.dp)
}
