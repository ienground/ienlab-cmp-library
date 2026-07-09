package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.interactive.IenTextButton
import zone.ien.utils.ui.components.primitives.IenText
import zone.ien.utils.ui.utils.instantPress

@Composable
fun IenListHeader(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            IenText(
                text = title,
                style = IenTheme.typography.title3,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (description != null) {
                IenText(
                    text = description,
                    style = IenTheme.typography.caption,
                    color = IenTheme.colors.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (actionText != null && onActionClick != null) {
            IenTextButton(text = actionText, onClick = onActionClick)
        }
        trailing?.invoke(this)
    }
}

enum class IenListFooterBorder {
    Full, Indented, None
}

object IenListFooterDefaults {
    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = IenTheme.colors.brand,
        fontWeight: FontWeight = FontWeight.Medium
    ) {
        IenText(
            text = text,
            modifier = modifier,
            style = IenTheme.typography.body2.copy(fontWeight = fontWeight),
            color = color
        )
    }

    @Composable
    fun Hairline(
        modifier: Modifier = Modifier,
        indent: Dp = 0.dp,
        color: Color = IenTheme.colors.border.copy(alpha = 0.4f)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = indent)
                .height(IenTheme.stroke.thin)
                .background(color)
        )
    }

    @Composable
    fun Shadow(
        modifier: Modifier = Modifier,
        color: Color = IenTheme.colors.brand.copy(alpha = 0.08f)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(color, Color.Transparent)
                    )
                )
        )
    }
}

@Composable
fun IenListFooter(
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    border: IenListFooterBorder = IenListFooterBorder.Full,
    textColor: Color = IenTheme.colors.brand,
    iconColor: Color = IenTheme.colors.brand,
    icon: (@Composable () -> Unit)? = null,
    hairline: (@Composable () -> Unit)? = null,
    shadow: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(0.dp),
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // 1. Divider/Hairline 렌더링
        if (hairline != null) {
            hairline()
        } else {
            when (border) {
                IenListFooterBorder.Full -> {
                    IenListFooterDefaults.Hairline()
                }
                IenListFooterBorder.Indented -> {
                    IenListFooterDefaults.Hairline(indent = IenTheme.spacing.md)
                }
                IenListFooterBorder.None -> {
                    // 구분선 없음
                }
            }
        }

        var isPressed by remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }

        // 2. 본체 콘텐츠 (중앙 정렬)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .instantPress(onClick != null) { isPressed = it }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = onClick != null,
                    onClick = { onClick?.invoke() }
                )
        ) {
            // shadow 커스텀 슬롯 렌더링 (눌림 상태일 때 노출)
            if (isPressed && onClick != null) {
                if (shadow != null) {
                    shadow()
                } else {
                    IenListFooterDefaults.Shadow()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .padding(horizontal = IenTheme.spacing.md, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
                if (icon != null) {
                    Box(modifier = Modifier.padding(start = 4.dp))
                    icon()
                }
            }
        }
    }
}

/**
 * 텍스트 문자열만을 받는 편의성 오버로딩 ListFooter
 */
@Composable
fun IenListFooter(
    text: String,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    border: IenListFooterBorder = IenListFooterBorder.Full,
    textColor: Color = IenTheme.colors.brand,
    icon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(0.dp),
) {
    IenListFooter(
        onClick = onClick,
        modifier = modifier,
        border = border,
        textColor = textColor,
        icon = icon,
        shape = shape
    ) {
        IenListFooterDefaults.Text(
            text = text,
            color = textColor
        )
    }
}
