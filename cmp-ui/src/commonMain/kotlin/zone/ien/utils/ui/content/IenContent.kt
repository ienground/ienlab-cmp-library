package zone.ien.utils.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.toneColor
import zone.ien.utils.ui.interactive.toneWeakColor
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

enum class IenBubbleTail {
    None,
    Start,
    End,
}

@Composable
fun IenBubble(
    text: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    tail: IenBubbleTail = IenBubbleTail.None,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
) {
    val container = if (tone == IenSemanticTone.Neutral) IenTheme.colors.surfaceWeak else toneWeakColor(tone)
    val content = if (tone == IenSemanticTone.Neutral) IenTheme.colors.textPrimary else toneColor(tone)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        if (tail == IenBubbleTail.Start) {
            BubbleTail(color = container)
        }
        IenSurface(
            color = container,
            contentColor = content,
            shape = RoundedCornerShape(IenTheme.radius.lg),
        ) {
            IenText(
                text = text,
                modifier = Modifier.padding(contentPadding),
                style = IenTheme.typography.body2,
                color = LocalContentColor.current,
            )
        }
        if (tail == IenBubbleTail.End) {
            BubbleTail(color = container)
        }
    }
}

@Composable
private fun BubbleTail(color: Color) {
    Box(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .rotate(45f)
            .background(color)
            .padding(4.dp),
    )
}

@Composable
fun IenHighlightText(
    text: String,
    highlights: List<String>,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    ignoreCase: Boolean = true,
) {
    val highlightColor = toneColor(tone)
    val annotated = buildAnnotatedString {
        var cursor = 0
        while (cursor < text.length) {
            val match = highlights
                .asSequence()
                .filter { it.isNotEmpty() }
                .mapNotNull { query ->
                    val index = text.indexOf(query, startIndex = cursor, ignoreCase = ignoreCase)
                    if (index >= 0) index to query.length else null
                }
                .minByOrNull { it.first }

            if (match == null) {
                append(text.substring(cursor))
                break
            }

            val start = match.first
            val end = start + match.second
            if (cursor < start) append(text.substring(cursor, start))
            withStyle(SpanStyle(color = highlightColor)) {
                append(text.substring(start, end))
            }
            cursor = end
        }
    }

    androidx.compose.material3.Text(
        text = annotated,
        modifier = modifier,
        style = IenTheme.typography.body2,
        color = IenTheme.colors.textPrimary,
    )
}
