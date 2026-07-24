package zone.ien.utils.ui.section.lazy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 섹션 내 개별 항목의 정보를 나타내는 클래스
 *
 * @property key 항목의 고유 키
 * @property contentType 항목의 타입
 * @property dividerPadding 구분선의 패딩 값 (null인 경우 구분선 없음)
 * @property content 항목을 렌더링하기 위한 컴포저블 블록
 */
internal class SectionItem(
    val key: Any? = null,
    val contentType: Any? = null,
    val dividerPadding: Dp?,
    val content: @Composable (PaddingValues) -> Unit,
)

/**
 * [LazySectionScope]의 구현체
 *
 * 섹션 항목들을 내부 리스트에 수집하고 관리합니다.
 */
@Stable
internal class LazySectionScopeImpl : LazySectionScope {
    /**
     * 섹션에 추가된 항목들의 리스트
     */
    val items: List<SectionItem>
        get() = _items

    private val _items = mutableListOf<SectionItem>()

    /**
     * 지정된 최소 높이를 가지는 섹션 항목을 추가합니다.
     *
     * @param key 항목의 고유 키
     * @param contentType 항목의 타입
     * @param dividerPadding 구분선의 패딩 값
     * @param minHeight 항목의 최소 높이
     * @param content 항목의 콘텐츠 컴포저블
     */
    internal fun item(
        key: Any? = null,
        contentType: Any? = null,
        dividerPadding: Dp? = null,
        minHeight: Dp,
        content: @Composable (PaddingValues) -> Unit,
    ) {
        _items +=
            SectionItem(
                key = key,
                contentType = contentType,
                content = {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .heightIn(min = minHeight)
                            .fillMaxWidth(),
                    ) {
                        content(it)
                    }
                },
                dividerPadding = dividerPadding
            )
    }

    /**
     * [LazySectionScope.item]의 인터페이스 구현체로, 기본 최소 높이(45.dp)를 적용하여 항목을 추가합니다.
     */
    override fun item(
        key: Any?,
        contentType: Any?,
        dividerPadding: Dp,
        content: @Composable (PaddingValues) -> Unit,
    ) {
        item(
            key = key,
            contentType = contentType,
            dividerPadding = dividerPadding,
            minHeight = 45.dp,
            content = content,
        )
    }
}
