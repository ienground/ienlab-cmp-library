package zone.ien.utils.ui.section.lazy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import zone.ien.utils.ui.foundation.IenTheme

/**
 * Lazy 리스트 섹션을 정의하는 확장 함수
 * 
 * 이 함수는_lazy_ 리스트 내에 섹션을 추가하여 스크롤이 가능한 항목들을 표시합니다.
 * 제목과 캡션을 포함할 수 있으며, 섹션의 콘텐츠를 정의합니다.
 * 
 * @param title 섹션의 제목
 * @param caption 섹션의 캡션
 * @param content 섹션의 내용을 정의하는 LazySectionScope 블록
 */
fun LazyListScope.m3Section(
    title: @Composable (LazyItemScope.() -> Unit)? = null,
    caption: @Composable (LazyItemScope.() -> Unit)? = null,
    content: LazySectionScope.() -> Unit,
) {
    val itemsPadding = PaddingValues.Zero

    item(contentType = SplitPaddingContentType) {
        Spacer(
            Modifier
                .height(16.dp)
                .fillMaxWidth(),
        )
    }

    if (title != null) {
        item(contentType = SectionTitleContentType) {
            SectionTitle(
                modifier = Modifier.animateItem()
            ) {
                title()
            }
        }
    }

    itemsAndCaption(
        itemsPadding = itemsPadding,
        caption = caption,
        content = content,
    )
}

/**
 * 고정형 Lazy 리스트 섹션을 정의하는 확장 함수
 * 
 * 이 함수는 스크롤 시 고정되는 섹션을 추가합니다.
 * 제목과 캡션을 포함할 수 있으며, 섹션의 콘텐츠를 정의합니다.
 * 
 * @param title 섹션의 제목
 * @param caption 섹션의 캡션
 * @param content 섹션의 내용을 정의하는 LazySectionScope 블록
 */
fun LazyListScope.m3StickySection(
    title: @Composable (LazyItemScope.(PaddingValues) -> Unit)? = null,
    caption: @Composable (LazyItemScope.() -> Unit)? = null,
    content: LazySectionScope.() -> Unit,
) {
    val itemsPadding = PaddingValues.Zero

    if (title != null) {
        item(contentType = SectionTitleContentType) {
            SectionTitle {
                title(it)
            }
        }
    }

    itemsAndCaption(
        itemsPadding = itemsPadding,
        caption = caption,
        content = content,
    )
}

private fun LazyListScope.itemsAndCaption(
    itemsPadding: PaddingValues,
    caption: @Composable (LazyItemScope.() -> Unit)?,
    content: LazySectionScope.() -> Unit,
) {
    val items = LazySectionScopeImpl().apply(content).items

    items.fastForEachIndexed { index, item ->
        item(item.key, item.contentType) {
            val shape = RoundedCornerShape(IenTheme.radius.lg)
            val itemShape = RoundedCornerShape(0.dp)
            val hasDivider =
                index != items.lastIndex &&
                    item.dividerPadding != null &&
                    items[index + 1].dividerPadding != null

            val clipShape =
                RoundedCornerShape(
                    topStart = if (index == 0) shape.topStart else itemShape.topStart,
                    topEnd = if (index == 0) shape.topEnd else itemShape.topEnd,
                    bottomStart = if (index == items.lastIndex) shape.bottomStart else itemShape.bottomStart,
                    bottomEnd = if (index == items.lastIndex) shape.bottomEnd else itemShape.bottomEnd,
                )

            val clipModifier = Modifier.clip(clipShape)

            Column(
                modifier = Modifier
                    .animateItem()
                    .padding(horizontal = 16.dp)
                    .then(clipModifier)
                    .background(IenTheme.colors.surfaceRaised)
                    .sectionContainerBorder(index = index, lastIndex = items.lastIndex)
            ) {
                item.content(itemsPadding)
                if (hasDivider) {
                    Spacer(modifier = Modifier.height(IenTheme.stroke.hairline))
                }
            }
        }
    }

    if (caption != null) {
        item(contentType = SectionCaptionContentType) {
            SectionCaption(
                content = {
                    caption()
                },
            )
        }
    }
}

private object SplitPaddingContentType

private object SectionTitleContentType

private object SectionCaptionContentType

@Composable
private fun Modifier.sectionContainerBorder(
    index: Int,
    lastIndex: Int,
): Modifier {
    val color = IenTheme.colors.border.copy(alpha = 0.72f)
    val strokeWidth = IenTheme.stroke.thin
    val radius = IenTheme.radius.lg

    return drawWithContent {
        drawContent()

        val strokePx = strokeWidth.toPx()
        val halfStroke = strokePx / 2f
        val radiusPx = radius.toPx()
        val width = size.width
        val height = size.height
        val style = Stroke(width = strokePx)

        if (lastIndex == 0) {
            drawRoundRect(
                color = color,
                topLeft = Offset(halfStroke, halfStroke),
                size = Size(width - strokePx, height - strokePx),
                cornerRadius = CornerRadius(radiusPx, radiusPx),
                style = style,
            )
            return@drawWithContent
        }

        if (index == 0) {
            drawLine(color, Offset(radiusPx, halfStroke), Offset(width - radiusPx, halfStroke), strokePx)
            drawLine(color, Offset(halfStroke, radiusPx), Offset(halfStroke, height), strokePx)
            drawLine(color, Offset(width - halfStroke, radiusPx), Offset(width - halfStroke, height), strokePx)
            drawArc(
                color = color,
                startAngle = 180f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(halfStroke, halfStroke),
                size = Size(radiusPx * 2f, radiusPx * 2f),
                style = style,
            )
            drawArc(
                color = color,
                startAngle = 270f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(width - radiusPx * 2f - halfStroke, halfStroke),
                size = Size(radiusPx * 2f, radiusPx * 2f),
                style = style,
            )
            return@drawWithContent
        }

        if (index == lastIndex) {
            drawLine(color, Offset(halfStroke, 0f), Offset(halfStroke, height - radiusPx), strokePx)
            drawLine(color, Offset(width - halfStroke, 0f), Offset(width - halfStroke, height - radiusPx), strokePx)
            drawLine(color, Offset(radiusPx, height - halfStroke), Offset(width - radiusPx, height - halfStroke), strokePx)
            drawArc(
                color = color,
                startAngle = 90f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(halfStroke, height - radiusPx * 2f - halfStroke),
                size = Size(radiusPx * 2f, radiusPx * 2f),
                style = style,
            )
            drawArc(
                color = color,
                startAngle = 0f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(width - radiusPx * 2f - halfStroke, height - radiusPx * 2f - halfStroke),
                size = Size(radiusPx * 2f, radiusPx * 2f),
                style = style,
            )
            return@drawWithContent
        }

        drawLine(color, Offset(halfStroke, 0f), Offset(halfStroke, height), strokePx)
        drawLine(color, Offset(width - halfStroke, 0f), Offset(width - halfStroke, height), strokePx)
    }
}
