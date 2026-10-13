package zone.ien.utils.pref.item

import androidx.compose.foundation.interaction.MutableInteractionSource
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
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.AdaptiveSectionSwitchItem
import zone.ien.utils.adaptive.section.HigSectionItemAdaptation
import zone.ien.utils.adaptive.section.IenSectionItemAdaptation
import zone.ien.utils.pref.LocalPrefsDataStore
import zone.ien.utils.pref.LocalPrefsSwitchAdaptive
import zone.ien.utils.ui.section.IenSectionColors
import zone.ien.utils.ui.section.IenSectionLinkDefault
import zone.ien.utils.ui.section.switch

/**
 * 기존 위치 인자와 선행 콘텐츠 후행 람다 호출을 유지하는 스위치 설정입니다.
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
    leadingContent: (@Composable () -> Unit)? = null,
) {
    SwitchPref(
        modifier = modifier,
        title = title,
        summaryOn = summaryOn,
        summaryOff = summaryOff,
        key = key,
        defaultValue = defaultValue,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        leadingContent = leadingContent,
        colors = IenSectionLinkDefault.colors(),
    )
}

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
 * @param leadingContent 제목 앞에 표시할 콘텐츠
 * @param colors Ien 설정 항목 색상
 * @param interactionSource Ien 컴포넌트의 상호작용 소스
 * @param adaptation Adaptive 모드의 플랫폼별 설정
 * @param thumbContent Ien 스위치 손잡이 콘텐츠
 * @param trackContent Ien 스위치 트랙 콘텐츠
 * @param onTrackContent Ien 스위치가 켜졌을 때 표시할 트랙 콘텐츠
 * @param offTrackContent Ien 스위치가 꺼졌을 때 표시할 트랙 콘텐츠
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
    leadingContent: (@Composable () -> Unit)? = null,
    colors: IenSectionColors = IenSectionLinkDefault.colors(),
    thumbContent: (@Composable () -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    trackContent: (@Composable () -> Unit)? = null,
    onTrackContent: (@Composable () -> Unit)? = null,
    offTrackContent: (@Composable () -> Unit)? = null,
    adaptation: AdaptationScope<HigSectionItemAdaptation, IenSectionItemAdaptation>.() -> Unit = {},
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

    if (!LocalPrefsSwitchAdaptive.current) {
        switch(
            modifier = modifier,
            leadingContent = leadingContent,
            checked = checked,
            onCheckedChange = { edit(it) },
            enabled = enabled,
            supportingContent = if (checked) summaryOn?.let { { Text(it) } } else summaryOff?.let { { Text(it) } },
            colors = colors,
            thumbContent = thumbContent,
            interactionSource = interactionSource,
            trackContent = trackContent,
            onTrackContent = onTrackContent,
            offTrackContent = offTrackContent,
            title = { Text(title) },
        )
        return
    }
    AdaptiveSectionSwitchItem(
        modifier = modifier,
        leadingContent = leadingContent,
        checked = checked,
        onCheckedChange = { edit(it) },
        enabled = enabled,
        supportingContent =
            if (checked) summaryOn?.let { { Text(text = it) } }
            else summaryOff?.let { { Text(text = it) } },
        colors = colors,
        thumbContent = thumbContent,
        interactionSource = interactionSource,
        trackContent = trackContent,
        onTrackContent = onTrackContent,
        offTrackContent = offTrackContent,
        adaptation = {
            material { this.colors = colors }
            adaptation()
        },
        title = { Text(text = title) }
    )
}
