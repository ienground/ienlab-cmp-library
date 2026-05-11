package zone.ien.utils.ui.section.lazy

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.section.M3SectionColors
import zone.ien.utils.ui.section.M3SectionLinkDefault

sealed interface LazySectionScope {
    /**
     * Lazy 섹션 스코프 인터페이스
     *
     * 이 인터페이스는 Lazy 리스트 섹션의 컨텍스트를 제공하여,
     * 섹션 내부에 항목을 추가할 수 있도록 합니다.
     *
     * @param key 항목의 고유 키
     * @param contentType 항목의 타입
     * @param dividerPadding 항목 사이의 구분선 패딩
     * @param content 항목의 콘텐츠를 표시하는 컴포저블 블록
     */
    fun item(
        key: Any? = null,
        contentType: Any? = null,
        dividerPadding: Dp = 0.dp,
        content: @Composable (padding: PaddingValues) -> Unit,
    )
}

/**
 * 링크 항목을 추가하는 함수
 * 
 * 이 함수는 클릭 가능한 링크 형태의 항목을 추가합니다.
 * 
 * @param onClick 클릭 시 호출되는 함수
 * @param key 항목의 고유 키
 * @param enabled 활성화 상태
 * @param leadingContent 앞쪽 콘텐츠
 * @param dividerPadding 항목 사이의 구분선 패딩
 * @param onClickLabel 클릭에 대한 설명 텍스트
 * @param interactionSource 상호작용 소스
 * @param supportingContent 지원 콘텐츠
 * @param trailingContent 뒤쪽 콘텐츠
 * @param title 제목
 */
fun LazySectionScope.link(
    onClick: () -> Unit,
    key: Any? = null,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
    dividerPadding: Dp = 0.dp,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    supportingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
) = labelWithCustomChevron(
    onClick = onClick,
    key = key,
    enabled = enabled,
    leadingContent = leadingContent,
    dividerPadding = dividerPadding,
    onClickLabel = onClickLabel,
    interactionSource = interactionSource,
    trailingContent = trailingContent,
    supportingContent = supportingContent,
    title = title,
)

/**
 * 스위치 항목을 추가하는 함수
 * 
 * 이 함수는 스위치 형태의 항목을 추가합니다.
 * 
 * @param checked 스위치의 현재 상태
 * @param onCheckedChange 스위치 상태가 변경될 때 호출되는 함수
 * @param modifier 적용할 Modifier
 * @param key 항목의 고유 키
 * @param enabled 활성화 상태
 * @param dividerPadding 항목 사이의 구분선 패딩
 * @param interactionSource 상호작용 소스
 * @param thumbContent 스위치의 썸네일 콘텐츠
 * @param supportingContent 지원 콘텐츠
 * @param title 제목
 */
fun LazySectionScope.switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    key: Any? = null,
    enabled: Boolean = true,
    dividerPadding: Dp = 0.dp,
    interactionSource: MutableInteractionSource? = null,
    thumbContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
) = row(
    key = key,
    contentType = ContentTypeToggle,
    dividerPadding = dividerPadding,
    title = title,
    supportingContent = supportingContent,
    trailingContent = {
        Switch(
            modifier = modifier,
            enabled = enabled,
            checked = checked,
            thumbContent = thumbContent,
            onCheckedChange = onCheckedChange,
            interactionSource = interactionSource ?: remember { MutableInteractionSource() },
        )
    },
)

/**
 * 비어있는 항목을 추가하는 함수
 * 
 * 이 함수는 빈 항목(공백)을 추가합니다.
 * 
 * @param modifier 적용할 Modifier
 * @param key 항목의 고유 키
 * @param content 항목의 콘텐츠
 */
fun LazySectionScope.empty(
    modifier: Modifier = Modifier,
    key: Any? = null,
    content: @Composable () -> Unit,
) {
    item(
        key = key,
        contentType = ContentTypeEmpty,
        dividerPadding = 0.dp,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .then(modifier)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            content()
        }
    }
}

inline fun <T> LazySectionScope.items(
    items: Collection<T>,
    key: (T) -> Any? = { null },
    contentType: (T) -> Any? = { null },
    dividerPadding: Dp = 0.dp,
    crossinline content: @Composable (item: T, padding: PaddingValues) -> Unit,
) = items.forEach {
    item(
        key = key(it),
        contentType = contentType(it),
        dividerPadding = dividerPadding,
    ) { pv ->
        content(it, pv)
    }
}

private fun LazySectionScope.labelWithCustomChevron(
    onClick: () -> Unit,
    key: Any? = null,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    dividerPadding: Dp = 0.dp,
    onClickLabel: String? = null,
    interactionSource: MutableInteractionSource? = null,
    trailingContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit,
) = row(
    key = key,
    contentType = ContentTypeLabel,
    dividerPadding = dividerPadding,
    modifier = {
        Modifier
            .clickable(
                enabled = enabled,
                onClick = onClick,
                role = Role.Button,
                onClickLabel = onClickLabel,
                interactionSource = interactionSource ?: remember { MutableInteractionSource() },
                indication = LocalIndication.current,
            )
    },
    enabled = enabled,
    leadingContent = leadingContent,
    trailingContent = trailingContent,
    supportingContent = supportingContent,
    title = title,
)

private fun LazySectionScope.row(
    key: Any?,
    contentType: Any?,
    dividerPadding: Dp,
    modifier: @Composable () -> Modifier = { Modifier },
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    colors: @Composable () -> M3SectionColors = { M3SectionLinkDefault.colors() },
    title: @Composable () -> Unit,
) = item(
    key = key,
    contentType = contentType,
    dividerPadding = dividerPadding,
) {
    ListItem(
        headlineContent = title,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        colors = colors().toListItemColors(enabled),
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .then(modifier())
    )
}


private object ContentTypeLabel

private object ContentTypeToggle

private object ContentTypeEmpty

private object ContentTypeDatePicker

private object ContentTypeTimePicker

private object ContentTypeTextField