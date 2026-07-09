package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenTheme

enum class IenBubbleBackground {
    Brand, Grey
}

@Composable
fun IenBubble(
    background: IenBubbleBackground,
    modifier: Modifier = Modifier,
    withTail: Boolean = true,
    children: @Composable () -> Unit
) {
    val backgroundColor = if (background == IenBubbleBackground.Brand) {
        IenTheme.colors.brand
    } else {
        IenTheme.colors.surfaceVariant
    }

    val contentColor = if (background == IenBubbleBackground.Brand) {
        Color.White
    } else {
        IenTheme.colors.textPrimary
    }

    val isLeft = (background == IenBubbleBackground.Grey)

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topStart = if (isLeft) 4.dp else 16.dp,
                    topEnd = if (isLeft) 16.dp else 4.dp,
                    bottomStart = if (isLeft && !withTail) 4.dp else 16.dp,
                    bottomEnd = if (!isLeft && !withTail) 4.dp else 16.dp,
                )
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            children()
        }
    }
}
