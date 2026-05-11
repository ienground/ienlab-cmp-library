package zone.ien.utils.ui.section.lazy

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed

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
            val shape = RoundedCornerShape(16.dp)
            val itemShape = RoundedCornerShape(4.dp)

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
            ) {
                item.content(itemsPadding)
            }

            if (index != items.lastIndex &&
                item.dividerPadding != null &&
                items[index + 1].dividerPadding != null
            ) {
                Spacer(modifier = Modifier.height(2.dp))
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