package zone.ien.utils.ui.components.composite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

@Composable
fun IenParagraph(
    title: String? = null,
    body: String,
    modifier: Modifier = Modifier,
    emphasis: String? = null,
    footer: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        if (title != null) {
            IenText(title, style = IenTheme.typography.title3)
        }
        if (emphasis != null) {
            IenHighlightText(
                text = body,
                highlights = listOf(emphasis),
            )
        } else {
            IenText(body, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
        }
        if (footer != null) {
            IenText(footer, style = IenTheme.typography.caption, color = IenTheme.colors.textTertiary)
        }
    }
}

@Composable
fun IenPost(
    title: String,
    modifier: Modifier = Modifier,
    author: String? = null,
    description: String? = null,
    media: (@Composable () -> Unit)? = null,
    metadata: (@Composable RowScope.() -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
) {
    IenSurface(
        modifier = modifier.fillMaxWidth(),
        color = IenTheme.colors.surface,
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        ) {
            if (author != null || metadata != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                ) {
                    if (author != null) {
                        IenText(
                            text = author,
                            modifier = Modifier.weight(1f),
                            style = IenTheme.typography.caption,
                            color = IenTheme.colors.textTertiary,
                        )
                    }
                    metadata?.invoke(this)
                }
            }
            IenText(title, style = IenTheme.typography.title3, maxLines = 2, overflow = TextOverflow.Ellipsis)
            if (description != null) {
                IenText(description, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
            }
            media?.invoke()
            action?.invoke()
        }
    }
}
