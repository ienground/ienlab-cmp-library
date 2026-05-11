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
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.AdaptiveSectionSwitchItem
import zone.ien.utils.pref.LocalPrefsDataStore

/**
 * 스위치 설정 항목을 생성하는 Composable 함수입니다.
 *
 * 이 설정 항목은 사용자가 불리언 값을 토글할 수 있는 스위치 컨트롤을 표시합니다.
 * DataStore와 통합되어 스위치 상태를 유지하고 값이 변경될 때 UI를 업데이트합니다.
 *
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 스위치 설정의 제목 텍스트
 * @param summaryOn 스위치가 켜져 있을 때 표시되는 선택적 요약 텍스트
 * @param summaryOff 스위치가 꺼져 있을 때 표시되는 선택적 요약 텍스트
 * @param key DataStore에서 이 설정을 식별하는 데 사용되는 Preferences.Key
 * @param defaultValue 스위치의 기본값
 * @param onCheckedChange 스위치 상태가 변경될 때 트리거되는 선택적 콜백
 * @param enabled 스위치의 활성화 여부
 * @param leadingIcon 제목 앞에 표시할 선택적 아이콘
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.SwitchPref(
    modifier: Modifier = Modifier,
    title: String,
    summaryOn: String? = null,
    summaryOff: String? = null,
    key: Preferences.Key<Boolean>,
    defaultValue: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember { dataStore.data }.collectAsState(initial = null)

    var checked = defaultValue
    prefs?.get(key)?.also { checked = it }

    fun edit(newValue: Boolean) = run {
        coroutineScope.launch {
            try {
                dataStore.edit { it[key] = newValue }
                checked = newValue
                onCheckedChange?.invoke(newValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    AdaptiveSectionSwitchItem(
        modifier = modifier,
        leadingContent = leadingIcon,
        checked = checked,
        onCheckedChange = { edit(it) },
        enabled = enabled,
        supportingContent =
            if (checked) summaryOn?.let { { Text(text = it) } }
            else summaryOff?.let { { Text(text = it) } },
        title = { Text(text = title) }
    )
}