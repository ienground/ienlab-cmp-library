package zone.ien.utils.pref.item

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch
import zone.ien.hig.section.SectionScope
import zone.ien.utils.pref.LocalPrefsDataStore
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

    val selectItem: (T) -> Unit = { item ->
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
}

private fun preferenceValueEquals(left: Any?, right: Any?): Boolean =
    if (left is ByteArray && right is ByteArray) left.contentEquals(right) else left == right
