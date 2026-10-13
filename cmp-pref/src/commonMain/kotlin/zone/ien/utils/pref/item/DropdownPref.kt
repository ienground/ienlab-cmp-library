package zone.ien.utils.pref.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.view.AdaptiveDropdownMenu
import zone.ien.utils.adaptive.view.DropdownMenuSection
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.pref.LocalPrefsAdaptive
import zone.ien.utils.pref.LocalPrefsDataStore
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.section.dropdown

/**
 * DataStore 값을 드롭다운에서 선택하는 설정 항목을 생성합니다.
 *
 * [key]는 Preferences DataStore가 지원하는 타입인 Boolean, Int, Long, Float, Double,
 * String, Set<String> 또는 ByteArray여야 합니다.
 *
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 설정 항목의 제목
 * @param key 선택값을 저장할 DataStore 키
 * @param defaultValue 저장된 값이 없을 때 사용할 기본값
 * @param itemsWithLabels 선택할 값과 표시 이름의 매핑
 * @param summary 제목 아래에 표시할 설명
 * @param enabled 설정 항목의 활성화 여부
 * @param leadingContent 제목 앞에 표시할 콘텐츠
 * @param onItemSelected 값을 저장한 뒤 호출할 콜백
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun <T> SectionScope.DropdownPref(
    modifier: Modifier = Modifier,
    title: String,
    key: Preferences.Key<T>,
    defaultValue: T,
    itemsWithLabels: Map<T, String>,
    summary: String? = null,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
    onItemSelected: ((T) -> Unit)? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember(dataStore) { dataStore.data }.collectAsState(initial = null)
    val value = prefs?.get(key) ?: defaultValue
    val selectedItem = itemsWithLabels.keys.firstOrNull { preferenceValueEquals(it, value) }
    val defaultLabel = itemsWithLabels.entries
        .firstOrNull { preferenceValueEquals(it.key, defaultValue) }
        ?.value
        .orEmpty()
    val selectedLabel = selectedItem?.let(itemsWithLabels::get) ?: defaultLabel
    var expanded by remember { mutableStateOf(false) }

    val selectItem: (T) -> Unit = { item ->
        expanded = false
        if (!preferenceValueEquals(item, value)) {
            coroutineScope.launch {
                try {
                    dataStore.edit { it[key] = item }
                    onItemSelected?.invoke(item)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    if (!LocalPrefsAdaptive.current) {
        dropdown(
            itemsWithLabels = itemsWithLabels,
            currentItem = selectedItem,
            onItemSelected = selectItem,
            modifier = modifier,
            enabled = enabled,
            leadingContent = leadingContent,
            supportingContent = summary?.let { { Text(it) } },
            defaultText = defaultLabel,
            title = { Text(title) },
        )
        return
    }

    TextPref(
        onClick = { if (enabled) expanded = true },
        modifier = modifier,
        enabled = enabled,
        leadingContent = leadingContent,
        summary = summary,
        title = title,
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = selectedLabel,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                AdaptiveWidget(
                    material = {
                        IenIcon(imageVector = SystemIcons.ChevronDown, contentDescription = null)
                    },
                    cupertino = { zone.ien.hig.section.CupertinoSectionDefaults.LabelChevron() },
                )
            }
        },
    )

    AdaptiveDropdownMenu(
        expanded = expanded && enabled,
        onDismissRequest = { expanded = false },
        items = emptyList(),
        sections = itemsWithLabels.map { (item, label) ->
            DropdownMenuSection.Action(
                text = { Text(label) },
                onClick = { selectItem(item) },
                icon = {
                    if (preferenceValueEquals(item, selectedItem)) {
                        IenIcon(imageVector = SystemIcons.Check, contentDescription = null)
                    }
                },
            )
        }.takeIf { it.isNotEmpty() }?.let { listOf(DropdownMenuSection(items = it)) }.orEmpty(),
    )
}

private fun preferenceValueEquals(left: Any?, right: Any?): Boolean =
    if (left is ByteArray && right is ByteArray) left.contentEquals(right) else left == right
