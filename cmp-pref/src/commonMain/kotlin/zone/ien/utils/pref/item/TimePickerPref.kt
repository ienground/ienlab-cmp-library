package zone.ien.utils.pref.item

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.dialog.TimePickerDialog
import zone.ien.utils.adaptive.section.HigSectionLinkAdaptation
import zone.ien.utils.adaptive.section.IenSectionLinkAdaptation
import zone.ien.utils.pref.LocalPrefsAdaptive
import zone.ien.utils.pref.LocalPrefsDataStore
import zone.ien.utils.ui.dialog.IenTimePickerDialog
import zone.ien.utils.ui.section.IenSectionColors
import zone.ien.utils.ui.section.IenSectionLinkDefault

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
 * @param leadingContent 제목 앞에 표시할 콘텐츠
 * @param colors Ien 설정 항목 색상
 * @param interactionSource Ien 컴포넌트의 상호작용 소스
 * @param adaptation Adaptive 모드의 플랫폼별 설정
 * @param onClickLabel 클릭 동작의 접근성 레이블
 * @param indication 클릭 시 시각적 피드백
 * @param trailingContent 설정 항목 뒤에 표시할 콘텐츠
 * @param dialogModifier 다이얼로그에 적용할 Modifier
 * @param is24Hour 24시간 표시 여부. Adaptive iOS에서는 시스템 설정을 따릅니다.
 * @param showIcon 앞쪽 콘텐츠를 표시할지 여부
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.TimePickerPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable (Int) -> String,
    key: Preferences.Key<Int>,
    defaultValue: Int,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    colors: IenSectionColors = IenSectionLinkDefault.colors(),
    trailingContent: @Composable () -> Unit = {},
    adaptation: AdaptationScope<HigSectionLinkAdaptation, IenSectionLinkAdaptation>.() -> Unit = {},
    dialogModifier: Modifier = Modifier,
    is24Hour: Boolean = false,
) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by rememberSaveable(key.name) { mutableStateOf(false) }
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember(dataStore) { dataStore.data }.collectAsState(initial = null)
    val value = prefs?.get(key) ?: defaultValue

    TextPref(
        onClick = { if (enabled) showDialog = !showDialog },
        title = title,
        modifier = modifier,
        enabled = enabled,
        summary = summary(value),
        leadingContent = if (showIcon) leadingContent else null,
        trailingContent = trailingContent,
        onClickLabel = onClickLabel,
        indication = indication,
        interactionSource = interactionSource,
        colors = colors,
        adaptation = {
            cupertino {
                this.showSupportingContent = true
                this.isCaption = false
            }
            adaptation()
        },
    )

    PrefsTimePickerDialog(
        modifier = dialogModifier,
        is24Hour = is24Hour,
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
 * @param leadingContent 제목 앞에 표시할 콘텐츠
 * @param colors Ien 설정 항목 색상
 * @param interactionSource Ien 컴포넌트의 상호작용 소스
 * @param adaptation Adaptive 모드의 플랫폼별 설정
 * @param onClickLabel 클릭 동작의 접근성 레이블
 * @param indication 클릭 시 시각적 피드백
 * @param trailingContent 설정 항목 뒤에 표시할 콘텐츠
 * @param dialogModifier 다이얼로그에 적용할 Modifier
 * @param is24Hour 24시간 표시 여부. Adaptive iOS에서는 시스템 설정을 따릅니다.
 * @param showIcon 앞쪽 콘텐츠를 표시할지 여부
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.TimePickerPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable ((Int) -> String),
    key: Preferences.Key<Int>,
    defaultValue: Int,
    enabled: Pair<Preferences.Key<Boolean>, Boolean>,
    leadingContent: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    colors: IenSectionColors = IenSectionLinkDefault.colors(),
    trailingContent: @Composable () -> Unit = {},
    adaptation: AdaptationScope<HigSectionLinkAdaptation, IenSectionLinkAdaptation>.() -> Unit = {},
    dialogModifier: Modifier = Modifier,
    is24Hour: Boolean = false,
) {
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember(dataStore) { dataStore.data }.collectAsState(initial = null)
    val checked = prefs?.get(enabled.first) ?: enabled.second

    TimePickerPref(
        modifier = modifier,
        title = title,
        summary = summary,
        key = key,
        defaultValue = defaultValue,
        enabled = checked,
        leadingContent = leadingContent,
        showIcon = showIcon,
        onClickLabel = onClickLabel,
        indication = indication,
        interactionSource = interactionSource,
        colors = colors,
        trailingContent = trailingContent,
        adaptation = adaptation,
        dialogModifier = dialogModifier,
        is24Hour = is24Hour,

    )
}


@Composable
private fun PrefsTimePickerDialog(
    modifier: Modifier,
    visible: Boolean,
    initialHour: Int,
    initialMinute: Int,
    is24Hour: Boolean,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
) {
    if (LocalPrefsAdaptive.current) {
        TimePickerDialog(
            modifier = modifier, visible = visible, initialHour = initialHour, initialMinute = initialMinute,
            is24Hour = is24Hour, title = title, onDismiss = onDismiss, onConfirm = onConfirm,
        )
    } else {
        IenTimePickerDialog(
            modifier = modifier, visible = visible, initialHour = initialHour, initialMinute = initialMinute,
            is24Hour = is24Hour, title = title, onDismiss = onDismiss, onConfirm = onConfirm,
        )
    }
}
