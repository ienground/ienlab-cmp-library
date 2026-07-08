package zone.ien.utils.pref.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.dialog.TimePickerDialog
import zone.ien.utils.pref.LocalPrefsDataStore

/**
 * TimePicker 다이얼로그를 통해 시간을 선택하는 설정 항목을 생성하는 Composable 함수입니다.
 * 
 * 이 설정 항목은 현재 시간(분 단위 정수) 값을 표시하며, 클릭 시 시/분을 선택할 수 있는 TimePicker 다이얼로그를 엽니다.
 * 내부적으로 시간은 총 분(0-1439)으로 저장됩니다.
 * 
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 시간 설정의 제목 텍스트
 * @param summary 현재 시간 값을 요약으로 표시하는 Composable 함수 (분 단위 Int를 전달받음)
 * @param key DataStore에서 이 설정을 식별하는 데 사용되는 Preferences.Key
 * @param defaultValue 시간의 기본값 (분 단위 Int, 0-1439)
 * @param enabled 설정의 활성화 여부
 * @param leadingIcon 제목 앞에 표시할 선택적 아이콘
 * @param showIcon 선행 아이콘을 표시할지 여부
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.TimeSelectPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable (Int) -> String,
    key: Preferences.Key<Int>,
    defaultValue: Int,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember { dataStore.data }.collectAsState(initial = null)
    val value = prefs?.get(key) ?: defaultValue

    TextPref(
        onClick = { if (enabled) showDialog = !showDialog },
        title = title,
        modifier = modifier,
        enabled = enabled,
        summary = summary(value),
        leadingIcon = if (showIcon) leadingIcon else null,
        chevron = {},
        adaptation = {
            cupertino {
                this.showSupportingContent = true
                this.isCaption = false
            }
        },
    )

    TimePickerDialog(
        visible = showDialog,
        initialHour = value / 60,
        initialMinute = value % 60,
        title = title,
        onDismiss = { showDialog = false },
        onConfirm = { hour, minute ->
            coroutineScope.launch {
                try {
                    dataStore.edit { it[key] = hour * 60 + minute }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            showDialog = false
        }
    )
}

/**
 * 조건부 활성화 기능이 포함된 TimePicker 설정 항목을 생성하는 Composable 함수입니다.
 *
 * 이 변체는 다른 불리언 설정 값에 따라 이 설정을 활성화 또는 비활성화할 수 있는 기능을 제공합니다.
 *
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 시간 설정의 제목 텍스트
 * @param summary 현재 시간 값을 요약으로 표시하는 Composable 함수 (분 단위 Int를 전달받음)
 * @param key DataStore에서 이 설정을 식별하는 데 사용되는 Preferences.Key
 * @param defaultValue 시간의 기본값 (분 단위 Int, 0-1439)
 * @param enabled 이 설정을 활성화/비활성화하기 위한 Preferences.Key와 기본 불리언 값의 쌍
 * @param leadingIcon 제목 앞에 표시할 선택적 아이콘
 * @param showIcon 선행 아이콘을 표시할지 여부
 */
@Composable
fun SectionScope.TimeSelectPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable ((Int) -> String),
    key: Preferences.Key<Int>,
    defaultValue: Int,
    enabled: Pair<Preferences.Key<Boolean>, Boolean>,
    leadingIcon: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
) {
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember { dataStore.data }.collectAsState(initial = null)
    val checked = prefs?.get(enabled.first) ?: enabled.second

    TimeSelectPref(
        modifier = modifier,
        title = title,
        summary = summary,
        key = key,
        defaultValue = defaultValue,
        enabled = checked,
        leadingIcon = leadingIcon,
        showIcon = showIcon,
    )
}